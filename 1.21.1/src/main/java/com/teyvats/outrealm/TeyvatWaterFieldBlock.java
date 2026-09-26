package com.teyvats.outrealm;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TeyvatWaterFieldBlock
extends Block {
    public static final MapCodec<TeyvatWaterFieldBlock> CODEC = TeyvatWaterFieldBlock.simpleCodec(TeyvatWaterFieldBlock::new);
    private static final VoxelShape SHAPE = Shapes.or((VoxelShape)Block.box((double)0.0, (double)0.0, (double)0.0, (double)16.0, (double)13.0, (double)16.0), (VoxelShape[])new VoxelShape[]{Block.box((double)0.0, (double)13.0, (double)0.0, (double)16.0, (double)16.0, (double)2.0), Block.box((double)0.0, (double)13.0, (double)14.0, (double)16.0, (double)16.0, (double)16.0), Block.box((double)0.0, (double)13.0, (double)2.0, (double)2.0, (double)16.0, (double)14.0), Block.box((double)14.0, (double)13.0, (double)2.0, (double)16.0, (double)16.0, (double)14.0)});

    public TeyvatWaterFieldBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public MapCodec<TeyvatWaterFieldBlock> codec() {
        return CODEC;
    }

    protected boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    public FluidState getFluidState(BlockState state) {
        return Fluids.WATER.getSource(false);
    }

    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return false;
    }
}

