package club.mineman.antigamingchair.check.impl.phase;

import club.mineman.antigamingchair.AntiGamingChair;
import club.mineman.antigamingchair.check.checks.PacketCheck;
import club.mineman.antigamingchair.data.PlayerData;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInEntityAction;
import net.minecraft.server.v1_8_R3.PacketPlayInEntityAction.EnumPlayerAction;
import org.bukkit.entity.Player;

public class PhaseB extends PacketCheck {
   private int stage;

   public PhaseB(AntiGamingChair plugin, PlayerData playerData) {
      super(plugin, playerData);
   }

   public void handleCheck(Player player, Packet packet) {
      String className = packet.getClass().getSimpleName();
      byte var5 = -1;
      switch(className.hashCode()) {
      case -1411520464:
         if (className.equals("PacketPlayInFlying")) {
            var5 = 0;
         }
         break;
      case -817313142:
         if (className.equals("PacketPlayInPosition")) {
            var5 = 2;
         }
         break;
      case 464466138:
         if (className.equals("PacketPlayInEntityAction")) {
            var5 = 1;
         }
      }

      switch(var5) {
      case 0:
         if (this.stage == 0) {
            ++this.stage;
         } else {
            this.stage = 0;
         }
         break;
      case 1:
         if (((PacketPlayInEntityAction)packet).b() == EnumPlayerAction.START_SNEAKING) {
            if (this.stage == 1) {
               ++this.stage;
            } else {
               this.stage = 0;
            }
         } else if (((PacketPlayInEntityAction)packet).b() == EnumPlayerAction.START_SNEAKING && this.stage >= 3) {
            this.plugin.getAlertsManager().forceAlert("Might be phasing: " + this.stage, player);
         }
         break;
      case 2:
         if (this.stage >= 2) {
            ++this.stage;
         }
      }

   }
}
