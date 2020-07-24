package com.rebirthofthenight.rotntweaker.modules.betternether.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import com.rebirthofthenight.rotntweaker.RotNTweaker;

public class BNBlockBone extends Block
{
	public BNBlockBone(String name)
	{
		super(Material.ROCK, MapColor.SAND);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        this.setHardness(2.0F);
        this.setSoundType(SoundType.STONE);
        this.setCreativeTab(RotNTweaker.BN_TAB);
		this.setRegistryName(name);
		this.setUnlocalizedName(name);
	}
}
