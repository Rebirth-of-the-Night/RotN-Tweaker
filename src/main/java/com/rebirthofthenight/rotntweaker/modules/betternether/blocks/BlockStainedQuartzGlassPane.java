package com.rebirthofthenight.rotntweaker.modules.betternether.blocks;

import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.block.SoundType;
import com.rebirthofthenight.rotntweaker.RotNTweaker;

public class BlockStainedQuartzGlassPane extends BlockStainedGlassPane
{
	public BlockStainedQuartzGlassPane(String name)
	{
		this.setCreativeTab(RotNTweaker.BN_TAB);
		this.setUnlocalizedName(name);
		this.setRegistryName(name);
		this.setHardness(3.0F);
		this.setResistance(10.0F);
		this.setSoundType(SoundType.GLASS);
	}
}
