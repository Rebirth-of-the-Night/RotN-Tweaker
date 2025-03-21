package com.rebirthofthenight.rotntweaker.render.overlay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class ClientEventHandler {

    public static void renderScreenOverlay(ScaledResolution resolution, ResourceLocation texture){

        GlStateManager.pushMatrix();

        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);

        Minecraft.getMinecraft().renderEngine.bindTexture(texture);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

        buffer.pos(0, 						resolution.getScaledHeight(), -90).tex(0, 1).endVertex();
        buffer.pos(resolution.getScaledWidth(), resolution.getScaledHeight(), -90).tex(1, 1).endVertex();
        buffer.pos(resolution.getScaledWidth(), 0, 						  -90).tex(1, 0).endVertex();
        buffer.pos(0, 						0, 						  -90).tex(0, 0).endVertex();

        tessellator.draw();

        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();

        GlStateManager.popMatrix();
    }
}
