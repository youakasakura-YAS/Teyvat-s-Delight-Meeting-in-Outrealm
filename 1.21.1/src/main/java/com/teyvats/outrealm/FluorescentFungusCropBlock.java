package com.teyvats.outrealm;

import com.teyvats.outrealm.TeyvatCropBlock;
import com.teyvats.outrealm.TeyvatDelight;
import com.teyvats.outrealm.TeyvatTags;
import com.mojang.serialization.MapCodec;
import java.util.function.Supplier;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

public class FluorescentFungusCropBlock
extends TeyvatCropBlock {
    public static final MapCodec<FluorescentFungusCropBlock> CODEC = FluorescentFungusCropBlock.simpleCodec(FluorescentFungusCropBlock::new);
    private static final int MATURE_LIGHT_LEVEL = 7;

    public FluorescentFungusCropBlock(BlockBehaviour.Properties properties) {
        super(properties, TeyvatTags.Blocks.NI_CI_ZHI_FIELDS, (Supplier<? extends ItemLike>)TeyvatDelight.FLUORESCENT_FUNGUS, (Supplier<? extends ItemLike>)TeyvatDelight.FLUORESCENT_FUNGUS_SPORES);
    }

    public static int getLightEmission(BlockState state) {
        return state.hasProperty((Property)AGE) && (Integer)state.getValue((Property)AGE) >= 7 ? 7 : 0;
    }

    public MapCodec<FluorescentFungusCropBlock> codec() {
        return CODEC;
    }
}

