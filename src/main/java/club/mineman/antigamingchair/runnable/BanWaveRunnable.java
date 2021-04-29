package club.mineman.antigamingchair.runnable;

import club.mineman.antigamingchair.AntiGamingChair;
import club.mineman.antigamingchair.manager.BanWaveManager;
import club.mineman.core.util.finalutil.CC;
import java.beans.ConstructorProperties;
import org.bukkit.scheduler.BukkitRunnable;

public class BanWaveRunnable extends BukkitRunnable {
   private static final String REASON = "[AntiGamingChair] Ban Wave v1";
   private final AntiGamingChair plugin;
   private int countdown = 6;

   public void run() {
      BanWaveManager manager = this.plugin.getBanWaveManager();
      if (manager.isBanWaveStarted()) {
         if (--this.countdown > 0) {
            this.plugin.getServer().broadcastMessage(CC.PINK + "The ban wave will commence in " + CC.D_PURPLE + this.countdown + " second(s)...");
         } else if (this.countdown == 0) {
            this.plugin.getServer().broadcastMessage(CC.GREEN + "The ban wave has commenced.");
            this.countdown = -1;
         } else if (manager.getCheaters().size() > 0) {
            long id = manager.queueCheater();
            String name = manager.getCheaterName(id);
            this.plugin.getServer().getScheduler().runTask(this.plugin, () -> {
               this.plugin.getServer().dispatchCommand(this.plugin.getServer().getConsoleSender(), "ban " + name + " " + "[AntiGamingChair] Ban Wave v1" + " -s");
            });
            this.plugin.getServer().broadcastMessage(CC.D_PURPLE + name + CC.L_PURPLE + " was banned for " + CC.D_PURPLE + "[AntiGamingChair] Ban Wave v1");
         } else if (manager.getCheaters().size() == 0) {
            manager.setBanWaveStarted(false);
         }
      } else {
         this.plugin.getServer().broadcastMessage(CC.S + "--------------------------------------------------\n" + CC.R + "✘ " + CC.D_PURPLE + "AntiGamingChair " + CC.PINK + "has finished the ban wave. A total of " + CC.D_PURPLE + manager.getCheaterNames().size() + " players " + CC.PINK + "were banned. Stay legit minemen...\n" + CC.R + CC.S + "--------------------------------------------------\n");
         this.plugin.getBanWaveManager().clearCheaters();
         this.cancel();
      }

   }

   @ConstructorProperties({"plugin"})
   public BanWaveRunnable(AntiGamingChair plugin) {
      this.plugin = plugin;
   }
}
