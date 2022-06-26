package cn.leomc.mobfarmutilities.common.compat;

import cn.leomc.mobfarmutilities.common.blockentity.BaseBlockEntity;
import cn.leomc.mobfarmutilities.common.network.NetworkHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.function.Consumer;

public class InfoProvider {

    private int syncCount = 0;

    public final void appendInfo(Consumer<Component> tooltip, BlockEntity blockEntity) {
        if (syncCount <= 0 && blockEntity instanceof BaseBlockEntity) {
            NetworkHandler.syncData(blockEntity.getBlockPos());
            syncCount = 20;
        }
        syncCount--;
        if (blockEntity instanceof IInfoProvider infoProvider)
            infoProvider.getInfo().forEach(tooltip);
    }

}
