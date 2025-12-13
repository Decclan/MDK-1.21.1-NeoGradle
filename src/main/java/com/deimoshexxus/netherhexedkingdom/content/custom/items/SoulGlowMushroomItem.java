package com.deimoshexxus.netherhexedkingdom.content.custom.items;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class SoulGlowMushroomItem extends BlockItem {

    public SoulGlowMushroomItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        ItemStack result = super.finishUsingItem(stack, level, entity);

        if (!level.isClientSide) {
            entity.addEffect(new MobEffectInstance(
                    MobEffects.NIGHT_VISION,
                    20 * 720,  // 6 minutes
                    1
            ));
            entity.addEffect(new MobEffectInstance(
                    MobEffects.LUCK,
                    20 * 120,  // 2 minutes
                    0
            ));
            entity.addEffect(new MobEffectInstance(
                    MobEffects.CONFUSION,
                    20 * 15,  // 15 seconds
                    1
            ));
            entity.addEffect(new MobEffectInstance(
                    MobEffects.MOVEMENT_SLOWDOWN,
                    20 * 20,  // 20 seconds
                    0
            ));
            entity.addEffect(new MobEffectInstance(
                    MobEffects.REGENERATION,
                    20 * 20,  // 20 seconds
                    0
            ));
            entity.addEffect(new MobEffectInstance(
                    MobEffects.HUNGER,
                    20 * 35,  // 35 seconds
                    0
            ));
        }
        return result;
    }
}
