package cn.leomc.mobfarmutilities.common.container;

import cn.leomc.mobfarmutilities.common.registry.BlockRegistry;
import cn.leomc.mobfarmutilities.common.registry.ContainerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;

public class ExperienceCollectorContainer extends BaseContainer {
    public ExperienceCollectorContainer(TileEntity tileEntity, PlayerEntity playerEntity, PlayerInventory playerInventory, int windowId) {
        super(ContainerRegistry.EXPERIENCE_COLLECTOR.get(), tileEntity, playerEntity, playerInventory, windowId);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(IWorldPosCallable.of(tileEntity.getWorld(), tileEntity.getPos()), playerIn, BlockRegistry.EXPERIENCE_COLLECTOR.get());
    }


}
