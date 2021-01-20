package cn.leomc.mobfarmutilities.common.api;

import cn.leomc.mobfarmutilities.common.item.upgrade.UpgradeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class FanUpgradeItemStackHandler extends net.minecraftforge.items.ItemStackHandler {

    public FanUpgradeItemStackHandler() {
        super(4);
    }

    public NonNullList<ItemStack> getStacks() {
        return stacks;
    }

    public void clear() {
        stacks.clear();
    }

    public int getUpgrades(UpgradeItem.Type type) {
        for (ItemStack itemStack : stacks) {
            if (itemStack.getItem() instanceof UpgradeItem)
                if (((UpgradeItem) itemStack.getItem()).getType() == type)
                    return itemStack.getCount();
        }
        return 0;
    }


    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        if (!isItemValid(slot, stack))
            return;
        super.setStackInSlot(slot, stack);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        if (!(stack.getItem() instanceof UpgradeItem))
            return false;
        UpgradeItem upgradeItem = (UpgradeItem) stack.getItem();
        if (slot == 0 && upgradeItem.getType() == UpgradeItem.Type.FAN_SPEED)
            return true;
        if (slot == 1 && upgradeItem.getType() == UpgradeItem.Type.FAN_DISTANCE)
            return true;
        if (slot == 2 && upgradeItem.getType() == UpgradeItem.Type.FAN_WIDTH)
            return true;
        return slot == 3 && upgradeItem.getType() == UpgradeItem.Type.FAN_HEIGHT;
    }
}
