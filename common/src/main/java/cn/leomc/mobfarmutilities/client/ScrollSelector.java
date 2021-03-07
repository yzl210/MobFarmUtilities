package cn.leomc.mobfarmutilities.client;


import cn.leomc.mobfarmutilities.client.screen.BaseScreen;
import cn.leomc.mobfarmutilities.client.utils.Textures;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.inventory.InventoryMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Environment(EnvType.CLIENT)
public class ScrollSelector<T> {

    public int width;
    public int height;
    protected BaseScreen<?> parent;
    protected int x;
    protected int y;
    protected int guiLeft;
    protected int guiTop;
    protected List<T> elements;
    protected int selectedIndex;

    protected String title;

    @SafeVarargs
    public ScrollSelector(BaseScreen<?> parent, int x, int y, String title, int guiLeft, int guiTop, T... elements) {
        this(parent, x, y, title, guiLeft, guiTop, Arrays.asList(elements));
    }

    public ScrollSelector(BaseScreen<?> parent, int x, int y, String title, int guiLeft, int guiTop, List<T> elements) {
        this.parent = parent;
        this.x = x;
        this.y = y;
        this.guiLeft = guiLeft;
        this.guiTop = guiTop;
        this.elements = elements;
        this.selectedIndex = 0;
        this.title = title;
    }

    public void onScroll(double mouseX, double mouseY, double delta) {
        if (checkInBound((int) mouseX, (int) mouseY)) {
            selectedIndex -= delta;
            if (!restrictIndex())
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 2.0F));
        }
    }

    public void onClick(int clickType, double mouseX, double mouseY) {
        if (checkInBound((int) mouseX, (int) mouseY))
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    public boolean checkInBound(int mouseX, int mouseY) {
        return mouseX >= x + guiLeft - 48 && mouseX <= x + guiLeft + 48 - 1 && mouseY >= y + guiTop - 5 && mouseY <= y + height + guiTop + 2;
    }

    public void updateSize() {
        restrictIndex();
        width = parent.getFont().width(elements.get(selectedIndex).toString());
        height = parent.getFont().lineHeight;
    }

    public boolean restrictIndex() {
        if (selectedIndex >= elements.size()) {
            selectedIndex = elements.size() - 1;
            return true;
        }
        if (selectedIndex < 0) {
            selectedIndex = 0;
            return true;
        }
        return false;
    }

    public void renderBackground(PoseStack matrixStack) {
        Minecraft.getInstance().getTextureManager().bind(InventoryMenu.BLOCK_ATLAS);
        GuiComponent.blit(matrixStack, guiLeft + x - 48, guiTop + y - 5, 0, 96, 16, Textures.TEXTFIELD.get());
    }

    public void renderForeground(PoseStack matrixStack) {
        String renderString;
        if (elements.isEmpty() || selectedIndex < 0 || selectedIndex >= elements.size())
            renderString = "Error!";
        else
            renderString = elements.get(selectedIndex).toString();

        Minecraft.getInstance().font.draw(matrixStack, renderString, parent.getCenteredOffset(renderString, x), y, 0xffffffff);
        updateSize();
    }

    public void renderToolTip(PoseStack matrixStack, int mouseX, int mouseY) {
        if (checkInBound(mouseX, mouseY)) {
            List<FormattedCharSequence> reorderingProcessorList = new ArrayList<>();
            for (int i = 0; i < elements.size(); i++) {
                if (selectedIndex == i) {
                    reorderingProcessorList.add(FormattedCharSequence.forward("->" + elements.get(i).toString(), Style.EMPTY.applyFormat(ChatFormatting.WHITE)));
                    continue;
                }
                reorderingProcessorList.add(FormattedCharSequence.forward(">" + elements.get(i).toString(), Style.EMPTY.applyFormat(ChatFormatting.GRAY)));
            }
            parent.renderTooltip(matrixStack, reorderingProcessorList, mouseX, mouseY);
        }
    }


    public List<T> getElements() {
        return elements;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }
}
