package cn.leomc.mobfarmutilities.common.compat;

import cn.leomc.mobfarmutilities.common.network.NetworkHandler;
import cn.leomc.mobfarmutilities.common.network.message.SyncDataMessage;
import me.shedaniel.architectury.extensions.BlockEntityExtension;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;

public class InfoProvider {

    int syncCount = 0;

    public void appendInfo(List<Component> tooltip, BlockEntity blockEntity) {
        if (syncCount <= 0 && blockEntity instanceof BlockEntityExtension) {
            NetworkHandler.INSTANCE.sendToServer(new SyncDataMessage(blockEntity.getBlockPos()));
            syncCount = 20;
        }
        syncCount--;
        if (blockEntity instanceof IInfoProvider)
            tooltip.addAll(((IInfoProvider) blockEntity).getInfo());

    }

}
