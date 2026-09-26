package com.teyvats.outrealm;

import com.teyvats.outrealm.TeyvatDelight;
import com.teyvats.outrealm.WildTeyvatCropBlock;
import java.util.function.Supplier;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class WildFrostlampFlowerBlock
extends WildTeyvatCropBlock {

    public WildFrostlampFlowerBlock(BlockBehaviour.Properties properties) {
        super(properties, (Supplier<? extends ItemLike>)TeyvatDelight.FROSTLAMP_FLOWER, (Supplier<? extends ItemLike>)TeyvatDelight.FROSTLAMP_FLOWER_SEEDS, WildTeyvatCropBlock.Surface.FROSTLAMP);
    }

}

