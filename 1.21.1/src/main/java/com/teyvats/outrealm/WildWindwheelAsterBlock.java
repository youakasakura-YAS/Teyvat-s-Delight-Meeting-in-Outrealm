package com.teyvats.outrealm;

import com.teyvats.outrealm.TeyvatDelight;
import com.teyvats.outrealm.WildTeyvatCropBlock;
import com.mojang.serialization.MapCodec;
import java.util.function.Supplier;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class WildWindwheelAsterBlock
extends WildTeyvatCropBlock {
    public static final MapCodec<WildWindwheelAsterBlock> CODEC = WildWindwheelAsterBlock.simpleCodec(WildWindwheelAsterBlock::new);

    public WildWindwheelAsterBlock(BlockBehaviour.Properties properties) {
        super(properties, (Supplier<? extends ItemLike>)TeyvatDelight.WINDWHEEL_ASTER, (Supplier<? extends ItemLike>)TeyvatDelight.WINDWHEEL_ASTER_SEEDS, WildTeyvatCropBlock.Surface.GRASS_OR_DIRT);
    }

    protected MapCodec<WildWindwheelAsterBlock> codec() {
        return CODEC;
    }
}

