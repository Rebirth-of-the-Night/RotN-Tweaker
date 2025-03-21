package com.rebirthofthenight.rotntweaker.content.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntitySmeltingVesselProjectile extends EntityThrowable {
    private static final double RANDOM_HORIZ = 0.8;
    private static final double RANDOM_VERT = 0.8;

    public EntitySmeltingVesselProjectile(World worldIn) {
        super(worldIn);
    }

    public EntitySmeltingVesselProjectile(World worldIn, EntityLivingBase shooter, ItemStack snowball) {
        super(worldIn, shooter);
    }

    @Override
    protected void onImpact(RayTraceResult result) {

        if (result.entityHit != null) {
            result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 4.0F);
        }

        //Check and drop stored items
        NBTTagList storedItems = this.getEntityData().getTagList("storedItems", 10);
        for (int i = 0; i < storedItems.tagCount(); i++) {
            NBTTagCompound itemTag = storedItems.getCompoundTagAt(i);
            ItemStack stack = new ItemStack(itemTag);
            EntityItem entityItem = new EntityItem(world, result.hitVec.x, result.hitVec.y, result.hitVec.z, stack);
            world.spawnEntity(entityItem);
        }

        spawnParticle(world, EnumParticleTypes.SMOKE_NORMAL, result.hitVec.x, result.hitVec.y, result.hitVec.z, 12);
        world.playSound(null, result.hitVec.x, result.hitVec.y, result.hitVec.z, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.NEUTRAL, 0.5F, 0.4F / (world.rand.nextFloat() * 0.4F + 0.8F));

        this.world.setEntityState(this, (byte)3);
        this.setDead();
    }

    public void setItemNBT(NBTTagCompound nbt) {
        if (nbt != null && nbt.hasKey("storedItems")) {
            NBTTagList storedItems = nbt.getTagList("storedItems", 10);
            this.getEntityData().setTag("storedItems", storedItems);
        }
    }
        /*
    /give @p rotntweaker:smelting_vessel 1 0 {storedItems:[{id:"minecraft:diamond",Count:1},{id:"minecraft:iron_ingot",Count:3}]}
     */

    private static void spawnParticle(World world, EnumParticleTypes sparkle, double x, double y, double z, int count) {
        for (int countparticles = 0; countparticles <= count; ++countparticles) {
            world.spawnParticle(sparkle,
                    x + getHorizRandom(world, RANDOM_HORIZ),
                    y + getVertRandom(world, RANDOM_VERT),
                    z + getHorizRandom(world, RANDOM_HORIZ),
                    0.0D, 0.0D, 0.0D);
        }
    }

    private static double getVertRandom(World world, double rando) {
        return world.rand.nextDouble() * (double) rando - (double) 0.1;
    }

    private static double getHorizRandom(World world, double rando) {
        return (world.rand.nextDouble() - 0.5D) * (double) rando;
    }

}
