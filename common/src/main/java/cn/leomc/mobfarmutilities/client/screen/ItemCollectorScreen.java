package cn.leomc.mobfarmutilities.client.screen;

import cn.leomc.mobfarmutilities.client.RedstoneModeButton;
import cn.leomc.mobfarmutilities.common.container.ItemCollectorContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ItemCollectorScreen extends BaseScreen<ItemCollectorContainer> {

    protected ITextComponent title;

    protected RedstoneModeButton redstoneModeButton;

    public ItemCollectorScreen(ItemCollectorContainer container, PlayerInventory playerInventory, ITextComponent titleIn) {
        super(container, playerInventory, titleIn);
        title = titleIn;
    }

    @Override
    protected void init() {
        super.init();
        redstoneModeButton = addButton(new RedstoneModeButton(this, guiLeft, guiTop, container.getTileEntity().getPos(), container.getTileEntity().getWorld()));
        addSlotBox(8, 34, 9, 18, 2, 18);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

}
