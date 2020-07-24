package com.rebirthofthenight.rotntweaker.modules.betternether.items;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemSoup;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import com.rebirthofthenight.rotntweaker.RotNTweaker;

public class ItemSoupStandart extends ItemSoup
{
	public ItemSoupStandart(String name, int amount)
	{
		super(amount);
		this.setRegistryName(name);
		this.setUnlocalizedName(name);
		this.setCreativeTab(RotNTweaker.BN_TAB);
	}
	
	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
    {
        super.onItemUseFinish(stack, worldIn, entityLiving);
        return new ItemStack(ItemsRegister.STALAGNATE_BOWL);
    }
}
