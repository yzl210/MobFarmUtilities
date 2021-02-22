package cn.leomc.mobfarmutilities.forge;

import cn.leomc.mobfarmutilities.common.api.UpgradeItemStackHandler;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;

public class ItemStackHandlerWrapper implements IItemHandler {

    protected UpgradeItemStackHandler upgradeItemStackHandler;

    public ItemStackHandlerWrapper(UpgradeItemStackHandler upgradeItemStackHandler) {
        this.upgradeItemStackHandler = upgradeItemStackHandler;
    }

    @Override
    public int getSlots() {
        return upgradeItemStackHandler.getSize();
    }

    @NotNull
    @Override
    public ItemStack getStackInSlot(int i) {
        return upgradeItemStackHandler.getItemStackInSlot(i);
    }

    @NotNull
    @Override
    public ItemStack insertItem(int i, @NotNull ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        } else if (!this.isItemValid(i, stack)) {
            return stack;
        } else {
            ItemStack existing = upgradeItemStackHandler.getItemStackInSlot(i);
            int limit = stack.getMaxStackSize();
            if (!existing.isEmpty()) {
                if (!ItemHandlerHelper.canItemStacksStack(stack, existing)) {
                    return stack;
                }

                limit -= existing.getCount();
            }

            if (limit <= 0) {
                return stack;
            } else {
                boolean reachedLimit = stack.getCount() > limit;
                if (!simulate) {
                    if (existing.isEmpty()) {
                        upgradeItemStackHandler.setItemStackInSlot(i, reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack);
                    } else {
                        existing.grow(reachedLimit ? limit : stack.getCount());
                    }

                }

                return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - limit) : ItemStack.EMPTY;
            }
        }
    }

    @NotNull
    @Override
    public ItemStack extractItem(int i, int amount, boolean simulate) {
        if (amount == 0) {
            return ItemStack.EMPTY;
        } else {
            ItemStack existing = upgradeItemStackHandler.getItemStackInSlot(i);
            if (existing.isEmpty()) {
                return ItemStack.EMPTY;
            } else {
                int toExtract = Math.min(amount, existing.getMaxStackSize());
                if (existing.getCount() <= toExtract) {
                    if (!simulate) {
                        upgradeItemStackHandler.setItemStackInSlot(i, ItemStack.EMPTY);
                        return existing;
                    } else {
                        return existing.copy();
                    }
                } else {
                    if (!simulate) {
                        upgradeItemStackHandler.setItemStackInSlot(i, ItemHandlerHelper.copyStackWithSize(existing, existing.getCount() - toExtract));
                    }

                    return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
                }
            }
        }
    }

    @Override
    public int getSlotLimit(int i) {
        return 64;
    }

    @Override
    public boolean isItemValid(int i, @NotNull ItemStack itemStack) {
        return upgradeItemStackHandler.isItemValid(i, itemStack.getItem());
    }
}
