package com.rebirthofthenight.rotntweaker.modules.betternether.blocks;

import net.minecraft.block.BlockButtonWood;
import net.minecraft.block.SoundType;
import com.rebirthofthenight.rotntweaker.modules.betternether.BetterNether;

public class BlockWoodenButton extends BlockButtonWood
{
	public BlockWoodenButton(String name)
	{
		super();
		this.setRegistryName(name);
		this.setUnlocalizedName(name);
		this.setSoundType(SoundType.WOOD);
		this.setHardness(2.0F);
		this.setCreativeTab(BetterNether.BN_TAB);
	}
}
