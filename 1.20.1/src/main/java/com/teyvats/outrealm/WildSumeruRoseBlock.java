package com.teyvats.outrealm;

import com.teyvats.outrealm.TeyvatDelight;
import com.teyvats.outrealm.WildTeyvatCropBlock;
import java.util.function.Supplier;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class WildSumeruRoseBlock
extends WildTeyvatCropBlock {

    public WildSumeruRoseBlock(BlockBehaviour.Properties properties) {
        super(properties, (Supplier<? extends ItemLike>)TeyvatDelight.SUMERU_ROSE, (Supplier<? extends ItemLike>)TeyvatDelight.SUMERU_ROSE_SEEDS, WildTeyvatCropBlock.Surface.GRASS_OR_DIRT);
    }

}

