package cn.leomc.mobfarmutilities.common.network;

import cn.leomc.mobfarmutilities.MobFarmUtilities;
import cn.leomc.mobfarmutilities.common.network.message.*;
import me.shedaniel.architectury.networking.NetworkChannel;
import net.minecraft.resources.ResourceLocation;


public class NetworkHandler {
    public static NetworkChannel INSTANCE;

    public static void register() {
        INSTANCE = NetworkChannel.create(new ResourceLocation(MobFarmUtilities.MODID, "main"));

        INSTANCE.register(RedstoneModeChangeMessage.class, RedstoneModeChangeMessage::encode, RedstoneModeChangeMessage::decode, RedstoneModeChangeMessage::handle);
        INSTANCE.register(MotionChangeMessage.class, MotionChangeMessage::encode, MotionChangeMessage::decode, MotionChangeMessage::handle);
        INSTANCE.register(ChangeGradeMessage.class, ChangeGradeMessage::encode, ChangeGradeMessage::decode, ChangeGradeMessage::handle);
        INSTANCE.register(ChangeExperienceMessage.class, ChangeExperienceMessage::encode, ChangeExperienceMessage::decode, ChangeExperienceMessage::handle);
        INSTANCE.register(SyncDataMessage.class, SyncDataMessage::encode, SyncDataMessage::decode, SyncDataMessage::handle);
    }


}
