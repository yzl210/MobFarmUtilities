package cn.leomc.mobfarmutilities.common.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;


public class BaseContainer extends Container {

    protected final TileEntity tileEntity;
    protected final PlayerEntity playerEntity;
    protected final PlayerInventory playerInventory;

    protected BaseContainer(ContainerType<?> type, TileEntity tileEntity, PlayerEntity playerEntity, PlayerInventory playerInventory, int windowId) {
        super(type, windowId);
        this.tileEntity = tileEntity;
        this.playerEntity = playerEntity;
        this.playerInventory = playerInventory;
        layoutPlayerInventorySlots(8, 84);
    }


    protected int addSlotRange(IInventory inventory, int index, int x, int y, int amount, int dx) {
        for (int i = 0; i < amount; i++) {
            int finalIndex = index;
            addSlot(new Slot(inventory, finalIndex, x, y) {
                @Override
                public boolean isItemValid(ItemStack stack) {
                    return inventory.isItemValidForSlot(finalIndex, stack);
                }
            });
            x += dx;
            index++;
        }
        return index;
    }

    protected int addSlotBox(IInventory inventory, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0; j < verAmount; j++) {
            index = addSlotRange(inventory, index, x, y, horAmount, dx);
            y += dy;
        }
        return index;
    }

    protected void layoutPlayerInventorySlots(int leftCol, int topRow) {
        addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);

        topRow += 58;
        addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
    }


    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return false;
    }

    public TileEntity getTileEntity() {
        return tileEntity;
    }
}
