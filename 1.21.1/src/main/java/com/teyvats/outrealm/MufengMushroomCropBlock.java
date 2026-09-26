package com.teyvats.outrealm;

import com.teyvats.outrealm.TeyvatCropBlock;
import com.teyvats.outrealm.TeyvatDelight;
import com.teyvats.outrealm.TeyvatTags;
import com.mojang.serialization.MapCodec;
import java.util.function.Supplier;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class MufengMushroomCropBlock
extends TeyvatCropBlock {
    public static final MapCodec<MufengMushroomCropBlock> CODEC = MufengMushroomCropBlock.simpleCodec(MufengMushroomCropBlock::new);

    public MufengMushroomCropBlock(BlockBehaviour.Properties properties) {
        super(properties, TeyvatTags.Blocks.NI_CI_ZHI_FIELDS, (Supplier<? extends ItemLike>)TeyvatDelight.MUFENG_MUSHROOM, (Supplier<? extends ItemLike>)TeyvatDelight.MUFENG_MUSHROOM_SPORES, 7, 1, 0);
    }

    public MapCodec<MufengMushroomCropBlock> codec() {
        return CODEC;
    }
}

