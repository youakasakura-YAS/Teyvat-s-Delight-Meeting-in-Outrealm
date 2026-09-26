package com.teyvats.outrealm;

import com.teyvats.outrealm.TeyvatDelight;
import com.teyvats.outrealm.WildTeyvatCropBlock;
import com.mojang.serialization.MapCodec;
import java.util.function.Supplier;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class WildMintBlock
extends WildTeyvatCropBlock {
    public static final MapCodec<WildMintBlock> CODEC = WildMintBlock.simpleCodec(WildMintBlock::new);

    public WildMintBlock(BlockBehaviour.Properties properties) {
        super(properties, (Supplier<? extends ItemLike>)TeyvatDelight.MINT, (Supplier<? extends ItemLike>)TeyvatDelight.MINT_SEEDS, WildTeyvatCropBlock.Surface.ROCKY);
    }

    protected MapCodec<WildMintBlock> codec() {
        return CODEC;
    }
}

