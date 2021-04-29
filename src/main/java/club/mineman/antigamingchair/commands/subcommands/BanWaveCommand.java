package club.mineman.antigamingchair.commands.subcommands;

import club.mineman.antigamingchair.AntiGamingChair;
import club.mineman.antigamingchair.data.PlayerData;
import club.mineman.antigamingchair.event.BanWaveEvent;
import club.mineman.antigamingchair.event.player.PlayerBanWaveEvent;
import club.mineman.antigamingchair.request.banwave.AGCGetBanWaveRequest;
import club.mineman.core.CorePlugin;
import club.mineman.core.api.request.AbstractCallback;
import club.mineman.core.util.finalutil.CC;
import club.mineman.core.util.finalutil.HttpUtil;
import club.mineman.core.util.finalutil.StringUtil;
import java.beans.ConstructorProperties;
import java.util.Iterator;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class BanWaveCommand implements SubCommand {
   private final AntiGamingChair plugin;

   public void execute(final Player player, Player target, String[] args) {
      if (args.length >= 2) {
         String var4 = args[1].toLowerCase();
         byte var5 = -1;
         switch(var4.hashCode()) {
         case 96417:
            if (var4.equals("add")) {
               var5 = 2;
            }
            break;
         case 3322014:
            if (var4.equals("list")) {
               var5 = 3;
            }
            break;
         case 3540994:
            if (var4.equals("stop")) {
               var5 = 1;
            }
            break;
         case 109757538:
            if (var4.equals("start")) {
               var5 = 0;
            }
         }

         switch(var5) {
         case 0:
            BanWaveEvent banWaveEvent = new BanWaveEvent(player.getName());
            this.plugin.getServer().getPluginManager().callEvent(banWaveEvent);
            break;
         case 1:
            this.plugin.getBanWaveManager().setBanWaveStarted(false);
            break;
         case 2:
            if (args.length < 3) {
               return;
            }

            Player player1 = this.plugin.getServer().getPlayer(args[2]);
            if (player1 == null) {
               player.sendMessage(String.format(StringUtil.PLAYER_NOT_FOUND, args[2]));
               return;
            }

            PlayerData playerData = this.plugin.getPlayerDataManager().getPlayerData(player1);
            if (!playerData.isBanWave()) {
               playerData.setBanWave(true);
               PlayerBanWaveEvent banEvent = new PlayerBanWaveEvent(player1, "Manual");
               this.plugin.getServer().getPluginManager().callEvent(banEvent);
               player.sendMessage(CC.L_PURPLE + "Added " + CC.D_PURPLE + player1.getName() + CC.L_PURPLE + " to the ban wave.");
            }
            break;
         case 3:
            CorePlugin.getInstance().getRequestManager().sendRequest(new AGCGetBanWaveRequest(), new AbstractCallback("Error fetching the ban wave list.") {
               public void callback(JSONObject data) {
                  JSONArray array = (JSONArray)data.get("data");
                  StringBuilder list = new StringBuilder();
                  Iterator var4 = array.iterator();

                  while(var4.hasNext()) {
                     Object object = var4.next();
                     JSONObject jsonObject = (JSONObject)object;
                     String name = (String)jsonObject.get("name");
                     list.append(name).append("\n");
                  }

                  BanWaveCommand.this.plugin.getServer().getScheduler().runTaskAsynchronously(BanWaveCommand.this.plugin, () -> {
                     String bin = HttpUtil.getHastebin(list.toString());
                     if (bin == null) {
                        player.sendMessage(CC.RED + "There was a problem uploading the ban wave list, check console for details.");
                     } else {
                        player.sendMessage(CC.L_PURPLE + "Banwave list: http://www.hastebin.com/" + bin);
                     }
                  });
               }
            });
         }

      }
   }

   @ConstructorProperties({"plugin"})
   public BanWaveCommand(AntiGamingChair plugin) {
      this.plugin = plugin;
   }
}
