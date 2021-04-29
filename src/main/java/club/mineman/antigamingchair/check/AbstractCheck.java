package club.mineman.antigamingchair.check;

import club.mineman.antigamingchair.AntiGamingChair;
import club.mineman.antigamingchair.data.PlayerData;
import club.mineman.antigamingchair.event.player.PlayerAlertEvent;
import club.mineman.antigamingchair.event.player.PlayerBanEvent;
import club.mineman.antigamingchair.event.player.PlayerBanWaveEvent;
import java.beans.ConstructorProperties;
import org.bukkit.entity.Player;

public abstract class AbstractCheck<T> implements ICheck<T> {
   protected final AntiGamingChair plugin;
   protected final PlayerData playerData;
   private final Class<T> clazz;

   public Class<? extends T> getType() {
      return this.clazz;
   }

   protected boolean alert(PlayerAlertEvent.AlertType alertType, Player player, String message) {
      PlayerAlertEvent event = new PlayerAlertEvent(alertType, player, message);
      this.plugin.getServer().getPluginManager().callEvent(event);
      if (!event.isCancelled()) {
         this.playerData.addViolation(this);
         return true;
      } else {
         return false;
      }
   }

   protected boolean banWave(Player player, String message) {
      this.playerData.setBanWave(true);
      PlayerBanWaveEvent event = new PlayerBanWaveEvent(player, message);
      this.plugin.getServer().getPluginManager().callEvent(event);
      return !event.isCancelled();
   }

   protected boolean ban(Player player, String message) {
      this.playerData.setBanning(true);
      PlayerBanEvent event = new PlayerBanEvent(player, message);
      this.plugin.getServer().getPluginManager().callEvent(event);
      return !event.isCancelled();
   }

   public AntiGamingChair getPlugin() {
      return this.plugin;
   }

   public PlayerData getPlayerData() {
      return this.playerData;
   }

   public Class<T> getClazz() {
      return this.clazz;
   }

   @ConstructorProperties({"plugin", "playerData", "clazz"})
   public AbstractCheck(AntiGamingChair plugin, PlayerData playerData, Class<T> clazz) {
      this.plugin = plugin;
      this.playerData = playerData;
      this.clazz = clazz;
   }
}
