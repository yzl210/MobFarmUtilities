package cn.leomc.mobfarmutilities.common.item;

import cn.leomc.mobfarmutilities.MobFarmUtilities;
import cn.leomc.mobfarmutilities.common.registry.ModRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LaserEmitterItem extends Item {
    public LaserEmitterItem() {
        super(new Properties()
                .tab(ModRegistry.ITEM_GROUP)
                .stacksTo(1)
                .defaultDurability(9)
        );
    }


    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> components, TooltipFlag isAdvanced) {
        components.add(new TranslatableComponent("text." + MobFarmUtilities.MODID + ".laser_emitter_refuel_tip", Items.REDSTONE_BLOCK.getDescription()).withStyle(ChatFormatting.GRAY));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if(level.isClientSide || stack.getDamageValue() >= stack.getMaxDamage())
            return InteractionResultHolder.pass(stack);

        player.getCooldowns().addCooldown(this, 5);

        if (!player.getAbilities().instabuild) {
            if (stack.hurt(1, player.getRandom(), (ServerPlayer) player)) {
                player.broadcastBreakEvent(hand);
                player.awardStat(Stats.ITEM_BROKEN.get(stack.getItem()));
            }
        }

        EntityHitResult result = emit(player);
        if(result != null && result.getEntity() != null)
            result.getEntity().hurt(new IndirectEntityDamageSource(MobFarmUtilities.MODID + "_laser", result.getEntity(), player), 6);

        return InteractionResultHolder.success(stack);
    }

    public static EntityHitResult emit(Player player) {
        Vec3 viewVec = player.getViewVector(1);
        Vec3 start = player.getEyePosition();
        Vec3 end = start.add(viewVec.x() * 15, viewVec.y() * 15, viewVec.z() * 15);
        return ProjectileUtil.getEntityHitResult(player, start, end, new AABB(start, end), EntitySelector.ENTITY_STILL_ALIVE, 0f);
    }

}
