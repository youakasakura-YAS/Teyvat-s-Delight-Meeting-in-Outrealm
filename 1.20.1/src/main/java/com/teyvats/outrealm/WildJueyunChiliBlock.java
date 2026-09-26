package com.teyvats.outrealm;

import com.teyvats.outrealm.TeyvatDelight;
import com.teyvats.outrealm.WildTeyvatCropBlock;
import java.util.function.Supplier;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class WildJueyunChiliBlock
extends WildTeyvatCropBlock {

    public WildJueyunChiliBlock(BlockBehaviour.Properties properties) {
        super(properties, (Supplier<? extends ItemLike>)TeyvatDelight.JUEYUN_CHILI, (Supplier<? extends ItemLike>)TeyvatDelight.JUEYUN_CHILI_SEEDS, WildTeyvatCropBlock.Surface.ROCKY);
    }

}

