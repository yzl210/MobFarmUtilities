package cn.leomc.mobfarmutilities.client;


import cn.leomc.mobfarmutilities.common.api.RedstoneMode;
import cn.leomc.mobfarmutilities.common.block.ActivatableBlock;
import cn.leomc.mobfarmutilities.common.network.NetworkHandler;
import cn.leomc.mobfarmutilities.common.network.message.RedstoneModeChangeMessage;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.Level;

public class RedstoneModeButton extends Button {

    private static final OnPress NO_ACTION = (button) -> {
    };
    private final BlockPos pos;
    private final Level world;
    private final AbstractContainerScreen<?> screen;
    private RedstoneMode mode;
    private int updateCooldown = 0;

    public RedstoneModeButton(AbstractContainerScreen<?> screen, int guiLeft, int guiTop, BlockPos pos, Level world) {
        super(guiLeft + 5, guiTop + 5, 20, 20, TextComponent.EMPTY, NO_ACTION);
        this.mode = world.getBlockState(pos).getValue(ActivatableBlock.MODE);
        this.pos = pos;
        this.world = world;
        this.screen = screen;
    }


    @Override
    public void onPress() {
        mode = mode.next();
        updateCooldown = 10;
        NetworkHandler.INSTANCE.sendToServer(new RedstoneModeChangeMessage(pos));
    }

    @Override
    public void renderToolTip(PoseStack matrixStack, int mouseX, int mouseY) {
        if (mouseX >= x && mouseX <= x + 20 && mouseY >= y && mouseY <= y + 20)
            screen.renderTooltip(matrixStack, new TranslatableComponent(RedstoneMode.getModeTranslationKey(), new TranslatableComponent(mode.getTranslationKey())), mouseX, mouseY);
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        if (updateCooldown == 0) {
            mode = world.getBlockState(pos).getValue(ActivatableBlock.MODE);
            updateCooldown = 5;
        } else
            updateCooldown--;
        Minecraft.getInstance().getTextureManager().bind(InventoryMenu.BLOCK_ATLAS);
        TextureAtlasSprite sprite = mode.getTextureAtlasSprite();
        Screen.blit(matrixStack, x + 2, y + 2, 1, 16, 16, sprite);
    }
}
