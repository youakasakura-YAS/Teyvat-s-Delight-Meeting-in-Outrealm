package com.teyvats.outrealm;

import com.teyvats.outrealm.SeedDispensaryItem;
import com.teyvats.outrealm.TeyvatBonusDrops;
import com.teyvats.outrealm.TeyvatCropDropTracker;
import com.teyvats.outrealm.TeyvatDelight;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WildHorsetailBlock
extends DoublePlantBlock
implements SimpleWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private static final VoxelShape SHAPE = Block.box((double)2.0, (double)0.0, (double)2.0, (double)14.0, (double)16.0, (double)14.0);

    public WildHorsetailBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)HALF, (Comparable)DoubleBlockHalf.LOWER)).setValue((Property)WATERLOGGED, (Comparable)Boolean.valueOf(true)));
    }

    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        DoubleBlockHalf half = (DoubleBlockHalf)state.getValue((Property)HALF);
        if (half == DoubleBlockHalf.UPPER) {
            BlockState belowState = level.getBlockState(pos.below());
            return belowState.is((Block)this) && belowState.getValue((Property)HALF) == DoubleBlockHalf.LOWER;
        }
        FluidState fluidState = level.getFluidState(pos);
        return fluidState.is(FluidTags.WATER) && fluidState.getAmount() == 8 && WildHorsetailBlock.canGrowOn(level.getBlockState(pos.below()));
    }

    public boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return WildHorsetailBlock.canGrowOn(state);
    }

    public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        return false;
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        FluidState fluidState = context.getLevel().getFluidState(pos);
        if (pos.getY() >= context.getLevel().getMaxBuildHeight() - 1 || !fluidState.is(FluidTags.WATER) || fluidState.getAmount() != 8 || !context.getLevel().getBlockState(pos.above()).isAir()) {
            return null;
        }
        return super.getStateForPlacement(context);
    }

    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        level.setBlock(pos.above(), (BlockState)((BlockState)this.defaultBlockState().setValue((Property)HALF, (Comparable)DoubleBlockHalf.UPPER)).setValue((Property)WATERLOGGED, (Comparable)Boolean.valueOf(false)), 3);
    }

    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue((Property)HALF) == DoubleBlockHalf.LOWER) {
            level.scheduleTick(pos, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay((LevelReader)level));
            if (direction == Direction.UP && !WildHorsetailBlock.isMatchingHalf(neighborState, DoubleBlockHalf.UPPER) || direction == Direction.DOWN && !state.canSurvive((LevelReader)level, pos)) {
                return Blocks.WATER.defaultBlockState();
            }
        } else if (direction == Direction.DOWN && !WildHorsetailBlock.isMatchingHalf(neighborState, DoubleBlockHalf.LOWER)) {
            return Blocks.AIR.defaultBlockState();
        }
        return state;
    }

    public FluidState getFluidState(BlockState state) {
        return state.getValue((Property)HALF) == DoubleBlockHalf.LOWER ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
    }

    public boolean canPlaceLiquid(Player player, BlockGetter level, BlockPos pos, BlockState state, Fluid fluid) {
        return state.getValue((Property)HALF) == DoubleBlockHalf.LOWER;
    }

    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide) {
            BlockPos lowerPos = state.getValue((Property)HALF) == DoubleBlockHalf.LOWER ? pos : pos.below();
            TeyvatCropDropTracker.skipNextDrop(level, lowerPos);
            TeyvatCropDropTracker.skipNextDrop(level, lowerPos.above());
        }
        super.playerWillDestroy(level, pos, state, player);
    }

    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        super.playerDestroy(level, player, pos, state, blockEntity, tool);
        if (!level.isClientSide) {
            BlockPos lowerPos = state.getValue((Property)HALF) == DoubleBlockHalf.LOWER ? pos : pos.below();
            BlockPos upperPos = lowerPos.above();
            TeyvatCropDropTracker.consumeSkipDrop(level, lowerPos);
            TeyvatCropDropTracker.consumeSkipDrop(level, upperPos);
            TeyvatCropDropTracker.skipNextDrop(level, lowerPos);
            TeyvatCropDropTracker.skipNextDrop(level, upperPos);
            level.setBlock(lowerPos, Blocks.WATER.defaultBlockState(), 35);
            level.setBlock(upperPos, Blocks.AIR.defaultBlockState(), 35);
            TeyvatCropDropTracker.consumeSkipDrop(level, lowerPos);
            TeyvatCropDropTracker.consumeSkipDrop(level, upperPos);
            if (!player.isCreative()) {
                WildHorsetailBlock.popResource((Level)level, (BlockPos)lowerPos, (ItemStack)new ItemStack((ItemLike)TeyvatDelight.HORSETAIL.get()));
                SeedDispensaryItem.dropExtraSeedIfHeld(level, lowerPos, player, (ItemLike)TeyvatDelight.HORSETAIL_SEEDS.get());
                TeyvatBonusDrops.dropMoraFromCrop(level, lowerPos, player, tool);
            }
        }
    }

    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!level.isClientSide && state.getBlock() != newState.getBlock() && !TeyvatCropDropTracker.consumeSkipDrop(level, pos)) {
            BlockPos lowerPos = state.getValue((Property)HALF) == DoubleBlockHalf.LOWER ? pos : pos.below();
            BlockPos upperPos = lowerPos.above();
            TeyvatCropDropTracker.skipNextDrop(level, lowerPos);
            TeyvatCropDropTracker.skipNextDrop(level, upperPos);
            level.setBlock(lowerPos, Blocks.WATER.defaultBlockState(), 35);
            level.setBlock(upperPos, Blocks.AIR.defaultBlockState(), 35);
            WildHorsetailBlock.popResource((Level)level, (BlockPos)lowerPos, (ItemStack)new ItemStack((ItemLike)TeyvatDelight.HORSETAIL.get()));
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return new ItemStack((ItemLike)this.asItem());
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{HALF, WATERLOGGED});
    }

    public static boolean canGrowOn(BlockState state) {
        return state.is(BlockTags.DIRT) || state.is(BlockTags.SAND);
    }

    private static boolean isMatchingHalf(BlockState state, DoubleBlockHalf half) {
        return state.is((Block)TeyvatDelight.WILD_HORSETAIL.get()) && state.getValue((Property)HALF) == half;
    }
}

