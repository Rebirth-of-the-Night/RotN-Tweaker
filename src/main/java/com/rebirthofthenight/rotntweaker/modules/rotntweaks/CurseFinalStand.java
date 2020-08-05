package com.rebirthofthenight.rotntweaker.modules.rotntweaks;

import com.rebirthofthenight.rotntweaker.RotNTweaker;

import java.util.Map;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

@Mod.EventBusSubscriber(modid = RotNTweaker.MODID)
public class CurseFinalStand {
	private static Enchantment curseFinalStand;
	
	public static void postInit(FMLPostInitializationEvent event) {
		curseFinalStand = Enchantment.getEnchantmentByLocation("contenttweaker:curse_finalstand");
		
		if (curseFinalStand == null)
			RotNTweaker.logger.error("Curse of the Final Stand enchantment not found!");
	}
	
	public static void onAnvilUpdate(AnvilUpdateEvent event) {
		ItemStack left = event.getLeft();
		ItemStack right = event.getRight();
		
		if (left == null || right == null)
			return;
		
		if (curseFinalStand == null)
			return;
		
		if (event.getOutput().isItemStackDamageable()) {
			if (left.isItemEnchanted()) {
				Map<Enchantment, Integer> enchs = EnchantmentHelper.getEnchantments(left);
				if (enchs.containsKey(curseFinalStand) && event.getOutput().getItem() == left.getItem()) {
					if (left.isItemStackDamageable() && event.getOutput().getItemDamage() > left.getItemDamage()) {
						event.setCanceled(true);
						return;
					}
					if (right.isItemStackDamageable() && event.getOutput().getItemDamage() > right.getItemDamage()) {
						event.setCanceled(true);
						return;
					}
				}
			}
			if (right.isItemEnchanted()) {
				Map<Enchantment, Integer> enchs = EnchantmentHelper.getEnchantments(right);
				if (enchs.containsKey(curseFinalStand) && event.getOutput().getItem() == right.getItem()) {
					if (left.isItemStackDamageable() && event.getOutput().getItemDamage() > left.getItemDamage()) {
						event.setCanceled(true);
						return;
					}
					if (right.isItemStackDamageable() && event.getOutput().getItemDamage() > right.getItemDamage()) {
						event.setCanceled(true);
						return;
					}
				}
			}
		}
	}
}
