package club.mineman.antigamingchair.commands.subcommands;

import club.mineman.antigamingchair.AntiGamingChair;
import club.mineman.antigamingchair.log.Log;
import club.mineman.antigamingchair.request.AGCInfoRequest;
import club.mineman.core.CorePlugin;
import club.mineman.core.api.request.AbstractCallback;
import club.mineman.core.mineman.Mineman;
import club.mineman.core.util.finalutil.CC;
import club.mineman.core.util.finalutil.HttpUtil;
import club.mineman.core.util.finalutil.StringUtil;
import club.mineman.core.util.finalutil.TimeUtil;
import java.beans.ConstructorProperties;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class LogsCommand implements SubCommand {
   private static final Pattern LOG_PATTERN = Pattern.compile("failed (.*) Check (\\D).(.*)");
   private final AntiGamingChair plugin;

   public void execute(final Player player, final Player target, final String[] args) {
      if (args.length != 1) {
         final boolean detailed = args.length > 2 && args[2].equalsIgnoreCase("-d");
         int index = detailed ? 3 : 2;
         String timeString = args.length > index ? StringUtil.buildMessage(args, index) : null;
         Timestamp time = new Timestamp(0L);
         if (timeString != null) {
            try {
               time.setTime(System.currentTimeMillis() - TimeUtil.toMillis(timeString));
            } catch (NumberFormatException var9) {
               player.sendMessage(CC.RED + "Invalid time specified.");
               return;
            }
         }

         CorePlugin.getInstance().getRequestManager().sendRequest(new AGCInfoRequest(args[1], time), new AbstractCallback("Error fetching AGC logs for " + args[1] + ".") {
            public void callback(JSONObject data) {
               Queue<Log> logs = new LinkedList();
               if (target != null) {
                  Mineman mineman = CorePlugin.getInstance().getPlayerManager().getPlayer(target.getUniqueId());
                  Iterator var4 = LogsCommand.this.plugin.getLogManager().getLogQueue().iterator();

                  while(var4.hasNext()) {
                     Log logx = (Log)var4.next();
                     if (logx.getMinemanId() == mineman.getId()) {
                        logs.add(logx);
                     }
                  }
               }

               String response = (String)data.get("response");
               byte var14 = -1;
               switch(response.hashCode()) {
               case -1867169789:
                  if (response.equals("success")) {
                     var14 = 3;
                  }
                  break;
               case -1685569673:
                  if (response.equals("invalid-player")) {
                     var14 = 1;
                  }
                  break;
               case 2063878715:
                  if (response.equals("no-logs")) {
                     var14 = 2;
                  }
                  break;
               case 2065248534:
                  if (response.equals("player-never-joined")) {
                     var14 = 0;
                  }
               }

               switch(var14) {
               case 0:
               case 1:
                  player.sendMessage(String.format(StringUtil.PLAYER_NOT_FOUND, args[1]));
                  break;
               case 2:
                  if (logs.size() == 0) {
                     player.sendMessage(CC.L_PURPLE + "Player " + CC.D_PURPLE + args[1] + CC.L_PURPLE + " has no logs.");
                     break;
                  }
               case 3:
                  StringBuilder sb = new StringBuilder();
                  String log;
                  if (detailed) {
                     JSONArray arrayx = (JSONArray)data.get("data");
                     Iterator var8 = arrayx.iterator();

                     while(var8.hasNext()) {
                        Object objectx = var8.next();
                        JSONObject jsonObjectx = (JSONObject)objectx;
                        Timestamp timestamp = Timestamp.valueOf(((String)jsonObjectx.get("timestamp")).replace("T", " ").replace("Z", ""));
                        log = (String)jsonObjectx.get("log");
                        sb.append(String.format("[%s] %s %s", timestamp.toString(), args[1], log));
                        sb.append("\n");
                     }

                     var8 = logs.iterator();

                     while(var8.hasNext()) {
                        Log logxx = (Log)var8.next();
                        sb.append(String.format("[%s] %s %s", (new Timestamp(logxx.getTimestamp())).toString(), args[1], logxx.getLog()));
                        sb.append("\n");
                     }

                     LogsCommand.this.plugin.getServer().getScheduler().runTaskAsynchronously(LogsCommand.this.plugin, () -> {
                        String bin = HttpUtil.getHastebin(sb.toString());
                        if (bin == null) {
                           player.sendMessage(CC.RED + "Error uploading logs. Check console for details.");
                        } else {
                           player.sendMessage(CC.L_PURPLE + "Player logs: https://www.hastebin.com/" + bin);
                        }
                     });
                  } else {
                     Map<String, Integer> violationMap = new ConcurrentHashMap();
                     JSONArray array = (JSONArray)data.get("data");
                     Iterator var18 = array.iterator();

                     while(var18.hasNext()) {
                        Object object = var18.next();
                        JSONObject jsonObject = (JSONObject)object;
                        log = (String)jsonObject.get("log");
                        LogsCommand.this.handleLog(violationMap, log);
                     }

                     var18 = logs.iterator();

                     while(var18.hasNext()) {
                        Log logxxx = (Log)var18.next();
                        LogsCommand.this.handleLog(violationMap, logxxx.getLog());
                     }

                     if (violationMap.isEmpty()) {
                        player.sendMessage(CC.GREEN + "Player " + args[1] + " has no logs.");
                        return;
                     }

                     var18 = violationMap.keySet().iterator();

                     while(var18.hasNext()) {
                        String string = (String)var18.next();
                        sb.append(CC.L_PURPLE).append(string).append(" ").append(CC.D_PURPLE).append("x").append(violationMap.get(string)).append("\n");
                     }

                     player.sendMessage(CC.L_PURPLE + args[1] + " logs:");
                     player.sendMessage(sb.toString());
                  }
               }

            }
         });
      }
   }

   private void handleLog(Map<String, Integer> violations, String log) {
      Matcher matcher = LOG_PATTERN.matcher(log);
      if (matcher.find()) {
         String type = matcher.group(1);
         String check = matcher.group(2);
         String finalData = type + " Check " + check;
         int count = (Integer)violations.getOrDefault(finalData, 0);
         ++count;
         violations.put(finalData, count);
      }

   }

   @ConstructorProperties({"plugin"})
   public LogsCommand(AntiGamingChair plugin) {
      this.plugin = plugin;
   }
}
