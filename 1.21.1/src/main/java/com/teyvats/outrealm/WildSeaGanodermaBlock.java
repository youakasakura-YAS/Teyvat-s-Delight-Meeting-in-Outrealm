package com.teyvats.outrealm;

import com.teyvats.outrealm.SeedDispensaryItem;
import com.teyvats.outrealm.TeyvatBonusDrops;
import com.teyvats.outrealm.TeyvatCropDropTracker;
import com.teyvats.outrealm.TeyvatDelight;
import com.mojang.serialization.MapCodec;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
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
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WildSeaGanodermaBlock
extends BushBlock
implements SimpleWaterloggedBlock {
    public static final MapCodec<WildSeaGanodermaBlock> CODEC = WildSeaGanodermaBlock.simpleCodec(WildSeaGanodermaBlock::new);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private static final VoxelShape SHAPE = Block.box((double)2.0, (double)0.0, (double)2.0, (double)14.0, (double)13.0, (double)14.0);

    public WildSeaGanodermaBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)WATERLOGGED, (Comparable)Boolean.valueOf(true)));
    }

    protected MapCodec<? extends BushBlock> codec() {
        return CODEC;
    }

    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(BlockTags.SAND) || state.is(BlockTags.CORAL_BLOCKS);
    }

    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        FluidState fluidState = level.getFluidState(pos);
        BlockState below = level.getBlockState(pos.below());
        return fluidState.is(FluidTags.WATER) && fluidState.getAmount() == 8 && (below.is(BlockTags.SAND) || below.is(BlockTags.CORAL_BLOCKS));
    }

    protected boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        return false;
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        return (BlockState)this.defaultBlockState().setValue((Property)WATERLOGGED, (Comparable)Boolean.valueOf(fluidState.is(FluidTags.WATER)));
    }

    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (((Boolean)state.getValue((Property)WATERLOGGED)).booleanValue()) {
            level.scheduleTick(pos, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay((LevelReader)level));
        }
        if (direction == Direction.DOWN && !state.canSurvive((LevelReader)level, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    public FluidState getFluidState(BlockState state) {
        return (Boolean)state.getValue((Property)WATERLOGGED) != false ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
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
        if (!level.isClientSide) {
            level.setBlock(pos, Blocks.WATER.defaultBlockState(), 35);
            if (!player.isCreative()) {
                WildSeaGanodermaBlock.popResource((Level)level, (BlockPos)pos, (ItemStack)new ItemStack((ItemLike)TeyvatDelight.SEA_GANODERMA.get()));
                SeedDispensaryItem.dropExtraSeedIfHeld(level, pos, player, (ItemLike)TeyvatDelight.SEA_GANODERMA_SAMPLE.get());
                TeyvatBonusDrops.dropMoraFromCrop(level, pos, player, tool);
            }
        }
    }

    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!level.isClientSide && state.getBlock() != newState.getBlock() && !TeyvatCropDropTracker.consumeSkipDrop(level, pos)) {
            if (((Boolean)state.getValue((Property)WATERLOGGED)).booleanValue() && !newState.is(Blocks.WATER)) {
                level.setBlock(pos, Blocks.WATER.defaultBlockState(), 35);
            }
            WildSeaGanodermaBlock.popResource((Level)level, (BlockPos)pos, (ItemStack)new ItemStack((ItemLike)TeyvatDelight.SEA_GANODERMA.get()));
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return new ItemStack((ItemLike)this.asItem());
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{WATERLOGGED});
    }
}

