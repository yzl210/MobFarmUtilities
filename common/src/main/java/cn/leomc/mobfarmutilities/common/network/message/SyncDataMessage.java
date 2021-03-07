package cn.leomc.mobfarmutilities.common.network.message;

import me.shedaniel.architectury.extensions.BlockEntityExtension;
import me.shedaniel.architectury.networking.NetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.function.Supplier;

public class SyncDataMessage {

    public BlockPos pos;

    public SyncDataMessage(BlockPos pos) {
        this.pos = pos;
    }

    public static void encode(SyncDataMessage message, FriendlyByteBuf packetBuffer) {
        packetBuffer.writeBlockPos(message.pos);
    }

    public static SyncDataMessage decode(FriendlyByteBuf packetBuffer) {
        return new SyncDataMessage(packetBuffer.readBlockPos());
    }

    public static void handle(SyncDataMessage message, Supplier<NetworkManager.PacketContext> context) {
        Player playerEntity = context.get().getPlayer();
        if (playerEntity == null)
            return;
        context.get().queue(() -> {
            Level world = playerEntity.getCommandSenderWorld();
            if (world.hasChunkAt(message.pos)) {
                BlockEntity blockEntity = world.getBlockEntity(message.pos);
                if (blockEntity instanceof BlockEntityExtension)
                    ((BlockEntityExtension) blockEntity).syncData();
            }
        });
    }

}
