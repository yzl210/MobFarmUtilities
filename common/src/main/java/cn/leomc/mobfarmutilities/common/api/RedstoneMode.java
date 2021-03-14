package cn.leomc.mobfarmutilities.common.api;

import cn.leomc.mobfarmutilities.MobFarmUtilities;
import cn.leomc.mobfarmutilities.client.utils.TextureUtils;
import cn.leomc.mobfarmutilities.common.block.ActivatableBlock;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public enum RedstoneMode implements ITranslatable, StringRepresentable {
    IGNORED,
    HIGH,
    LOW;

    @Environment(EnvType.CLIENT)
    private net.minecraft.client.renderer.texture.TextureAtlasSprite sprite;

    public static String getModeTranslationKey() {
        return "text." + MobFarmUtilities.MODID + ".mode";
    }

    public static void updateRedstone(BlockState state, Level world, BlockPos pos) {
        RedstoneMode mode = state.getValue(ActivatableBlock.MODE);
        if (mode == RedstoneMode.HIGH)
            if (world.hasNeighborSignal(pos))
                world.setBlockAndUpdate(pos, state.setValue(ActivatableBlock.ACTIVE, true));
            else
                world.setBlockAndUpdate(pos, state.setValue(ActivatableBlock.ACTIVE, false));
        if (mode == RedstoneMode.LOW)
            if (world.hasNeighborSignal(pos))
                world.setBlockAndUpdate(pos, state.setValue(ActivatableBlock.ACTIVE, false));
            else
                world.setBlockAndUpdate(pos, state.setValue(ActivatableBlock.ACTIVE, true));
        if (mode == RedstoneMode.IGNORED)
            world.setBlockAndUpdate(pos, state.setValue(ActivatableBlock.ACTIVE, true));
    }

    public RedstoneMode next() {
        switch (this) {
            case HIGH:
                return LOW;
            case LOW:
                return IGNORED;
            case IGNORED:
                return HIGH;
            default:
                throw new IllegalStateException("What am I?");
        }
    }

    @Override
    public String getSerializedName() {
        return toString().toLowerCase();
    }

    @Override
    public String getTranslationKey() {
        return "text." + MobFarmUtilities.MODID + ".mode." + getSerializedName();
    }

    public ResourceLocation getTextureResourceLocation() {
        switch (this) {
            case HIGH:
                return new ResourceLocation("minecraft", "block/redstone_torch");
            case LOW:
                return new ResourceLocation("minecraft", "block/redstone_torch_off");
            case IGNORED:
                return new ResourceLocation("minecraft", "item/gunpowder");
            default:
                throw new IllegalStateException("What am I?");
        }
    }

    @Environment(EnvType.CLIENT)
    public net.minecraft.client.renderer.texture.TextureAtlasSprite getTextureAtlasSprite() {
        if (sprite == null)
            sprite = TextureUtils.getAtlasTexture(getTextureResourceLocation());
        return sprite;
    }

    @Environment(EnvType.CLIENT)
    public void resetTextureAtlasSprite() {
        this.sprite = null;
    }

}
