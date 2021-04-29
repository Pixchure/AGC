package club.mineman.antigamingchair.check.impl.velocity;

import club.mineman.antigamingchair.AntiGamingChair;
import club.mineman.antigamingchair.check.checks.PositionCheck;
import club.mineman.antigamingchair.data.PlayerData;
import club.mineman.antigamingchair.event.player.PlayerAlertEvent;
import club.mineman.antigamingchair.util.MathUtil;
import club.mineman.paper.event.PlayerUpdatePositionEvent;
import org.bukkit.entity.Player;

public class VelocityA extends PositionCheck {
   public VelocityA(AntiGamingChair plugin, PlayerData playerData) {
      super(plugin, playerData);
   }

   public void handleCheck(Player player, PlayerUpdatePositionEvent event) {
      int vl = (int)this.playerData.getCheckVl(this);
      if (this.playerData.getVelocityY() > 0.0D && !this.playerData.isUnderBlock() && !this.playerData.isInLiquid()) {
         int threshold = 12 + MathUtil.pingFormula(this.playerData.getPing()) * 2;
         ++vl;
         if (vl >= threshold) {
            if (this.alert(PlayerAlertEvent.AlertType.RELEASE, player, "failed Velocity Check A. VL " + vl + ".")) {
               int violations = this.playerData.getViolations(this, 60000L);
               if (!this.playerData.isBanning() && (long)violations > Math.max(this.playerData.getPing() / 10L, 15L)) {
                  this.ban(player, "Velocity Check A");
               }
            }

            this.playerData.setVelocityY(0.0D);
            vl = 0;
         }
      } else {
         vl = 0;
      }

      this.playerData.setCheckVl((double)vl, this);
   }
}
