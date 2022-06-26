package cn.leomc.mobfarmutilities.client.screen;

import cn.leomc.mobfarmutilities.client.RedstoneModeButton;
import cn.leomc.mobfarmutilities.client.ShowAreaButton;
import cn.leomc.mobfarmutilities.common.api.IHasArea;
import cn.leomc.mobfarmutilities.common.menu.ItemCollectorMenu;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ItemCollectorScreen extends BaseScreen<ItemCollectorMenu> {

    protected Component title;

    protected RedstoneModeButton redstoneModeButton;
    protected ShowAreaButton showAreaButton;

    public ItemCollectorScreen(ItemCollectorMenu container, Inventory playerInventory, Component titleIn) {
        super(container, playerInventory, titleIn);
        title = titleIn;
    }

    @Override
    protected void init() {
        super.init();
        redstoneModeButton = addRenderableWidget(new RedstoneModeButton(this, leftPos, topPos, menu.getTileEntity().getBlockPos(), menu.getTileEntity().getLevel()));
        if (menu.getTileEntity() instanceof IHasArea)
            showAreaButton = addRenderableWidget(new ShowAreaButton(this, leftPos, topPos, (IHasArea) menu.getTileEntity()));
        addSlotBox(8, 34, 9, 18, 2, 18);
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        renderTooltip(matrixStack, mouseX, mouseY);
    }

}
