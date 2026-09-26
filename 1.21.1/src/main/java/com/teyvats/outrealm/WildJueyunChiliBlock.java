package com.teyvats.outrealm;

import com.teyvats.outrealm.TeyvatDelight;
import com.teyvats.outrealm.WildTeyvatCropBlock;
import com.mojang.serialization.MapCodec;
import java.util.function.Supplier;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class WildJueyunChiliBlock
extends WildTeyvatCropBlock {
    public static final MapCodec<WildJueyunChiliBlock> CODEC = WildJueyunChiliBlock.simpleCodec(WildJueyunChiliBlock::new);

    public WildJueyunChiliBlock(BlockBehaviour.Properties properties) {
        super(properties, (Supplier<? extends ItemLike>)TeyvatDelight.JUEYUN_CHILI, (Supplier<? extends ItemLike>)TeyvatDelight.JUEYUN_CHILI_SEEDS, WildTeyvatCropBlock.Surface.ROCKY);
    }

    protected MapCodec<WildJueyunChiliBlock> codec() {
        return CODEC;
    }
}

