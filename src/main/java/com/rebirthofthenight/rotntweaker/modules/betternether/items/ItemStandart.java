package com.rebirthofthenight.rotntweaker.modules.betternether.items;

import net.minecraft.item.Item;
import com.rebirthofthenight.rotntweaker.RotNTweaker;

public class ItemStandart extends Item
{
	public ItemStandart(String name)
	{
		this.setRegistryName(name);
		this.setUnlocalizedName(name);
		this.setCreativeTab(RotNTweaker.BN_TAB);
	}
}
