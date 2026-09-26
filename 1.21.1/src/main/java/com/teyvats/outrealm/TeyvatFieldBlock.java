package com.teyvats.outrealm;

import com.mojang.serialization.MapCodec;
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
    public static final MapCodec<TeyvatFieldBlock> CODEC = TeyvatFieldBlock.simpleCodec(TeyvatFieldBlock::new);
    private static final VoxelShape SHAPE = Block.box((double)0.0, (double)0.0, (double)0.0, (double)16.0, (double)15.0, (double)16.0);

    public TeyvatFieldBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public MapCodec<TeyvatFieldBlock> codec() {
        return CODEC;
    }

    protected boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return false;
    }
}

