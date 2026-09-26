package com.teyvats.outrealm;

import com.teyvats.outrealm.JinxinFlowerBehavior;
import com.teyvats.outrealm.SeedDispensaryItem;
import com.teyvats.outrealm.TeyvatBonusDrops;
import com.teyvats.outrealm.TeyvatCropDropTracker;
import com.teyvats.outrealm.TeyvatDelight;
import com.teyvats.outrealm.ThirstingJinxinFlowerBlock;
import com.mojang.serialization.MapCodec;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BurntOutJinxinFlowerBlock
extends BushBlock {
    public static final MapCodec<BurntOutJinxinFlowerBlock> CODEC = BurntOutJinxinFlowerBlock.simpleCodec(BurntOutJinxinFlowerBlock::new);
    private static final float HOT_TOUCH_DAMAGE = 0.5f;
    private static final VoxelShape SHAPE = Block.box((double)2.0, (double)0.0, (double)2.0, (double)14.0, (double)13.0, (double)14.0);

    public BurntOutJinxinFlowerBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    protected MapCodec<BurntOutJinxinFlowerBlock> codec() {
        return CODEC;
    }

    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return JinxinFlowerBehavior.canGrowOn(state);
    }

    protected boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        return false;
    }

    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (JinxinFlowerBehavior.isCoolingItem(stack) && this.harvestAndReset(level, pos, player)) {
            return ItemInteractionResult.sidedSuccess((boolean)level.isClientSide);
        }
        BurntOutJinxinFlowerBlock.warnHot(level, player);
        return ItemInteractionResult.sidedSuccess((boolean)level.isClientSide);
    }

    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        BurntOutJinxinFlowerBlock.warnHot(level, player);
        return InteractionResult.sidedSuccess((boolean)level.isClientSide);
    }

    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return new ItemStack((ItemLike)TeyvatDelight.JINXIN_FLOWER_BUD.get());
    }

    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        super.playerDestroy(level, player, pos, state, blockEntity, tool);
        TeyvatCropDropTracker.consumeSkipDrop(level, pos);
        if (!level.isClientSide && !player.isCreative()) {
            JinxinFlowerBehavior.dropBud(level, pos);
        }
    }

    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide) {
            TeyvatCropDropTracker.skipNextDrop(level, pos);
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!level.isClientSide && state.getBlock() != newState.getBlock() && !TeyvatCropDropTracker.consumeSkipDrop(level, pos)) {
            JinxinFlowerBehavior.dropBud(level, pos);
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    private boolean harvestAndReset(Level level, BlockPos pos, Player player) {
        if (!level.isClientSide) {
            JinxinFlowerBehavior.dropHarvest(level, pos);
            SeedDispensaryItem.dropExtraSeedIfHeld(level, pos, player, (ItemLike)TeyvatDelight.JINXIN_FLOWER_BUD.get());
            TeyvatBonusDrops.dropMoraFromCrop(level, pos, player, player.getMainHandItem());
            TeyvatCropDropTracker.skipNextDrop(level, pos);
            level.setBlock(pos, ((ThirstingJinxinFlowerBlock)((Object)TeyvatDelight.THIRSTING_JINXIN_FLOWER.get())).defaultBlockState(), 2);
            level.playSound(null, pos, SoundEvents.CROP_BREAK, SoundSource.BLOCKS, 1.0f, 1.0f);
            JinxinFlowerBehavior.playCoolingSound(level, pos);
        }
        return true;
    }

    private static void warnHot(Level level, Player player) {
        if (!level.isClientSide) {
            player.hurt(level.damageSources().hotFloor(), 0.5f);
            player.displayClientMessage((Component)Component.translatable((String)"message.teyvats_delight_meeting_in_outrealm.jinxin_flower.too_hot"), true);
        }
    }
}

