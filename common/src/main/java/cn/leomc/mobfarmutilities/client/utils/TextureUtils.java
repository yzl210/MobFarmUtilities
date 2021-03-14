package cn.leomc.mobfarmutilities.client.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.math.Matrix4f;
import me.shedaniel.architectury.hooks.FluidStackHooks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.material.Fluid;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class TextureUtils {

    public static TextureAtlasSprite getAtlasTexture(ResourceLocation rl) {
        return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(rl);
    }


    public static void drawHorizontalFluidTank(Matrix4f matrix4f, Fluid fluid, int amount, int tankCapacity, double x, double y, double width, double height) {
        if (fluid == null || amount <= 0) {
            return;
        }

        TextureAtlasSprite icon = FluidStackHooks.getStillTexture(fluid);
        if (icon == null) {
            return;
        }
        int renderAmount;

        renderAmount = (int) Math.max(Math.min(width, amount * width / tankCapacity), 1);

        int posY;

        posY = (int) y;

        Minecraft.getInstance().getTextureManager().bind(InventoryMenu.BLOCK_ATLAS);
        int color = FluidStackHooks.getColor(fluid);
        float r = ((color >> 16) & 0xFF) / 255f;
        float g = ((color >> 8) & 0xFF) / 255f;
        float b = (color & 0xFF) / 255f;
        RenderSystem.color3f(r, g, b);

        RenderSystem.enableBlend();
        for (int i = 0; i < width; i += 16) {
            for (int j = 0; j < renderAmount; j += 16) {

                int drawWidth = Math.min(renderAmount - i, 16);
                int drawHeight = (int) Math.min(height - j, 16);


                int drawX = (int) (x + i);
                int drawY = posY + j;

                float minU = icon.getU0();
                float maxU = icon.getU1();
                float minV = icon.getV0();
                float maxV = icon.getV1();

                Tesselator tessellator = Tesselator.getInstance();
                BufferBuilder tes = tessellator.getBuilder();
                tes.begin(GL11.GL_QUADS, DefaultVertexFormat.POSITION_TEX);
                float v = minV + (maxV - minV) * drawHeight / 16F;
                tes.vertex(matrix4f, drawX, drawY + drawHeight, 0).uv(minU, minV + (maxV - minV) * drawHeight / 16F).endVertex();
                tes.vertex(matrix4f, drawX + drawWidth, drawY + drawHeight, 0).uv(minU + (maxU - minU) * drawWidth / 16F, minV + (maxV - minV) * drawHeight / 16F).endVertex();
                tes.vertex(matrix4f, drawX + drawWidth, drawY, 0).uv(minU + (maxU - minU) * drawWidth / 16F, minV).endVertex();
                tes.vertex(matrix4f, drawX, drawY, 0).uv(minU, minV).endVertex();
                tessellator.end();
            }
        }
        RenderSystem.disableBlend();
        RenderSystem.color3f(1, 1, 1);
    }

    public static void drawVerticalFluidTank(Matrix4f matrix4f, Fluid fluid, int amount, int tankCapacity, double x, double y, double width, double height) {
        if (fluid == null || amount <= 0) {
            return;
        }

        TextureAtlasSprite icon = FluidStackHooks.getStillTexture(fluid);
        if (icon == null) {
            return;
        }

        int renderAmount = (int) Math.max(Math.min(height, amount * height / tankCapacity), 1);
        int posY = (int) (y + height - renderAmount);

        Minecraft.getInstance().getTextureManager().bind(InventoryMenu.BLOCK_ATLAS);
        int color = FluidStackHooks.getColor(fluid);
        float r = ((color >> 16) & 0xFF) / 255f;
        float g = ((color >> 8) & 0xFF) / 255f;
        float b = (color & 0xFF) / 255f;
        RenderSystem.color3f(r, g, b);

        RenderSystem.enableBlend();
        for (int i = 0; i < width; i += 16) {
            for (int j = 0; j < renderAmount; j += 16) {
                int drawWidth = (int) Math.min(width - i, 16);
                int drawHeight = Math.min(renderAmount - j, 16);

                int drawX = (int) (x + i);
                int drawY = posY + j;

                float minU = icon.getU0();
                float maxU = icon.getU1();
                float minV = icon.getV0();
                float maxV = icon.getV1();

                Tesselator tessellator = Tesselator.getInstance();
                BufferBuilder tes = tessellator.getBuilder();
                tes.begin(GL11.GL_QUADS, DefaultVertexFormat.POSITION_TEX);
                float v = minV + (maxV - minV) * drawHeight / 16F;
                tes.vertex(matrix4f, drawX, drawY + drawHeight, 0).uv(minU, minV + (maxV - minV) * drawHeight / 16F).endVertex();
                tes.vertex(matrix4f, drawX + drawWidth, drawY + drawHeight, 0).uv(minU + (maxU - minU) * drawWidth / 16F, minV + (maxV - minV) * drawHeight / 16F).endVertex();
                tes.vertex(matrix4f, drawX + drawWidth, drawY, 0).uv(minU + (maxU - minU) * drawWidth / 16F, minV).endVertex();
                tes.vertex(matrix4f, drawX, drawY, 0).uv(minU, minV).endVertex();
                tessellator.end();
            }
        }
        RenderSystem.disableBlend();
        RenderSystem.color3f(1, 1, 1);
    }


}
