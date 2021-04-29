package club.mineman.antigamingchair;

import club.mineman.antigamingchair.commands.AGCCommand;
import club.mineman.antigamingchair.commands.PingCommand;
import club.mineman.antigamingchair.listener.BanWaveListener;
import club.mineman.antigamingchair.listener.PlayerListener;
import club.mineman.antigamingchair.manager.AlertsManager;
import club.mineman.antigamingchair.manager.BanWaveManager;
import club.mineman.antigamingchair.manager.LogManager;
import club.mineman.antigamingchair.manager.PlayerDataManager;
import club.mineman.antigamingchair.packet.CustomPacketHandler;
import club.mineman.antigamingchair.runnable.ExportLogsRunnable;
import club.mineman.core.CorePlugin;
import club.mineman.paper.PaperServer;
import java.util.Arrays;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.bukkit.plugin.java.JavaPlugin;

public class AntiGamingChair extends JavaPlugin {
   private static AntiGamingChair instance;
   private PlayerDataManager playerDataManager;
   private BanWaveManager banWaveManager;
   private AlertsManager alertsManager;
   private LogManager logManager;
   private double rangeVl = 25.0D;
   private double rangeVlLow = 25.0D;

   public void onEnable() {
      instance = this;
      this.registerPacketHandler();
      this.registerManagers();
      this.registerListeners();
      this.registerCommands();
      this.registerRunnables();
   }

   public void onDisable() {
   }

   public boolean isAntiCheatEnabled() {
      return MinecraftServer.getServer().tps1.getAverage() > 19.0D && MinecraftServer.LAST_TICK_TIME + 100L > System.currentTimeMillis();
   }

   private void registerPacketHandler() {
      PaperServer.INSTANCE.addPacketHandler(new CustomPacketHandler(this));
   }

   private void registerManagers() {
      this.alertsManager = new AlertsManager(this);
      this.banWaveManager = new BanWaveManager(this);
      this.playerDataManager = new PlayerDataManager(this);
      this.logManager = new LogManager();
   }

   private void registerListeners() {
      this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
      this.getServer().getPluginManager().registerEvents(new BanWaveListener(this), this);
   }

   private void registerCommands() {
      Arrays.asList(new AGCCommand(this), new PingCommand(this)).forEach((command) -> {
         CorePlugin.getInstance().registerCommand(command, this.getName());
      });
   }

   private void registerRunnables() {
      this.getServer().getScheduler().runTaskTimerAsynchronously(this, new ExportLogsRunnable(this), 1200L, 200L);
   }

   public PlayerDataManager getPlayerDataManager() {
      return this.playerDataManager;
   }

   public BanWaveManager getBanWaveManager() {
      return this.banWaveManager;
   }

   public AlertsManager getAlertsManager() {
      return this.alertsManager;
   }

   public LogManager getLogManager() {
      return this.logManager;
   }

   public static AntiGamingChair getInstance() {
      return instance;
   }

   public double getRangeVl() {
      return this.rangeVl;
   }

   public double getRangeVlLow() {
      return this.rangeVlLow;
   }

   public void setRangeVl(double rangeVl) {
      this.rangeVl = rangeVl;
   }

   public void setRangeVlLow(double rangeVlLow) {
      this.rangeVlLow = rangeVlLow;
   }
}
