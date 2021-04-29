package club.mineman.antigamingchair.check.impl.aimassist;

import club.mineman.antigamingchair.AntiGamingChair;
import club.mineman.antigamingchair.check.checks.RotationCheck;
import club.mineman.antigamingchair.data.PlayerData;
import club.mineman.antigamingchair.util.MathUtil;
import club.mineman.paper.event.PlayerUpdateRotationEvent;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class AimAssistD extends RotationCheck {
   private final Deque<Double> notOverSpeeds = new LinkedList();
   private final Deque<Double> overSpeeds = new LinkedList();

   public AimAssistD(AntiGamingChair plugin, PlayerData playerData) {
      super(plugin, playerData);
   }

   public void handleCheck(Player player, PlayerUpdateRotationEvent event) {
      Location from = event.getFrom();
      Location to = event.getTo();
      if (from.getYaw() != to.getYaw()) {
         double speed = Math.hypot((double)from.getYaw(), (double)to.getYaw());
         if (MathUtil.isMouseOverEntity(player)) {
            this.overSpeeds.addLast(speed);
         } else {
            this.notOverSpeeds.addLast(speed);
         }

         int total = this.overSpeeds.size() + this.notOverSpeeds.size();
         if (total == 1000) {
            double averageNotOver = 0.0D;

            double d;
            for(Iterator var10 = this.notOverSpeeds.iterator(); var10.hasNext(); averageNotOver += d) {
               d = (Double)var10.next();
            }

            averageNotOver /= (double)this.notOverSpeeds.size();
            double averageOver = 0.0D;

            double d;
            for(Iterator var12 = this.overSpeeds.iterator(); var12.hasNext(); averageOver += d) {
               d = (Double)var12.next();
            }

            averageOver /= (double)this.overSpeeds.size();
            double vl = this.playerData.getCheckVl(this);
            if (averageNotOver > averageOver) {
               ++vl;
               if (vl > 5.0D) {
                  this.plugin.getAlertsManager().forceAlert("failed Aim Assist Check D (Development). " + averageNotOver + " :: " + averageOver + ". VL " + vl + ".", player);
               }
            } else {
               vl -= 0.4D;
            }

            this.playerData.setCheckVl(vl, this);
            this.notOverSpeeds.clear();
            this.overSpeeds.clear();
         }
      }

   }
}
