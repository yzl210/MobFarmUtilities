package cn.leomc.mobfarmutilities.common.network.message;


import cn.leomc.mobfarmutilities.common.block.ActivatableBlock;
import cn.leomc.mobfarmutilities.common.menu.BaseMenu;
import me.shedaniel.architectury.networking.NetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class RedstoneModeChangeMessage {

    public BlockPos pos;

    public RedstoneModeChangeMessage(BlockPos pos) {
        this.pos = pos;
    }

    public static void encode(RedstoneModeChangeMessage message, FriendlyByteBuf packetBuffer) {
        packetBuffer.writeBlockPos(message.pos);
    }

    public static RedstoneModeChangeMessage decode(FriendlyByteBuf packetBuffer) {
        return new RedstoneModeChangeMessage(packetBuffer.readBlockPos());
    }

    public static void handle(RedstoneModeChangeMessage message, Supplier<NetworkManager.PacketContext> context) {
        Player playerEntity = context.get().getPlayer();
        if (playerEntity == null)
            return;
        context.get().queue(() -> {
            if (playerEntity.containerMenu instanceof BaseMenu) {
                Level world = playerEntity.getCommandSenderWorld();
                if (world.hasChunkAt(message.pos)) {
                    BlockState state = world.getBlockState(message.pos);
                    world.setBlockAndUpdate(message.pos, state.setValue(ActivatableBlock.MODE, state.getValue(ActivatableBlock.MODE).next()));
                }
            }
        });
    }

}
