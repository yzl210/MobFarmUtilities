package cn.leomc.mobfarmutilities.common.menu;

import cn.leomc.mobfarmutilities.common.registry.BlockRegistry;
import cn.leomc.mobfarmutilities.common.registry.ContainerMenuRegistry;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ExperienceCollectorMenu extends BaseMenu {
    public ExperienceCollectorMenu(BlockEntity tileEntity, Player playerEntity, Inventory playerInventory, int windowId) {
        super(ContainerMenuRegistry.EXPERIENCE_COLLECTOR.get(), tileEntity, playerEntity, playerInventory, windowId);
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return stillValid(ContainerLevelAccess.create(tileEntity.getLevel(), tileEntity.getBlockPos()), playerIn, BlockRegistry.EXPERIENCE_COLLECTOR.get());
    }


}
