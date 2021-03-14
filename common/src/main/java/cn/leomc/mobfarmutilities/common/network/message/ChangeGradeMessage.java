package cn.leomc.mobfarmutilities.common.network.message;

import cn.leomc.mobfarmutilities.common.api.UpgradeType;
import cn.leomc.mobfarmutilities.common.api.blockstate.Upgradable;
import cn.leomc.mobfarmutilities.common.menu.BaseMenu;
import me.shedaniel.architectury.networking.NetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

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

    public static void encode(ChangeGradeMessage message, FriendlyByteBuf packetBuffer) {
        packetBuffer.writeBlockPos(message.pos);
        packetBuffer.writeEnum(message.type);
        packetBuffer.writeInt(message.upgrade ? 0 : 1);
    }

    public static ChangeGradeMessage decode(FriendlyByteBuf packetBuffer) {
        return new ChangeGradeMessage(packetBuffer.readBlockPos(), packetBuffer.readEnum(UpgradeType.class), packetBuffer.readInt() == 0);
    }

    public static void handle(ChangeGradeMessage message, Supplier<NetworkManager.PacketContext> context) {
        Player playerEntity = context.get().getPlayer();
        if (playerEntity == null)
            return;
        context.get().queue(() -> {
            if (playerEntity.containerMenu instanceof BaseMenu) {
                Level world = playerEntity.getCommandSenderWorld();
                if (world.hasChunkAt(message.pos)) {
                    BlockEntity tileEntity = world.getBlockEntity(message.pos);
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
