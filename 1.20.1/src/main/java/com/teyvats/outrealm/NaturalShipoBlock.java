package com.teyvats.outrealm;

import com.teyvats.outrealm.TeyvatBonusDrops;
import com.teyvats.outrealm.TeyvatDelight;
import com.teyvats.outrealm.TeyvatTags;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class NaturalShipoBlock
extends BushBlock {
    private static final VoxelShape SHAPE = Block.box((double)0.0, (double)0.0, (double)1.0, (double)16.0, (double)8.0, (double)16.0);
    private final Supplier<? extends ItemLike> mineralItem;
    private final TagKey<Block> surfaceTag;

    public NaturalShipoBlock(BlockBehaviour.Properties properties) {
        this(properties, (Supplier<? extends ItemLike>)TeyvatDelight.SHIPO, TeyvatTags.Blocks.NATURAL_SHIPO_SURFACES);
    }

    public NaturalShipoBlock(BlockBehaviour.Properties properties, Supplier<? extends ItemLike> mineralItem, TagKey<Block> surfaceTag) {
        super(properties);
        this.mineralItem = mineralItem;
        this.surfaceTag = surfaceTag;
    }


    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    public boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(this.surfaceTag);
    }

    public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        return false;
    }

    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return new ItemStack((ItemLike)this.asItem());
    }

    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        super.playerDestroy(level, player, pos, state, blockEntity, tool);
        if (!level.isClientSide && !player.isCreative() && tool.is(ItemTags.PICKAXES)) {
            TeyvatBonusDrops.dropPrimogemFromMineral(level, pos, player, tool);
            NaturalShipoBlock.popResource((Level)level, (BlockPos)pos, (ItemStack)new ItemStack(this.mineralItem.get()));
            level.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_BREAK, SoundSource.BLOCKS, 1.0f, 1.0f);
        }
    }
}

