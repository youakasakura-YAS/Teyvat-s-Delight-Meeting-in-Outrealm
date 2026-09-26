package com.teyvats.outrealm;

import com.teyvats.outrealm.TeyvatBonusDrops;
import com.teyvats.outrealm.TeyvatDelight;
import com.mojang.serialization.MapCodec;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class NaturalJinghuagusuiBlock
extends BushBlock {
    public static final MapCodec<NaturalJinghuagusuiBlock> CODEC = NaturalJinghuagusuiBlock.simpleCodec(NaturalJinghuagusuiBlock::new);
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    private static final VoxelShape SHAPE = Block.box((double)3.0, (double)3.0, (double)3.0, (double)13.0, (double)13.0, (double)13.0);

    public NaturalJinghuagusuiBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.UP));
    }

    protected MapCodec<? extends BushBlock> codec() {
        return CODEC;
    }

    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{FACING});
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState)this.defaultBlockState().setValue((Property)FACING, (Comparable)context.getClickedFace());
    }

    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction facing = (Direction)state.getValue((Property)FACING);
        BlockPos supportPos = pos.relative(facing.getOpposite());
        return level.getBlockState(supportPos).is(Blocks.AMETHYST_BLOCK);
    }

    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return new ItemStack((ItemLike)TeyvatDelight.JINGHUAGUSUI.get());
    }

    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        super.playerDestroy(level, player, pos, state, blockEntity, tool);
        if (!level.isClientSide && !player.isCreative() && tool.is(ItemTags.PICKAXES)) {
            TeyvatBonusDrops.dropPrimogemFromMineral(level, pos, player, tool);
            NaturalJinghuagusuiBlock.popResource((Level)level, (BlockPos)pos, (ItemStack)new ItemStack((ItemLike)TeyvatDelight.JINGHUAGUSUI.get()));
            level.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_BREAK, SoundSource.BLOCKS, 1.0f, 1.0f);
        }
    }
}

