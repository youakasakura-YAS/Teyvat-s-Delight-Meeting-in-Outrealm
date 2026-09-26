package com.teyvats.outrealm;

import com.teyvats.outrealm.GlazeLilyBlooming;
import com.teyvats.outrealm.TeyvatCropBlock;
import com.teyvats.outrealm.TeyvatDelight;
import com.teyvats.outrealm.TeyvatTags;
import java.util.function.Supplier;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;

public class GlazeLilyCropBlock
extends TeyvatCropBlock {

    public GlazeLilyCropBlock(BlockBehaviour.Properties properties) {
        super(properties, TeyvatTags.Blocks.NI_CI_ZHI_FIELDS, (Supplier<? extends ItemLike>)TeyvatDelight.GLAZE_LILY, (Supplier<? extends ItemLike>)TeyvatDelight.GLAZE_LILY_SEEDS, 7, 1, 1);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)AGE, (Comparable)Integer.valueOf(0))).setValue((Property)GlazeLilyBlooming.BLOOMING, (Comparable)Boolean.valueOf(false)));
    }


    public BlockState getStateForAge(int age) {
        return (BlockState)((BlockState)this.defaultBlockState().setValue((Property)AGE, (Comparable)Integer.valueOf(Math.min(age, this.getMaxAge())))).setValue((Property)GlazeLilyBlooming.BLOOMING, (Comparable)Boolean.valueOf(false));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(new Property[]{GlazeLilyBlooming.BLOOMING});
    }
}

