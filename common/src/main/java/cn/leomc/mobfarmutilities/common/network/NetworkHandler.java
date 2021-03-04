package cn.leomc.mobfarmutilities.common.network;

import cn.leomc.mobfarmutilities.MobFarmUtilities;
import cn.leomc.mobfarmutilities.common.network.message.ChangeExperienceMessage;
import cn.leomc.mobfarmutilities.common.network.message.ChangeGradeMessage;
import cn.leomc.mobfarmutilities.common.network.message.MotionChangeMessage;
import cn.leomc.mobfarmutilities.common.network.message.RedstoneModeChangeMessage;
import me.shedaniel.architectury.networking.NetworkChannel;
import net.minecraft.util.ResourceLocation;


public class NetworkHandler {
    public static NetworkChannel INSTANCE;

    public static void register() {
        INSTANCE = NetworkChannel.create(new ResourceLocation(MobFarmUtilities.MODID, "main"));

        INSTANCE.register(RedstoneModeChangeMessage.class, RedstoneModeChangeMessage::encode, RedstoneModeChangeMessage::decode, RedstoneModeChangeMessage::handle);
        INSTANCE.register(MotionChangeMessage.class, MotionChangeMessage::encode, MotionChangeMessage::decode, MotionChangeMessage::handle);
        INSTANCE.register(ChangeGradeMessage.class, ChangeGradeMessage::encode, ChangeGradeMessage::decode, ChangeGradeMessage::handle);
        INSTANCE.register(ChangeExperienceMessage.class, ChangeExperienceMessage::encode, ChangeExperienceMessage::decode, ChangeExperienceMessage::handle);
    }


}
