package cn.leomc.mobfarmutilities;

import cn.leomc.mobfarmutilities.client.ClientProxy;
import cn.leomc.mobfarmutilities.common.api.IProxy;
import cn.leomc.mobfarmutilities.common.registry.ModRegistry;
import cn.leomc.mobfarmutilities.server.ServerProxy;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(MobFarmUtilities.MODID)
public class MobFarmUtilities {

    public static final String MODID = "mobfarmutilities";

    private static final Logger LOGGER = LogManager.getLogger();

    public static IProxy PROXY = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> ServerProxy::new);

    public MobFarmUtilities() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModRegistry.register(eventBus);
        eventBus.addListener(this::onCommonSetup);
    }

    public void onCommonSetup(FMLCommonSetupEvent event) {
        PROXY.init();
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @OnlyIn(Dist.CLIENT)
        @SubscribeEvent
        public static void onTextureStitch(TextureStitchEvent.Pre event) {
            if (!(event.getMap().getTextureLocation() == PlayerContainer.LOCATION_BLOCKS_TEXTURE))
                return;
            event.addSprite(new ResourceLocation(MODID, "gui/generic"));
            event.addSprite(new ResourceLocation(MODID, "gui/slot_small"));
        }
    }


}
