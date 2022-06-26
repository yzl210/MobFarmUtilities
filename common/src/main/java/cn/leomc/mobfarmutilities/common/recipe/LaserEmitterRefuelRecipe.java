package cn.leomc.mobfarmutilities.common.recipe;

import cn.leomc.mobfarmutilities.MobFarmUtilities;
import cn.leomc.mobfarmutilities.common.registry.ItemRegistry;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class LaserEmitterRefuelRecipe extends CustomRecipe {

    public static final ResourceLocation ID = new ResourceLocation(MobFarmUtilities.MODID, "laser_emitter_refuel");

    public static final RecipeType<LaserEmitterRefuelRecipe> TYPE = new RecipeType<>(){
        @Override
        public String toString() {
            return ID.toString();
        }
    };

    public static final RecipeSerializer<LaserEmitterRefuelRecipe> SERIALIZER = new SimpleRecipeSerializer<>(LaserEmitterRefuelRecipe::new);


    public LaserEmitterRefuelRecipe(ResourceLocation resourceLocation) {
        super(resourceLocation);
    }

    @Override
    public boolean matches(@NotNull CraftingContainer container, @NotNull Level level) {
        return container.getContainerSize() > 1 && container.countItem(ItemRegistry.LASER_EMITTER.get()) == 1 && container.countItem(Items.REDSTONE_BLOCK) > 0;
    }

    @Override
    public ItemStack assemble(@NotNull CraftingContainer container) {
        for(int i = 0; i < container.getContainerSize(); i++) {
            ItemStack itemStack = container.getItem(i);
            if(itemStack.is(ItemRegistry.LASER_EMITTER.get())) {
                itemStack = itemStack.copy();
                itemStack.setDamageValue(itemStack.getDamageValue() - 9);
                return itemStack;
            }
        }

        return ItemStack.EMPTY;
    }


    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(Ingredient.of(ItemRegistry.LASER_EMITTER.get()), Ingredient.of(Items.REDSTONE_BLOCK));
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height > 1;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }
}
