package cn.leomc.mobfarmutilities.common.api;

import cn.leomc.mobfarmutilities.common.item.upgrade.UpgradeType;
import com.google.common.collect.Lists;
import me.shedaniel.architectury.extensions.BlockEntityExtension;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class UpgradeHandler {

    protected HashMap<UpgradeType, Integer> upgrades;
    protected SimpleContainer inventory;
    protected BlockEntityExtension syncer;

    public UpgradeHandler(BlockEntityExtension extension, UpgradeType... types) {
        this.syncer = extension;
        inventory = new SimpleContainer(1) {
            @Override
            public boolean canPlaceItem(int index, ItemStack stack) {
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


    public SimpleContainer getInventory() {
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

    public CompoundTag write(CompoundTag nbt) {
        return write(nbt, true);
    }

    public CompoundTag write(CompoundTag nbt, boolean includeInv) {
        ListTag listNBT = new ListTag();

        for (UpgradeType type : upgrades.keySet()) {
            CompoundTag compoundNBT = new CompoundTag();
            compoundNBT.putString("Upgrade", type.name());
            compoundNBT.putInt("Level", getUpgradeLevel(type));
            listNBT.add(compoundNBT);
        }

        if (!listNBT.isEmpty()) {
            nbt.put("Upgrades", listNBT);
        }

        if (includeInv)
            nbt.put("inventory", inventory.createTag());

        return nbt;
    }

    public void read(CompoundTag nbt) {
        ListTag listNBT = nbt.getList("Upgrades", 10);
        for (int i = 0; i < listNBT.size(); ++i) {
            CompoundTag compoundNBT = listNBT.getCompound(i);
            UpgradeType type = UpgradeType.valueOf(compoundNBT.getString("Upgrade"));
            upgrades.put(type, compoundNBT.getInt("Level"));
        }
        if (nbt.contains("inventory"))
            inventory.fromTag(nbt.getList("inventory", 10));

    }

    public List<UpgradeType> getSupportedUpgrades() {
        return Lists.newArrayList(upgrades.keySet());
    }

    public void upgrade(UpgradeType type) {
        validateUpgradeType(type);
        if (type.isEnough(inventory.getItem(0)) && getUpgradeLevel(type) < type.getMaxLevel()) {
            inventory.getItem(0).shrink(type.getRequiredCount());
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


    public void dropAllItem(Level world, BlockPos pos) {
        Containers.dropContents(world, pos, inventory);
        inventory.clearContent();
        SimpleContainer inventory = new SimpleContainer(100);
        upgrades.forEach((type, count) -> {
            inventory.addItem(new ItemStack(type.getDelegate(), type.getRequiredCount() * count));
            upgrades.put(type, 0);
        });
        Containers.dropContents(world, pos, inventory);
    }

    public boolean canHold(ItemStack itemStack) {
        return inventory.canAddItem(itemStack) && inventory.getItem(0).getMaxStackSize() - inventory.getItem(0).getCount() >= itemStack.getCount();
    }

}
