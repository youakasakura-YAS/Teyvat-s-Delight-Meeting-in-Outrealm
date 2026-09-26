package com.teyvats.outrealm;

import com.teyvats.outrealm.TeyvatDelight;
import com.teyvats.outrealm.WildTeyvatCropBlock;
import java.util.function.Supplier;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class WildFluorescentFungusBlock
extends WildTeyvatCropBlock {

    public WildFluorescentFungusBlock(BlockBehaviour.Properties properties) {
        super(properties, (Supplier<? extends ItemLike>)TeyvatDelight.FLUORESCENT_FUNGUS, (Supplier<? extends ItemLike>)TeyvatDelight.FLUORESCENT_FUNGUS_SPORES, WildTeyvatCropBlock.Surface.FLUORESCENT_FUNGUS);
    }

}

