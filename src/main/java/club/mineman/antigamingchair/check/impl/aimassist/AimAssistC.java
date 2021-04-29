package club.mineman.antigamingchair.check.impl.aimassist;

import club.mineman.antigamingchair.AntiGamingChair;
import club.mineman.antigamingchair.check.checks.RotationCheck;
import club.mineman.antigamingchair.data.PlayerData;
import club.mineman.antigamingchair.event.player.PlayerAlertEvent;
import club.mineman.antigamingchair.util.MathUtil;
import club.mineman.paper.event.PlayerUpdateRotationEvent;
import org.bukkit.entity.Player;

public class AimAssistC extends RotationCheck {
   public AimAssistC(AntiGamingChair plugin, PlayerData playerData) {
      super(plugin, playerData);
   }

   public void handleCheck(Player player, PlayerUpdateRotationEvent event) {
      if (System.currentTimeMillis() - this.playerData.getLastAttackPacket() < 10000L) {
         double diff = MathUtil.getDistanceBetweenAngles(event.getTo().getYaw(), event.getFrom().getYaw());
         double vl = this.playerData.getCheckVl(this);
         if (event.getFrom().getPitch() == event.getTo().getPitch() && diff >= 3.0D && event.getFrom().getPitch() != 90.0F && event.getTo().getPitch() != 90.0F) {
            if ((vl += 0.9D) >= 5.0D) {
               this.alert(PlayerAlertEvent.AlertType.EXPERIMENTAL, player, String.format("failed Aim Assist Check C (Experimental). YD %.1f. VL %.1f.", diff, vl));
            }
         } else {
            --vl;
         }

         this.playerData.setCheckVl(vl, this);
      }
   }
}
