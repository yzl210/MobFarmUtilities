package cn.leomc.mobfarmutilities.common.api;

import cn.leomc.mobfarmutilities.MobFarmUtilities;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

public enum UpgradeType implements ITranslatable {

    FAN_DISTANCE(10, 2, Items.SUGAR, null),
    FAN_WIDTH(10, 4, Items.IRON_INGOT, new ResourceLocation("forge", "ingots/iron")),
    FAN_HEIGHT(10, 4, Items.REDSTONE, new ResourceLocation("forge", "dusts/redstone")),
    FAN_SPEED(5, 4, Items.FEATHER, new ResourceLocation("forge", "feathers")),

    SLAUGHTERER_SHARPNESS(15, 1, Items.IRON_SWORD, null),
    SLAUGHTERER_LOOTING(10, 2, Items.LAPIS_BLOCK, new ResourceLocation("forge", "storage_blocks/lapis")),
    SLAUGHTERER_FIRE_ASPECT(1, 1, Items.FLINT_AND_STEEL, null),
    SLAUGHTERER_AREA(10, 8, Items.IRON_INGOT, new ResourceLocation("forge", "ingots/iron"));


    private final int maxLevel;
    private final int requiredCount;
    private final Item delegate;
    private final ResourceLocation tagRL;
    private final Tag<Item> tag;

    UpgradeType(int maxLevel, int requiredCount, Item delegate, ResourceLocation tag) {
        this.maxLevel = maxLevel;
        this.requiredCount = requiredCount;
        this.delegate = delegate;
        this.tagRL = tag;
        this.tag = ItemTags.getAllTags().getTag(tag);
    }


    public int getMaxLevel() {
        return maxLevel;
    }

    public List<Item> getSupportedItems() {
        List<Item> itemList = new ArrayList<>();
        if (tag != null)
            itemList.addAll(tag.getValues());
        if (delegate != null && !itemList.contains(delegate))
            itemList.add(delegate);
        return itemList;
    }

    public ResourceLocation getTagResourceLocation() {
        return tagRL;
    }

    public String getTagName() {
        return tagRL == null || getTag() == null ? "" : tagRL.toString();
    }

    public Tag<Item> getTag() {
        return tag;
    }

    public Item getDelegate() {
        return delegate;
    }

    public int getRequiredCount() {
        return requiredCount;
    }

    public boolean isEnough(ItemStack itemStack) {
        return getSupportedItems().contains(itemStack.getItem()) && itemStack.getCount() >= getRequiredCount() && !itemStack.isDamaged();
    }

    @Override
    public String getTranslationKey() {
        return "text." + MobFarmUtilities.MODID + ".upgrade." + name().toLowerCase();
    }

    @Environment(EnvType.CLIENT)
    public String getLocalizedName() {
        return I18n.get(getTranslationKey());
    }


}
