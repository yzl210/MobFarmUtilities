package cn.leomc.mobfarmutilities.client;


import cn.leomc.mobfarmutilities.common.api.RedstoneMode;
import cn.leomc.mobfarmutilities.common.block.ActivatableBlock;
import cn.leomc.mobfarmutilities.common.network.NetworkHandler;
import cn.leomc.mobfarmutilities.common.network.message.RedstoneModeChangeMessage;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class RedstoneModeButton extends Button {

    private static final IPressable NO_ACTION = (button) -> {
    };
    private final BlockPos pos;
    private final World world;
    private final ContainerScreen<?> screen;
    private RedstoneMode mode;
    private int updateCooldown = 0;

    public RedstoneModeButton(ContainerScreen<?> screen, BlockPos pos, World world) {
        super(screen.getGuiLeft() + 5, screen.getGuiTop() + 5, 20, 20, StringTextComponent.EMPTY, NO_ACTION);
        this.mode = world.getBlockState(pos).get(ActivatableBlock.MODE);
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
    public void renderToolTip(MatrixStack matrixStack, int mouseX, int mouseY) {
        if (mouseX >= x && mouseX <= x + 20 && mouseY >= y && mouseY <= y + 20)
            screen.renderTooltip(matrixStack, new TranslationTextComponent(RedstoneMode.getModeTranslationKey(), new TranslationTextComponent(mode.getTranslationKey())), mouseX, mouseY);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        if (updateCooldown == 0) {
            mode = world.getBlockState(pos).get(ActivatableBlock.MODE);
            updateCooldown = 5;
        } else
            updateCooldown--;
        Minecraft.getInstance().getTextureManager().bindTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE);
        TextureAtlasSprite sprite = mode.getTextureAtlasSprite();
        Screen.blit(matrixStack, x + 2, y + 2, 1, 16, 16, sprite);
    }
}
