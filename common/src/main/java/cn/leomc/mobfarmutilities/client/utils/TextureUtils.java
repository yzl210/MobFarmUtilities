package cn.leomc.mobfarmutilities.client.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import me.shedaniel.architectury.hooks.FluidStackHooks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class TextureUtils {

    public static TextureAtlasSprite getAtlasTexture(ResourceLocation rl) {
        return Minecraft.getInstance().getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE).apply(rl);
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

        Minecraft.getInstance().getTextureManager().bindTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE);
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

                float minU = icon.getMinU();
                float maxU = icon.getMaxU();
                float minV = icon.getMinV();
                float maxV = icon.getMaxV();

                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder tes = tessellator.getBuffer();
                tes.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
                float v = minV + (maxV - minV) * drawHeight / 16F;
                tes.pos(matrix4f, drawX, drawY + drawHeight, 0).tex(minU, minV + (maxV - minV) * drawHeight / 16F).endVertex();
                tes.pos(matrix4f, drawX + drawWidth, drawY + drawHeight, 0).tex(minU + (maxU - minU) * drawWidth / 16F, minV + (maxV - minV) * drawHeight / 16F).endVertex();
                tes.pos(matrix4f, drawX + drawWidth, drawY, 0).tex(minU + (maxU - minU) * drawWidth / 16F, minV).endVertex();
                tes.pos(matrix4f, drawX, drawY, 0).tex(minU, minV).endVertex();
                tessellator.draw();
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

        Minecraft.getInstance().getTextureManager().bindTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE);
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

                float minU = icon.getMinU();
                float maxU = icon.getMaxU();
                float minV = icon.getMinV();
                float maxV = icon.getMaxV();

                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder tes = tessellator.getBuffer();
                tes.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
                float v = minV + (maxV - minV) * drawHeight / 16F;
                tes.pos(matrix4f, drawX, drawY + drawHeight, 0).tex(minU, minV + (maxV - minV) * drawHeight / 16F).endVertex();
                tes.pos(matrix4f, drawX + drawWidth, drawY + drawHeight, 0).tex(minU + (maxU - minU) * drawWidth / 16F, minV + (maxV - minV) * drawHeight / 16F).endVertex();
                tes.pos(matrix4f, drawX + drawWidth, drawY, 0).tex(minU + (maxU - minU) * drawWidth / 16F, minV).endVertex();
                tes.pos(matrix4f, drawX, drawY, 0).tex(minU, minV).endVertex();
                tessellator.draw();
            }
        }
        RenderSystem.disableBlend();
        RenderSystem.color3f(1, 1, 1);
    }

}
