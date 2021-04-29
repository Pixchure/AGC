package club.mineman.antigamingchair.check.impl.velocity;

import club.mineman.antigamingchair.AntiGamingChair;
import club.mineman.antigamingchair.check.checks.PositionCheck;
import club.mineman.antigamingchair.data.PlayerData;
import club.mineman.antigamingchair.event.player.PlayerAlertEvent;
import club.mineman.paper.event.PlayerUpdatePositionEvent;
import org.bukkit.entity.Player;

public class VelocityB extends PositionCheck {
   public VelocityB(AntiGamingChair plugin, PlayerData playerData) {
      super(plugin, playerData);
   }

   public void handleCheck(Player player, PlayerUpdatePositionEvent event) {
      double offsetY = event.getTo().getY() - event.getFrom().getY();
      if (this.playerData.getVelocityY() > 0.0D && this.playerData.isOnGround() && event.getFrom().getY() % 1.0D == 0.0D && !this.playerData.isUnderBlock() && !this.playerData.isInLiquid() && offsetY > 0.0D && offsetY < 0.41999998688697815D) {
         double ratioY = offsetY / this.playerData.getVelocityY();
         int vl = (int)this.playerData.getCheckVl(this);
         if (ratioY < 0.99D) {
            int percent = (int)Math.round(ratioY * 100.0D);
            ++vl;
            if (vl >= 5 && this.alert(PlayerAlertEvent.AlertType.RELEASE, player, "failed Velocity Check B. P " + percent + ". VL " + vl + ".") && vl >= 15) {
               if (ratioY < 0.6D && !this.playerData.isBanning()) {
                  this.ban(player, "Velocity Check B");
               } else if (ratioY > 0.6D && !this.playerData.isBanWave()) {
                  this.banWave(player, "Velocity Check B");
               }

               vl = 0;
            }
         } else {
            --vl;
         }

         this.playerData.setCheckVl((double)vl, this);
      }

   }
}
