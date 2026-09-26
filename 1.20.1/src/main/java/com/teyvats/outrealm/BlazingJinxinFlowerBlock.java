package com.teyvats.outrealm;

import com.teyvats.outrealm.BurntOutJinxinFlowerBlock;
import com.teyvats.outrealm.JinxinFlowerBehavior;
import com.teyvats.outrealm.TeyvatCropDropTracker;
import com.teyvats.outrealm.TeyvatDelight;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlazingJinxinFlowerBlock
extends BushBlock {
    public static final int MAX_AGE = 112;
    public static final IntegerProperty AGE = IntegerProperty.create((String)"age", (int)0, (int)111);
    private static final VoxelShape SHAPE = Block.box((double)2.0, (double)0.0, (double)2.0, (double)14.0, (double)13.0, (double)14.0);

    public BlazingJinxinFlowerBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)AGE, (Comparable)Integer.valueOf(0)));
    }


    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    public boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return JinxinFlowerBehavior.canGrowOn(state);
    }

    public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        return false;
    }

    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.getRawBrightness(pos, 0) < 9) {
            return;
        }
        if ((Integer)state.getValue((Property)AGE) >= 112) {
            BlazingJinxinFlowerBlock.becomeBurntOut(level, pos);
            return;
        }
        if (random.nextInt(7) == 0) {
            BlazingJinxinFlowerBlock.advance(level, pos, state, 16);
        }
    }

    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextInt(8) == 0) {
            level.playLocalSound((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 0.45f, 0.8f + random.nextFloat() * 0.4f, false);
        }
    }

    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack stack = player.getItemInHand(hand);
        int burnTime = JinxinFlowerBehavior.getFuelBurnTime(stack);
        if (burnTime <= 0) {
            return super.use(state, level, pos, player, hand, hitResult);
        }
        if (!level.isClientSide) {
            JinxinFlowerBehavior.consumeFuel(player, hand, stack);
            JinxinFlowerBehavior.playFuelSound(level, pos);
            BlazingJinxinFlowerBlock.advance(level, pos, state, JinxinFlowerBehavior.getFuelGrowthSteps(burnTime));
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
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

    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide) {
            TeyvatCropDropTracker.skipNextDrop(level, pos);
        }
        super.playerWillDestroy(level, pos, state, player);
    }

    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!level.isClientSide && state.getBlock() != newState.getBlock() && !TeyvatCropDropTracker.consumeSkipDrop(level, pos)) {
            JinxinFlowerBehavior.dropBud(level, pos);
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{AGE});
    }

    private static void advance(ServerLevel level, BlockPos pos, BlockState state, int steps) {
        int age = (Integer)state.getValue((Property)AGE);
        int nextAge = age + steps;
        if (nextAge >= 112) {
            BlazingJinxinFlowerBlock.becomeBurntOut(level, pos);
        } else {
            level.setBlock(pos, (BlockState)state.setValue((Property)AGE, (Comparable)Integer.valueOf(nextAge)), 2);
        }
    }

    private static void advance(Level level, BlockPos pos, BlockState state, int steps) {
        int age = (Integer)state.getValue((Property)AGE);
        int nextAge = age + steps;
        if (nextAge >= 112) {
            TeyvatCropDropTracker.skipNextDrop(level, pos);
            level.setBlock(pos, ((BurntOutJinxinFlowerBlock)((Object)TeyvatDelight.BURNT_OUT_JINXIN_FLOWER.get())).defaultBlockState(), 2);
            JinxinFlowerBehavior.playBurntOutSound(level, pos);
        } else {
            level.setBlock(pos, (BlockState)state.setValue((Property)AGE, (Comparable)Integer.valueOf(nextAge)), 2);
        }
    }

    private static void becomeBurntOut(ServerLevel level, BlockPos pos) {
        TeyvatCropDropTracker.skipNextDrop((Level)level, pos);
        level.setBlock(pos, ((BurntOutJinxinFlowerBlock)((Object)TeyvatDelight.BURNT_OUT_JINXIN_FLOWER.get())).defaultBlockState(), 2);
        JinxinFlowerBehavior.playBurntOutSound((Level)level, pos);
    }
}

