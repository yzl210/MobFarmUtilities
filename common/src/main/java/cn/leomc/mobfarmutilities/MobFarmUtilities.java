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
import cn.leomc.mobfarmutilities.common.registry.ModRegistry;
import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.event.events.client.ClientTextureStitchEvent;
import dev.architectury.platform.Platform;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.utils.Env;
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
        ModRegistry.register();
        if (Platform.getEnvironment() == Env.CLIENT) {
            ClientTextureStitchEvent.PRE.register(this::onPreTextureStitch);
            ClientTextureStitchEvent.POST.register(this::onPostTextureStitch);
            ClientLifecycleEvent.CLIENT_SETUP.register(this::onClientSetup);
        }
    }

    @Environment(EnvType.CLIENT)
    private void onClientSetup(Minecraft minecraft) {
        MenuRegistry.registerScreenFactory(ContainerMenuRegistry.FAN.get(), FanScreen::new);
        MenuRegistry.registerScreenFactory(ContainerMenuRegistry.ITEM_COLLECTOR.get(), ItemCollectorScreen::new);
        MenuRegistry.registerScreenFactory(ContainerMenuRegistry.EXPERIENCE_COLLECTOR.get(), ExperienceCollectorScreen::new);
        MenuRegistry.registerScreenFactory(ContainerMenuRegistry.SLAUGHTERER.get(), SlaughtererScreen::new);
        BlockEntityRendererRegistry.register(BlockEntityRegistry.FAN.get(), AreaRenderer::new);
        BlockEntityRendererRegistry.register(BlockEntityRegistry.ITEM_COLLECTOR.get(), AreaRenderer::new);
        BlockEntityRendererRegistry.register(BlockEntityRegistry.EXPERIENCE_COLLECTOR.get(), AreaRenderer::new);
        BlockEntityRendererRegistry.register(BlockEntityRegistry.SLAUGHTERER.get(), AreaRenderer::new);
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
