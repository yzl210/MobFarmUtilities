package cn.leomc.mobfarmutilities.common.blockentity;

import cn.leomc.mobfarmutilities.MobFarmUtilities;
import cn.leomc.mobfarmutilities.common.api.*;
import cn.leomc.mobfarmutilities.common.block.ActivatableBlock;
import cn.leomc.mobfarmutilities.common.compat.IInfoProvider;
import cn.leomc.mobfarmutilities.common.menu.SlaughtererMenu;
import cn.leomc.mobfarmutilities.common.registry.BlockEntityRegistry;
import cn.leomc.mobfarmutilities.common.registry.ItemRegistry;
import cn.leomc.mobfarmutilities.common.utils.PlatformCompatibility;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


public class SlaughtererBlockEntity extends BaseBlockEntity implements MenuProvider, Upgradable, IInfoProvider, IHasArea {


    protected final UpgradeHandler upgradeHandler = new UpgradeHandler(this, UpgradeType.SLAUGHTERER_AREA, UpgradeType.SLAUGHTERER_SHARPNESS, UpgradeType.SLAUGHTERER_LOOTING, UpgradeType.SLAUGHTERER_FIRE_ASPECT);
    protected int coolDown;
    protected boolean renderArea = false;

    public SlaughtererBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.SLAUGHTERER.get(), pos, state);
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
    public void serverTick(ServerLevel level, BlockPos pos, BlockState state) {
        super.serverTick(level, pos, state);
        if (coolDown > 0) {
            coolDown--;
            return;
        }
        coolDown = 20;

        ServerPlayer player = PlatformCompatibility.getFakePlayer(level, "Slaughterer");
        if (player.isDeadOrDying())
            player.respawn();
        player.setPos(worldPosition.getX(), 4096, worldPosition.getZ());

        RedstoneMode.updateRedstone(state, level, worldPosition);
        if (!state.getValue(ActivatableBlock.ACTIVE))
            return;

        ItemStack itemStack = new ItemStack(ItemRegistry.FAKE_SWORD.get());
        itemStack.getOrCreateTag().putBoolean("Unbreakable", true);
        if (upgradeHandler.getUpgradeLevel(UpgradeType.SLAUGHTERER_LOOTING) > 0)
            itemStack.enchant(Enchantments.MOB_LOOTING, upgradeHandler.getUpgradeLevel(UpgradeType.SLAUGHTERER_LOOTING));
        if (upgradeHandler.getUpgradeLevel(UpgradeType.SLAUGHTERER_FIRE_ASPECT) > 0)
            itemStack.enchant(Enchantments.FIRE_ASPECT, upgradeHandler.getUpgradeLevel(UpgradeType.SLAUGHTERER_FIRE_ASPECT));
        player.setItemInHand(InteractionHand.MAIN_HAND, itemStack);
        level.getEntitiesOfClass(LivingEntity.class, getAABB()).forEach(entity -> {
            float damage = 1.0F;
            damage += upgradeHandler.getUpgradeLevel(UpgradeType.SLAUGHTERER_SHARPNESS);
            damage -= Math.sqrt(entity.distanceToSqr(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ()));
            if (damage <= 0)
                damage = 0.5F;
            attack(player, entity, damage);
        });
        player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
    }

    private void attack(Player attacker, LivingEntity attacked, float damage) {
        if (attacked.isAttackable() && !attacked.skipAttackInteraction(attacker)) {
            float f = damage;
            float f1 = EnchantmentHelper.getDamageBonus(attacker.getMainHandItem(), attacked.getMobType());

            if (f > 0.0F || f1 > 0.0F) {
                boolean flag1 = false;
                int i = 0;
                i += EnchantmentHelper.getKnockbackBonus(attacker);
                if (attacker.isSprinting()) {
                    attacker.level.playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(), SoundEvents.PLAYER_ATTACK_KNOCKBACK, attacker.getSoundSource(), 1.0F, 1.0F);
                    ++i;
                    flag1 = true;
                }

                boolean flag2 = attacker.fallDistance > 0.0F && !attacker.isOnGround() && !attacker.onClimbable() && !attacker.isInWater() && !attacker.hasEffect(MobEffects.BLINDNESS) && !attacker.isPassenger() && attacked instanceof LivingEntity;
                flag2 = flag2 && !attacker.isSprinting();
                if (flag2) {
                    f *= 1.5F;
                }

                f += f1;
                boolean flag3 = false;
                double d0 = attacker.walkDist - attacker.walkDistO;
                if (!flag2 && !flag1 && attacker.isOnGround() && d0 < (double) attacker.getSpeed()) {
                    ItemStack itemstack = attacker.getItemInHand(InteractionHand.MAIN_HAND);
                    if (itemstack.getItem() instanceof SwordItem) {
                        flag3 = true;
                    }
                }

                float f4;
                boolean flag4 = false;
                int j = EnchantmentHelper.getFireAspect(attacker);
                f4 = attacked.getHealth();
                if (j > 0 && !attacked.isOnFire()) {
                    flag4 = true;
                    attacked.setSecondsOnFire(1);
                }

                Vec3 vec3 = attacked.getDeltaMovement();
                boolean flag5 = attacked.hurt(new SlaughtererDamageSource(attacker), f);
                if (flag5) {
                    if (i > 0) {
                        attacked.knockback((float) i * 0.5F, Mth.sin(attacker.getYRot() * ((float) Math.PI / 180F)), -Mth.cos(attacker.getYRot() * ((float) Math.PI / 180F)));

                        attacker.setDeltaMovement(attacker.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
                        attacker.setSprinting(false);
                    }

                    if (flag3) {
                        float f3 = 1.0F + EnchantmentHelper.getSweepingDamageRatio(attacker) * f;

                        for (LivingEntity livingentity : attacker.level.getEntitiesOfClass(LivingEntity.class, attacked.getBoundingBox().inflate(1.0D, 0.25D, 1.0D))) {
                            if (livingentity != attacker && livingentity != attacked && !attacker.isAlliedTo(livingentity) && (!(livingentity instanceof ArmorStand) || !((ArmorStand) livingentity).isMarker()) && attacker.distanceToSqr(livingentity) < 9.0D) {
                                livingentity.knockback(0.4F, Mth.sin(attacker.getYRot() * ((float) Math.PI / 180F)), -Mth.cos(attacker.getYRot() * ((float) Math.PI / 180F)));
                                livingentity.hurt(DamageSource.playerAttack(attacker), f3);
                            }
                        }

                        attacker.level.playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, attacker.getSoundSource(), 1.0F, 1.0F);
                        attacker.sweepAttack();
                    }

                    if (attacked instanceof ServerPlayer && attacked.hurtMarked) {
                        ((ServerPlayer) attacked).connection.send(new ClientboundSetEntityMotionPacket(attacked));
                        attacked.hurtMarked = false;
                        attacked.setDeltaMovement(vec3);
                    }

                    if (flag2) {
                        attacker.level.playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(), SoundEvents.PLAYER_ATTACK_CRIT, attacker.getSoundSource(), 1.0F, 1.0F);
                        attacker.crit(attacked);
                    }

                    if (!flag2 && !flag3) {
                        attacker.level.playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(), SoundEvents.PLAYER_ATTACK_STRONG, attacker.getSoundSource(), 1.0F, 1.0F);
                    }

                    if (f1 > 0.0F) {
                        attacker.magicCrit(attacked);
                    }

                    attacker.setLastHurtMob(attacked);
                    EnchantmentHelper.doPostHurtEffects(attacked, attacker);

                    EnchantmentHelper.doPostDamageEffects(attacker, attacked);
                    ItemStack itemstack1 = attacker.getMainHandItem();

                    if (!attacker.level.isClientSide && !itemstack1.isEmpty()) {
                        itemstack1.hurtEnemy(attacked, attacker);
                        if (itemstack1.isEmpty()) {
                            attacker.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                        }
                    }

                    float f5 = f4 - attacked.getHealth();
                    attacker.awardStat(Stats.DAMAGE_DEALT, Math.round(f5 * 10.0F));
                    if (j > 0) {
                        attacked.setSecondsOnFire(j * 4);
                    }

                    if (attacker.level instanceof ServerLevel && f5 > 2.0F) {
                        int k = (int) ((double) f5 * 0.5D);
                        ((ServerLevel) attacker.level).sendParticles(ParticleTypes.DAMAGE_INDICATOR, attacked.getX(), attacked.getY(0.5D), attacked.getZ(), k, 0.1D, 0.0D, 0.1D, 0.2D);
                    }

                    attacker.causeFoodExhaustion(0.1F);
                } else {
                    attacker.level.playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(), SoundEvents.PLAYER_ATTACK_NODAMAGE, attacker.getSoundSource(), 1.0F, 1.0F);
                    if (flag4) {
                        attacked.clearFire();
                    }
                }
            }

        }
    }

    @Override
    public void saveData(CompoundTag tag) {
        tag.put("upgrade", upgradeHandler.write(new CompoundTag()));
    }

    @Override
    public void loadData(CompoundTag tag) {
        upgradeHandler.read(tag.getCompound("upgrade"));
    }

    @Override
    public void loadClientData(CompoundTag tag) {
        upgradeHandler.read(tag.getCompound("upgrade"));
    }

    @Override
    public void saveClientData(CompoundTag tag) {
        tag.put("upgrade", upgradeHandler.write(new CompoundTag(), false));
    }

    public UpgradeHandler getUpgradeHandler() {
        return upgradeHandler;
    }

    public void remove() {
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
