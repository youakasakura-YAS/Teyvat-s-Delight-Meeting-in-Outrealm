package com.teyvats.outrealm;

import com.teyvats.outrealm.TeyvatDelight;
import com.teyvats.outrealm.WildTeyvatCropBlock;
import java.util.function.Supplier;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class WildWindwheelAsterBlock
extends WildTeyvatCropBlock {

    public WildWindwheelAsterBlock(BlockBehaviour.Properties properties) {
        super(properties, (Supplier<? extends ItemLike>)TeyvatDelight.WINDWHEEL_ASTER, (Supplier<? extends ItemLike>)TeyvatDelight.WINDWHEEL_ASTER_SEEDS, WildTeyvatCropBlock.Surface.GRASS_OR_DIRT);
    }

}

