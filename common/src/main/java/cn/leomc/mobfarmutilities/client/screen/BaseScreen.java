package cn.leomc.mobfarmutilities.client.screen;

import cn.leomc.mobfarmutilities.MobFarmUtilities;
import cn.leomc.mobfarmutilities.common.utils.TextureUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseScreen<C extends Container> extends ContainerScreen<C> {

    protected List<Pair<Integer, Integer>> slots = new ArrayList<>();

    public BaseScreen(C screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }


    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
        Minecraft.getInstance().fontRenderer.drawString(matrixStack, title.getString(), getCenteredOffset(title.getString()), 6, 0x404040);
        Minecraft.getInstance().fontRenderer.drawString(matrixStack, playerInventory.getDisplayName().getString(), 8, ySize - 96 + 3, 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.getTextureManager().bindTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE);
        int relX = (this.width - this.xSize) / 2;
        int relY = (this.height - this.ySize) / 2;
        blit(matrixStack, relX, relY, 0, this.xSize, this.xSize, TextureUtils.getAtlasTexture(new ResourceLocation(MobFarmUtilities.MODID, "gui/generic")));
        TextureAtlasSprite slot = TextureUtils.getAtlasTexture(new ResourceLocation(MobFarmUtilities.MODID, "gui/slot_small"));
        for (Pair<Integer, Integer> pair : slots) {
            blit(matrixStack, guiLeft + pair.getFirst(), guiTop + pair.getSecond(), 1, slot.getWidth(), slot.getHeight(), slot);
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

    protected int getCenteredOffset(String string) {
        return this.getCenteredOffset(string, xSize / 2);
    }

    protected int getCenteredOffset(String string, int xPos) {
        return ((xPos * 2) - font.getStringWidth(string)) / 2;
    }

}
