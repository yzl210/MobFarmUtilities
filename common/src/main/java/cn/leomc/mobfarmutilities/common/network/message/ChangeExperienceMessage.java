package cn.leomc.mobfarmutilities.common.network.message;

import cn.leomc.mobfarmutilities.common.container.BaseContainer;
import cn.leomc.mobfarmutilities.common.tileentity.ExperienceCollectorTileEntity;
import cn.leomc.mobfarmutilities.common.utils.Utils;
import me.shedaniel.architectury.networking.NetworkManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.function.Supplier;

public class ChangeExperienceMessage {

    public BlockPos pos;
    public int amount;
    public boolean level;
    public boolean toPlayer;

    public ChangeExperienceMessage(BlockPos pos, int amount, boolean level, boolean toPlayer) {
        this.pos = pos;
        this.amount = amount;
        this.level = level;
        this.toPlayer = toPlayer;
    }

    public static void encode(ChangeExperienceMessage message, PacketBuffer packetBuffer) {
        packetBuffer.writeBlockPos(message.pos);
        packetBuffer.writeInt(message.amount);
        packetBuffer.writeString(Boolean.toString(message.level));
        packetBuffer.writeByteArray(Boolean.toString(message.toPlayer).getBytes());
    }

    public static ChangeExperienceMessage decode(PacketBuffer packetBuffer) {
        return new ChangeExperienceMessage(packetBuffer.readBlockPos(), packetBuffer.readInt(), Boolean.parseBoolean(packetBuffer.readString()), Boolean.parseBoolean(new String(packetBuffer.readByteArray())));
    }


    public static void handle(ChangeExperienceMessage message, Supplier<NetworkManager.PacketContext> context) {
        if (true)
            return;
        PlayerEntity playerEntity = context.get().getPlayer();
        if (playerEntity == null)
            return;
        context.get().queue(() -> {
            if (playerEntity.openContainer instanceof BaseContainer) {
                World world = playerEntity.getEntityWorld();
                if (world.isBlockLoaded(message.pos)) {
                    TileEntity tileEntityO = world.getTileEntity(message.pos);
                    if (tileEntityO instanceof ExperienceCollectorTileEntity) {
                        ExperienceCollectorTileEntity tileEntity = (ExperienceCollectorTileEntity) tileEntityO;
                        if (message.toPlayer)
                            if (message.level) {
                                int neededPoints = Utils.getPointsForNextLevel(playerEntity.experienceLevel, message.amount);
                                if (tileEntity.getAmount() >= neededPoints) {
                                    tileEntity.addAmount(-neededPoints);
                                    playerEntity.addExperienceLevel(message.amount);
                                }
                            } else {
                                if (tileEntity.getAmount() >= message.amount) {
                                    tileEntity.addAmount(-message.amount);
                                    playerEntity.giveExperiencePoints(message.amount);
                                }
                            }
                        else {
                            if (tileEntity.getAmount() >= tileEntity.getLimit())
                                return;
                            if (message.level && playerEntity.experienceLevel > 0) {
                                int neededPoints = Utils.getPointsForNextLevel(playerEntity.experienceLevel - message.amount, message.amount);
                                if (playerEntity.experienceLevel >= message.amount) {
                                    tileEntity.addAmount(neededPoints);
                                    playerEntity.addExperienceLevel(-message.amount);
                                }
                            } else {
                                if (playerEntity.experienceTotal >= message.amount) {
                                    tileEntity.addAmount(message.amount);
                                    playerEntity.giveExperiencePoints(-message.amount);
                                }
                            }
                        }
                    }
                }
            }
        });
    }

}
