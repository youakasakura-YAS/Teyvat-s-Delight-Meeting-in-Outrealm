package com.teyvats.outrealm;

import com.teyvats.outrealm.TeyvatDelight;
import com.teyvats.outrealm.WildTeyvatCropBlock;
import com.mojang.serialization.MapCodec;
import java.util.function.Supplier;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class WildSmallLampGrassBlock
extends WildTeyvatCropBlock {
    public static final MapCodec<WildSmallLampGrassBlock> CODEC = WildSmallLampGrassBlock.simpleCodec(WildSmallLampGrassBlock::new);

    public WildSmallLampGrassBlock(BlockBehaviour.Properties properties) {
        super(properties, (Supplier<? extends ItemLike>)TeyvatDelight.SMALL_LAMP_GRASS, (Supplier<? extends ItemLike>)TeyvatDelight.SMALL_LAMP_GRASS_SEEDS, WildTeyvatCropBlock.Surface.GRASS_OR_DIRT);
    }

    protected MapCodec<WildSmallLampGrassBlock> codec() {
        return CODEC;
    }
}

