package com.rebirthofthenight.rotntweaker.content.potions.compat;

import com.google.common.collect.Maps;
import com.rebirthofthenight.rotntweaker.RotNTweaker;
import gloomyfolken.hooklib.api.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import java.util.Map;

@HookContainer
public class PotionHooks {




    /*

    //TODO: Hook EntityLivingBase.class: between line 589 and 590
    @Hook(targetMethod = "updatePotionEffects")
    @OnExpression(expressionPattern = "someVarCalculation", shift = Shift.BEFORE)
    protected void updatePotionEffects(EntityLivingBase entityLivingBase, @LocalVariable(id = 11) Map<Potion, PotionEffect> activePotionsMap) {
        if (this.activePotionsMap.size() == 0 || (this.activePotionsMap.size() == 2 &&
                this.activePotionsMap.containsKey(RotNTweaker.POTION_FREEZING) && this.activePotionsMap.containsKey(RotNTweaker.POTION_SWELTERING))) return;
    }

    private static double someVarCalculation(int i) {
        return (double)(i >> 16 & 255) / 255.0;
    }
    */
}
