package com.teyvats.outrealm;

import com.teyvats.outrealm.TeyvatDelight;
import com.teyvats.outrealm.TeyvatTags;
import com.mojang.serialization.MapCodec;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class NaturalCoralPearlBlock
extends BushBlock
implements SimpleWaterloggedBlock {
    public static final MapCodec<NaturalCoralPearlBlock> CODEC = NaturalCoralPearlBlock.simpleCodec(NaturalCoralPearlBlock::new);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    private static final VoxelShape SHAPE = Block.box((double)2.0, (double)0.0, (double)2.0, (double)14.0, (double)8.0, (double)14.0);

    public NaturalCoralPearlBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)WATERLOGGED, (Comparable)Boolean.valueOf(false)));
    }

    protected MapCodec<? extends BushBlock> codec() {
        return CODEC;
    }

    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{FACING, WATERLOGGED});
    }

    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(TeyvatTags.Blocks.WILD_CORAL_PEARL_SURFACES);
    }

    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        if (!level.getBlockState(pos.below()).is(TeyvatTags.Blocks.WILD_CORAL_PEARL_SURFACES)) {
            return false;
        }
        if (((Boolean)state.getValue((Property)WATERLOGGED)).booleanValue()) {
            FluidState fluidState = level.getFluidState(pos);
            return fluidState.is(FluidTags.WATER) && fluidState.getAmount() == 8;
        }
        return true;
    }

    protected boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        return false;
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        return (BlockState)((BlockState)this.defaultBlockState().setValue((Property)FACING, (Comparable)context.getHorizontalDirection().getOpposite())).setValue((Property)WATERLOGGED, (Comparable)Boolean.valueOf(fluidState.is(FluidTags.WATER)));
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

    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return new ItemStack((ItemLike)TeyvatDelight.CORAL_PEARL.get());
    }

    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        super.playerDestroy(level, player, pos, state, blockEntity, tool);
        if (!level.isClientSide) {
            if (((Boolean)state.getValue((Property)WATERLOGGED)).booleanValue()) {
                level.setBlock(pos, Blocks.WATER.defaultBlockState(), 35);
            }
            if (!player.isCreative()) {
                NaturalCoralPearlBlock.popResource((Level)level, (BlockPos)pos, (ItemStack)new ItemStack((ItemLike)TeyvatDelight.CORAL_PEARL.get()));
                NaturalCoralPearlBlock.popResource((Level)level, (BlockPos)pos, (ItemStack)new ItemStack((ItemLike)TeyvatDelight.CORAL_SHELL.get()));
            }
        }
    }
}

