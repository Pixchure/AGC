package club.mineman.antigamingchair.commands;

import club.mineman.antigamingchair.AntiGamingChair;
import club.mineman.antigamingchair.data.PlayerData;
import club.mineman.core.util.finalutil.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PingCommand extends Command {
   private final AntiGamingChair plugin;

   public PingCommand(AntiGamingChair plugin) {
      super("ping");
      this.plugin = plugin;
      this.usageMessage = CC.RED + "Usage: /ping <player>";
   }

   public boolean execute(CommandSender sender, String alias, String[] args) {
      if (sender instanceof Player) {
         Player player = args.length >= 1 && this.plugin.getServer().getPlayer(args[0]) != null ? this.plugin.getServer().getPlayer(args[0]) : (Player)sender;
         PlayerData data = this.plugin.getPlayerDataManager().getPlayerData(player);
         sender.sendMessage(CC.PINK + player.getName() + " has a ping of " + CC.D_PURPLE + data.getPing() + CC.PINK + " ms.");
      }

      return true;
   }
}
