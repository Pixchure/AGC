package club.mineman.antigamingchair.check.impl.aimassist;

import club.mineman.antigamingchair.AntiGamingChair;
import club.mineman.antigamingchair.check.checks.RotationCheck;
import club.mineman.antigamingchair.data.PlayerData;
import club.mineman.antigamingchair.event.player.PlayerAlertEvent;
import club.mineman.paper.event.PlayerUpdateRotationEvent;
import org.bukkit.entity.Player;

public class AimAssistB extends RotationCheck {
   private float suspiciousYaw;

   public AimAssistB(AntiGamingChair plugin, PlayerData playerData) {
      super(plugin, playerData);
   }

   public void handleCheck(Player player, PlayerUpdateRotationEvent event) {
      if (System.currentTimeMillis() - this.playerData.getLastAttackPacket() < 10000L) {
         float diff = Math.abs(event.getTo().getYaw() - event.getFrom().getYaw()) % 360.0F;
         if (diff > 1.0F && (float)Math.round(diff * 10.0F) * 0.1F == diff && (float)Math.round(diff) != diff) {
            if (diff == this.suspiciousYaw) {
               this.alert(PlayerAlertEvent.AlertType.EXPERIMENTAL, player, "failed Aim Assist Check B (Experimental). Y " + diff + ".");
            }

            this.suspiciousYaw = (float)Math.round(diff * 10.0F) * 0.1F;
         } else {
            this.suspiciousYaw = 0.0F;
         }

      }
   }
}
