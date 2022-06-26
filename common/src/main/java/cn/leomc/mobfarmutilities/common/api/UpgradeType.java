package cn.leomc.mobfarmutilities.common.api;

import cn.leomc.mobfarmutilities.MobFarmUtilities;
import cn.leomc.mobfarmutilities.common.registry.ItemRegistry;
import dev.architectury.platform.Platform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

public enum UpgradeType {

    FAN_DISTANCE(10, 2, Items.SUGAR, null),
    FAN_WIDTH(10, 4, Items.IRON_INGOT, of("forge:ingots/iron")),
    FAN_HEIGHT(10, 4, Items.REDSTONE, of("forge:dusts/redstone")),
    FAN_SPEED(5, 4, Items.FEATHER, of("forge:feathers")),

    SLAUGHTERER_SHARPNESS(15, 1, ItemRegistry.LASER_EMITTER.get(), null),
    SLAUGHTERER_LOOTING(10, 2, Items.LAPIS_BLOCK, of("forge:storage_blocks/lapis")),
    SLAUGHTERER_FIRE_ASPECT(1, 1, Items.FLINT_AND_STEEL, null),
    SLAUGHTERER_AREA(10, 1, Items.REDSTONE_BLOCK, of("forge:storage_blocks/redstone"));


    private final int maxLevel;
    private final int requiredCount;
    private final Item delegate;
    private final TagKey<Item> tag;

    UpgradeType(int maxLevel, int requiredCount, Item item) {
        this.maxLevel = maxLevel;
        this.requiredCount = requiredCount;
        this.delegate = item;
        this.tag = null;
    }

    UpgradeType(int maxLevel, int requiredCount, Item delegate, TagKey<Item> tag) {
        this.maxLevel = maxLevel;
        this.requiredCount = requiredCount;
        this.delegate = delegate;
        this.tag = tag;
    }

    public static TagKey<Item> of(String name) {
        if(name.startsWith("forge:") && Platform.isFabric())
            return null;
        ResourceLocation rl = ResourceLocation.tryParse(name);
        if(rl == null)
            return null;
        return TagKey.create(Registry.ITEM_REGISTRY, rl);
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public List<Item> getSupportedItems() {
        List<Item> itemList = new ArrayList<>();
        if (tag != null)
            itemList.addAll(Registry.ITEM.getTag(tag).stream().flatMap(HolderSet.ListBacked::stream).map(Holder::value).toList());
        if (delegate != null && !itemList.contains(delegate))
            itemList.add(delegate);
        return itemList;
    }

    public String getTagName() {
        return tag == null ? "" : tag.location().toString();
    }

    public TagKey<Item> getTag() {
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

    public String getTranslationKey() {
        return "text." + MobFarmUtilities.MODID + ".upgrade." + name().toLowerCase();
    }

    @Environment(EnvType.CLIENT)
    public String getLocalizedName() {
        return I18n.get(getTranslationKey());
    }

}
