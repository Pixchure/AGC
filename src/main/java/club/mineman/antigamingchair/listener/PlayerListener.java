package club.mineman.antigamingchair.listener;

import club.mineman.antigamingchair.AntiGamingChair;
import club.mineman.antigamingchair.check.ICheck;
import club.mineman.antigamingchair.commands.subcommands.ToggleCommand;
import club.mineman.antigamingchair.data.PlayerData;
import club.mineman.antigamingchair.event.player.PlayerAlertEvent;
import club.mineman.antigamingchair.event.player.PlayerBanEvent;
import club.mineman.antigamingchair.event.player.PlayerBanWaveEvent;
import club.mineman.antigamingchair.log.Log;
import club.mineman.antigamingchair.request.banwave.AGCAddBanWaveRequest;
import club.mineman.antigamingchair.util.BlockUtil;
import club.mineman.core.CorePlugin;
import club.mineman.core.api.request.AbstractCallback;
import club.mineman.core.mineman.Mineman;
import club.mineman.core.rank.Rank;
import club.mineman.core.util.finalutil.CC;
import club.mineman.paper.event.PlayerUpdatePositionEvent;
import club.mineman.paper.event.PlayerUpdateRotationEvent;
import io.netty.buffer.Unpooled;
import java.beans.ConstructorProperties;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutCustomPayload;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.json.simple.JSONObject;

public class PlayerListener implements Listener {
   private final AntiGamingChair plugin;

   @EventHandler
   public void onPlayerJoin(PlayerJoinEvent event) {
      this.plugin.getPlayerDataManager().addPlayerData(event.getPlayer());
      this.plugin.getServer().getScheduler().runTaskLater(this.plugin, () -> {
         ((CraftPlayer)event.getPlayer()).getHandle().playerConnection.sendPacket(new PacketPlayOutCustomPayload("REGISTER", new PacketDataSerializer(Unpooled.wrappedBuffer("CB-Client".getBytes()))));
         ((CraftPlayer)event.getPlayer()).getHandle().playerConnection.sendPacket(new PacketPlayOutCustomPayload("REGISTER", new PacketDataSerializer(Unpooled.wrappedBuffer("CC".getBytes()))));
      }, 10L);
   }

   @EventHandler
   public void onPlayerQuit(PlayerQuitEvent event) {
      if (this.plugin.getAlertsManager().hasAlertsToggled(event.getPlayer())) {
         this.plugin.getAlertsManager().toggleAlerts(event.getPlayer());
      }

      this.plugin.getPlayerDataManager().removePlayerData(event.getPlayer());
   }

   @EventHandler
   public void onTeleport(PlayerTeleportEvent event) {
      Player player = event.getPlayer();
      PlayerData playerData = this.plugin.getPlayerDataManager().getPlayerData(player);
      if (playerData != null) {
         playerData.setLastTeleportTime(System.currentTimeMillis());
         playerData.setSendingVape(true);
      }

   }

   @EventHandler
   public void onDeath(PlayerDeathEvent event) {
      Player player = event.getEntity();
      PlayerData playerData = this.plugin.getPlayerDataManager().getPlayerData(player);
      if (playerData != null) {
         playerData.setInventoryOpen(false);
      }

   }

   @EventHandler
   public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
      Player player = event.getPlayer();
      PlayerData playerData = this.plugin.getPlayerDataManager().getPlayerData(player);
      if (playerData != null) {
         playerData.setInventoryOpen(false);
      }

   }

   @EventHandler
   public void onPlayerUpdatePosition(PlayerUpdatePositionEvent event) {
      Player player = event.getPlayer();
      if (!player.getAllowFlight()) {
         PlayerData playerData = this.plugin.getPlayerDataManager().getPlayerData(player);
         if (playerData != null) {
            playerData.setOnGround(BlockUtil.isOnGround(event.getTo(), 0) || BlockUtil.isOnGround(event.getTo(), 1));
            if (playerData.isOnGround()) {
               playerData.setLastGroundY(event.getTo().getY());
            }

            playerData.setInLiquid(BlockUtil.isOnLiquid(event.getTo(), 0) || BlockUtil.isOnLiquid(event.getTo(), 1));
            playerData.setInWeb(BlockUtil.isOnWeb(event.getTo(), 0));
            playerData.setOnIce(BlockUtil.isOnIce(event.getTo(), 1) || BlockUtil.isOnIce(event.getTo(), 2));
            playerData.setOnStairs(BlockUtil.isOnStairs(event.getTo(), 0) || BlockUtil.isOnStairs(event.getTo(), 1));
            playerData.setUnderBlock(BlockUtil.isOnGround(event.getTo(), -2));
            if (event.getTo().getY() != event.getFrom().getY() && playerData.getVelocityV() > 0) {
               playerData.setVelocityV(playerData.getVelocityV() - 1);
            }

            if (Math.hypot(event.getTo().getX() - event.getFrom().getX(), event.getTo().getZ() - event.getFrom().getZ()) > 0.0D && playerData.getVelocityH() > 0) {
               playerData.setVelocityH(playerData.getVelocityH() - 1);
            }

            Class[] var4 = PlayerData.CHECKS;
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               Class<? extends ICheck> checkClass = var4[var6];
               if (!ToggleCommand.DISABLED_CHECKS.contains(checkClass.getSimpleName().toUpperCase())) {
                  ICheck check = playerData.getCheck(checkClass);
                  if (check.getType() == PlayerUpdatePositionEvent.class) {
                     check.handleCheck(player, event);
                  }
               }
            }

            if (playerData.getVelocityY() > 0.0D && event.getTo().getY() > event.getFrom().getY()) {
               playerData.setVelocityY(0.0D);
            }

         }
      }
   }

   @EventHandler
   public void onPlayerUpdateRotation(PlayerUpdateRotationEvent event) {
      Player player = event.getPlayer();
      if (!player.getAllowFlight()) {
         PlayerData playerData = this.plugin.getPlayerDataManager().getPlayerData(player);
         if (playerData != null) {
            Class[] var4 = PlayerData.CHECKS;
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               Class<? extends ICheck> checkClass = var4[var6];
               if (!ToggleCommand.DISABLED_CHECKS.contains(checkClass.getSimpleName().toUpperCase())) {
                  ICheck check = playerData.getCheck(checkClass);
                  if (check.getType() == PlayerUpdateRotationEvent.class) {
                     check.handleCheck(player, event);
                  }
               }
            }

         }
      }
   }

   @EventHandler
   public void onPlayerBanWave(PlayerBanWaveEvent event) {
      if (this.plugin.isAntiCheatEnabled() || event.getReason().equals("Manual")) {
         final Player player = event.getPlayer();
         if (player != null) {
            Mineman mineman = CorePlugin.getInstance().getPlayerManager().getPlayer(player.getUniqueId());
            Log log = new Log(mineman.getId(), "was added to the next ban wave for " + event.getReason());
            this.plugin.getLogManager().addToLogQueue(log);
            CorePlugin.getInstance().getRequestManager().sendRequest(new AGCAddBanWaveRequest(mineman.getId(), event.getReason()), new AbstractCallback("Error adding " + player.getName() + " to the ban wave.") {
               public void callback(JSONObject data) {
                  String response = (String)data.get("response");
                  if (response.equals("success")) {
                     PlayerListener.this.plugin.getLogger().info("Added " + player.getName() + " to the ban wave.");
                  }

               }
            });
         }
      }
   }

   @EventHandler
   public void onPlayerAlert(PlayerAlertEvent event) {
      if (!this.plugin.isAntiCheatEnabled()) {
         event.setCancelled(true);
      } else {
         Player player = event.getPlayer();
         if (player != null) {
            PlayerData playerData = this.plugin.getPlayerDataManager().getPlayerData(player);
            if (playerData != null) {
               double tps = MinecraftServer.getServer().tps1.getAverage();
               String fixedTPS = (new DecimalFormat(".##")).format(tps);
               if (tps > 20.0D) {
                  fixedTPS = "20.0";
               }

               String alert = event.getAlert() + ChatColor.LIGHT_PURPLE + " Ping " + playerData.getPing() + " ms. TPS " + fixedTPS + ".";
               Set<UUID> playerUUIDs = new HashSet(this.plugin.getAlertsManager().getAlertsToggled());
               playerUUIDs.addAll(playerData.getPlayersWatching());
               PlayerAlertEvent.AlertType type = event.getAlertType();
               Stream var10000 = playerUUIDs.stream();
               Server var10001 = this.plugin.getServer();
               var10001.getClass();
               var10000.map(var10001::getPlayer).filter(Objects::nonNull).forEach((p) -> {
                  Mineman mineman = CorePlugin.getInstance().getPlayerManager().getPlayer(p.getUniqueId());
                  if (mineman.getRank().hasRank(Rank.ADMIN) && type == PlayerAlertEvent.AlertType.RELEASE || mineman.getRank().hasRank(Rank.DEVELOPER)) {
                     PlayerData alertedData = this.plugin.getPlayerDataManager().getPlayerData(p);
                     boolean sendAlert = true;
                     Iterator var8 = alertedData.getFilteredPhrases().iterator();

                     while(var8.hasNext()) {
                        String phrase = (String)var8.next();
                        if (alert.toLowerCase().contains(phrase)) {
                           sendAlert = false;
                           break;
                        }
                     }

                     if (sendAlert) {
                        p.sendMessage(ChatColor.LIGHT_PURPLE + event.getPlayer().getName() + CC.D_PURPLE + " " + alert);
                     }
                  }

               });
               Mineman mineman = CorePlugin.getInstance().getPlayerManager().getPlayer(player.getUniqueId());
               Log log = new Log(mineman.getId(), ChatColor.stripColor(alert));
               this.plugin.getLogManager().addToLogQueue(log);
            }
         }
      }
   }

   @EventHandler
   public void onPlayerBan(PlayerBanEvent event) {
      if (!this.plugin.isAntiCheatEnabled()) {
         event.setCancelled(true);
      } else {
         Player player = event.getPlayer();
         if (player != null) {
            this.plugin.getServer().broadcastMessage(CC.S + "--------------------------------------------------\n" + CC.R + "✘ " + CC.PINK + player.getName() + CC.D_PURPLE + " was banned by " + CC.PINK + "AntiGamingChair" + CC.D_PURPLE + " for cheating.\n" + CC.R + CC.S + "--------------------------------------------------\n");
            this.plugin.getServer().getScheduler().runTask(this.plugin, () -> {
               this.plugin.getServer().dispatchCommand(this.plugin.getServer().getConsoleSender(), "ban " + player.getName() + " Unfair Advantage -s");
            });
            Mineman mineman = CorePlugin.getInstance().getPlayerManager().getPlayer(player.getUniqueId());
            Log log = new Log(mineman.getId(), "was autobanned for " + event.getReason());
            this.plugin.getLogManager().addToLogQueue(log);
         }
      }
   }

   @ConstructorProperties({"plugin"})
   public PlayerListener(AntiGamingChair plugin) {
      this.plugin = plugin;
   }
}
