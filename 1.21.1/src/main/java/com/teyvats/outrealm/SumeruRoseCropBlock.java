package com.teyvats.outrealm;

import com.teyvats.outrealm.TeyvatCropBlock;
import com.teyvats.outrealm.TeyvatDelight;
import com.teyvats.outrealm.TeyvatTags;
import com.mojang.serialization.MapCodec;
import java.util.function.Supplier;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class SumeruRoseCropBlock
extends TeyvatCropBlock {
    public static final MapCodec<SumeruRoseCropBlock> CODEC = SumeruRoseCropBlock.simpleCodec(SumeruRoseCropBlock::new);

    public SumeruRoseCropBlock(BlockBehaviour.Properties properties) {
        super(properties, TeyvatTags.Blocks.XUAN_CI_JADE_FIELDS, (Supplier<? extends ItemLike>)TeyvatDelight.SUMERU_ROSE, (Supplier<? extends ItemLike>)TeyvatDelight.SUMERU_ROSE_SEEDS, 4, 1, 0);
    }

    public MapCodec<SumeruRoseCropBlock> codec() {
        return CODEC;
    }
}

