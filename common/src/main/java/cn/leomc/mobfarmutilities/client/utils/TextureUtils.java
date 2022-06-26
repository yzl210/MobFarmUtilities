package cn.leomc.mobfarmutilities.client.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import dev.architectury.hooks.fluid.FluidStackHooks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.AABB;

@Environment(EnvType.CLIENT)
public class TextureUtils {

    public static TextureAtlasSprite getAtlasTexture(ResourceLocation rl) {
        return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(rl);
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

        RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
        int color = FluidStackHooks.getColor(fluid);
        float r = ((color >> 16) & 0xFF) / 255f;
        float g = ((color >> 8) & 0xFF) / 255f;
        float b = (color & 0xFF) / 255f;
        RenderSystem.setShaderColor(r, g, b, 1);

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

                Tesselator tesselator = Tesselator.getInstance();
                BufferBuilder builder = tesselator.getBuilder();

                float v = minV + (maxV - minV) * drawHeight / 16F;
                float v1 = minU + (maxU - minU) * drawWidth / 16F;

                builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
                builder.vertex(matrix4f, drawX, drawY + drawHeight, 0).uv(minU, v).endVertex();
                builder.vertex(matrix4f, drawX + drawWidth, drawY + drawHeight, 0).uv(v1, v).endVertex();
                builder.vertex(matrix4f, drawX + drawWidth, drawY, 0).uv(v1, minV).endVertex();
                builder.vertex(matrix4f, drawX, drawY, 0).uv(minU, minV).endVertex();
                tesselator.end();
            }
        }
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1, 1, 1, 1);
    }


    public static void drawAABB(PoseStack poseStack, MultiBufferSource multiBufferSource, AABB aabb) {
        VertexConsumer vertexConsumer = multiBufferSource.getBuffer(RenderType.lines());
        poseStack.pushPose();
        LevelRenderer.renderLineBox(poseStack, vertexConsumer, aabb, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
    }
}
