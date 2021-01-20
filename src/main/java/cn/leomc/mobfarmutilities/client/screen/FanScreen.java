package cn.leomc.mobfarmutilities.client.screen;

import cn.leomc.mobfarmutilities.client.RedstoneModeButton;
import cn.leomc.mobfarmutilities.common.container.FanContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class FanScreen extends BaseScreen<FanContainer> {

    protected ITextComponent title;

    protected RedstoneModeButton redstoneModeButton;


    public FanScreen(FanContainer container, PlayerInventory playerInventory, ITextComponent titleIn) {
        super(container, playerInventory, titleIn);
        this.title = titleIn;
    }

    @Override
    protected void init() {
        super.init();
        redstoneModeButton = addButton(new RedstoneModeButton(this, container.getTileEntity().getPos(), container.getTileEntity().getWorld()));
        addSlotRange(20, 40, 4, 40);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }


}
