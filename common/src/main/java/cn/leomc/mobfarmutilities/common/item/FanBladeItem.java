package cn.leomc.mobfarmutilities.common.item;

import cn.leomc.mobfarmutilities.common.registry.ModRegistry;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

public class FanBladeItem extends Item {
    public FanBladeItem() {
        super(new Properties()
                .tab(ModRegistry.ITEM_GROUP)
                .stacksTo(1)
                .defaultDurability(27)
        );
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if(level.isClientSide || stack.getDamageValue() >= stack.getMaxDamage())
            return InteractionResultHolder.pass(stack);

        player.getCooldowns().addCooldown(this, 5);
        stack.hurtAndBreak(1, player, entity -> entity.broadcastBreakEvent(hand));

        EntityHitResult result = LaserEmitterItem.emit(player);
        if(result != null && result.getEntity() != null){
            Entity entity = result.getEntity();
            entity.setDeltaMovement(entity.getDeltaMovement().add(player.getViewVector(1).multiply(2, 2, 2)));
            if(!(entity instanceof LivingEntity) || entity instanceof Player){
                ((ServerPlayer)player).connection.send(new ClientboundSetEntityMotionPacket(entity));
            }
        }

        return InteractionResultHolder.success(stack);
    }
}