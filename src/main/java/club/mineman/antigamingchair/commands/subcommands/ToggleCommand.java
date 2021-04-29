package club.mineman.antigamingchair.commands.subcommands;

import club.mineman.core.rank.Rank;
import club.mineman.core.util.finalutil.CC;
import club.mineman.core.util.finalutil.PlayerUtil;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.entity.Player;

public class ToggleCommand implements SubCommand {
   public static final Set<String> DISABLED_CHECKS = new HashSet();

   public void execute(Player player, Player target, String[] args) {
      if (PlayerUtil.testPermission(player, Rank.DEVELOPER)) {
         String check = args[1].toUpperCase();
         if (!DISABLED_CHECKS.remove(check)) {
            DISABLED_CHECKS.add(check);
            player.sendMessage(CC.L_PURPLE + "Enabled check " + CC.D_PURPLE + check);
         } else {
            player.sendMessage(CC.L_PURPLE + "Disabled check " + CC.D_PURPLE + check);
         }

      }
   }
}
