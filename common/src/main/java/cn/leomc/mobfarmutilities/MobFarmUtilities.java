package cn.leomc.mobfarmutilities;

import cn.leomc.mobfarmutilities.client.renderer.AreaRenderer;
import cn.leomc.mobfarmutilities.client.screen.ExperienceCollectorScreen;
import cn.leomc.mobfarmutilities.client.screen.FanScreen;
import cn.leomc.mobfarmutilities.client.screen.ItemCollectorScreen;
import cn.leomc.mobfarmutilities.client.screen.SlaughtererScreen;
import cn.leomc.mobfarmutilities.client.utils.Textures;
import cn.leomc.mobfarmutilities.common.api.RedstoneMode;
import cn.leomc.mobfarmutilities.common.registry.BlockEntityRegistry;
import cn.leomc.mobfarmutilities.common.registry.ContainerMenuRegistry;
import cn.leomc.mobfarmutilities.common.registry.FluidRegistry;
import cn.leomc.mobfarmutilities.common.registry.ModRegistry;
import cn.leomc.mobfarmutilities.common.utils.FakePlayers;
import me.shedaniel.architectury.event.events.LifecycleEvent;
import me.shedaniel.architectury.event.events.TextureStitchEvent;
import me.shedaniel.architectury.event.events.client.ClientLifecycleEvent;
import me.shedaniel.architectury.platform.Platform;
import me.shedaniel.architectury.registry.BlockEntityRenderers;
import me.shedaniel.architectury.registry.MenuRegistry;
import me.shedaniel.architectury.utils.Env;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Consumer;

public class MobFarmUtilities {

    public static final String MODID = "mobfarmutilities";

    public static final Logger LOGGER = LogManager.getLogger();


    public MobFarmUtilities() {
        FluidRegistry.register();
        ModRegistry.register();
        LifecycleEvent.SERVER_WORLD_UNLOAD.register(FakePlayers::onWorldUnloaded);
        if (Platform.getEnvironment() == Env.CLIENT) {
            TextureStitchEvent.PRE.register(this::onPreTextureStitch);
            TextureStitchEvent.POST.register(this::onPostTextureStitch);
            ClientLifecycleEvent.CLIENT_SETUP.register(this::onClientSetup);
        }
    }

    @Environment(EnvType.CLIENT)
    private void onClientSetup(Minecraft minecraft) {
        MenuRegistry.registerScreenFactory(ContainerMenuRegistry.FAN.get(), FanScreen::new);
        MenuRegistry.registerScreenFactory(ContainerMenuRegistry.ITEM_COLLECTOR.get(), ItemCollectorScreen::new);
        MenuRegistry.registerScreenFactory(ContainerMenuRegistry.EXPERIENCE_COLLECTOR.get(), ExperienceCollectorScreen::new);
        MenuRegistry.registerScreenFactory(ContainerMenuRegistry.SLAUGHTERER.get(), SlaughtererScreen::new);
        BlockEntityRenderers.registerRenderer(BlockEntityRegistry.FAN.get(), AreaRenderer::new);
        BlockEntityRenderers.registerRenderer(BlockEntityRegistry.ITEM_COLLECTOR.get(), AreaRenderer::new);
        BlockEntityRenderers.registerRenderer(BlockEntityRegistry.EXPERIENCE_COLLECTOR.get(), AreaRenderer::new);
        BlockEntityRenderers.registerRenderer(BlockEntityRegistry.SLAUGHTERER.get(), AreaRenderer::new);
    }


    @Environment(EnvType.CLIENT)
    private void onPreTextureStitch(TextureAtlas atlasTexture, Consumer<ResourceLocation> spriteAdder) {
        if (atlasTexture.location() == InventoryMenu.BLOCK_ATLAS) {
            Textures.REGISTRIES.forEach(spriteAdder);
        }
    }

    @Environment(EnvType.CLIENT)
    private void onPostTextureStitch(TextureAtlas atlasTexture) {
        if (atlasTexture.location() == InventoryMenu.BLOCK_ATLAS) {
            Textures.TEXTURE_MAP.clear();
            Textures.REGISTRIES.forEach(rl -> Textures.TEXTURE_MAP.put(rl, atlasTexture.getSprite(rl)));
        }
        for (RedstoneMode mode : RedstoneMode.values())
            mode.resetTextureAtlasSprite();
    }


}
