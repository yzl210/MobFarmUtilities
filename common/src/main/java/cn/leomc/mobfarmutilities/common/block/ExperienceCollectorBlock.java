package cn.leomc.mobfarmutilities.common.block;

import cn.leomc.mobfarmutilities.common.registry.ItemRegistry;
import cn.leomc.mobfarmutilities.common.tileentity.ExperienceCollectorTileEntity;
import me.shedaniel.architectury.platform.Platform;
import me.shedaniel.architectury.registry.BlockProperties;
import me.shedaniel.architectury.registry.MenuRegistry;
import me.shedaniel.architectury.registry.ToolType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;

public class ExperienceCollectorBlock extends ActivatableBlock implements ITileEntityProvider {

    public ExperienceCollectorBlock() {
        super(BlockProperties.of(Material.IRON)
                .tool(ToolType.PICKAXE, 1)
                .hardnessAndResistance(1.5F, 6.0F)
                .setRequiresTool()
        );
    }

    public static ExperienceCollectorTileEntity getTileEntity() {
        if (Platform.isForge()) {
            Class<ExperienceCollectorTileEntity> tileEntityClass;
            try {
                tileEntityClass = (Class<ExperienceCollectorTileEntity>) Class.forName("cn.leomc.mobfarmutilities.forge.ForgeExperienceCollectorTileEntity");
                return tileEntityClass.getConstructor().newInstance();
            } catch (ClassNotFoundException | ClassCastException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return new ExperienceCollectorTileEntity();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (worldIn.isRemote || handIn != Hand.MAIN_HAND)
            return ActionResultType.CONSUME;

        TileEntity tileEntity = worldIn.getTileEntity(pos);

        if (player.getHeldItem(Hand.MAIN_HAND).getItem() == Items.BUCKET && tileEntity instanceof ExperienceCollectorTileEntity && !player.isSneaking())
            if (((ExperienceCollectorTileEntity) tileEntity).getAmount() >= 1000) {
                if (player.isCreative())
                    player.addItemStackToInventory(new ItemStack(ItemRegistry.LIQUID_EXPERIENCE_BUCKET.get()));
                else
                    player.setHeldItem(Hand.MAIN_HAND, new ItemStack(ItemRegistry.LIQUID_EXPERIENCE_BUCKET.get()));
                ((ExperienceCollectorTileEntity) tileEntity).addAmount(-1000);
                worldIn.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.PLAYERS, 1.0F, 1.0F);
                return ActionResultType.SUCCESS;
            }

        if (player.getHeldItem(Hand.MAIN_HAND).getItem() == ItemRegistry.LIQUID_EXPERIENCE_BUCKET.get() && tileEntity instanceof ExperienceCollectorTileEntity && !player.isSneaking())
            if (((ExperienceCollectorTileEntity) tileEntity).getAmount() < ((ExperienceCollectorTileEntity) tileEntity).getLimit() - 1000) {
                if (!player.isCreative())
                    player.setHeldItem(Hand.MAIN_HAND, new ItemStack(Items.BUCKET));
                ((ExperienceCollectorTileEntity) tileEntity).addAmount(1000);
                worldIn.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.PLAYERS, 1.0F, 1.0F);
                return ActionResultType.SUCCESS;
            }

        if (tileEntity instanceof INamedContainerProvider) {
            if (player instanceof ServerPlayerEntity)
                MenuRegistry.openExtendedMenu((ServerPlayerEntity) player, (INamedContainerProvider) tileEntity, packetBuffer -> packetBuffer.writeBlockPos(tileEntity.getPos()));
            return ActionResultType.SUCCESS;
        } else
            throw new IllegalStateException("Named Container not found!");
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() == newState.getBlock())
            return;
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof ExperienceCollectorTileEntity) {
            ((ExperienceCollectorTileEntity) tileEntity).dropAllExperience();
        }
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }


    @Override
    public @Nullable TileEntity createNewTileEntity(IBlockReader worldIn) {
        return getTileEntity();
    }

}
