package com.teyvats.outrealm;

import com.teyvats.outrealm.TeyvatDelight;
import com.teyvats.outrealm.WildTeyvatCropBlock;
import java.util.function.Supplier;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class WildSmallLampGrassBlock
extends WildTeyvatCropBlock {

    public WildSmallLampGrassBlock(BlockBehaviour.Properties properties) {
        super(properties, (Supplier<? extends ItemLike>)TeyvatDelight.SMALL_LAMP_GRASS, (Supplier<? extends ItemLike>)TeyvatDelight.SMALL_LAMP_GRASS_SEEDS, WildTeyvatCropBlock.Surface.GRASS_OR_DIRT);
    }

}

