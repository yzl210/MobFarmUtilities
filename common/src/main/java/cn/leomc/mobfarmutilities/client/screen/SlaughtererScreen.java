package cn.leomc.mobfarmutilities.client.screen;

import cn.leomc.mobfarmutilities.MobFarmUtilities;
import cn.leomc.mobfarmutilities.client.RedstoneModeButton;
import cn.leomc.mobfarmutilities.client.ShowAreaButton;
import cn.leomc.mobfarmutilities.client.UpgradeSelector;
import cn.leomc.mobfarmutilities.common.api.IHasArea;
import cn.leomc.mobfarmutilities.common.blockentity.SlaughtererBlockEntity;
import cn.leomc.mobfarmutilities.common.menu.SlaughtererMenu;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class SlaughtererScreen extends BaseScreen<SlaughtererMenu> {

    protected Component title;

    protected RedstoneModeButton redstoneModeButton;
    protected ShowAreaButton showAreaButton;

    protected UpgradeSelector upgradeSelector;


    public SlaughtererScreen(SlaughtererMenu container, Inventory playerInventory, Component titleIn) {
        super(container, playerInventory, titleIn);
        this.title = titleIn;
    }

    @Override
    protected void init() {
        super.init();
        redstoneModeButton = addRenderableWidget(new RedstoneModeButton(this, leftPos, topPos, menu.getTileEntity().getBlockPos(), menu.getTileEntity().getLevel()));
        if (menu.getTileEntity() instanceof IHasArea)
            showAreaButton = addRenderableWidget(new ShowAreaButton(this, leftPos, topPos, (IHasArea) menu.getTileEntity()));
        if (menu.getTileEntity() instanceof SlaughtererBlockEntity) {
            String title = I18n.get("text." + MobFarmUtilities.MODID + ".upgrade.slaughterer");
            upgradeSelector = new UpgradeSelector(this, getCenteredOffset(""), 30, title, leftPos, topPos, ((SlaughtererBlockEntity) menu.getTileEntity()).getUpgradeHandler(), ((SlaughtererBlockEntity) menu.getTileEntity()).getUpgradeHandler().getSupportedUpgrades());
        }
        addSlotRange(79, 60, 1, 0);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        upgradeSelector.onScroll(mouseX, mouseY, delta);
        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        upgradeSelector.onClick(button, mouseX, mouseY);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int x, int y) {
        super.renderLabels(matrixStack, x, y);
        upgradeSelector.renderForeground(matrixStack);
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y) {
        super.renderBg(matrixStack, partialTicks, x, y);
        upgradeSelector.renderBackground(matrixStack);
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderTooltip(PoseStack matrixStack, int x, int y) {
        super.renderTooltip(matrixStack, x, y);
        upgradeSelector.renderToolTip(matrixStack, x, y);
    }
}
