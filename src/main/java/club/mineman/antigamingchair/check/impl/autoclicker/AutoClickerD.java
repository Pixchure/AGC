package club.mineman.antigamingchair.check.impl.autoclicker;

import club.mineman.antigamingchair.AntiGamingChair;
import club.mineman.antigamingchair.check.checks.PacketCheck;
import club.mineman.antigamingchair.data.PlayerData;
import club.mineman.antigamingchair.event.player.PlayerAlertEvent;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInArmAnimation;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.entity.Player;

public class AutoClickerE extends PacketCheck {
   private boolean failed;
   private boolean sent;

   public AutoClickerE(AntiGamingChair plugin, PlayerData playerData) {
      super(plugin, playerData);
   }

   public void handleCheck(Player player, Packet packet) {
      if (packet instanceof PacketPlayInArmAnimation && System.currentTimeMillis() - super.playerData.getLastDelayedMovePacket() > 110L && System.currentTimeMillis() - super.playerData.getLastMovePacket().getTimestamp() < 110L && !super.playerData.isDigging() && !super.playerData.isPlacing()) {
         if (super.sent) {
            if (!super.failed) {
               int vl = (int)super.playerData.getCheckVl(this);
               ++vl;
               if (vl >= 5) {
                  vl = 0;
                  this.alert(PlayerAlertEvent.AlertType.EXPERIMENTAL, player, "failed Auto Clicker Check D (Experimental).");
               }

               this.playerData.setCheckVl((double)vl, this);
               super.failed = true;
            }
         } else {
            super.sent = true;
         }
      } else if (packet instanceof PacketPlayInFlying) {
         super.sent = super.failed = false;
      }

   }
}
