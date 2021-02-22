package cn.leomc.mobfarmutilities.common.api;

import cn.leomc.mobfarmutilities.common.item.upgrade.UpgradeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.NonNullList;

import java.util.Arrays;
import java.util.HashMap;


public class UpgradeItemStackHandler {
    protected HashMap<Integer, ItemStack> upgradeItems;
    protected HashMap<UpgradeItem.Type, Integer> supportedUpgrades;

    public UpgradeItemStackHandler(UpgradeItem.Type... types) {
        upgradeItems = new HashMap<>();
        supportedUpgrades = new HashMap<>();
        int i = 0;
        for (UpgradeItem.Type type : types) {
            supportedUpgrades.put(type, i++);
        }
        for (UpgradeItem.Type type : types) {
            upgradeItems.put(supportedUpgrades.get(type), ItemStack.EMPTY);
        }

    }

    public ItemStack[] getItemStacks() {
        return upgradeItems.values().toArray(new ItemStack[0]);
    }

    public NonNullList<ItemStack> getNonNullItemStackList() {
        NonNullList<ItemStack> nonNullList = NonNullList.withSize(4, ItemStack.EMPTY);
        upgradeItems.forEach(nonNullList::set);
        return nonNullList;
    }

    public ItemStack getItemStackInSlot(int index) {
        return upgradeItems.get(index);
    }

    public void clear() {
        upgradeItems.clear();
        for (UpgradeItem.Type type : supportedUpgrades.keySet()) {
            upgradeItems.put(supportedUpgrades.get(type), ItemStack.EMPTY);
        }
    }

    public int getSize() {
        return upgradeItems.size();
    }

    public boolean checkUpgradeType(UpgradeItem.Type type) {
        return supportedUpgrades.containsKey(type);

    }

    public void setItemStackInSlot(int index, ItemStack itemStack) {
        upgradeItems.put(index, itemStack);
    }

    public void validateUpgradeType(UpgradeItem.Type type) {
        if (!checkUpgradeType(type))
            throw new IllegalArgumentException(type.toString() + " not allowed, allowed types: " + Arrays.toString(upgradeItems.keySet().toArray()));
    }

    public int getUpgradeCount(UpgradeItem.Type type) {
        validateUpgradeType(type);
        return upgradeItems.get(supportedUpgrades.get(type)).getCount();
    }

    public ItemStack getUpgradeItemStack(UpgradeItem.Type type) {
        validateUpgradeType(type);
        return upgradeItems.get(supportedUpgrades.get(type));
    }

    public boolean isItemValid(int index, Item item) {
        return item instanceof UpgradeItem && supportedUpgrades.get(((UpgradeItem) item).getType()) == index;
    }

    public CompoundNBT write(CompoundNBT nbt) {
        ListNBT listNBT = new ListNBT();

        for (UpgradeItem.Type type : supportedUpgrades.keySet()) {
            ItemStack itemStack = upgradeItems.get(supportedUpgrades.get(type));
            if (!itemStack.isEmpty()) {
                CompoundNBT compoundNBT = new CompoundNBT();
                compoundNBT.putString("Upgrade", type.name());
                itemStack.write(compoundNBT);
                listNBT.add(compoundNBT);
            }
        }

        if (!listNBT.isEmpty()) {
            nbt.put("Upgrades", listNBT);
        }

        return nbt;
    }

    public void read(CompoundNBT nbt) {
        ListNBT listNBT = nbt.getList("Upgrades", 10);
        for (int i = 0; i < listNBT.size(); ++i) {
            CompoundNBT compoundNBT = listNBT.getCompound(i);
            UpgradeItem.Type type = UpgradeItem.Type.valueOf(compoundNBT.getString("Upgrade"));
            upgradeItems.put(supportedUpgrades.get(type), ItemStack.read(compoundNBT));
        }
    }

}
