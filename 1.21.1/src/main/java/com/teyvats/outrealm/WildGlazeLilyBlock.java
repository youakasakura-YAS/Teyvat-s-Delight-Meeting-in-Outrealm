package com.teyvats.outrealm;

import com.teyvats.outrealm.GlazeLilyBlooming;
import com.teyvats.outrealm.TeyvatDelight;
import com.teyvats.outrealm.WildTeyvatCropBlock;
import com.mojang.serialization.MapCodec;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;

public class WildGlazeLilyBlock
extends WildTeyvatCropBlock {
    public static final MapCodec<WildGlazeLilyBlock> CODEC = WildGlazeLilyBlock.simpleCodec(WildGlazeLilyBlock::new);

    public WildGlazeLilyBlock(BlockBehaviour.Properties properties) {
        super(properties, (Supplier<? extends ItemLike>)TeyvatDelight.GLAZE_LILY, (Supplier<? extends ItemLike>)TeyvatDelight.GLAZE_LILY_SEEDS, WildTeyvatCropBlock.Surface.GRASS_DIRT_OR_MUD);
        this.registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)GlazeLilyBlooming.BLOOMING, (Comparable)Boolean.valueOf(false)));
    }

    protected MapCodec<WildGlazeLilyBlock> codec() {
        return CODEC;
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        return state == null ? null : (BlockState)state.setValue((Property)GlazeLilyBlooming.BLOOMING, (Comparable)Boolean.valueOf(GlazeLilyBlooming.shouldBloom(context.getLevel())));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(new Property[]{GlazeLilyBlooming.BLOOMING});
    }
}

