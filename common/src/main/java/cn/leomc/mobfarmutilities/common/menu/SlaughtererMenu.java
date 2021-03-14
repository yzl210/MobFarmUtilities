package cn.leomc.mobfarmutilities.common.menu;

import cn.leomc.mobfarmutilities.common.blockentity.SlaughtererBlockEntity;
import cn.leomc.mobfarmutilities.common.registry.BlockRegistry;
import cn.leomc.mobfarmutilities.common.registry.ContainerMenuRegistry;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class SlaughtererMenu extends BaseMenu {

    public SlaughtererMenu(BlockEntity tileEntity, Player playerEntity, Inventory playerInventory, int windowId) {
        super(ContainerMenuRegistry.SLAUGHTERER.get(), tileEntity, playerEntity, playerInventory, windowId);
        if (tileEntity instanceof SlaughtererBlockEntity)
            addSlotRange(((SlaughtererBlockEntity) tileEntity).getUpgradeHandler().getInventory(), 0, 79, 60, 1, 0);
    }


    @Override
    public boolean stillValid(Player playerIn) {
        return stillValid(ContainerLevelAccess.create(tileEntity.getLevel(), tileEntity.getBlockPos()), playerIn, BlockRegistry.SLAUGHTERER.get());
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stack = slot.getItem();
            itemstack = stack.copy();
            if (index >= 36 && index <= 37) {
                if (!this.moveItemStackTo(stack, 0, 36, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(stack, itemstack);
            } else {

                if (!this.moveItemStackTo(stack, 36, 37, false)) {
                    return ItemStack.EMPTY;
                }

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
