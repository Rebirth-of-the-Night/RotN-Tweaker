package com.rebirthofthenight.rotntweaker.modules.betternether.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import com.rebirthofthenight.rotntweaker.RotNTweaker;

public class BlockStalagnatePlanks extends Block
{
	public BlockStalagnatePlanks()
	{
		super(Material.WOOD, MapColor.LIME_STAINED_HARDENED_CLAY);
		this.setRegistryName("stalagnate_planks");
		this.setUnlocalizedName("stalagnate_planks");
		this.setSoundType(SoundType.WOOD);
		this.setHardness(2.0F);
		this.setCreativeTab(RotNTweaker.BN_TAB);
	}
}
