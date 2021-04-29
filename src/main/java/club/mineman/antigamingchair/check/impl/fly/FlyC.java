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

public class FlyC extends PositionCheck {
   private int illegalMovements;
   private int legalMovements;

   public FlyC(AntiGamingChair plugin, PlayerData playerData) {
      super(plugin, playerData);
   }

   public void handleCheck(Player player, PlayerUpdatePositionEvent event) {
      if (!player.getAllowFlight() && !player.isInsideVehicle() && this.playerData.getVelocityH() == 0) {
         double offsetH = Math.hypot(event.getTo().getX() - event.getFrom().getX(), event.getTo().getZ() - event.getFrom().getZ());
         int speed = 0;
         Iterator var6 = player.getActivePotionEffects().iterator();

         while(var6.hasNext()) {
            PotionEffect effect = (PotionEffect)var6.next();
            if (effect.getType().equals(PotionEffectType.SPEED)) {
               speed = effect.getAmplifier() + 1;
               break;
            }
         }

         double limit;
         if (this.playerData.isOnGround()) {
            limit = 0.34D;
            if (this.playerData.isOnStairs()) {
               limit = 0.45D;
            } else if (this.playerData.isOnIce()) {
               if (this.playerData.isUnderBlock()) {
                  limit = 1.3D;
               } else {
                  limit = 0.65D;
               }
            } else if (this.playerData.isUnderBlock()) {
               limit = 0.7D;
            }

            limit += (double)(player.getWalkSpeed() > 0.2F ? player.getWalkSpeed() * 10.0F * 0.33F : 0.0F);
            limit += 0.06D * (double)speed;
         } else {
            limit = 0.36D;
            if (this.playerData.isOnStairs()) {
               limit = 0.45D;
            } else if (this.playerData.isOnIce()) {
               if (this.playerData.isUnderBlock()) {
                  limit = 1.3D;
               } else {
                  limit = 0.65D;
               }
            } else if (this.playerData.isUnderBlock()) {
               limit = 0.7D;
            }

            limit += (double)(player.getWalkSpeed() > 0.2F ? player.getWalkSpeed() * 10.0F * 0.33F : 0.0F);
            limit += 0.02D * (double)speed;
         }

         if (offsetH > limit) {
            ++this.illegalMovements;
         } else {
            ++this.legalMovements;
         }

         int total = this.illegalMovements + this.legalMovements;
         if (total >= 20) {
            double percentage = (double)this.illegalMovements / 20.0D * 100.0D;
            if (percentage >= 45.0D && this.alert(PlayerAlertEvent.AlertType.RELEASE, player, String.format("failed Fly Check C. P %.1f.", percentage))) {
               int violations = this.playerData.getViolations(this, 30000L);
               if (!this.playerData.isBanning() && violations > 5) {
                  this.ban(player, "Fly Check C");
               }
            }

            this.illegalMovements = 0;
            this.legalMovements = 0;
         }
      }

   }
}
