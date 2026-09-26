package com.teyvats.outrealm;

import com.teyvats.outrealm.TeyvatCropBlock;
import com.teyvats.outrealm.TeyvatDelight;
import com.teyvats.outrealm.TeyvatTags;
import java.util.function.Supplier;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

public class SmallLampGrassCropBlock
extends TeyvatCropBlock {
    private static final int MATURE_LIGHT_LEVEL = 7;

    public SmallLampGrassCropBlock(BlockBehaviour.Properties properties) {
        super(properties, TeyvatTags.Blocks.XUAN_CI_JADE_FIELDS, (Supplier<? extends ItemLike>)TeyvatDelight.SMALL_LAMP_GRASS, (Supplier<? extends ItemLike>)TeyvatDelight.SMALL_LAMP_GRASS_SEEDS);
    }

    public static int getLightEmission(BlockState state) {
        return state.hasProperty((Property)AGE) && (Integer)state.getValue((Property)AGE) >= 7 ? 7 : 0;
    }

}

