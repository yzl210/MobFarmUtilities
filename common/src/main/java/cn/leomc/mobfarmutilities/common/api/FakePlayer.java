package cn.leomc.mobfarmutilities.common.api;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.protocol.game.ServerboundClientInformationPacket;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public class FakePlayer extends ServerPlayer {
    public FakePlayer(ServerLevel level, GameProfile gameProfile, Vec3 pos) {
        super(level.getServer(), level, gameProfile, new ServerPlayerGameMode(level));
        setPos(pos.x, pos.y, pos.z);
        gameMode.setGameModeForPlayer(GameType.CREATIVE);
    }


    @Override
    public void displayClientMessage(Component chatComponent, boolean actionBar) {
    }

    @Override
    public void sendMessage(Component component, ChatType chatType, UUID uUID) {
    }

    @Override
    public void awardStat(Stat par1StatBase, int par2) {
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return true;
    }


    @Override
    public boolean canHarmPlayer(Player player) {
        return false;
    }

    @Override
    public void die(DamageSource source) {
    }

    @Override
    public void tick() {
        this.attackStrengthTicker = Integer.MAX_VALUE;
    }

    @Override
    public void attack(Entity entity) {
        if (entity.isAttackable()) {
            if (!entity.skipAttackInteraction(this)) {
                float f = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
                float h;
                if (entity instanceof LivingEntity) {
                    h = EnchantmentHelper.getDamageBonus(this.getMainHandItem(), ((LivingEntity) entity).getMobType());
                } else {
                    h = EnchantmentHelper.getDamageBonus(this.getMainHandItem(), MobType.UNDEFINED);
                }

                if (f > 0.0F || h > 0.0F) {
                    int j = EnchantmentHelper.getKnockbackBonus(this);
                    f += h;
                    if (getMainHandItem().getItem() instanceof SwordItem)
                        f += ((SwordItem) getMainHandItem().getItem()).getDamage();
                    if (getMainHandItem().getItem() instanceof DiggerItem)
                        f += ((DiggerItem) getMainHandItem().getItem()).getAttackDamage();


                    float k = 0.0F;
                    boolean bl5 = false;
                    int l = EnchantmentHelper.getFireAspect(this);
                    if (entity instanceof LivingEntity) {
                        k = ((LivingEntity) entity).getHealth();
                        if (l > 0 && !entity.isOnFire()) {
                            bl5 = true;
                            entity.setSecondsOnFire(1);
                        }
                    }

                    Vec3 vec3 = entity.getDeltaMovement();
                    boolean bl6 = entity.hurt(new SlaughtererDamageSource(this), f);
                    if (bl6) {
                        if (j > 0) {
                            if (entity instanceof LivingEntity) {
                                ((LivingEntity) entity).knockback((float) j * 0.5F, Mth.sin(this.yRot * 0.017453292F), -Mth.cos(this.yRot * 0.017453292F));
                            } else {
                                entity.push(-Mth.sin(this.yRot * 0.017453292F) * (float) j * 0.5F, 0.1D, Mth.cos(this.yRot * 0.017453292F) * (float) j * 0.5F);
                            }

                        }

                        if (entity instanceof ServerPlayer && entity.hurtMarked) {
                            ((ServerPlayer) entity).connection.send(new ClientboundSetEntityMotionPacket(entity));
                            entity.hurtMarked = false;
                            entity.setDeltaMovement(vec3);
                        }

                        if (h > 0.0F) {
                            this.magicCrit(entity);
                        }

                        this.setLastHurtMob(entity);
                        if (entity instanceof LivingEntity) {
                            EnchantmentHelper.doPostHurtEffects((LivingEntity) entity, this);
                        }

                        EnchantmentHelper.doPostDamageEffects(this, entity);
                        ItemStack itemStack = this.getMainHandItem();
                        Entity entity2 = entity;
                        if (entity instanceof EnderDragonPart)
                            entity2 = ((EnderDragonPart) entity).parentMob;


                        if (!this.level.isClientSide && !itemStack.isEmpty() && entity2 instanceof LivingEntity) {
                            itemStack.hurtEnemy((LivingEntity) entity2, this);
                            if (itemStack.isEmpty()) {
                                this.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                            }
                        }

                        if (entity instanceof LivingEntity) {
                            float n = k - ((LivingEntity) entity).getHealth();
                            this.awardStat(Stats.DAMAGE_DEALT, Math.round(n * 10.0F));
                            if (l > 0) {
                                entity.setSecondsOnFire(l * 4);
                            }

                            if (this.level instanceof ServerLevel && n > 2.0F) {
                                int o = (int) ((double) n * 0.5D);
                                ((ServerLevel) this.level).sendParticles(ParticleTypes.DAMAGE_INDICATOR, entity.getX(), entity.getY(0.5D), entity.getZ(), o, 0.1D, 0.0D, 0.1D, 0.2D);
                            }
                        }
                    } else {
                        if (bl5) {
                            entity.clearFire();
                        }
                    }
                }

            }
        }
    }

    @Override
    public void updateOptions(ServerboundClientInformationPacket pkt) {
    }

    @Override
    public PlayerAdvancements getAdvancements() {
        return super.getAdvancements();
    }

    @Override
    public void setItemSlot(EquipmentSlot equipmentSlot, ItemStack itemStack) {
        if (equipmentSlot == EquipmentSlot.MAINHAND)
            this.inventory.items.set(this.inventory.selected, itemStack);
        else if (equipmentSlot == EquipmentSlot.OFFHAND)
            this.inventory.offhand.set(0, itemStack);
        else if (equipmentSlot.getType() == EquipmentSlot.Type.ARMOR)
            this.inventory.armor.set(equipmentSlot.getIndex(), itemStack);
    }

    @Override
    public Component getDisplayName() {
        return new TextComponent("");
    }
}
