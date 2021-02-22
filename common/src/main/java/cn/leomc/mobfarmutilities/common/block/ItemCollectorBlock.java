package cn.leomc.mobfarmutilities.common.block;

import cn.leomc.mobfarmutilities.common.api.InventoryWrapper;
import cn.leomc.mobfarmutilities.common.api.blockstate.IFluidLoggable;
import cn.leomc.mobfarmutilities.common.tileentity.ItemCollectorTileEntity;
import me.shedaniel.architectury.platform.Platform;
import me.shedaniel.architectury.registry.MenuRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ISidedInventoryProvider;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
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

public class ItemCollectorBlock extends ActivatableBlock implements ITileEntityProvider, IFluidLoggable, ISidedInventoryProvider {

    public ItemCollectorBlock() {
        super(Properties.create(Material.IRON)
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
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder.add(WATERLOGGED));
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        FluidState fluidState = context.getWorld().getFluidState(context.getPos());
        return super.getStateForPlacement(context).with(WATERLOGGED, fluidState.getFluid() == getSupportedFluid());
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return getFluid(state);
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
