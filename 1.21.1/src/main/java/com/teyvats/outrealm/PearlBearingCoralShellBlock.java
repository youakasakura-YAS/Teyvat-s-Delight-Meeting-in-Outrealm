package com.teyvats.outrealm;

import com.teyvats.outrealm.GrowingCoralShellBlock;
import com.teyvats.outrealm.TeyvatDelight;
import com.teyvats.outrealm.TeyvatTags;
import com.mojang.serialization.MapCodec;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
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
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PearlBearingCoralShellBlock
extends BushBlock {
    public static final MapCodec<PearlBearingCoralShellBlock> CODEC = PearlBearingCoralShellBlock.simpleCodec(PearlBearingCoralShellBlock::new);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    private static final TagKey<Item> KNIFE_TAG = TagKey.create((ResourceKey)Registries.ITEM, (ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"c", (String)"tools/knife"));
    private static final VoxelShape SHAPE = Block.box((double)2.0, (double)0.0, (double)2.0, (double)14.0, (double)8.0, (double)14.0);

    public PearlBearingCoralShellBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH));
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
        return (BlockState)this.defaultBlockState().setValue((Property)FACING, (Comparable)context.getHorizontalDirection().getOpposite());
    }

    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(TeyvatTags.Blocks.CHU_CI_ZHU_FIELDS);
    }

    protected boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        return false;
    }

    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!stack.is(KNIFE_TAG)) {
            PearlBearingCoralShellBlock.showKnifeHint(level, player);
            return ItemInteractionResult.sidedSuccess((boolean)level.isClientSide);
        }
        if (!level.isClientSide) {
            PearlBearingCoralShellBlock.popResource((Level)level, (BlockPos)pos, (ItemStack)new ItemStack((ItemLike)TeyvatDelight.CORAL_PEARL.get()));
            BlockState next = (BlockState)((GrowingCoralShellBlock)((Object)TeyvatDelight.GROWING_CORAL_SHELL.get())).defaultBlockState().setValue((Property)FACING, (Comparable)((Direction)state.getValue((Property)FACING)));
            level.setBlock(pos, next, 2);
            if (!player.isCreative()) {
                EquipmentSlot slot = hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
                stack.hurtAndBreak(1, (LivingEntity)player, slot);
            }
        }
        return ItemInteractionResult.sidedSuccess((boolean)level.isClientSide);
    }

    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        PearlBearingCoralShellBlock.showKnifeHint(level, player);
        return InteractionResult.sidedSuccess((boolean)level.isClientSide);
    }

    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return new ItemStack((ItemLike)TeyvatDelight.CORAL_SHELL.get());
    }

    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        super.playerDestroy(level, player, pos, state, blockEntity, tool);
        if (!level.isClientSide && !player.isCreative()) {
            PearlBearingCoralShellBlock.popResource((Level)level, (BlockPos)pos, (ItemStack)new ItemStack((ItemLike)TeyvatDelight.CORAL_SHELL.get()));
            PearlBearingCoralShellBlock.popResource((Level)level, (BlockPos)pos, (ItemStack)new ItemStack((ItemLike)TeyvatDelight.CORAL_PEARL.get()));
        }
    }

    private static void showKnifeHint(Level level, Player player) {
        if (!level.isClientSide) {
            player.displayClientMessage((Component)Component.translatable((String)"message.teyvats_delight_meeting_in_outrealm.coral_shell.needs_knife"), true);
        }
    }
}

