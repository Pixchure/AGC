package club.mineman.antigamingchair.check.impl.inventory;

import club.mineman.antigamingchair.AntiGamingChair;
import club.mineman.antigamingchair.check.checks.PacketCheck;
import club.mineman.antigamingchair.data.PlayerData;
import club.mineman.antigamingchair.event.player.PlayerAlertEvent;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInArmAnimation;
import net.minecraft.server.v1_8_R3.PacketPlayInEntityAction;
import net.minecraft.server.v1_8_R3.PacketPlayInEntityAction.EnumPlayerAction;
import org.bukkit.entity.Player;

public class InventoryB extends PacketCheck {
   public InventoryB(AntiGamingChair plugin, PlayerData playerData) {
      super(plugin, playerData);
   }

   public void handleCheck(Player player, Packet packet) {
      if (this.playerData.isInventoryOpen() && (packet instanceof PacketPlayInEntityAction && ((PacketPlayInEntityAction)packet).b() == EnumPlayerAction.START_SPRINTING || packet instanceof PacketPlayInArmAnimation)) {
         this.alert(PlayerAlertEvent.AlertType.EXPERIMENTAL, player, "failed Inventory Check B (Experimental).");
      }

   }
}
