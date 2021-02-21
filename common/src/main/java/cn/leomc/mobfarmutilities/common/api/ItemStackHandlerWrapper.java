package cn.leomc.mobfarmutilities.common.api;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class ItemStackHandlerWrapper implements IInventory {
    protected UpgradeItemStackHandler upgradeItemStackHandler;

    public ItemStackHandlerWrapper(UpgradeItemStackHandler upgradeItemStackHandler) {
        this.upgradeItemStackHandler = upgradeItemStackHandler;
    }

    @Override
    public int getSizeInventory() {
        return upgradeItemStackHandler.getSize();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return upgradeItemStackHandler.getItemStackInSlot(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        ItemStack itemStack = upgradeItemStackHandler.getItemStackInSlot(index);
        return itemStack.isEmpty() ? ItemStack.EMPTY : itemStack.split(count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        ItemStack s = getStackInSlot(index);
        if (s.isEmpty()) return ItemStack.EMPTY;
        setInventorySlotContents(index, ItemStack.EMPTY);
        return s;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        upgradeItemStackHandler.setItemStackInSlot(index, stack);
    }

    @Override
    public void markDirty() {

    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        return true;
    }

    @Override
    public void clear() {
        upgradeItemStackHandler.clear();
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return upgradeItemStackHandler.isItemValid(index, stack.getItem());
    }


}
