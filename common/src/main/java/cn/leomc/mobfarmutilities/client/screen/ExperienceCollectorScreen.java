package cn.leomc.mobfarmutilities.client.screen;

import cn.leomc.mobfarmutilities.client.RedstoneModeButton;
import cn.leomc.mobfarmutilities.client.utils.TextureUtils;
import cn.leomc.mobfarmutilities.client.utils.Textures;
import cn.leomc.mobfarmutilities.common.container.ExperienceCollectorContainer;
import cn.leomc.mobfarmutilities.common.network.NetworkHandler;
import cn.leomc.mobfarmutilities.common.network.message.ChangeExperienceMessage;
import cn.leomc.mobfarmutilities.common.registry.BlockRegistry;
import cn.leomc.mobfarmutilities.common.registry.FluidRegistry;
import cn.leomc.mobfarmutilities.common.tileentity.ExperienceCollectorTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.List;

public class ExperienceCollectorScreen extends BaseScreen<ExperienceCollectorContainer> {

    protected ITextComponent title;

    protected RedstoneModeButton redstoneModeButton;


    public ExperienceCollectorScreen(ExperienceCollectorContainer container, PlayerInventory playerInventory, ITextComponent titleIn) {
        super(container, playerInventory, titleIn);
        title = titleIn;
    }

    @Override
    protected void init() {
        super.init();
        redstoneModeButton = addButton(new RedstoneModeButton(this, guiLeft, guiTop, container.getTileEntity().getPos(), container.getTileEntity().getWorld()));
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (true)
            return super.mouseScrolled(mouseX, mouseY, delta);
        if (mouseX >= guiLeft + 75 && mouseX <= guiLeft + 75 + 17 && mouseY >= guiTop + 15 && mouseY <= guiTop + 15 + 61) {
            TileEntity tileEntity = container.getTileEntity();
            if (tileEntity instanceof ExperienceCollectorTileEntity)
                NetworkHandler.INSTANCE.sendToServer(new ChangeExperienceMessage(tileEntity.getPos(), (int) (delta > 0 ? delta : -delta), hasShiftDown(), !(delta > 0)));
            return true;
        }
        return false;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        super.drawGuiContainerBackgroundLayer(matrixStack, partialTicks, x, y);
        int width = 18;
        int height = 62;
        int posX = 75;
        int posY = 15;
        Minecraft.getInstance().getTextureManager().bindTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE);
        int amount = ((ExperienceCollectorTileEntity) container.getTileEntity()).getAmount();
        ContainerScreen.blit(matrixStack, guiLeft + posX, guiTop + posY, 0, width, height, Textures.VERTICAL_FLUID_TANK.get());
        TextureUtils.drawVerticalFluidTank(matrixStack.getLast().getMatrix(), FluidRegistry.LIQUID_EXPERIENCE.get(), amount, 5000, guiLeft + posX + 1, guiTop + posY + 1, width - 2, height - 2);
    }

    @Override
    protected void renderHoveredTooltip(MatrixStack matrixStack, int x, int y) {
        super.renderHoveredTooltip(matrixStack, x, y);
        if (x >= guiLeft + 75 && x <= guiLeft + 75 + 17 && y >= guiTop + 15 && y <= guiTop + 15 + 61) {
            int amount = ((ExperienceCollectorTileEntity) container.getTileEntity()).getAmount();
            List<IReorderingProcessor> reorderingProcessorList = new ArrayList<>();
            ITextComponent liquid = new TranslationTextComponent("text.mobfarmutilities.fluid_tank", new TranslationTextComponent(BlockRegistry.LIQUID_EXPERIENCE.get().getTranslationKey()), amount);
            reorderingProcessorList.add(IReorderingProcessor.fromString(liquid.getString(), Style.EMPTY));
            reorderingProcessorList.add(IReorderingProcessor.fromString(I18n.format("text.mobfarmutilities.how_to_store_get_experience"), Style.EMPTY.applyFormatting(TextFormatting.DARK_GRAY)));
            renderTooltip(matrixStack, reorderingProcessorList, x, y);
        }
    }
}
