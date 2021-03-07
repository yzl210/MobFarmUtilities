package cn.leomc.mobfarmutilities.common.menu;

import cn.leomc.mobfarmutilities.common.blockentity.ItemCollectorBlockEntity;
import cn.leomc.mobfarmutilities.common.registry.BlockRegistry;
import cn.leomc.mobfarmutilities.common.registry.ContainerMenuRegistry;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ItemCollectorMenu extends BaseMenu {
    public ItemCollectorMenu(BlockEntity tileEntity, Player playerEntity, Inventory playerInventory, int windowId) {
        super(ContainerMenuRegistry.ITEM_COLLECTOR.get(), tileEntity, playerEntity, playerInventory, windowId);
        if (tileEntity instanceof ItemCollectorBlockEntity)
            addSlotBox(((ItemCollectorBlockEntity) tileEntity).getInventory(), 0, 8, 34, 9, 18, 2, 18);
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return stillValid(ContainerLevelAccess.create(tileEntity.getLevel(), tileEntity.getBlockPos()), playerIn, BlockRegistry.ITEM_COLLECTOR.get());
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stack = slot.getItem();
            itemstack = stack.copy();
            if (index >= 36) {
                if (!this.moveItemStackTo(stack, 0, 36, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(stack, itemstack);
            } else {
                return ItemStack.EMPTY;
            }

            if (stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stack.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, stack);
        }

        return itemstack;
    }

}
