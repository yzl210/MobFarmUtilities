package cn.leomc.mobfarmutilities.common.api;

import cn.leomc.mobfarmutilities.MobFarmUtilities;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.Random;

public class LaserDamageSource extends EntityDamageSource {


    public LaserDamageSource(Entity entity) {
        super("laser", entity);
    }

    @Override
    public Component getLocalizedDeathMessage(LivingEntity livingEntity) {
        String string = "text." + MobFarmUtilities.MODID + ".death." + this.msgId + "." + new Random().nextInt(3);
        return new TranslatableComponent(string, livingEntity.getDisplayName());
    }

    @Override
    public boolean scalesWithDifficulty() {
        return false;
    }

    @Override
    public String toString() {
        return "LaserDamageSource (" + this.entity + ")";
    }
}
