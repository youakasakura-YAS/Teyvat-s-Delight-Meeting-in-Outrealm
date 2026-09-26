package com.teyvats.outrealm;

import com.teyvats.outrealm.TeyvatCropBlock;
import com.teyvats.outrealm.TeyvatDelight;
import com.teyvats.outrealm.TeyvatTags;
import java.util.function.Supplier;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class CallaLilyCropBlock
extends TeyvatCropBlock {

    public CallaLilyCropBlock(BlockBehaviour.Properties properties) {
        super(properties, TeyvatTags.Blocks.CHU_CI_ZHU_FIELDS, (Supplier<? extends ItemLike>)TeyvatDelight.CALLA_LILY, (Supplier<? extends ItemLike>)TeyvatDelight.CALLA_LILY_SEEDS);
    }

}

