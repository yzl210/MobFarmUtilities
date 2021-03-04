package cn.leomc.mobfarmutilities.client;

import cn.leomc.mobfarmutilities.MobFarmUtilities;
import cn.leomc.mobfarmutilities.client.screen.BaseScreen;
import cn.leomc.mobfarmutilities.common.api.UpgradeHandler;
import cn.leomc.mobfarmutilities.common.container.BaseContainer;
import cn.leomc.mobfarmutilities.common.item.upgrade.UpgradeType;
import cn.leomc.mobfarmutilities.common.network.NetworkHandler;
import cn.leomc.mobfarmutilities.common.network.message.ChangeGradeMessage;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class UpgradeSelector extends ScrollSelector<UpgradeType> {

    protected UpgradeHandler upgradeHandler;

    protected double showCount = 0;

    protected int lastSelectedIndex = 0;

    public UpgradeSelector(BaseScreen<?> parent, int x, int y, String title, int guiLeft, int guiTop, UpgradeHandler upgradeHandler, UpgradeType... elements) {
        super(parent, x, y, title, guiLeft, guiTop, elements);
        this.upgradeHandler = upgradeHandler;
    }

    public UpgradeSelector(BaseScreen<?> parent, int x, int y, String title, int guiLeft, int guiTop, UpgradeHandler upgradeHandler, List<UpgradeType> elements) {
        super(parent, x, y, title, guiLeft, guiTop, elements);
        this.upgradeHandler = upgradeHandler;
    }

    @Override
    public void updateSize() {
        restrictIndex();
        width = parent.getFont().getStringWidth(elements.get(selectedIndex).getLocalizedName());
        height = parent.getFont().FONT_HEIGHT;
    }

    @Override
    public void onClick(int clickType, double mouseX, double mouseY) {
        if (checkInBound((int) mouseX, (int) mouseY))
            if (parent.getContainer() instanceof BaseContainer) {
                switch (clickType) {
                    case 0:
                        NetworkHandler.INSTANCE.sendToServer(new ChangeGradeMessage(((BaseContainer) parent.getContainer()).getTileEntity().getPos(), elements.get(selectedIndex), true));
                        Minecraft.getInstance().getSoundHandler().play(SimpleSound.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                        break;
                    case 1:
                        NetworkHandler.INSTANCE.sendToServer(new ChangeGradeMessage(((BaseContainer) parent.getContainer()).getTileEntity().getPos(), elements.get(selectedIndex), false));
                        Minecraft.getInstance().getSoundHandler().play(SimpleSound.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                        break;
                    case 3:
                        onScroll(mouseX, mouseY, -1D);
                        break;
                    case 4:
                        onScroll(mouseX, mouseY, 1D);
                }
            }
    }


    @Override
    public void renderForeground(MatrixStack matrixStack) {
        Minecraft.getInstance().fontRenderer.drawString(matrixStack, title, parent.getCenteredOffset(title, x), y, 0xffffffff);
        updateSize();
    }

    @Override
    public void renderToolTip(MatrixStack matrixStack, int mouseX, int mouseY) {
        if (checkInBound(mouseX, mouseY)) {
            List<IReorderingProcessor> reorderingProcessorList = new ArrayList<>();
            reorderingProcessorList.add(IReorderingProcessor.fromString(title, Style.EMPTY.applyFormatting(TextFormatting.AQUA)));
            for (int i = 0; i < elements.size(); i++) {
                UpgradeType type = elements.get(i);
                String s = I18n.format("text." + MobFarmUtilities.MODID + ".upgrade", type.getLocalizedName(), upgradeHandler.getUpgradeLevel(type), type.getMaxLevel());
                if (selectedIndex == i) {
                    if (selectedIndex != lastSelectedIndex || showCount > type.getSupportedItems().size())
                        showCount = 0;
                    lastSelectedIndex = selectedIndex;
                    TextFormatting color = upgradeHandler.isMaxLevel(type) ? TextFormatting.GREEN : TextFormatting.WHITE;
                    reorderingProcessorList.add(IReorderingProcessor.fromString("-> " + s, Style.EMPTY.applyFormatting(color)));
                    String itemName = I18n.format(type.getSupportedItems().get((int) Math.floor(showCount)).getTranslationKey());
                    String material = I18n.format("text." + MobFarmUtilities.MODID + ".material", itemName, type.getRequiredCount());
                    TextFormatting materialColor = type.isEnough(upgradeHandler.getInventory().getStackInSlot(0)) ? upgradeHandler.isMaxLevel(type) ? TextFormatting.WHITE : TextFormatting.GREEN : TextFormatting.RED;
                    reorderingProcessorList.add(IReorderingProcessor.fromString(material, Style.EMPTY.applyFormatting(materialColor)));
                    if (!type.getTagName().isEmpty()) {
                        String tag = I18n.format("text." + MobFarmUtilities.MODID + ".tag", type.getTagName());
                        reorderingProcessorList.add(IReorderingProcessor.fromString(tag, Style.EMPTY.applyFormatting(TextFormatting.DARK_GRAY)));
                    }
                    continue;
                }
                TextFormatting color = upgradeHandler.isMaxLevel(type) ? TextFormatting.DARK_GREEN : TextFormatting.GRAY;
                reorderingProcessorList.add(IReorderingProcessor.fromString("> " + s, Style.EMPTY.applyFormatting(color)));
            }
            reorderingProcessorList.add(IReorderingProcessor.fromString(I18n.format("text." + MobFarmUtilities.MODID + ".scroll"), Style.EMPTY.applyFormatting(TextFormatting.DARK_GRAY)));
            reorderingProcessorList.add(IReorderingProcessor.fromString(I18n.format("text." + MobFarmUtilities.MODID + ".how_to_upgrade.1"), Style.EMPTY.applyFormatting(TextFormatting.DARK_GRAY)));
            reorderingProcessorList.add(IReorderingProcessor.fromString(I18n.format("text." + MobFarmUtilities.MODID + ".how_to_upgrade.2"), Style.EMPTY.applyFormatting(TextFormatting.DARK_GRAY)));
            showCount += 0.015;
            parent.renderTooltip(matrixStack, reorderingProcessorList, mouseX, mouseY);
        }
    }

}