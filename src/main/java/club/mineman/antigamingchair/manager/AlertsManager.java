package club.mineman.antigamingchair.manager;

import club.mineman.antigamingchair.AntiGamingChair;
import club.mineman.antigamingchair.data.PlayerData;
import club.mineman.core.util.finalutil.CC;
import java.beans.ConstructorProperties;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;

public class AlertsManager {
   private final Set<UUID> alertsToggled = new HashSet();
   private final AntiGamingChair plugin;

   public boolean hasAlertsToggled(Player player) {
      return this.alertsToggled.contains(player.getUniqueId());
   }

   public void toggleAlerts(Player player) {
      if (!this.alertsToggled.remove(player.getUniqueId())) {
         this.alertsToggled.add(player.getUniqueId());
      }

   }

   public void forceAlert(String message) {
      this.forceAlertWithData(message, (PlayerData)null);
   }

   private void forceAlertWithData(String message, PlayerData playerData) {
      Set<UUID> playerUUIDs = new HashSet(this.plugin.getAlertsManager().getAlertsToggled());
      if (playerData != null) {
         playerUUIDs.addAll(playerData.getPlayersWatching());
      }

      Stream var10000 = playerUUIDs.stream();
      Server var10001 = this.plugin.getServer();
      var10001.getClass();
      var10000.map(var10001::getPlayer).filter(Objects::nonNull).forEach((p) -> {
         p.sendMessage(message);
      });
   }

   public void forceAlert(String message, Player player) {
      double tps = MinecraftServer.getServer().tps1.getAverage();
      String fixedTPS = (new DecimalFormat(".##")).format(tps);
      if (tps > 20.0D) {
         fixedTPS = "20.0";
      }

      PlayerData playerData = this.plugin.getPlayerDataManager().getPlayerData(player);
      String alert = message + ChatColor.LIGHT_PURPLE + " Ping " + playerData.getPing() + " ms. TPS " + fixedTPS + ".";
      this.forceAlertWithData(ChatColor.LIGHT_PURPLE + player.getName() + CC.D_PURPLE + " " + alert, playerData);
   }

   @ConstructorProperties({"plugin"})
   public AlertsManager(AntiGamingChair plugin) {
      this.plugin = plugin;
   }

   public Set<UUID> getAlertsToggled() {
      return this.alertsToggled;
   }
}
