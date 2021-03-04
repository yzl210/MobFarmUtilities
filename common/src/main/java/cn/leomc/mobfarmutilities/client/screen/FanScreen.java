package cn.leomc.mobfarmutilities.client.screen;

import cn.leomc.mobfarmutilities.MobFarmUtilities;
import cn.leomc.mobfarmutilities.client.RedstoneModeButton;
import cn.leomc.mobfarmutilities.client.UpgradeSelector;
import cn.leomc.mobfarmutilities.common.container.FanContainer;
import cn.leomc.mobfarmutilities.common.tileentity.FanTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class FanScreen extends BaseScreen<FanContainer> {

    protected ITextComponent title;

    protected RedstoneModeButton redstoneModeButton;

    protected UpgradeSelector upgradeSelector;


    public FanScreen(FanContainer container, PlayerInventory playerInventory, ITextComponent titleIn) {
        super(container, playerInventory, titleIn);
        this.title = titleIn;
    }

    @Override
    protected void init() {
        super.init();
        redstoneModeButton = addButton(new RedstoneModeButton(this, guiLeft, guiTop, container.getTileEntity().getPos(), container.getTileEntity().getWorld()));
        if (container.getTileEntity() instanceof FanTileEntity) {
            String title = I18n.format("text." + MobFarmUtilities.MODID + ".upgrade.fan");
            upgradeSelector = new UpgradeSelector(this, getCenteredOffset(""), 30, title, guiLeft, guiTop, ((FanTileEntity) container.getTileEntity()).getUpgradeHandler(), ((FanTileEntity) container.getTileEntity()).getUpgradeHandler().getSupportedUpgrades());
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
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
        super.drawGuiContainerForegroundLayer(matrixStack, x, y);
        upgradeSelector.renderForeground(matrixStack);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        super.drawGuiContainerBackgroundLayer(matrixStack, partialTicks, x, y);
        upgradeSelector.renderBackground(matrixStack);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderHoveredTooltip(MatrixStack matrixStack, int x, int y) {
        super.renderHoveredTooltip(matrixStack, x, y);
        upgradeSelector.renderToolTip(matrixStack, x, y);
    }
}
