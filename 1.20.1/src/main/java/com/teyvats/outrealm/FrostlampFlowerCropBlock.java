package com.teyvats.outrealm;

import com.teyvats.outrealm.TeyvatCropBlock;
import com.teyvats.outrealm.TeyvatDelight;
import com.teyvats.outrealm.TeyvatTags;
import java.util.function.Supplier;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class FrostlampFlowerCropBlock
extends TeyvatCropBlock {

    public FrostlampFlowerCropBlock(BlockBehaviour.Properties properties) {
        super(properties, TeyvatTags.Blocks.NI_CI_ZHI_FIELDS, (Supplier<? extends ItemLike>)TeyvatDelight.FROSTLAMP_FLOWER, (Supplier<? extends ItemLike>)TeyvatDelight.FROSTLAMP_FLOWER_SEEDS);
    }

}

