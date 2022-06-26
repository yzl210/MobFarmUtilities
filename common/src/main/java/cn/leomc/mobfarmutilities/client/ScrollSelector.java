package cn.leomc.mobfarmutilities.client;


import cn.leomc.mobfarmutilities.client.screen.BaseScreen;
import com.mojang.blaze3d.systems.RenderSystem;
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
            if (delta < 0)
                selectedIndex += 1;
            else if (delta > 0)
                selectedIndex -= 1;
            if (!restrictIndex())
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 2.0F));
        }
    }

    public void onClick(int clickType, double mouseX, double mouseY) {
        if (checkInBound((int) mouseX, (int) mouseY))
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    public boolean checkInBound(int mouseX, int mouseY) {
        int width = parent.getFont().width(title);
        int minX = guiLeft + x - width / 2 - 3;
        int minY = guiTop + y - 3;
        int maxX = guiLeft + x + width / 2 + 2;
        int maxY = guiTop + y + parent.getFont().lineHeight + 2;
        return mouseX >= minX && mouseX <= maxX && mouseY >= minY && mouseY <= maxY;
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
        RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
        int width = parent.getFont().width(title);
        int minX = guiLeft + x - width / 2 - 3;
        int minY = guiTop + y - 3;
        int maxX = guiLeft + x + width / 2 + 2;
        int maxY = guiTop + y + parent.getFont().lineHeight + 2;
        GuiComponent.fill(matrixStack, minX, minY, maxX, maxY, 0xff8b8b8b);
        GuiComponent.fill(matrixStack, minX - 1, maxY + 1, minX, maxY, 0xff8b8b8b);
        GuiComponent.fill(matrixStack, minX - 1, maxY, minX, minY, 0xff373737);
        GuiComponent.fill(matrixStack, minX - 1, minY, maxX, minY - 1, 0xff373737);
        GuiComponent.fill(matrixStack, maxX, minY, maxX + 1, minY - 1, 0xff8b8b8b);
        GuiComponent.fill(matrixStack, maxX, minY, maxX + 1, maxY + 1, 0xffffffff);
        GuiComponent.fill(matrixStack, minX, maxY, maxX, maxY + 1, 0xffffffff);
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
