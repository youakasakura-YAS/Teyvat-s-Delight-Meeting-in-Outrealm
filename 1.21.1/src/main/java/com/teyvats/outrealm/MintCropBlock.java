package com.teyvats.outrealm;

import com.teyvats.outrealm.TeyvatCropBlock;
import com.teyvats.outrealm.TeyvatDelight;
import com.teyvats.outrealm.TeyvatTags;
import com.mojang.serialization.MapCodec;
import java.util.function.Supplier;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class MintCropBlock
extends TeyvatCropBlock {
    public static final MapCodec<MintCropBlock> CODEC = MintCropBlock.simpleCodec(MintCropBlock::new);

    public MintCropBlock(BlockBehaviour.Properties properties) {
        super(properties, TeyvatTags.Blocks.XUAN_CI_JADE_FIELDS, (Supplier<? extends ItemLike>)TeyvatDelight.MINT, (Supplier<? extends ItemLike>)TeyvatDelight.MINT_SEEDS, 5, 1, 0);
    }

    public MapCodec<MintCropBlock> codec() {
        return CODEC;
    }
}

