package cn.leomc.mobfarmutilities.fabric;

import cn.leomc.mobfarmutilities.MobFarmUtilities;
import net.fabricmc.api.ModInitializer;

public class MobFarmUtilitiesFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        new MobFarmUtilities();
    }

}
