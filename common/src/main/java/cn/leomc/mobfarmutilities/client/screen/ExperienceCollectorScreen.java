package cn.leomc.mobfarmutilities.client.screen;

import cn.leomc.mobfarmutilities.client.RedstoneModeButton;
import cn.leomc.mobfarmutilities.client.ShowAreaButton;
import cn.leomc.mobfarmutilities.client.utils.TextureUtils;
import cn.leomc.mobfarmutilities.client.utils.Textures;
import cn.leomc.mobfarmutilities.common.api.IHasArea;
import cn.leomc.mobfarmutilities.common.blockentity.ExperienceCollectorBlockEntity;
import cn.leomc.mobfarmutilities.common.menu.ExperienceCollectorMenu;
import cn.leomc.mobfarmutilities.common.network.NetworkHandler;
import cn.leomc.mobfarmutilities.common.network.message.ChangeExperienceMessage;
import cn.leomc.mobfarmutilities.common.registry.BlockRegistry;
import cn.leomc.mobfarmutilities.common.registry.FluidRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.List;

public class ExperienceCollectorScreen extends BaseScreen<ExperienceCollectorMenu> {

    protected Component title;

    protected RedstoneModeButton redstoneModeButton;
    protected ShowAreaButton showAreaButton;


    public ExperienceCollectorScreen(ExperienceCollectorMenu container, Inventory playerInventory, Component titleIn) {
        super(container, playerInventory, titleIn);
        title = titleIn;
    }

    @Override
    protected void init() {
        super.init();
        redstoneModeButton = addButton(new RedstoneModeButton(this, leftPos, topPos, menu.getTileEntity().getBlockPos(), menu.getTileEntity().getLevel()));
        if (menu.getTileEntity() instanceof IHasArea)
            showAreaButton = addButton(new ShowAreaButton(this, leftPos, topPos, (IHasArea) menu.getTileEntity()));
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (mouseX >= leftPos + 75 && mouseX <= leftPos + 75 + 17 && mouseY >= topPos + 15 && mouseY <= topPos + 15 + 61) {
            BlockEntity tileEntity = menu.getTileEntity();
            if (delta < 0 && tileEntity instanceof ExperienceCollectorBlockEntity)
                NetworkHandler.INSTANCE.sendToServer(new ChangeExperienceMessage(tileEntity.getBlockPos(), (int) -delta, hasShiftDown()));
            return true;
        }
        return false;
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y) {
        super.renderBg(matrixStack, partialTicks, x, y);
        int width = 18;
        int height = 62;
        int posX = 75;
        int posY = 15;
        Minecraft.getInstance().getTextureManager().bind(InventoryMenu.BLOCK_ATLAS);
        int amount = ((ExperienceCollectorBlockEntity) menu.getTileEntity()).getAmount();
        AbstractContainerScreen.blit(matrixStack, leftPos + posX, topPos + posY, 0, width, height, Textures.VERTICAL_FLUID_TANK.get());
        TextureUtils.drawVerticalFluidTank(matrixStack.last().pose(), FluidRegistry.LIQUID_EXPERIENCE.get(), amount, 5000, leftPos + posX + 1, topPos + posY + 1, width - 2, height - 2);
    }

    @Override
    protected void renderTooltip(PoseStack matrixStack, int x, int y) {
        super.renderTooltip(matrixStack, x, y);
        if (x >= leftPos + 75 && x <= leftPos + 75 + 17 && y >= topPos + 15 && y <= topPos + 15 + 61) {
            int amount = ((ExperienceCollectorBlockEntity) menu.getTileEntity()).getAmount();
            List<FormattedCharSequence> reorderingProcessorList = new ArrayList<>();
            Component liquid = new TranslatableComponent("text.mobfarmutilities.fluid_tank", new TranslatableComponent(BlockRegistry.LIQUID_EXPERIENCE.get().getDescriptionId()), amount);
            reorderingProcessorList.add(FormattedCharSequence.forward(liquid.getString(), Style.EMPTY));
            reorderingProcessorList.add(FormattedCharSequence.forward(I18n.get("text.mobfarmutilities.how_to_store_get_experience.1"), Style.EMPTY.applyFormat(ChatFormatting.DARK_GRAY)));
            reorderingProcessorList.add(FormattedCharSequence.forward(I18n.get("text.mobfarmutilities.how_to_store_get_experience.2"), Style.EMPTY.applyFormat(ChatFormatting.DARK_GRAY)));
            renderTooltip(matrixStack, reorderingProcessorList, x, y);
        }
    }
}
