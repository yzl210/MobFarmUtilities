package cn.leomc.mobfarmutilities.common.network.message;

import me.shedaniel.architectury.networking.NetworkManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.vector.Vector3d;

import java.util.StringTokenizer;
import java.util.function.Supplier;

public class MotionChangeMessage {

    public Vector3d vector3d;

    public MotionChangeMessage(Vector3d vector3d) {
        this.vector3d = vector3d;
    }

    public MotionChangeMessage(double x, double y, double z) {
        this(new Vector3d(x, y, z));
    }

    public static void encode(MotionChangeMessage message, PacketBuffer packetBuffer) {
        packetBuffer.writeString(message.vector3d.x + " " + message.vector3d.y + " " + message.vector3d.z);
    }

    public static MotionChangeMessage decode(PacketBuffer packetBuffer) {
        StringTokenizer st = new StringTokenizer(packetBuffer.readString());
        return new MotionChangeMessage(Double.parseDouble(st.nextToken()), Double.parseDouble(st.nextToken()), Double.parseDouble(st.nextToken()));
    }

    public static void handle(MotionChangeMessage message, Supplier<NetworkManager.PacketContext> context) {
        PlayerEntity player = context.get().getPlayer();
        if (player == null)
            return;
        context.get().queue(() -> player.setMotion(player.getMotion().add(message.vector3d)));
    }
}
