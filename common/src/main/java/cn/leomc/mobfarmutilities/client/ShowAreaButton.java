package cn.leomc.mobfarmutilities.client;

import cn.leomc.mobfarmutilities.MobFarmUtilities;
import cn.leomc.mobfarmutilities.client.utils.TextureUtils;
import cn.leomc.mobfarmutilities.common.api.IHasArea;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

public class ShowAreaButton extends Button {

    private static final OnPress NO_ACTION = (button) -> {
    };
    private final IHasArea iHasArea;
    private final AbstractContainerScreen<?> screen;

    public ShowAreaButton(AbstractContainerScreen<?> screen, int guiLeft, int guiTop, IHasArea iHasArea) {
        super(guiLeft + 5 + 144, guiTop + 5, 20, 20, TextComponent.EMPTY, NO_ACTION);
        this.iHasArea = iHasArea;
        this.screen = screen;
    }


    @Override
    public void onPress() {
        iHasArea.switchRenderArea();
    }

    @Override
    public void renderToolTip(PoseStack matrixStack, int mouseX, int mouseY) {
        if (mouseX >= x && mouseX <= x + 20 && mouseY >= y && mouseY <= y + 20) {
            String text = I18n.get("text." + MobFarmUtilities.MODID + ".show_area." + iHasArea.doRenderArea());
            screen.renderTooltip(matrixStack, new TextComponent(text), mouseX, mouseY);
        }
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        Minecraft.getInstance().getTextureManager().bind(InventoryMenu.BLOCK_ATLAS);
        TextureAtlasSprite sprite;
        if (iHasArea.doRenderArea())
            sprite = TextureUtils.getAtlasTexture(new ResourceLocation("minecraft", "block/lime_wool"));
        else
            sprite = TextureUtils.getAtlasTexture(new ResourceLocation("minecraft", "block/red_wool"));

        Screen.blit(matrixStack, x + 2, y + 2, 0, 16, 16, sprite);
    }
}
