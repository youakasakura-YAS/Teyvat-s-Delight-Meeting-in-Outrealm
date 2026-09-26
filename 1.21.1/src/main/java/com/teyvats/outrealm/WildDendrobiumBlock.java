package com.teyvats.outrealm;

import com.teyvats.outrealm.TeyvatDelight;
import com.teyvats.outrealm.WildTeyvatCropBlock;
import com.mojang.serialization.MapCodec;
import java.util.function.Supplier;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class WildDendrobiumBlock
extends WildTeyvatCropBlock {
    public static final MapCodec<WildDendrobiumBlock> CODEC = WildDendrobiumBlock.simpleCodec(WildDendrobiumBlock::new);

    public WildDendrobiumBlock(BlockBehaviour.Properties properties) {
        super(properties, (Supplier<? extends ItemLike>)TeyvatDelight.DENDROBIUM, (Supplier<? extends ItemLike>)TeyvatDelight.DENDROBIUM_SEEDS, WildTeyvatCropBlock.Surface.DENDROBIUM);
    }

    protected MapCodec<WildDendrobiumBlock> codec() {
        return CODEC;
    }
}

