package com.teyvats.outrealm;

import com.teyvats.outrealm.TeyvatDelight;
import com.teyvats.outrealm.WildTeyvatCropBlock;
import java.util.function.Supplier;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class WildYunyanCrackleafBlock
extends WildTeyvatCropBlock {

    public WildYunyanCrackleafBlock(BlockBehaviour.Properties properties) {
        super(properties, (Supplier<? extends ItemLike>)TeyvatDelight.YUNYAN_LIEYE, (Supplier<? extends ItemLike>)TeyvatDelight.YUNYAN_LIEYE_SEEDS, WildTeyvatCropBlock.Surface.STONE_OR_TERRACOTTA);
    }

}

