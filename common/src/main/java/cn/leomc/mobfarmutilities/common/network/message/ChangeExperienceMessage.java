package cn.leomc.mobfarmutilities.common.network.message;

import cn.leomc.mobfarmutilities.common.blockentity.ExperienceCollectorBlockEntity;
import cn.leomc.mobfarmutilities.common.menu.BaseMenu;
import cn.leomc.mobfarmutilities.common.utils.Utils;
import me.shedaniel.architectury.networking.NetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.function.Supplier;

public class ChangeExperienceMessage {

    public BlockPos pos;
    public int amount;
    public boolean level;

    public ChangeExperienceMessage(BlockPos pos, int amount, boolean level) {
        this.pos = pos;
        this.amount = amount;
        this.level = level;
    }

    public static void encode(ChangeExperienceMessage message, FriendlyByteBuf packetBuffer) {
        packetBuffer.writeBlockPos(message.pos);
        packetBuffer.writeInt(message.amount);
        packetBuffer.writeUtf(Boolean.toString(message.level));
    }

    public static ChangeExperienceMessage decode(FriendlyByteBuf packetBuffer) {
        return new ChangeExperienceMessage(packetBuffer.readBlockPos(), packetBuffer.readInt(), Boolean.parseBoolean(packetBuffer.readUtf(32767)));
    }


    public static void handle(ChangeExperienceMessage message, Supplier<NetworkManager.PacketContext> context) {
        Player playerEntity = context.get().getPlayer();
        if (playerEntity == null)
            return;
        context.get().queue(() -> {
            if (playerEntity.containerMenu instanceof BaseMenu) {
                Level world = playerEntity.getCommandSenderWorld();
                if (world.hasChunkAt(message.pos)) {
                    BlockEntity blockEntityO = world.getBlockEntity(message.pos);
                    if (blockEntityO instanceof ExperienceCollectorBlockEntity) {
                        ExperienceCollectorBlockEntity blockEntity = (ExperienceCollectorBlockEntity) blockEntityO;
                        if (message.level) {
                            int neededPoints = Utils.getPointsForNextLevel(playerEntity.experienceLevel);
                            neededPoints = neededPoints - Mth.floor(playerEntity.experienceProgress * (float) playerEntity.getXpNeededForNextLevel());
                            if (blockEntity.getAmount() >= neededPoints) {
                                blockEntity.addAmount(-neededPoints);
                                playerEntity.giveExperiencePoints(neededPoints);
                            }
                        } else {
                            if (blockEntity.getAmount() >= message.amount) {
                                blockEntity.addAmount(-message.amount);
                                playerEntity.giveExperiencePoints(message.amount);
                            }
                        }
                    }
                }
            }
        });
    }

}
