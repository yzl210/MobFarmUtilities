package cn.leomc.mobfarmutilities.common.container;

import cn.leomc.mobfarmutilities.common.registry.BlockRegistry;
import cn.leomc.mobfarmutilities.common.registry.ContainerRegistry;
import cn.leomc.mobfarmutilities.common.tileentity.ItemCollectorTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;

public class ItemCollectorContainer extends BaseContainer {
    public ItemCollectorContainer(TileEntity tileEntity, PlayerEntity playerEntity, PlayerInventory playerInventory, int windowId) {
        super(ContainerRegistry.ITEM_COLLECTOR.get(), tileEntity, playerEntity, playerInventory, windowId);
        if (tileEntity instanceof ItemCollectorTileEntity)
            addSlotBox(((ItemCollectorTileEntity) tileEntity).getInventory(), 0, 8, 34, 9, 18, 2, 18);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(IWorldPosCallable.of(tileEntity.getWorld(), tileEntity.getPos()), playerIn, BlockRegistry.ITEM_COLLECTOR.get());
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack stack = slot.getStack();
            itemstack = stack.copy();
            if (index >= 36) {
                if (!this.mergeItemStack(stack, 0, 36, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(stack, itemstack);
            } else {
                return ItemStack.EMPTY;
            }

            if (stack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (stack.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, stack);
        }

        return itemstack;
    }

}
