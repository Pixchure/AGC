package club.mineman.antigamingchair.check.impl.fly;

import club.mineman.antigamingchair.AntiGamingChair;
import club.mineman.antigamingchair.check.checks.PositionCheck;
import club.mineman.antigamingchair.data.PlayerData;
import club.mineman.antigamingchair.event.player.PlayerAlertEvent;
import club.mineman.paper.event.PlayerUpdatePositionEvent;
import java.util.Iterator;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FlyA extends PositionCheck {
   public FlyA(AntiGamingChair plugin, PlayerData playerData) {
      super(plugin, playerData);
   }

   public void handleCheck(Player player, PlayerUpdatePositionEvent event) {
      int vl = (int)this.playerData.getCheckVl(this);
      if (!player.getAllowFlight() && !player.isInsideVehicle() && !this.playerData.isInLiquid() && !this.playerData.isOnGround() && this.playerData.getVelocityV() == 0 && event.getTo().getY() > event.getFrom().getY()) {
         double distance = event.getTo().getY() - this.playerData.getLastGroundY();
         double limit = 2.0D;
         if (player.hasPotionEffect(PotionEffectType.JUMP)) {
            Iterator var8 = player.getActivePotionEffects().iterator();

            while(var8.hasNext()) {
               PotionEffect effect = (PotionEffect)var8.next();
               if (effect.getType().equals(PotionEffectType.JUMP)) {
                  int level = effect.getAmplifier() + 1;
                  limit += Math.pow((double)level + 4.2D, 2.0D) / 16.0D;
                  break;
               }
            }
         }

         if (distance > limit) {
            ++vl;
            if (vl >= 10 && this.alert(PlayerAlertEvent.AlertType.RELEASE, player, "failed Fly Check A. VL " + vl + ".")) {
               int violations = this.playerData.getViolations(this, 60000L);
               if (!this.playerData.isBanning() && violations > 8) {
                  this.ban(player, "Fly Check A");
               }
            }
         } else {
            vl = 0;
         }
      } else {
         vl = 0;
      }

      this.playerData.setCheckVl((double)vl, this);
   }
}
