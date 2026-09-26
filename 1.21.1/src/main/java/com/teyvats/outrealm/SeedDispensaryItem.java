package com.teyvats.outrealm;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class SeedDispensaryItem
extends Item {
    public SeedDispensaryItem(Item.Properties properties) {
        super(properties);
    }

    public static boolean isHeldBy(@Nullable Player player) {
        return player != null && (player.getMainHandItem().is(TeyvatDelight.SEED_DISPENSARY.get()) || player.getOffhandItem().is(TeyvatDelight.SEED_DISPENSARY.get()));
    }

    public static void dropExtraSeedIfHeld(Level level, BlockPos pos, @Nullable Player player, ItemLike seedItem) {
        if (!level.isClientSide && SeedDispensaryItem.isHeldBy(player)) {
            Block.popResource(level, pos, new ItemStack(seedItem));
        }
    }
}
