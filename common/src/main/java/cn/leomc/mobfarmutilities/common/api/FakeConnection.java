package cn.leomc.mobfarmutilities.common.api;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class FakeConnection extends ServerGamePacketListenerImpl {

    private static final Connection DUMMY = new Connection(PacketFlow.CLIENTBOUND);

    public FakeConnection(MinecraftServer minecraftServer, ServerPlayer serverPlayer) {
        super(minecraftServer, DUMMY, serverPlayer);
    }

    @Override
    public void tick() {
    }

    @Override
    public void send(Packet<?> packet) {
    }

    @Override
    public void send(Packet<?> packet, @Nullable GenericFutureListener<? extends Future<? super Void>> genericFutureListener) {
    }

    @Override
    public void disconnect(Component component) {
    }

    @Override
    public void handlePlayerInput(ServerboundPlayerInputPacket serverboundPlayerInputPacket) {
    }

    @Override
    public void handleMoveVehicle(ServerboundMoveVehiclePacket serverboundMoveVehiclePacket) {
    }

    @Override
    public void handleAcceptTeleportPacket(ServerboundAcceptTeleportationPacket serverboundAcceptTeleportationPacket) {
    }

    @Override
    public void handleRecipeBookSeenRecipePacket(ServerboundRecipeBookSeenRecipePacket serverboundRecipeBookSeenRecipePacket) {
    }

    @Override
    public void handleRecipeBookChangeSettingsPacket(ServerboundRecipeBookChangeSettingsPacket serverboundRecipeBookChangeSettingsPacket) {
    }

    @Override
    public void handleSeenAdvancements(ServerboundSeenAdvancementsPacket serverboundSeenAdvancementsPacket) {
    }

    @Override
    public void handleCustomCommandSuggestions(ServerboundCommandSuggestionPacket serverboundCommandSuggestionPacket) {
    }

    @Override
    public void handleSetCommandBlock(ServerboundSetCommandBlockPacket serverboundSetCommandBlockPacket) {
    }

    @Override
    public void handleSetCommandMinecart(ServerboundSetCommandMinecartPacket serverboundSetCommandMinecartPacket) {
    }

    @Override
    public void handlePickItem(ServerboundPickItemPacket serverboundPickItemPacket) {
    }

    @Override
    public void handleRenameItem(ServerboundRenameItemPacket serverboundRenameItemPacket) {
    }

    @Override
    public void handleSetBeaconPacket(ServerboundSetBeaconPacket serverboundSetBeaconPacket) {
    }

    @Override
    public void handleSetStructureBlock(ServerboundSetStructureBlockPacket serverboundSetStructureBlockPacket) {
    }

    @Override
    public void handleSetJigsawBlock(ServerboundSetJigsawBlockPacket serverboundSetJigsawBlockPacket) {
    }

    @Override
    public void handleJigsawGenerate(ServerboundJigsawGeneratePacket serverboundJigsawGeneratePacket) {
    }

    @Override
    public void handleSelectTrade(ServerboundSelectTradePacket serverboundSelectTradePacket) {
    }

    @Override
    public void handleEditBook(ServerboundEditBookPacket serverboundEditBookPacket) {
    }

    @Override
    public void handleEntityTagQuery(ServerboundEntityTagQuery serverboundEntityTagQuery) {
    }

    @Override
    public void handleBlockEntityTagQuery(ServerboundBlockEntityTagQuery serverboundBlockEntityTagQuery) {
    }

    @Override
    public void handleMovePlayer(ServerboundMovePlayerPacket serverboundMovePlayerPacket) {
    }

    @Override
    public void teleport(double d, double e, double f, float g, float h) {
    }

    @Override
    public void teleport(double d, double e, double f, float g, float h, Set<ClientboundPlayerPositionPacket.RelativeArgument> set) {
    }

    @Override
    public void handlePlayerAction(ServerboundPlayerActionPacket serverboundPlayerActionPacket) {
    }

    @Override
    public void handleUseItemOn(ServerboundUseItemOnPacket serverboundUseItemOnPacket) {
    }

    @Override
    public void handleUseItem(ServerboundUseItemPacket serverboundUseItemPacket) {
    }

    @Override
    public void handleTeleportToEntityPacket(ServerboundTeleportToEntityPacket serverboundTeleportToEntityPacket) {
    }

    @Override
    public void handleResourcePackResponse(ServerboundResourcePackPacket serverboundResourcePackPacket) {
    }

    @Override
    public void handlePaddleBoat(ServerboundPaddleBoatPacket serverboundPaddleBoatPacket) {
    }

    @Override
    public void onDisconnect(Component component) {
    }

    @Override
    public void handleSetCarriedItem(ServerboundSetCarriedItemPacket serverboundSetCarriedItemPacket) {
    }

    @Override
    public void handleChat(ServerboundChatPacket serverboundChatPacket) {
    }

    @Override
    public void handleAnimate(ServerboundSwingPacket serverboundSwingPacket) {
    }

    @Override
    public void handlePlayerCommand(ServerboundPlayerCommandPacket serverboundPlayerCommandPacket) {
    }

    @Override
    public void handleInteract(ServerboundInteractPacket serverboundInteractPacket) {
    }

    @Override
    public void handleClientCommand(ServerboundClientCommandPacket serverboundClientCommandPacket) {
    }

    @Override
    public void handleContainerClose(ServerboundContainerClosePacket serverboundContainerClosePacket) {
    }

    @Override
    public void handleContainerClick(ServerboundContainerClickPacket serverboundContainerClickPacket) {
    }

    @Override
    public void handlePlaceRecipe(ServerboundPlaceRecipePacket serverboundPlaceRecipePacket) {
    }

    @Override
    public void handleContainerButtonClick(ServerboundContainerButtonClickPacket serverboundContainerButtonClickPacket) {
    }

    @Override
    public void handleSetCreativeModeSlot(ServerboundSetCreativeModeSlotPacket serverboundSetCreativeModeSlotPacket) {
    }

    @Override
    public void handleContainerAck(ServerboundContainerAckPacket serverboundContainerAckPacket) {
    }

    @Override
    public void handleSignUpdate(ServerboundSignUpdatePacket serverboundSignUpdatePacket) {
    }

    @Override
    public void handleKeepAlive(ServerboundKeepAlivePacket serverboundKeepAlivePacket) {
    }

    @Override
    public void handlePlayerAbilities(ServerboundPlayerAbilitiesPacket serverboundPlayerAbilitiesPacket) {
    }

    @Override
    public void handleClientInformation(ServerboundClientInformationPacket serverboundClientInformationPacket) {
    }

    @Override
    public void handleCustomPayload(ServerboundCustomPayloadPacket serverboundCustomPayloadPacket) {
    }

    @Override
    public void handleChangeDifficulty(ServerboundChangeDifficultyPacket serverboundChangeDifficultyPacket) {
    }

    @Override
    public void handleLockDifficulty(ServerboundLockDifficultyPacket serverboundLockDifficultyPacket) {
    }
}
