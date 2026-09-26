package com.teyvats.outrealm;

import com.teyvats.outrealm.TeyvatDelight;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public final class TeyvatBonusDrops {
    private static final float MORA_CHANCE = 0.15f;
    private static final float PRIMOGEM_CHANCE = 0.05f;

    private TeyvatBonusDrops() {
    }

    public static void dropMoraFromCrop(Level level, BlockPos pos, Player player, ItemStack tool) {
        TeyvatBonusDrops.dropWithFortune(level, pos, player, tool, new ItemStack((ItemLike)TeyvatDelight.MORA.get()), 0.15f);
    }

    public static void dropPrimogemFromMineral(Level level, BlockPos pos, Player player, ItemStack tool) {
        if (tool.is(ItemTags.PICKAXES)) {
            TeyvatBonusDrops.dropWithFortune(level, pos, player, tool, new ItemStack((ItemLike)TeyvatDelight.PRIMOGEM.get()), 0.05f);
        }
    }

    private static void dropWithFortune(Level level, BlockPos pos, Player player, ItemStack tool, ItemStack stack, float baseChance) {
        if (level.isClientSide || player.isCreative()) {
            return;
        }
        float chance = Math.min(1.0f, baseChance * (float)(TeyvatBonusDrops.getFortuneLevel(level, tool) + 1));
        if (level.random.nextFloat() < chance) {
            Block.popResource((Level)level, (BlockPos)pos, (ItemStack)stack);
        }
    }

    private static int getFortuneLevel(Level level, ItemStack tool) {
        if (tool.isEmpty()) {
            return 0;
        }
        return EnchantmentHelper.getItemEnchantmentLevel((Holder)level.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.FORTUNE), (ItemStack)tool);
    }
}

