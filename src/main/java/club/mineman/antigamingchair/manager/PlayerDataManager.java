package club.mineman.antigamingchair.manager;

import club.mineman.antigamingchair.AntiGamingChair;
import club.mineman.antigamingchair.data.PlayerData;
import java.beans.ConstructorProperties;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.entity.Player;

public class PlayerDataManager {
   private final Map<UUID, PlayerData> playerDataMap = new HashMap();
   private final AntiGamingChair plugin;

   public void addPlayerData(Player player) {
      this.playerDataMap.put(player.getUniqueId(), new PlayerData(this.plugin));
   }

   public void removePlayerData(Player player) {
      this.playerDataMap.remove(player.getUniqueId());
   }

   public PlayerData getPlayerData(Player player) {
      return (PlayerData)this.playerDataMap.get(player.getUniqueId());
   }

   @ConstructorProperties({"plugin"})
   public PlayerDataManager(AntiGamingChair plugin) {
      this.plugin = plugin;
   }
}
