package cn.leomc.mobfarmutilities.common.blockentity;

import cn.leomc.mobfarmutilities.MobFarmUtilities;
import cn.leomc.mobfarmutilities.common.api.FakePlayer;
import cn.leomc.mobfarmutilities.common.api.IHasArea;
import cn.leomc.mobfarmutilities.common.api.RedstoneMode;
import cn.leomc.mobfarmutilities.common.api.UpgradeHandler;
import cn.leomc.mobfarmutilities.common.api.UpgradeType;
import cn.leomc.mobfarmutilities.common.api.blockstate.Upgradable;
import cn.leomc.mobfarmutilities.common.block.ActivatableBlock;
import cn.leomc.mobfarmutilities.common.compat.IInfoProvider;
import cn.leomc.mobfarmutilities.common.menu.SlaughtererMenu;
import cn.leomc.mobfarmutilities.common.registry.BlockEntityRegistry;
import cn.leomc.mobfarmutilities.common.utils.FakePlayers;
import com.mojang.authlib.GameProfile;
import me.shedaniel.architectury.extensions.BlockEntityExtension;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class SlaughtererBlockEntity extends BlockEntity implements TickableBlockEntity, MenuProvider, Upgradable, BlockEntityExtension, IInfoProvider, IHasArea {


    protected final UpgradeHandler upgradeHandler = new UpgradeHandler(this, UpgradeType.SLAUGHTERER_AREA, UpgradeType.SLAUGHTERER_SHARPNESS, UpgradeType.SLAUGHTERER_LOOTING, UpgradeType.SLAUGHTERER_FIRE_ASPECT);
    protected int coolDown;
    protected FakePlayer fakePlayer;
    protected boolean renderArea = false;

    public SlaughtererBlockEntity() {
        super(BlockEntityRegistry.SLAUGHTERER.get());
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("container." + MobFarmUtilities.MODID + ".slaughterer");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        syncData();
        return new SlaughtererMenu(this, player, inventory, i);
    }

    @Override
    public void tick() {
        if (level.isClientSide)
            return;
        if (fakePlayer == null || fakePlayer.isDeadOrDying())
            fakePlayer = FakePlayers.getOrCreate((ServerLevel) level, new GameProfile(UUID.randomUUID(), "Slaughterer"), new Vec3(worldPosition.getX(), worldPosition.getY() + 4096, worldPosition.getZ()));

        if (coolDown > 0) {
            coolDown--;
            return;
        }
        coolDown = 20;
        BlockState state = level.getBlockState(worldPosition);
        RedstoneMode.updateRedstone(state, level, worldPosition);
        if (!state.getValue(ActivatableBlock.ACTIVE))
            return;


        ItemStack itemStack = new ItemStack(Items.WOODEN_SHOVEL);
        itemStack.getOrCreateTag().putBoolean("Unbreakable", true);
        if (upgradeHandler.getUpgradeLevel(UpgradeType.SLAUGHTERER_LOOTING) > 0)
            itemStack.enchant(Enchantments.MOB_LOOTING, upgradeHandler.getUpgradeLevel(UpgradeType.SLAUGHTERER_LOOTING));
        if (upgradeHandler.getUpgradeLevel(UpgradeType.SLAUGHTERER_FIRE_ASPECT) > 0)
            itemStack.enchant(Enchantments.FIRE_ASPECT, upgradeHandler.getUpgradeLevel(UpgradeType.SLAUGHTERER_FIRE_ASPECT));
        fakePlayer.setItemInHand(InteractionHand.MAIN_HAND, itemStack);
        level.getEntitiesOfClass(LivingEntity.class, getAABB()).forEach(entity -> {
            float damage = 1.0F;
            damage += upgradeHandler.getUpgradeLevel(UpgradeType.SLAUGHTERER_SHARPNESS);
            damage -= Math.sqrt(entity.distanceToSqr(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ()));
            if (damage <= 0)
                damage = 0.5F;
            fakePlayer.attack(entity, damage);
        });
        fakePlayer.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag) {
        return upgradeHandler.write(super.save(compoundTag));
    }

    @Override
    public void load(BlockState blockState, CompoundTag tag) {
        super.load(blockState, tag);
        upgradeHandler.read(tag);
    }

    @Override
    public void loadClientData(@NotNull BlockState pos, @NotNull CompoundTag tag) {
        upgradeHandler.read(tag);
    }

    @Override
    public @NotNull CompoundTag saveClientData(@NotNull CompoundTag tag) {
        return upgradeHandler.write(tag, false);
    }

    public UpgradeHandler getUpgradeHandler() {
        return upgradeHandler;
    }


    public void remove() {
        if (fakePlayer != null)
            FakePlayers.remove(fakePlayer.getGameProfile());
        upgradeHandler.dropAllItem(level, worldPosition);
    }

    @Override
    public List<Component> getInfo() {
        List<Component> components = new ArrayList<>();
        upgradeHandler.getSupportedUpgrades().forEach(type -> components.add(new TranslatableComponent("text.mobfarmutilities.info.upgrade", new TranslatableComponent(type.getTranslationKey()), upgradeHandler.getUpgradeLevel(type), type.getMaxLevel())));
        return components;
    }

    @Override
    public AABB getAABB() {
        int increase = upgradeHandler.getUpgradeLevel(UpgradeType.SLAUGHTERER_AREA);
        return new AABB(worldPosition
                .relative(Direction.NORTH, 2)
                .relative(Direction.WEST, 2)
                .relative(Direction.UP, 2)
                .offset(-increase, 0, -increase)
                , worldPosition
                .relative(Direction.SOUTH, 2)
                .relative(Direction.EAST, 2)
                .relative(Direction.DOWN, 2)
                .offset(1, 1, 1)
                .offset(increase, 0, increase)
        );
    }

    @Override
    public AABB getRenderAABB() {
        return getAABB().move(-worldPosition.getX(), -worldPosition.getY(), -worldPosition.getZ());
    }

    @Override
    public boolean doRenderArea() {
        return renderArea;
    }

    @Override
    public void setRenderArea(boolean renderArea) {
        this.renderArea = renderArea;
    }
}
