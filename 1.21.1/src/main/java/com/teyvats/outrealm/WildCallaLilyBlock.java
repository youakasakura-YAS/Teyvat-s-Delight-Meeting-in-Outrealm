package com.teyvats.outrealm;

import com.teyvats.outrealm.TeyvatDelight;
import com.teyvats.outrealm.WildTeyvatCropBlock;
import com.mojang.serialization.MapCodec;
import java.util.function.Supplier;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class WildCallaLilyBlock
extends WildTeyvatCropBlock {
    public static final MapCodec<WildCallaLilyBlock> CODEC = WildCallaLilyBlock.simpleCodec(WildCallaLilyBlock::new);

    public WildCallaLilyBlock(BlockBehaviour.Properties properties) {
        super(properties, (Supplier<? extends ItemLike>)TeyvatDelight.CALLA_LILY, (Supplier<? extends ItemLike>)TeyvatDelight.CALLA_LILY_SEEDS, WildTeyvatCropBlock.Surface.SAND_NEAR_WATER);
    }

    protected MapCodec<WildCallaLilyBlock> codec() {
        return CODEC;
    }
}

