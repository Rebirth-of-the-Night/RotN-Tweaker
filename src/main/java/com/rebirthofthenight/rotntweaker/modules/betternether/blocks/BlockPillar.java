package com.rebirthofthenight.rotntweaker.modules.betternether.blocks;

import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import com.rebirthofthenight.rotntweaker.RotNTweaker;

public class BlockPillar extends BlockRotatedPillar
{
	protected BlockPillar(String name)
	{
		super(Material.IRON, MapColor.GOLD);
		this.setHardness(3.0F);
		this.setResistance(10.0F);
		this.setSoundType(SoundType.METAL);
		this.setUnlocalizedName(name);
		this.setRegistryName(name);
		this.setCreativeTab(RotNTweaker.BN_TAB);
	}
}
