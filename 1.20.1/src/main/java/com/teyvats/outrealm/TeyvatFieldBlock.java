package com.teyvats.outrealm;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TeyvatFieldBlock
extends Block {
    private static final VoxelShape SHAPE = Block.box((double)0.0, (double)0.0, (double)0.0, (double)16.0, (double)15.0, (double)16.0);

    public TeyvatFieldBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }


    public boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return false;
    }
}

