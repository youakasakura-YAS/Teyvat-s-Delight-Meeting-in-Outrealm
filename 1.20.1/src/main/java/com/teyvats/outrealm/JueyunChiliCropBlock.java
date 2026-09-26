package com.teyvats.outrealm;

import com.teyvats.outrealm.TeyvatCropBlock;
import com.teyvats.outrealm.TeyvatDelight;
import com.teyvats.outrealm.TeyvatTags;
import java.util.function.Supplier;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class JueyunChiliCropBlock
extends TeyvatCropBlock {

    public JueyunChiliCropBlock(BlockBehaviour.Properties properties) {
        super(properties, TeyvatTags.Blocks.XUAN_CI_JADE_FIELDS, (Supplier<? extends ItemLike>)TeyvatDelight.JUEYUN_CHILI, (Supplier<? extends ItemLike>)TeyvatDelight.JUEYUN_CHILI_SEEDS, 7, 3, 0);
    }

}

