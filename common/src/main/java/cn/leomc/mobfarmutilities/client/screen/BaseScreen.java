package cn.leomc.mobfarmutilities.client.screen;

import cn.leomc.mobfarmutilities.client.utils.Textures;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseScreen<C extends AbstractContainerMenu> extends AbstractContainerScreen<C> {

    protected List<Pair<Integer, Integer>> slots = new ArrayList<>();

    public BaseScreen(C screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
    }


    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int x, int y) {
        Minecraft.getInstance().font.draw(matrixStack, title.getString(), getCenteredOffset(title.getString()), 6, 0x404040);
        Minecraft.getInstance().font.draw(matrixStack, playerInventoryTitle, 8, imageHeight - 96 + 3, 0x404040);
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        blit(matrixStack, relX, relY, 0, this.imageWidth, this.imageWidth, Textures.GENERIC_GUI.get());
        if (!slots.isEmpty()) {
            TextureAtlasSprite slot = Textures.SLOT_SMALL.get();
            for (Pair<Integer, Integer> pair : slots) {
                blit(matrixStack, leftPos + pair.getFirst(), topPos + pair.getSecond(), 1, slot.getWidth(), slot.getHeight(), slot);
            }
        }
    }

    protected void addSlotRange(int x, int y, int amount, int dx) {
        for (int i = 0; i < amount; i++) {
            slots.add(Pair.of(x - 1, y - 1));
            x += dx;
        }
    }


    protected void addSlotBox(int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0; j < verAmount; j++) {
            addSlotRange(x, y, horAmount, dx);
            y += dy;
        }
    }

    public int getCenteredOffset(String string) {
        return this.getCenteredOffset(string, imageWidth / 2);
    }

    public int getCenteredOffset(String string, int xPos) {
        return ((xPos * 2) - font.width(string)) / 2;
    }

    public Font getFont() {
        return font;
    }


}
