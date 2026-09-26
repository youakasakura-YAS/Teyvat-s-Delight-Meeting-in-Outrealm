package com.teyvats.outrealm;

import com.teyvats.outrealm.TeyvatCropBlock;
import com.teyvats.outrealm.TeyvatDelight;
import com.teyvats.outrealm.TeyvatTags;
import java.util.function.Supplier;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class GrainfruitCropBlock
extends TeyvatCropBlock {

    public GrainfruitCropBlock(BlockBehaviour.Properties properties) {
        super(properties, TeyvatTags.Blocks.XUAN_CI_JADE_FIELDS, (Supplier<? extends ItemLike>)TeyvatDelight.GRAINFRUIT, (Supplier<? extends ItemLike>)TeyvatDelight.GRAINFRUIT_SEEDS, 7, 1, 0);
    }

}

