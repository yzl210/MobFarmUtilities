package cn.leomc.mobfarmutilities.common.block;

import cn.leomc.mobfarmutilities.common.api.InventoryWrapper;
import cn.leomc.mobfarmutilities.common.tileentity.ItemCollectorTileEntity;
import me.shedaniel.architectury.platform.Platform;
import me.shedaniel.architectury.registry.BlockProperties;
import me.shedaniel.architectury.registry.MenuRegistry;
import me.shedaniel.architectury.registry.ToolType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ISidedInventoryProvider;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;

public class ItemCollectorBlock extends ActivatableBlock implements ITileEntityProvider, ISidedInventoryProvider {

    public ItemCollectorBlock() {
        super(BlockProperties.of(Material.IRON)
                .tool(ToolType.PICKAXE, 1)
                .hardnessAndResistance(1.5F, 6.0F)
                .setRequiresTool()
        );
    }

    public static ItemCollectorTileEntity getTileEntity() {
        if (Platform.isForge()) {
            Class<ItemCollectorTileEntity> tileEntityClass;
            try {
                tileEntityClass = (Class<ItemCollectorTileEntity>) Class.forName("cn.leomc.mobfarmutilities.forge.ForgeItemCollectorTileEntity");
                return tileEntityClass.getConstructor().newInstance();
            } catch (ClassNotFoundException | ClassCastException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return new ItemCollectorTileEntity();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (worldIn.isRemote || handIn != Hand.MAIN_HAND)
            return ActionResultType.CONSUME;

        TileEntity tileEntity = worldIn.getTileEntity(pos);
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
        if (tileEntity instanceof ItemCollectorTileEntity) {
            ((ItemCollectorTileEntity) tileEntity).dropAllItems();
        }
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public ISidedInventory createInventory(BlockState state, IWorld world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof ItemCollectorTileEntity) {
            Inventory inventory = ((ItemCollectorTileEntity) tileEntity).getInventory();
            return new InventoryWrapper(inventory);
        }
        return null;
    }

    @Override
    public @Nullable TileEntity createNewTileEntity(IBlockReader worldIn) {
        return getTileEntity();
    }

}
