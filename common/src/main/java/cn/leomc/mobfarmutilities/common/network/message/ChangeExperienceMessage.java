package cn.leomc.mobfarmutilities.common.network.message;

import cn.leomc.mobfarmutilities.common.blockentity.ExperienceCollectorBlockEntity;
import cn.leomc.mobfarmutilities.common.menu.BaseMenu;
import cn.leomc.mobfarmutilities.common.utils.Utils;
import me.shedaniel.architectury.networking.NetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

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

    public static void encode(ChangeExperienceMessage message, FriendlyByteBuf packetBuffer) {
        packetBuffer.writeBlockPos(message.pos);
        packetBuffer.writeInt(message.amount);
        packetBuffer.writeUtf(Boolean.toString(message.level));
        packetBuffer.writeByteArray(Boolean.toString(message.toPlayer).getBytes());
    }

    public static ChangeExperienceMessage decode(FriendlyByteBuf packetBuffer) {
        return new ChangeExperienceMessage(packetBuffer.readBlockPos(), packetBuffer.readInt(), Boolean.parseBoolean(packetBuffer.readUtf()), Boolean.parseBoolean(new String(packetBuffer.readByteArray())));
    }


    public static void handle(ChangeExperienceMessage message, Supplier<NetworkManager.PacketContext> context) {
        if (true)
            return;
        Player playerEntity = context.get().getPlayer();
        if (playerEntity == null)
            return;
        context.get().queue(() -> {
            if (playerEntity.containerMenu instanceof BaseMenu) {
                Level world = playerEntity.getCommandSenderWorld();
                if (world.hasChunkAt(message.pos)) {
                    BlockEntity tileEntityO = world.getBlockEntity(message.pos);
                    if (tileEntityO instanceof ExperienceCollectorBlockEntity) {
                        ExperienceCollectorBlockEntity tileEntity = (ExperienceCollectorBlockEntity) tileEntityO;
                        if (message.toPlayer)
                            if (message.level) {
                                int neededPoints = Utils.getPointsForNextLevel(playerEntity.experienceLevel, message.amount);
                                if (tileEntity.getAmount() >= neededPoints) {
                                    tileEntity.addAmount(-neededPoints);
                                    playerEntity.giveExperienceLevels(message.amount);
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
                                    playerEntity.giveExperienceLevels(-message.amount);
                                }
                            } else {
                                if (playerEntity.totalExperience >= message.amount) {
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
