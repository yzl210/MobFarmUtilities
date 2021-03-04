package cn.leomc.mobfarmutilities.client.utils;

import cn.leomc.mobfarmutilities.MobFarmUtilities;
import cn.leomc.mobfarmutilities.common.fluid.LiquidExperienceFluid;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class Textures {

    public static final List<ResourceLocation> REGISTRIES = new ArrayList<>();
    public static final HashMap<ResourceLocation, TextureAtlasSprite> TEXTURE_MAP = new HashMap<>();

    public static Supplier<TextureAtlasSprite> GENERIC_GUI = register("gui/generic");

    public static Supplier<TextureAtlasSprite> SLOT_SMALL = register("gui/slot_small");

    public static Supplier<TextureAtlasSprite> HORIZONTAL_FLUID_TANK = register("gui/horizontal_fluid_tank");

    public static Supplier<TextureAtlasSprite> VERTICAL_FLUID_TANK = register("gui/vertical_fluid_tank");

    public static Supplier<TextureAtlasSprite> TEXTFIELD = register("gui/textfield");


    public static Supplier<TextureAtlasSprite> STILL_LIQUID_EXPERIENCE = register(LiquidExperienceFluid.Still.getResourceLocation());

    public static Supplier<TextureAtlasSprite> FLOWING_LIQUID_EXPERIENCE = register(LiquidExperienceFluid.Flowing.getResourceLocation());


    public static Supplier<TextureAtlasSprite> register(String file) {
        ResourceLocation rl = new ResourceLocation(MobFarmUtilities.MODID, file);
        REGISTRIES.add(rl);
        return () -> TEXTURE_MAP.get(rl);
    }

    public static Supplier<TextureAtlasSprite> register(ResourceLocation resourceLocation) {
        REGISTRIES.add(resourceLocation);
        return () -> TEXTURE_MAP.get(resourceLocation);
    }

}
