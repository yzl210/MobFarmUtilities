package cn.leomc.mobfarmutilities.fabric;

import cn.leomc.mobfarmutilities.client.screen.ExperienceCollectorScreen;
import cn.leomc.mobfarmutilities.client.screen.FanScreen;
import cn.leomc.mobfarmutilities.client.screen.ItemCollectorScreen;
import cn.leomc.mobfarmutilities.client.utils.Textures;
import cn.leomc.mobfarmutilities.common.registry.BlockRegistry;
import cn.leomc.mobfarmutilities.common.registry.ContainerRegistry;
import cn.leomc.mobfarmutilities.common.registry.FluidRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;


public class MobFarmUtilitiesFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ScreenRegistry.register(ContainerRegistry.FAN.get(), FanScreen::new);
        ScreenRegistry.register(ContainerRegistry.ITEM_COLLECTOR.get(), ItemCollectorScreen::new);
        ScreenRegistry.register(ContainerRegistry.EXPERIENCE_COLLECTOR.get(), ExperienceCollectorScreen::new);
        registerFluid(FluidRegistry.LIQUID_EXPERIENCE.get(), FluidRegistry.FLOWING_LIQUID_EXPERIENCE.get(), new Supplier[]{Textures.STILL_LIQUID_EXPERIENCE, Textures.FLOWING_LIQUID_EXPERIENCE}, -1);
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.LIQUID_EXPERIENCE.get(), RenderType.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putFluids(RenderType.getTranslucent(), FluidRegistry.LIQUID_EXPERIENCE.get(), FluidRegistry.FLOWING_LIQUID_EXPERIENCE.get());

    }

    public void registerFluid(Fluid still, Fluid flowing, Supplier<TextureAtlasSprite>[] fluidSpritesSupplier, int color) {

        FluidRenderHandler renderHandler = new FluidRenderHandler() {

            @Override
            public TextureAtlasSprite[] getFluidSprites(@Nullable IBlockDisplayReader view, @Nullable BlockPos pos, FluidState state) {
                return new TextureAtlasSprite[]{fluidSpritesSupplier[0].get(), fluidSpritesSupplier[1].get()};
            }

            @Override
            public int getFluidColor(@Nullable IBlockDisplayReader view, @Nullable BlockPos pos, FluidState state) {
                return color;
            }
        };

        FluidRenderHandlerRegistry.INSTANCE.register(still, renderHandler);
        FluidRenderHandlerRegistry.INSTANCE.register(flowing, renderHandler);
    }

}
