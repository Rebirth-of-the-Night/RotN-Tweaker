package com.rebirthofthenight.rotntweaker.modules.betternether.blocks;

import net.minecraft.block.BlockNetherBrick;
import net.minecraft.block.SoundType;
import com.rebirthofthenight.rotntweaker.RotNTweaker;

public class BNNetherBrick extends BlockNetherBrick
{
	public BNNetherBrick(String name)
	{
		super();
		this.setCreativeTab(RotNTweaker.BN_TAB);
		this.setRegistryName(name);
		this.setUnlocalizedName(name);
		this.setHardness(2.0F);
		this.setResistance(10.0F);
		this.setSoundType(SoundType.STONE);
	}
}
