package club.mineman.antigamingchair.util.api;

import club.mineman.antigamingchair.AntiGamingChair;
import club.mineman.antigamingchair.check.impl.range.RangeC;
import club.mineman.antigamingchair.client.ClientType;
import club.mineman.antigamingchair.data.PlayerData;
import org.bukkit.entity.Player;

public final class AGCAPI {
   private AGCAPI() {
   }

   public static boolean isCheatBreaker(Player player) {
      PlayerData playerData = AntiGamingChair.getInstance().getPlayerDataManager().getPlayerData(player);
      return playerData != null && playerData.getClient() == ClientType.CHEAT_BREAKER;
   }

   public static int getPing(Player player) {
      PlayerData playerData = AntiGamingChair.getInstance().getPlayerDataManager().getPlayerData(player);
      return playerData != null ? (int)playerData.getPing() : 0;
   }

   public static void spawnEntityAtCursor(Player player) {
      PlayerData playerData = AntiGamingChair.getInstance().getPlayerDataManager().getPlayerData(player);
      if (playerData != null) {
         RangeC rangeC = (RangeC)playerData.getCheck(RangeC.class);
         if (rangeC == null) {
            return;
         }

         rangeC.spawnEntity(player);
      }

   }

   public static void destroyEntityAtCursor(Player player) {
      PlayerData playerData = AntiGamingChair.getInstance().getPlayerDataManager().getPlayerData(player);
      if (playerData != null) {
         RangeC rangeC = (RangeC)playerData.getCheck(RangeC.class);
         if (rangeC == null) {
            return;
         }

         rangeC.destroyEntity(player);
      }

   }
}
