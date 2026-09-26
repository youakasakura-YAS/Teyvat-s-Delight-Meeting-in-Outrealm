package com.teyvats.outrealm;

import com.teyvats.outrealm.BlazingJinxinFlowerBlock;
import com.teyvats.outrealm.BurntOutJinxinFlowerBlock;
import com.teyvats.outrealm.JinxinFlowerBehavior;
import com.teyvats.outrealm.TeyvatCropDropTracker;
import com.teyvats.outrealm.TeyvatDelight;
import com.mojang.serialization.MapCodec;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
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
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ThirstingJinxinFlowerBlock
extends BushBlock {
    public static final MapCodec<ThirstingJinxinFlowerBlock> CODEC = ThirstingJinxinFlowerBlock.simpleCodec(ThirstingJinxinFlowerBlock::new);
    private static final VoxelShape SHAPE = Block.box((double)2.0, (double)0.0, (double)2.0, (double)14.0, (double)13.0, (double)14.0);

    public ThirstingJinxinFlowerBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    protected MapCodec<ThirstingJinxinFlowerBlock> codec() {
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
        int burnTime = JinxinFlowerBehavior.getFuelBurnTime(stack);
        if (burnTime <= 0) {
            return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
        }
        if (!level.isClientSide) {
            int growthSteps = JinxinFlowerBehavior.getFuelGrowthSteps(burnTime);
            JinxinFlowerBehavior.consumeFuel(player, hand, stack);
            JinxinFlowerBehavior.playFuelSound(level, pos);
            TeyvatCropDropTracker.skipNextDrop(level, pos);
            if (growthSteps >= 112) {
                level.setBlock(pos, ((BurntOutJinxinFlowerBlock)((Object)TeyvatDelight.BURNT_OUT_JINXIN_FLOWER.get())).defaultBlockState(), 2);
                JinxinFlowerBehavior.playBurntOutSound(level, pos);
            } else {
                level.setBlock(pos, (BlockState)((BlazingJinxinFlowerBlock)((Object)TeyvatDelight.BLAZING_JINXIN_FLOWER.get())).defaultBlockState().setValue((Property)BlazingJinxinFlowerBlock.AGE, (Comparable)Integer.valueOf(growthSteps)), 2);
            }
        }
        return ItemInteractionResult.sidedSuccess((boolean)level.isClientSide);
    }

    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return new ItemStack((ItemLike)TeyvatDelight.JINXIN_FLOWER_BUD.get());
    }

    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide) {
            TeyvatCropDropTracker.skipNextDrop(level, pos);
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        super.playerDestroy(level, player, pos, state, blockEntity, tool);
        TeyvatCropDropTracker.consumeSkipDrop(level, pos);
        if (!level.isClientSide && !player.isCreative()) {
            JinxinFlowerBehavior.dropBud(level, pos);
        }
    }

    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!level.isClientSide && state.getBlock() != newState.getBlock() && !TeyvatCropDropTracker.consumeSkipDrop(level, pos)) {
            JinxinFlowerBehavior.dropBud(level, pos);
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }
}

