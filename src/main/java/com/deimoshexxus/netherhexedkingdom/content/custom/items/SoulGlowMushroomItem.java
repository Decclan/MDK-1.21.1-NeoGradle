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
                    20 * 30,  // 30 seconds
                    0
            ));
        }

        return result;
    }
}
