package cn.leomc.mobfarmutilities.common.network.message;

import cn.leomc.mobfarmutilities.common.api.blockstate.Upgradable;
import cn.leomc.mobfarmutilities.common.container.BaseContainer;
import cn.leomc.mobfarmutilities.common.item.upgrade.UpgradeType;
import me.shedaniel.architectury.networking.NetworkManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.function.Supplier;

public class ChangeGradeMessage {

    public BlockPos pos;
    public UpgradeType type;
    public boolean upgrade;

    public ChangeGradeMessage(BlockPos pos, UpgradeType type, boolean upgrade) {
        this.pos = pos;
        this.type = type;
        this.upgrade = upgrade;
    }

    public static void encode(ChangeGradeMessage message, PacketBuffer packetBuffer) {
        packetBuffer.writeBlockPos(message.pos);
        packetBuffer.writeEnumValue(message.type);
        packetBuffer.writeInt(message.upgrade ? 0 : 1);
    }

    public static ChangeGradeMessage decode(PacketBuffer packetBuffer) {
        return new ChangeGradeMessage(packetBuffer.readBlockPos(), packetBuffer.readEnumValue(UpgradeType.class), packetBuffer.readInt() == 0);
    }

    public static void handle(ChangeGradeMessage message, Supplier<NetworkManager.PacketContext> context) {
        PlayerEntity playerEntity = context.get().getPlayer();
        if (playerEntity == null)
            return;
        context.get().queue(() -> {
            if (playerEntity.openContainer instanceof BaseContainer) {
                World world = playerEntity.getEntityWorld();
                if (world.isBlockLoaded(message.pos)) {
                    TileEntity tileEntity = world.getTileEntity(message.pos);
                    if (tileEntity instanceof Upgradable) {
                        if (message.upgrade)
                            ((Upgradable) tileEntity).getUpgradeHandler().upgrade(message.type);
                        else
                            ((Upgradable) tileEntity).getUpgradeHandler().downgrade(message.type);
                    }
                }
            }
        });
    }

}
