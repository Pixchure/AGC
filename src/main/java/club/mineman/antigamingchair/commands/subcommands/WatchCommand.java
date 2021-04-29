package club.mineman.antigamingchair.commands.subcommands;

import club.mineman.antigamingchair.AntiGamingChair;
import club.mineman.antigamingchair.data.PlayerData;
import club.mineman.core.util.finalutil.CC;
import club.mineman.core.util.finalutil.StringUtil;
import java.beans.ConstructorProperties;
import org.bukkit.entity.Player;

public class WatchCommand implements SubCommand {
   private final AntiGamingChair plugin;

   public void execute(Player player, Player target, String[] args) {
      if (target == null) {
         player.sendMessage(String.format("AGC > Player Not Found!", args[1]));
      } else {
         PlayerData targetData = this.plugin.getPlayerDataManager().getPlayerData(target);
         targetData.togglePlayerWatching(player);
         player.sendMessage(targetData.isPlayerWatching(player) ? CC.L_PURPLE + "You are now watching " + CC.D_PURPLE + target.getName() : CC.L_PURPLE + "You are no longer watching " + CC.D_PURPLE + target.getName());
      }

   }

   @ConstructorProperties({"plugin"})
   public WatchCommand(AntiGamingChair plugin) {
      this.plugin = plugin;
   }
}
