package club.mineman.antigamingchair.check.impl.range;

import club.mineman.antigamingchair.AntiGamingChair;
import club.mineman.antigamingchair.check.AbstractCheck;
import club.mineman.antigamingchair.data.PlayerData;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import org.bukkit.entity.Player;

public class KillAuraD extends AbstractCheck<double[]> {
   private final Deque<Double> distances = new LinkedList();

   public KillAuraD(AntiGamingChair var1, PlayerData var2) {
      super(var1, var2, double[].class);
   }

   public void handleCheck(Player var1, double[] var2) {
      double var3 = var2[0];
      double var5 = var2[1];
      this.distances.addLast(var3 - var5);
      if (this.distances.size() == 30) {
         int var7 = 0;
         double var8 = 0.0D;
         Iterator var10 = this.distances.iterator();

         while(var10.hasNext()) {
            double var11 = (Double)var10.next();
            if (!(var11 < -1.0D)) {
               ++var7;
               var8 += var11;
            }
         }

         var8 /= (double)var7;
         double var12 = this.playerData.getCheckVl(this);
         if (var8 > -0.2D && var7 > 10) {
            this.plugin.getAlertsManager().forceAlert(String.format("failed KillAura Check D (Development). %.3f. %.2f. VL %.2f.", var8, -0.2D, var12), var1);
         } else {
            var12 -= 0.3D;
         }

         this.playerData.setCheckVl(var12, this);
         this.distances.clear();
      }

   }
}
