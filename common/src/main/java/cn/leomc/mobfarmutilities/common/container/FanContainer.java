package cn.leomc.mobfarmutilities.common.container;

import cn.leomc.mobfarmutilities.common.api.ItemStackHandlerWrapper;
import cn.leomc.mobfarmutilities.common.registry.BlockRegistry;
import cn.leomc.mobfarmutilities.common.registry.ContainerRegistry;
import cn.leomc.mobfarmutilities.common.tileentity.FanTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;

public class FanContainer extends BaseContainer {

    public FanContainer(TileEntity tileEntity, PlayerEntity playerEntity, PlayerInventory playerInventory, int windowId) {
        super(ContainerRegistry.FAN.get(), tileEntity, playerEntity, playerInventory, windowId);
        if (tileEntity instanceof FanTileEntity)
            addSlotRange(new ItemStackHandlerWrapper(((FanTileEntity) tileEntity).getItems()), 0, 20, 40, 4, 40);
    }


    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(IWorldPosCallable.of(tileEntity.getWorld(), tileEntity.getPos()), playerIn, BlockRegistry.FAN.get());
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack stack = slot.getStack();
            itemstack = stack.copy();
            if (index >= 36 && index <= 39) {
                if (!this.mergeItemStack(stack, 0, 36, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(stack, itemstack);
            } else {

                if (!this.mergeItemStack(stack, 36, 40, false)) {
                    return ItemStack.EMPTY;
                }

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
