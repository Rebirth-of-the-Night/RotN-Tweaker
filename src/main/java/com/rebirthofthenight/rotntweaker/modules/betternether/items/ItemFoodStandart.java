package com.rebirthofthenight.rotntweaker.modules.betternether.items;

import net.minecraft.item.ItemFood;
import com.rebirthofthenight.rotntweaker.modules.betternether.BetterNether;

public class ItemFoodStandart extends ItemFood
{
	public ItemFoodStandart(String name, int amount, float saturation)
	{
		super(amount, saturation, false);
		this.setRegistryName(name);
		this.setUnlocalizedName(name);
		this.setCreativeTab(BetterNether.BN_TAB);
	}
}
