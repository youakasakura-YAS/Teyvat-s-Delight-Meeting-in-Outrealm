package com.teyvats.outrealm;

import com.teyvats.outrealm.StarconchEntity;
import com.teyvats.outrealm.TeyvatDelight;
import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class StarconchItem
extends Item {
    public StarconchItem(Item.Properties properties) {
        super(properties);
    }

    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (!(level instanceof ServerLevel)) {
            return InteractionResult.SUCCESS;
        }
        ServerLevel serverLevel = (ServerLevel)level;
        ItemStack stack = context.getItemInHand();
        BlockPos clickedPos = context.getClickedPos();
        Direction face = context.getClickedFace();
        BlockState clickedState = level.getBlockState(clickedPos);
        BlockPos spawnPos = clickedState.getCollisionShape((BlockGetter)level, clickedPos).isEmpty() ? clickedPos : clickedPos.relative(face);
        StarconchEntity entity = (StarconchEntity)((EntityType)TeyvatDelight.STARCONCH.get()).spawn(serverLevel, stack, context.getPlayer(), spawnPos, MobSpawnType.SPAWN_EGG, true, !Objects.equals(clickedPos, spawnPos) && face == Direction.UP);
        if (entity != null && !context.getPlayer().getAbilities().instabuild) {
            stack.shrink(1);
        }
        return InteractionResult.CONSUME;
    }
}

