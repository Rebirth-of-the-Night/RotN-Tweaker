package com.rebirthofthenight.rotntweaker.tweaks.rotn;

import gloomyfolken.hooklib.api.Hook;
import gloomyfolken.hooklib.api.HookContainer;
import gloomyfolken.hooklib.api.OnBegin;
import gloomyfolken.hooklib.api.ReturnConstant;
import gloomyfolken.hooklib.asm.ReturnCondition;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import party.lemons.totemexpansion.handler.TotemEventHandler;
import party.lemons.totemexpansion.item.TotemType;

@HookContainer
public class TotemOfRepairingDupeFix {

    @Hook(returnCondition = ReturnCondition.ALWAYS)
    @OnBegin
    public static void onToolBreak(TotemEventHandler handler, PlayerDestroyItemEvent event) {
    }

    @Hook(returnCondition = ReturnCondition.ON_TRUE, returnConstant = @ReturnConstant(booleanValue = false))
    @OnBegin
    public static boolean attemptDamageItem(ItemStack stack, int amount, Random rand, @Nullable EntityPlayerMP damager) {
        if (damager != null) {
            if (stack.getItemDamage() + amount > stack.getMaxDamage()) {
                ItemStack totem = TotemEventHandler.findTotem(damager, TotemType.TOOL_BREAK);
                if (!totem.isEmpty()) {
                    if (TotemEventHandler.activateTotem(damager, totem, null)) {
                        stack.setItemDamage(0);
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
