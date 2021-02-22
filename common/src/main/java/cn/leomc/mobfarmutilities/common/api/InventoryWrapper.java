package cn.leomc.mobfarmutilities.common.api;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import org.jetbrains.annotations.Nullable;

public class InventoryWrapper implements ISidedInventory {

    protected IInventory inventory;

    public InventoryWrapper(IInventory iInventory) {
        this.inventory = iInventory;
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        int[] slots = new int[inventory.getSizeInventory()];
        for (int i = 0; i < inventory.getSizeInventory(); i++)
            slots[i] = i;
        return slots;
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, @Nullable Direction direction) {
        return inventory.isItemValidForSlot(index, itemStackIn);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        return true;
    }

    @Override
    public int getSizeInventory() {
        return inventory.getSizeInventory();
    }

    @Override
    public boolean isEmpty() {
        return inventory.isEmpty();
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return inventory.getStackInSlot(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return inventory.decrStackSize(index, count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return inventory.removeStackFromSlot(index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        inventory.setInventorySlotContents(index, stack);
    }

    @Override
    public void markDirty() {
        inventory.markDirty();
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        return inventory.isUsableByPlayer(player);
    }

    @Override
    public void clear() {
        inventory.clear();
    }
}
