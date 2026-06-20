package com.deimoshexxus.netherhexedkingdom.content.custom.items;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class MasoniaeMushroomItem extends BlockItem {

    public MasoniaeMushroomItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 20 * 5, 0)); // 5seconds
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 20 * 30, 0)); // 30seconds
            player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 20 * 30, 0)); // 30seconds

            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

}
