package com.rebirthofthenight.rotntweaker.modules.betternether.items;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import com.rebirthofthenight.rotntweaker.RotNTweaker;

public class BNItemPickaxe extends ItemPickaxe
{
	private float speed;
	
	public BNItemPickaxe(String name, ToolMaterial material, int maxDamage, float speed)
	{
		super(material);
		this.speed = speed;
		this.setUnlocalizedName(name);
		this.setRegistryName(name);
		this.setCreativeTab(RotNTweaker.BN_TAB);
		this.setMaxDamage(maxDamage);
	}
	
	public float getDestroySpeed(ItemStack stack, IBlockState state)
    {
        return super.getDestroySpeed(stack, state) * speed;
    }
}
