package com.teyvats.outrealm;

import com.teyvats.outrealm.TeyvatDelight;
import com.teyvats.outrealm.WildTeyvatCropBlock;
import com.mojang.serialization.MapCodec;
import java.util.function.Supplier;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class WildFluorescentFungusBlock
extends WildTeyvatCropBlock {
    public static final MapCodec<WildFluorescentFungusBlock> CODEC = WildFluorescentFungusBlock.simpleCodec(WildFluorescentFungusBlock::new);

    public WildFluorescentFungusBlock(BlockBehaviour.Properties properties) {
        super(properties, (Supplier<? extends ItemLike>)TeyvatDelight.FLUORESCENT_FUNGUS, (Supplier<? extends ItemLike>)TeyvatDelight.FLUORESCENT_FUNGUS_SPORES, WildTeyvatCropBlock.Surface.FLUORESCENT_FUNGUS);
    }

    protected MapCodec<WildFluorescentFungusBlock> codec() {
        return CODEC;
    }
}

