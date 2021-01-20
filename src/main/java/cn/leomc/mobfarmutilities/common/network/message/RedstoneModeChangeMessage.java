package cn.leomc.mobfarmutilities.common.network.message;


import cn.leomc.mobfarmutilities.common.block.ActivatableBlock;
import cn.leomc.mobfarmutilities.common.container.BaseContainer;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class RedstoneModeChangeMessage {

    public BlockPos pos;

    public RedstoneModeChangeMessage(BlockPos pos) {
        this.pos = pos;
    }

    public static void encode(RedstoneModeChangeMessage message, PacketBuffer packetBuffer) {
        packetBuffer.writeBlockPos(message.pos);
    }

    public static RedstoneModeChangeMessage decode(PacketBuffer packetBuffer) {
        return new RedstoneModeChangeMessage(packetBuffer.readBlockPos());
    }

    public static void handle(RedstoneModeChangeMessage message, Supplier<NetworkEvent.Context> context) {
        PlayerEntity playerEntity = context.get().getSender();
        if (playerEntity == null)
            return;
        context.get().enqueueWork(() -> {
            if (playerEntity.openContainer instanceof BaseContainer) {
                World world = playerEntity.getEntityWorld();
                if (world.isBlockLoaded(message.pos)) {
                    BlockState state = world.getBlockState(message.pos);
                    world.setBlockState(message.pos, state.with(ActivatableBlock.MODE, state.get(ActivatableBlock.MODE).next()));
                }
            }
        });
    }

}
