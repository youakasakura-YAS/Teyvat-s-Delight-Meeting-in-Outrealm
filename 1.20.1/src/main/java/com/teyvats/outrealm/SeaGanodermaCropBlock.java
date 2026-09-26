package com.teyvats.outrealm;

import com.teyvats.outrealm.TeyvatCropBlock;
import com.teyvats.outrealm.TeyvatDelight;
import com.teyvats.outrealm.TeyvatTags;
import java.util.function.Supplier;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

public class SeaGanodermaCropBlock
extends TeyvatCropBlock {
    private static final int MATURE_LIGHT_LEVEL = 7;

    public SeaGanodermaCropBlock(BlockBehaviour.Properties properties) {
        super(properties, TeyvatTags.Blocks.CHU_CI_ZHU_FIELDS, (Supplier<? extends ItemLike>)TeyvatDelight.SEA_GANODERMA, (Supplier<? extends ItemLike>)TeyvatDelight.SEA_GANODERMA_SAMPLE);
    }

    public static int getLightEmission(BlockState state) {
        return state.hasProperty((Property)AGE) && (Integer)state.getValue((Property)AGE) >= 7 ? 7 : 0;
    }

}

