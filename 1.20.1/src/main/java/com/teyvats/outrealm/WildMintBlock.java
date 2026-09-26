package com.teyvats.outrealm;

import com.teyvats.outrealm.TeyvatDelight;
import com.teyvats.outrealm.WildTeyvatCropBlock;
import java.util.function.Supplier;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class WildMintBlock
extends WildTeyvatCropBlock {

    public WildMintBlock(BlockBehaviour.Properties properties) {
        super(properties, (Supplier<? extends ItemLike>)TeyvatDelight.MINT, (Supplier<? extends ItemLike>)TeyvatDelight.MINT_SEEDS, WildTeyvatCropBlock.Surface.ROCKY);
    }

}

