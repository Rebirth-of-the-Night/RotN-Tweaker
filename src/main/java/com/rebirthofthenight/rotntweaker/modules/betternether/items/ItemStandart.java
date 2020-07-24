package com.rebirthofthenight.rotntweaker.modules.betternether.items;

import net.minecraft.item.Item;
import com.rebirthofthenight.rotntweaker.modules.betternether.BetterNether;

public class ItemStandart extends Item
{
	public ItemStandart(String name)
	{
		this.setRegistryName(name);
		this.setUnlocalizedName(name);
		this.setCreativeTab(BetterNether.BN_TAB);
	}
}
