package com.teyvats.outrealm;

import com.teyvats.outrealm.TeyvatDelight;
import com.teyvats.outrealm.WildTeyvatCropBlock;
import com.mojang.serialization.MapCodec;
import java.util.function.Supplier;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class WildFrostlampFlowerBlock
extends WildTeyvatCropBlock {
    public static final MapCodec<WildFrostlampFlowerBlock> CODEC = WildFrostlampFlowerBlock.simpleCodec(WildFrostlampFlowerBlock::new);

    public WildFrostlampFlowerBlock(BlockBehaviour.Properties properties) {
        super(properties, (Supplier<? extends ItemLike>)TeyvatDelight.FROSTLAMP_FLOWER, (Supplier<? extends ItemLike>)TeyvatDelight.FROSTLAMP_FLOWER_SEEDS, WildTeyvatCropBlock.Surface.FROSTLAMP);
    }

    protected MapCodec<WildFrostlampFlowerBlock> codec() {
        return CODEC;
    }
}

