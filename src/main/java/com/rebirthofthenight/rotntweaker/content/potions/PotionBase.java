package com.rebirthofthenight.rotntweaker.content.potions;

import com.rebirthofthenight.rotntweaker.RotNTweaker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class PotionBase extends Potion {

    private ResourceLocation icon;
    private boolean beneficial;

    public PotionBase(String name, boolean b, int potionColor) {
        super(false, potionColor);
        //this.applyAttributesModifiersToEntity(entityLivingBaseIn, attributeMapIn, amplifier);
        this.beneficial = b;
        this.setIcon(new ResourceLocation(RotNTweaker.MODID, "textures/potions/" + name + ".png"));
        this.setPotionName("potion." + name);
    }

    @Override
    public void affectEntity(Entity source, Entity indirectSource, EntityLivingBase entityLivingBaseIn, int amplifier, double health) {
        super.affectEntity(source, indirectSource, entityLivingBaseIn, amplifier, health);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isBeneficial() {
        return this.beneficial;//decides top or bottom row
    }

    /*
    @Override
    @SideOnly(Side.CLIENT)
    public void renderInventoryEffect(int x, int y, PotionEffect effect, net.minecraft.client.Minecraft mc) {
        UtilTextureRender.drawTextureSquare(getIcon(), x + 6, y + 6, Const.SQ - 2);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderHUDEffect(int x, int y, PotionEffect effect, net.minecraft.client.Minecraft mc, float alpha) {
        if (mc.gameSettings.showDebugInfo == false)//dont texture on top of the debug text
            UtilTextureRender.drawTextureSquare(getIcon(), x + 4, y + 4, Const.SQ - 2);
    }

     */

    public static ResourceLocation loc(String name) {
        return new ResourceLocation(RotNTweaker.MODID, name);
    }

    public ResourceLocation getIcon(PotionEffect effect) {
        return icon;
    }

    public void setIcon(ResourceLocation icon) {
        this.icon = icon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderInventoryEffect(PotionEffect effect, Gui gui, int x, int y, float z) {
        renderEffect(effect, x + 6, y + 7, 1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderHUDEffect(int x, int y, PotionEffect effect, Minecraft mc, float alpha) {
        mc.renderEngine.bindTexture(getIcon(effect));
        Gui.drawModalRectWithCustomSizedTexture(x + 4, y + 4, 0, 0, 16, 16, 16, 16);
    }

    @SideOnly(Side.CLIENT)
    protected void renderEffect(PotionEffect effect, int x, int y, float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.color(1, 1, 1, alpha);
        Minecraft.getMinecraft().renderEngine.bindTexture(getIcon(effect));
        Gui.drawScaledCustomSizeModalRect(x, y, 0, 0 , 18, 18, 18, 18, 18, 18);
        GlStateManager.popMatrix();
    }
}