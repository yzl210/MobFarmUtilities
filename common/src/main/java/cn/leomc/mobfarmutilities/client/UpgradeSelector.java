package cn.leomc.mobfarmutilities.client;

import cn.leomc.mobfarmutilities.MobFarmUtilities;
import cn.leomc.mobfarmutilities.client.screen.BaseScreen;
import cn.leomc.mobfarmutilities.common.api.UpgradeHandler;
import cn.leomc.mobfarmutilities.common.api.UpgradeType;
import cn.leomc.mobfarmutilities.common.menu.BaseMenu;
import cn.leomc.mobfarmutilities.common.network.NetworkHandler;
import cn.leomc.mobfarmutilities.common.network.message.ChangeGradeMessage;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FormattedCharSequence;

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
        width = parent.getFont().width(elements.get(selectedIndex).getLocalizedName());
        height = parent.getFont().lineHeight;
    }

    @Override
    public void onClick(int clickType, double mouseX, double mouseY) {
        if (checkInBound((int) mouseX, (int) mouseY))
            if (parent.getMenu() instanceof BaseMenu) {
                switch (clickType) {
                    case 0 -> {
                        NetworkHandler.INSTANCE.sendToServer(new ChangeGradeMessage(((BaseMenu) parent.getMenu()).getTileEntity().getBlockPos(), elements.get(selectedIndex), true));
                        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                    }
                    case 1 -> {
                        NetworkHandler.INSTANCE.sendToServer(new ChangeGradeMessage(((BaseMenu) parent.getMenu()).getTileEntity().getBlockPos(), elements.get(selectedIndex), false));
                        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                    }
                    case 3 -> onScroll(mouseX, mouseY, -1D);
                    case 4 -> onScroll(mouseX, mouseY, 1D);
                }
            }
    }


    @Override
    public void renderForeground(PoseStack matrixStack) {
        Minecraft.getInstance().font.draw(matrixStack, title, parent.getCenteredOffset(title, x), y, 0xffffffff);
        updateSize();
    }

    @Override
    public void renderToolTip(PoseStack matrixStack, int mouseX, int mouseY) {
        if (checkInBound(mouseX, mouseY)) {
            List<FormattedCharSequence> reorderingProcessorList = new ArrayList<>();
            reorderingProcessorList.add(FormattedCharSequence.forward(title, Style.EMPTY.applyFormat(ChatFormatting.AQUA)));
            for (int i = 0; i < elements.size(); i++) {
                UpgradeType type = elements.get(i);
                String s = I18n.get("text." + MobFarmUtilities.MODID + ".upgrade", type.getLocalizedName(), upgradeHandler.getUpgradeLevel(type), type.getMaxLevel());
                if (selectedIndex == i) {
                    if (selectedIndex != lastSelectedIndex || showCount > type.getSupportedItems().size())
                        showCount = 0;
                    lastSelectedIndex = selectedIndex;
                    ChatFormatting color = upgradeHandler.isMaxLevel(type) ? ChatFormatting.GREEN : ChatFormatting.WHITE;
                    reorderingProcessorList.add(FormattedCharSequence.forward("-> " + s, Style.EMPTY.applyFormat(color)));
                    String itemName = I18n.get(type.getSupportedItems().get((int) Math.floor(showCount)).getDescriptionId());
                    String material = I18n.get("text." + MobFarmUtilities.MODID + ".material", itemName, type.getRequiredCount());
                    ChatFormatting materialColor = type.isEnough(upgradeHandler.getInventory().getItem(0)) ? upgradeHandler.isMaxLevel(type) ? ChatFormatting.WHITE : ChatFormatting.GREEN : ChatFormatting.RED;
                    reorderingProcessorList.add(FormattedCharSequence.forward(material, Style.EMPTY.applyFormat(materialColor)));
                    if (!type.getTagName().isEmpty()) {
                        String tag = I18n.get("text." + MobFarmUtilities.MODID + ".tag", type.getTagName());
                        reorderingProcessorList.add(FormattedCharSequence.forward(tag, Style.EMPTY.applyFormat(ChatFormatting.DARK_GRAY)));
                    }
                    continue;
                }
                ChatFormatting color = upgradeHandler.isMaxLevel(type) ? ChatFormatting.DARK_GREEN : ChatFormatting.GRAY;
                reorderingProcessorList.add(FormattedCharSequence.forward("> " + s, Style.EMPTY.applyFormat(color)));
            }
            reorderingProcessorList.add(FormattedCharSequence.forward(I18n.get("text." + MobFarmUtilities.MODID + ".scroll"), Style.EMPTY.applyFormat(ChatFormatting.DARK_GRAY)));
            reorderingProcessorList.add(FormattedCharSequence.forward(I18n.get("text." + MobFarmUtilities.MODID + ".how_to_upgrade.1"), Style.EMPTY.applyFormat(ChatFormatting.DARK_GRAY)));
            reorderingProcessorList.add(FormattedCharSequence.forward(I18n.get("text." + MobFarmUtilities.MODID + ".how_to_upgrade.2"), Style.EMPTY.applyFormat(ChatFormatting.DARK_GRAY)));
            showCount += 0.015;
            parent.renderTooltip(matrixStack, reorderingProcessorList, mouseX, mouseY);
        }
    }

}