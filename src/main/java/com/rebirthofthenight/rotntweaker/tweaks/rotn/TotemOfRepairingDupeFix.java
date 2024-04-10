package com.rebirthofthenight.rotntweaker.tweaks.rotn;

import gloomyfolken.hooklib.api.*;
import java.util.*;
import javax.annotation.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraftforge.event.entity.player.*;
import party.lemons.totemexpansion.handler.*;
import party.lemons.totemexpansion.item.*;

@HookContainer
public class TotemOfRepairingDupeFix {

    @Hook
    @OnBegin
    public static ReturnSolve<Void> onToolBreak(TotemEventHandler handler, PlayerDestroyItemEvent event) {
        return ReturnSolve.yes(null);
    }

    @Hook
    @OnBegin
    public static ReturnSolve<Boolean> attemptDamageItem(ItemStack stack, int amount, Random rand, @Nullable EntityPlayerMP damager) {
        if (damager != null) {
            if (stack.getItemDamage() + amount > stack.getMaxDamage()) {
                ItemStack totem = TotemEventHandler.findTotem(damager, TotemType.TOOL_BREAK);
                if (!totem.isEmpty()) {
                    if (TotemEventHandler.activateTotem(damager, totem, null)) {
                        stack.setItemDamage(0);
                        return ReturnSolve.yes(false);
                    }
                }
            }
        }
        return ReturnSolve.no();
    }
}
