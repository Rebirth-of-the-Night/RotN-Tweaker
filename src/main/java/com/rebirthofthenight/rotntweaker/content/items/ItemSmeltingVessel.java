package com.rebirthofthenight.rotntweaker.content.items;

import com.rebirthofthenight.rotntweaker.RotNTweaker;
import com.rebirthofthenight.rotntweaker.content.entity.EntitySmeltingVesselProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemSnowball;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemSmeltingVessel extends ItemSnowball {
    public static final String NAME = "smelting_vessel";

    public ItemSmeltingVessel() {
        super();
        this.setUnlocalizedName(RotNTweaker.MODID + "." + NAME);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {

        ItemStack stack = playerIn.getHeldItem(handIn);

        if (!worldIn.isRemote) {

            if (!playerIn.capabilities.isCreativeMode) {
                stack.shrink(1);
            }

            worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

            double offsetDistance = 1.5;
            double dx = -Math.sin(Math.toRadians(playerIn.rotationYaw)) * Math.cos(Math.toRadians(playerIn.rotationPitch)) * offsetDistance;
            double dz = Math.cos(Math.toRadians(playerIn.rotationYaw)) * Math.cos(Math.toRadians(playerIn.rotationPitch)) * offsetDistance;
            double dy = -Math.sin(Math.toRadians(playerIn.rotationPitch)) * offsetDistance;



            EntitySmeltingVesselProjectile vessel = new EntitySmeltingVesselProjectile(worldIn, playerIn, stack);
            vessel.setPosition(playerIn.posX + dx, playerIn.posY + playerIn.getEyeHeight() + dy, playerIn.posZ + dz);
            vessel.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F, 1.0F);
            vessel.setItemNBT(stack.getTagCompound());
            worldIn.spawnEntity(vessel);

            playerIn.addStat(StatList.getObjectUseStats(this));
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }
}
