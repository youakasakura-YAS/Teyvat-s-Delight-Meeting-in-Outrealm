package com.teyvats.outrealm;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.npc.VillagerTrades.ItemListing;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Fabric 移植版事件注册：
 * - ItemTooltipEvent        -> ItemTooltipCallback（fabric-item-api-v1）
 * - PlayerInteractEvent     -> UseBlockCallback（fabric-interaction-api-v1）
 * - AddReloadListenerEvent  -> ServerLifecycleEvents.END_DATA_PACK_RELOAD
 * - LevelTickEvent.Post     -> ServerTickEvents.END_SERVER_TICK
 * - VillagerTradesEvent     -> 直接写入 vanilla VillagerTrades.TRADES
 */
public final class TeyvatDelightEvents {
    private static final int GLAZE_LILY_SYNC_CHUNK_RADIUS = 6;
    private static final java.util.Map<ServerLevel, Boolean> GLAZE_LILY_LAST_BLOOM = new java.util.HashMap<>();

    private TeyvatDelightEvents() {
    }

    public static void registerAll() {
        // 右键点击成熟作物直接收获（先于其他右键处理器执行）
        UseBlockCallback.EVENT.register(TeyvatDelightEvents::harvestTeyvatCrop);
        // 进度布局重排（数据包重载完成后）
        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, resourceManager, success) ->
                TeyvatAdvancementLayout.arrange(server.getAdvancements()));
        // 琉璃百合夜间绽放状态同步（仅在昼夜切换时刻扫描，一天约 2 次，避免每 20 tick 全量扫描 169 个 chunk 的方块导致严重卡顿）
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerLevel level : server.getAllLevels()) {
                boolean nightBlooming = GlazeLilyBlooming.shouldBloom(level);
                Boolean last = GLAZE_LILY_LAST_BLOOM.get(level);
                if (last == null || last != nightBlooming) {
                    GLAZE_LILY_LAST_BLOOM.put(level, nightBlooming);
                    TeyvatDelightEvents.syncGlazeLilies(level);
                }
            }
        });
        // 提瓦特商人的交易
        TeyvatDelightEvents.registerTeyvatMerchantTrades();
    }

    private static InteractionResult harvestTeyvatCrop(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        BlockPos pos = hitResult.getBlockPos();
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();
        if (!(block instanceof TeyvatCropBlock crop) || !crop.isMaxAge(state)) {
            return InteractionResult.PASS;
        }
        crop.harvestAndReplant(level, pos, state, player);
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    private static void syncGlazeLilies(ServerLevel level) {
        boolean nightBlooming = GlazeLilyBlooming.shouldBloom(level);
        HashSet<Long> checkedChunks = new HashSet<>();
        for (ServerPlayer player : level.players()) {
            ChunkPos center = player.chunkPosition();
            for (int chunkX = center.x - GLAZE_LILY_SYNC_CHUNK_RADIUS; chunkX <= center.x + GLAZE_LILY_SYNC_CHUNK_RADIUS; ++chunkX) {
                for (int chunkZ = center.z - GLAZE_LILY_SYNC_CHUNK_RADIUS; chunkZ <= center.z + GLAZE_LILY_SYNC_CHUNK_RADIUS; ++chunkZ) {
                    long chunkKey = ChunkPos.asLong(chunkX, chunkZ);
                    if (!checkedChunks.add(chunkKey)) {
                        continue;
                    }
                    TeyvatDelightEvents.syncGlazeLiliesInChunk(level, chunkX, chunkZ, nightBlooming);
                }
            }
        }
    }

    private static void syncGlazeLiliesInChunk(ServerLevel level, int chunkX, int chunkZ, boolean nightBlooming) {
        LevelChunk chunk = level.getChunkSource().getChunkNow(chunkX, chunkZ);
        if (chunk == null) {
            return;
        }
        chunk.findBlocks(GlazeLilyBlooming::isGlazeLily, (pos, state) -> GlazeLilyBlooming.updateIfNeeded(level, pos, state, nightBlooming));
    }

    private static void registerTeyvatMerchantTrades() {
        VillagerProfession profession = TeyvatDelight.TEYVAT_MERCHANT_PROFESSION.get();
        Int2ObjectMap<ItemListing[]> trades = VillagerTrades.TRADES.computeIfAbsent(profession, p -> new Int2ObjectOpenHashMap<>());
        int maxUses = 16;
        float priceMult = 0.05f;
        int xp = 250;
        TeyvatDelightEvents.addTrades(trades, 1,
                TeyvatDelightEvents.listing(TeyvatDelight.MORA.get(), 3, TeyvatDelight.PEPPER.get(), 1, maxUses, xp, priceMult),
                TeyvatDelightEvents.listing(TeyvatDelight.MORA.get(), 3, TeyvatDelight.SALT.get(), 1, maxUses, xp, priceMult));
        TeyvatDelightEvents.addTrades(trades, 2,
                TeyvatDelightEvents.listing(TeyvatDelight.MORA.get(), 3, TeyvatDelight.TOFU.get(), 1, maxUses, xp, priceMult),
                TeyvatDelightEvents.listing(TeyvatDelight.MORA.get(), 3, TeyvatDelight.GLABROUS_BEANS.get(), 1, maxUses, xp, priceMult));
        TeyvatDelightEvents.addTrades(trades, 3,
                TeyvatDelightEvents.listing(TeyvatDelight.MORA.get(), 3, TeyvatDelight.ALMOND.get(), 1, maxUses, xp, priceMult),
                TeyvatDelightEvents.listing(TeyvatDelight.MORA.get(), 3, TeyvatDelight.SAUSAGE.get(), 1, maxUses, xp, priceMult));
        TeyvatDelightEvents.addTrades(trades, 4,
                TeyvatDelightEvents.listing(TeyvatDelight.MORA.get(), 3, TeyvatDelight.CHENYU_TEA.get(), 1, maxUses, xp, priceMult),
                TeyvatDelightEvents.listing(TeyvatDelight.MORA.get(), 3, TeyvatDelight.MATSUTAKE.get(), 1, maxUses, xp, priceMult));
        TeyvatDelightEvents.addTrades(trades, 5,
                TeyvatDelightEvents.listing(TeyvatDelight.MORA.get(), 3, TeyvatDelight.SHRIMP_MEAT.get(), 1, maxUses, xp, priceMult),
                TeyvatDelightEvents.listing(TeyvatDelight.MORA.get(), 3, TeyvatDelight.CRAB.get(), 1, maxUses, xp, priceMult));
    }

    private static ItemListing listing(ItemLike cost, int costCount, ItemLike result, int resultCount, int maxUses, int xp, float priceMult) {
        return (entity, random) -> new MerchantOffer(new ItemStack(cost, costCount), new ItemStack(result, resultCount), maxUses, xp, priceMult);
    }

    private static void addTrades(Int2ObjectMap<ItemListing[]> trades, int level, ItemListing... listings) {
        ItemListing[] existing = trades.get(level);
        List<ItemListing> combined = new ArrayList<>(Arrays.asList(existing == null ? new ItemListing[0] : existing));
        combined.addAll(Arrays.asList(listings));
        trades.put(level, combined.toArray(new ItemListing[0]));
    }
}
