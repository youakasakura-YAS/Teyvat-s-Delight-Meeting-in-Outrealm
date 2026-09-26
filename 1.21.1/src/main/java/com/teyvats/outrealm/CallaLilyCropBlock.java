package com.teyvats.outrealm;

import com.teyvats.outrealm.TeyvatCropBlock;
import com.teyvats.outrealm.TeyvatDelight;
import com.teyvats.outrealm.TeyvatTags;
import com.mojang.serialization.MapCodec;
import java.util.function.Supplier;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class CallaLilyCropBlock
extends TeyvatCropBlock {
    public static final MapCodec<CallaLilyCropBlock> CODEC = CallaLilyCropBlock.simpleCodec(CallaLilyCropBlock::new);

    public CallaLilyCropBlock(BlockBehaviour.Properties properties) {
        super(properties, TeyvatTags.Blocks.CHU_CI_ZHU_FIELDS, (Supplier<? extends ItemLike>)TeyvatDelight.CALLA_LILY, (Supplier<? extends ItemLike>)TeyvatDelight.CALLA_LILY_SEEDS);
    }

    public MapCodec<CallaLilyCropBlock> codec() {
        return CODEC;
    }
}

