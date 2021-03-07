package cn.leomc.mobfarmutilities.common.network.message;

import me.shedaniel.architectury.networking.NetworkManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.StringTokenizer;
import java.util.function.Supplier;

public class MotionChangeMessage {

    public Vec3 vector3d;

    public MotionChangeMessage(Vec3 vector3d) {
        this.vector3d = vector3d;
    }

    public MotionChangeMessage(double x, double y, double z) {
        this(new Vec3(x, y, z));
    }

    public static void encode(MotionChangeMessage message, FriendlyByteBuf packetBuffer) {
        packetBuffer.writeUtf(message.vector3d.x + " " + message.vector3d.y + " " + message.vector3d.z);
    }

    public static MotionChangeMessage decode(FriendlyByteBuf packetBuffer) {
        StringTokenizer st = new StringTokenizer(packetBuffer.readUtf());
        return new MotionChangeMessage(Double.parseDouble(st.nextToken()), Double.parseDouble(st.nextToken()), Double.parseDouble(st.nextToken()));
    }

    public static void handle(MotionChangeMessage message, Supplier<NetworkManager.PacketContext> context) {
        Player player = context.get().getPlayer();
        if (player == null)
            return;
        context.get().queue(() -> player.setDeltaMovement(player.getDeltaMovement().add(message.vector3d)));
    }
}
