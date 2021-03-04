package cn.leomc.mobfarmutilities.common.api;

import cn.leomc.mobfarmutilities.common.item.upgrade.UpgradeType;
import com.google.common.collect.Lists;
import me.shedaniel.architectury.extensions.BlockEntityExtension;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class UpgradeHandler {

    protected HashMap<UpgradeType, Integer> upgrades;
    protected Inventory inventory;
    protected BlockEntityExtension syncer;

    public UpgradeHandler(BlockEntityExtension extension, UpgradeType... types) {
        this.syncer = extension;
        inventory = new Inventory(1) {
            @Override
            public boolean isItemValidForSlot(int index, ItemStack stack) {
                List<Item> item = new ArrayList<>();
                for (UpgradeType type : upgrades.keySet()) {
                    item.addAll(type.getSupportedItems());
                }
                return item.contains(stack.getItem());
            }
        };
        upgrades = new HashMap<>();
        for (UpgradeType type : types)
            upgrades.put(type, 0);
    }


    public Inventory getInventory() {
        return inventory;
    }


    public boolean checkUpgradeType(UpgradeType type) {
        return upgrades.containsKey(type);
    }


    public void validateUpgradeType(UpgradeType type) {
        if (!checkUpgradeType(type))
            throw new IllegalArgumentException(type.toString() + " not allowed, allowed types: " + Arrays.toString(upgrades.keySet().toArray()));
    }

    public int getUpgradeLevel(UpgradeType type) {
        validateUpgradeType(type);
        return upgrades.get(type);
    }

    public CompoundNBT write(CompoundNBT nbt) {
        return write(nbt, true);
    }

    public CompoundNBT write(CompoundNBT nbt, boolean includeInv) {
        ListNBT listNBT = new ListNBT();

        for (UpgradeType type : upgrades.keySet()) {
            CompoundNBT compoundNBT = new CompoundNBT();
            compoundNBT.putString("Upgrade", type.name());
            compoundNBT.putInt("Level", getUpgradeLevel(type));
            listNBT.add(compoundNBT);
        }

        if (!listNBT.isEmpty()) {
            nbt.put("Upgrades", listNBT);
        }

        if (includeInv)
            nbt.put("inventory", inventory.write());

        return nbt;
    }

    public void read(CompoundNBT nbt) {
        ListNBT listNBT = nbt.getList("Upgrades", 10);
        for (int i = 0; i < listNBT.size(); ++i) {
            CompoundNBT compoundNBT = listNBT.getCompound(i);
            UpgradeType type = UpgradeType.valueOf(compoundNBT.getString("Upgrade"));
            upgrades.put(type, compoundNBT.getInt("Level"));
        }
        if (nbt.contains("inventory"))
            inventory.read(nbt.getList("inventory", 10));

    }

    public List<UpgradeType> getSupportedUpgrades() {
        return Lists.newArrayList(upgrades.keySet());
    }

    public void upgrade(UpgradeType type) {
        validateUpgradeType(type);
        if (type.isEnough(inventory.getStackInSlot(0)) && getUpgradeLevel(type) < type.getMaxLevel()) {
            inventory.getStackInSlot(0).shrink(type.getRequiredCount());
            int newLevel = upgrades.get(type) + 1;
            if (newLevel > type.getMaxLevel())
                newLevel = type.getMaxLevel();
            if (newLevel < 0)
                newLevel = 0;
            upgrades.put(type, newLevel);
        }
        syncer.syncData();
    }

    public void downgrade(UpgradeType type) {
        validateUpgradeType(type);
        ItemStack itemStack = new ItemStack(type.getDelegate(), type.getRequiredCount());
        if (getUpgradeLevel(type) > 0 && canHold(itemStack)) {
            int newLevel = upgrades.get(type) - 1;
            if (newLevel > type.getMaxLevel())
                newLevel = type.getMaxLevel();
            if (newLevel < 0)
                newLevel = 0;
            upgrades.put(type, newLevel);
            inventory.addItem(itemStack);
        }
        syncer.syncData();
    }

    public boolean isMaxLevel(UpgradeType type) {
        validateUpgradeType(type);
        return type.getMaxLevel() == upgrades.get(type);
    }


    public void dropAllItem(World world, BlockPos pos) {
        InventoryHelper.dropInventoryItems(world, pos, inventory);
        inventory.clear();
        Inventory inventory = new Inventory(100);
        upgrades.forEach((type, count) -> {
            inventory.addItem(new ItemStack(type.getDelegate(), type.getRequiredCount() * count));
            upgrades.put(type, 0);
        });
        InventoryHelper.dropInventoryItems(world, pos, inventory);
    }

    public boolean canHold(ItemStack itemStack) {
        return inventory.func_233541_b_(itemStack) && inventory.getStackInSlot(0).getMaxStackSize() - inventory.getStackInSlot(0).getCount() >= itemStack.getCount();
    }

}
