package com.rebirthofthenight.rotntweaker.tweaks.rotn.torch.particles;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFlame;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleFlame2 extends ParticleFlame {
    public static EnumParticleTypes FLAME2;

    public static void register() {
        FLAME2 = EnumHelper.addEnum(EnumParticleTypes.class, "FLAME2",
                new Class[]{String.class, int.class, boolean.class},
                "flame2", EnumParticleTypes.values().length, false
        );
        Minecraft.getMinecraft().effectRenderer.registerParticle(FLAME2.getParticleID(), new ParticleFlame2.Factory());
    }

    protected ParticleFlame2(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        this.setParticleTextureIndex(50);
    }


    @SideOnly(Side.CLIENT)
    public static class Factory implements IParticleFactory {

        public Particle createParticle(int particleID, World worldIn,
                                       double xCoordIn, double yCoordIn, double zCoordIn,
                                       double xSpeedIn, double ySpeedIn, double zSpeedIn, int... unused) {
            return new ParticleFlame2(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }
}
