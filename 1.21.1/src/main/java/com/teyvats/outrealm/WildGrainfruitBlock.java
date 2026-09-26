package com.teyvats.outrealm;

import com.teyvats.outrealm.TeyvatDelight;
import com.teyvats.outrealm.WildTeyvatCropBlock;
import com.mojang.serialization.MapCodec;
import java.util.function.Supplier;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class WildGrainfruitBlock
extends WildTeyvatCropBlock {
    public static final MapCodec<WildGrainfruitBlock> CODEC = WildGrainfruitBlock.simpleCodec(WildGrainfruitBlock::new);

    public WildGrainfruitBlock(BlockBehaviour.Properties properties) {
        super(properties, (Supplier<? extends ItemLike>)TeyvatDelight.GRAINFRUIT, (Supplier<? extends ItemLike>)TeyvatDelight.GRAINFRUIT_SEEDS, WildTeyvatCropBlock.Surface.GRAINFRUIT);
    }

    protected MapCodec<WildGrainfruitBlock> codec() {
        return CODEC;
    }
}

