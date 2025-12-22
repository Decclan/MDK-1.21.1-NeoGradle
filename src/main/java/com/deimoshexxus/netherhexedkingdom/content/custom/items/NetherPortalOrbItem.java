package com.deimoshexxus.netherhexedkingdom.content.custom.items;

import com.deimoshexxus.netherhexedkingdom.content.entities.NetherPortalOrbEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class NetherPortalOrbItem extends Item {

    public NetherPortalOrbItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            NetherPortalOrbEntity orb = new NetherPortalOrbEntity(level, player);
            orb.setItem(stack);
            orb.shootFromRotation(player, player.getXRot(), player.getYRot(),
                    0.0F, 1.3F, 1.0F);
            level.addFreshEntity(orb);
        }

        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }
}
