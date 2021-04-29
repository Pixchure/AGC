package club.mineman.antigamingchair.data;

import club.mineman.antigamingchair.AntiGamingChair;
import club.mineman.antigamingchair.check.ICheck;
import club.mineman.antigamingchair.check.impl.aimassist.AimAssistA;
import club.mineman.antigamingchair.check.impl.aimassist.AimAssistB;
import club.mineman.antigamingchair.check.impl.aimassist.AimAssistC;
import club.mineman.antigamingchair.check.impl.autoclicker.AutoClickerA;
import club.mineman.antigamingchair.check.impl.autoclicker.AutoClickerB;
import club.mineman.antigamingchair.check.impl.autoclicker.AutoClickerC;
import club.mineman.antigamingchair.check.impl.autoclicker.AutoClickerD;
import club.mineman.antigamingchair.check.impl.autoclicker.AutoClickerE;
import club.mineman.antigamingchair.check.impl.autoclicker.AutoClickerF;
import club.mineman.antigamingchair.check.impl.autoclicker.AutoClickerG;
import club.mineman.antigamingchair.check.impl.badpackets.BadPacketsA;
import club.mineman.antigamingchair.check.impl.badpackets.BadPacketsB;
import club.mineman.antigamingchair.check.impl.badpackets.BadPacketsC;
import club.mineman.antigamingchair.check.impl.badpackets.BadPacketsD;
import club.mineman.antigamingchair.check.impl.badpackets.BadPacketsE;
import club.mineman.antigamingchair.check.impl.badpackets.BadPacketsF;
import club.mineman.antigamingchair.check.impl.fly.FlyA;
import club.mineman.antigamingchair.check.impl.fly.FlyB;
import club.mineman.antigamingchair.check.impl.fly.FlyC;
import club.mineman.antigamingchair.check.impl.inventory.InventoryA;
import club.mineman.antigamingchair.check.impl.inventory.InventoryB;
import club.mineman.antigamingchair.check.impl.killaura.KillAuraA;
import club.mineman.antigamingchair.check.impl.killaura.KillAuraB;
import club.mineman.antigamingchair.check.impl.killaura.KillAuraC;
import club.mineman.antigamingchair.check.impl.killaura.KillAuraD;
import club.mineman.antigamingchair.check.impl.killaura.KillAuraE;
import club.mineman.antigamingchair.check.impl.killaura.KillAuraF;
import club.mineman.antigamingchair.check.impl.range.RangeA;
import club.mineman.antigamingchair.check.impl.timer.TimerA;
import club.mineman.antigamingchair.check.impl.velocity.VelocityA;
import club.mineman.antigamingchair.check.impl.velocity.VelocityB;
import club.mineman.antigamingchair.check.impl.velocity.VelocityC;
import club.mineman.antigamingchair.check.impl.velocity.VelocityD;
import club.mineman.antigamingchair.client.ClientType;
import club.mineman.antigamingchair.location.CustomLocation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.entity.Player;

public class PlayerData {
   public static final Class<? extends ICheck>[] CHECKS = new Class[]{AimAssistB.class, AimAssistA.class, AimAssistC.class, AutoClickerA.class, AutoClickerB.class, AutoClickerC.class, AutoClickerD.class, AutoClickerE.class, AutoClickerF.class, AutoClickerG.class, BadPacketsA.class, BadPacketsB.class, BadPacketsC.class, BadPacketsD.class, BadPacketsE.class, BadPacketsF.class, FlyA.class, FlyB.class, FlyC.class, InventoryA.class, InventoryB.class, KillAuraA.class, KillAuraB.class, KillAuraC.class, KillAuraD.class, KillAuraE.class, KillAuraF.class, RangeA.class, TimerA.class, VelocityA.class, VelocityB.class, VelocityC.class, VelocityD.class};
   private static final Map<Class<? extends ICheck>, Constructor<? extends ICheck>> CONSTRUCTORS = new ConcurrentHashMap();
   private final Map<UUID, List<CustomLocation>> recentPlayerPackets = new HashMap();
   private final Map<ICheck, Set<Long>> checkViolationTimes = new HashMap();
   private final Map<Class<? extends ICheck>, ICheck> checkMap = new HashMap();
   private final Map<Integer, Long> keepAliveTimes = new HashMap();
   private final Map<ICheck, Double> checkVlMap = new HashMap();
   private final Set<UUID> playersWatching = new HashSet();
   private final Set<String> filteredPhrases = new HashSet();
   private final Set<CustomLocation> teleportLocations = new HashSet();
   private StringBuilder sniffedPacketBuilder = new StringBuilder();
   private CustomLocation lastMovePacket;
   private ClientType client;
   private boolean allowTeleport;
   private boolean inventoryOpen;
   private boolean sendingVape;
   private boolean attackedSinceVelocity;
   private boolean underBlock;
   private boolean sprinting;
   private boolean inLiquid;
   private boolean onGround;
   private boolean sniffing;
   private boolean onStairs;
   private boolean banWave;
   private boolean placing;
   private boolean banning;
   private boolean digging;
   private boolean inWeb;
   private boolean onIce;
   private double lastGroundY;
   private double velocityX;
   private double velocityY;
   private double velocityZ;
   private long lastDelayedMovePacket;
   private long lastAttackPacket;
   private long lastTeleportTime;
   private long lastVelocity;
   private long ping;
   private int velocityH;
   private int velocityV;
   private int cps;

   public PlayerData(AntiGamingChair plugin) {
      this.client = ClientType.VANILLA;
      Iterator var2 = CONSTRUCTORS.keySet().iterator();

      while(var2.hasNext()) {
         Class<? extends ICheck> check = (Class)var2.next();
         Constructor constructor = (Constructor)CONSTRUCTORS.get(check);

         try {
            this.checkMap.put(check, constructor.newInstance(plugin, this));
         } catch (IllegalAccessException | InvocationTargetException | InstantiationException var6) {
            var6.printStackTrace();
         }
      }

   }

   public <T extends ICheck> T getCheck(Class<T> clazz) {
      return (ICheck)this.checkMap.get(clazz);
   }

   public CustomLocation getLastPlayerPacket(UUID playerUUID, int index) {
      return this.recentPlayerPackets.containsKey(playerUUID) && ((List)this.recentPlayerPackets.get(playerUUID)).size() > index ? (CustomLocation)((List)this.recentPlayerPackets.get(playerUUID)).get(((List)this.recentPlayerPackets.get(playerUUID)).size() - index) : null;
   }

   public void addPlayerPacket(UUID playerUUID, CustomLocation customLocation) {
      List<CustomLocation> customLocations = (List)this.recentPlayerPackets.get(playerUUID);
      if (customLocations == null) {
         customLocations = new ArrayList();
      }

      if (((List)customLocations).size() == 20) {
         ((List)customLocations).remove(0);
      }

      ((List)customLocations).add(customLocation);
      this.recentPlayerPackets.put(playerUUID, customLocations);
   }

   public void addTeleportLocation(CustomLocation teleportLocation) {
      this.teleportLocations.add(teleportLocation);
   }

   public boolean allowTeleport(CustomLocation teleportLocation) {
      Iterator var2 = this.teleportLocations.iterator();

      CustomLocation customLocation;
      double delta;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         customLocation = (CustomLocation)var2.next();
         delta = Math.pow(teleportLocation.getX() - customLocation.getX(), 2.0D) + Math.pow(teleportLocation.getY() - customLocation.getY(), 2.0D) + Math.pow(teleportLocation.getZ() - customLocation.getZ(), 2.0D);
      } while(delta != 0.0D);

      this.teleportLocations.remove(customLocation);
      return true;
   }

   public double getCheckVl(ICheck check) {
      if (!this.checkVlMap.containsKey(check)) {
         this.checkVlMap.put(check, 0.0D);
      }

      return (Double)this.checkVlMap.get(check);
   }

   public void setCheckVl(double vl, ICheck check) {
      if (vl < 0.0D) {
         vl = 0.0D;
      }

      this.checkVlMap.put(check, vl);
   }

   public boolean isPlayerWatching(Player player) {
      return this.playersWatching.contains(player.getUniqueId());
   }

   public void togglePlayerWatching(Player player) {
      if (!this.playersWatching.remove(player.getUniqueId())) {
         this.playersWatching.add(player.getUniqueId());
      }

   }

   public boolean isKeywordFiltered(String keyword) {
      return this.filteredPhrases.contains(keyword);
   }

   public void toggleKeywordFilter(String keyword) {
      if (!this.filteredPhrases.remove(keyword)) {
         this.filteredPhrases.add(keyword);
      }

   }

   public boolean keepAliveExists(int id) {
      return this.keepAliveTimes.containsKey(id);
   }

   public long getKeepAliveTime(int id) {
      long time = (Long)this.keepAliveTimes.get(id);
      this.keepAliveTimes.remove(id);
      return time;
   }

   public void addKeepAliveTime(int id) {
      this.keepAliveTimes.put(id, System.currentTimeMillis());
   }

   public int getViolations(ICheck check, Long time) {
      Set<Long> timestamps = (Set)this.checkViolationTimes.get(check);
      if (timestamps != null) {
         int violations = 0;
         Iterator var5 = timestamps.iterator();

         while(var5.hasNext()) {
            long timestamp = (Long)var5.next();
            if (System.currentTimeMillis() - timestamp <= time) {
               ++violations;
            }
         }

         return violations;
      } else {
         return 0;
      }
   }

   public void addViolation(ICheck check) {
      Set<Long> timestamps = (Set)this.checkViolationTimes.get(check);
      if (timestamps == null) {
         timestamps = new HashSet();
      }

      ((Set)timestamps).add(System.currentTimeMillis());
      this.checkViolationTimes.put(check, timestamps);
   }

   public Map<UUID, List<CustomLocation>> getRecentPlayerPackets() {
      return this.recentPlayerPackets;
   }

   public Map<ICheck, Set<Long>> getCheckViolationTimes() {
      return this.checkViolationTimes;
   }

   public Map<Class<? extends ICheck>, ICheck> getCheckMap() {
      return this.checkMap;
   }

   public Map<Integer, Long> getKeepAliveTimes() {
      return this.keepAliveTimes;
   }

   public Map<ICheck, Double> getCheckVlMap() {
      return this.checkVlMap;
   }

   public Set<UUID> getPlayersWatching() {
      return this.playersWatching;
   }

   public Set<String> getFilteredPhrases() {
      return this.filteredPhrases;
   }

   public Set<CustomLocation> getTeleportLocations() {
      return this.teleportLocations;
   }

   public StringBuilder getSniffedPacketBuilder() {
      return this.sniffedPacketBuilder;
   }

   public CustomLocation getLastMovePacket() {
      return this.lastMovePacket;
   }

   public ClientType getClient() {
      return this.client;
   }

   public boolean isAllowTeleport() {
      return this.allowTeleport;
   }

   public boolean isInventoryOpen() {
      return this.inventoryOpen;
   }

   public boolean isSendingVape() {
      return this.sendingVape;
   }

   public boolean isAttackedSinceVelocity() {
      return this.attackedSinceVelocity;
   }

   public boolean isUnderBlock() {
      return this.underBlock;
   }

   public boolean isSprinting() {
      return this.sprinting;
   }

   public boolean isInLiquid() {
      return this.inLiquid;
   }

   public boolean isOnGround() {
      return this.onGround;
   }

   public boolean isSniffing() {
      return this.sniffing;
   }

   public boolean isOnStairs() {
      return this.onStairs;
   }

   public boolean isBanWave() {
      return this.banWave;
   }

   public boolean isPlacing() {
      return this.placing;
   }

   public boolean isBanning() {
      return this.banning;
   }

   public boolean isDigging() {
      return this.digging;
   }

   public boolean isInWeb() {
      return this.inWeb;
   }

   public boolean isOnIce() {
      return this.onIce;
   }

   public double getLastGroundY() {
      return this.lastGroundY;
   }

   public double getVelocityX() {
      return this.velocityX;
   }

   public double getVelocityY() {
      return this.velocityY;
   }

   public double getVelocityZ() {
      return this.velocityZ;
   }

   public long getLastDelayedMovePacket() {
      return this.lastDelayedMovePacket;
   }

   public long getLastAttackPacket() {
      return this.lastAttackPacket;
   }

   public long getLastTeleportTime() {
      return this.lastTeleportTime;
   }

   public long getLastVelocity() {
      return this.lastVelocity;
   }

   public long getPing() {
      return this.ping;
   }

   public int getVelocityH() {
      return this.velocityH;
   }

   public int getVelocityV() {
      return this.velocityV;
   }

   public int getCps() {
      return this.cps;
   }

   public void setLastMovePacket(CustomLocation lastMovePacket) {
      this.lastMovePacket = lastMovePacket;
   }

   public void setClient(ClientType client) {
      this.client = client;
   }

   public void setAllowTeleport(boolean allowTeleport) {
      this.allowTeleport = allowTeleport;
   }

   public void setInventoryOpen(boolean inventoryOpen) {
      this.inventoryOpen = inventoryOpen;
   }

   public void setSendingVape(boolean sendingVape) {
      this.sendingVape = sendingVape;
   }

   public void setAttackedSinceVelocity(boolean attackedSinceVelocity) {
      this.attackedSinceVelocity = attackedSinceVelocity;
   }

   public void setUnderBlock(boolean underBlock) {
      this.underBlock = underBlock;
   }

   public void setSprinting(boolean sprinting) {
      this.sprinting = sprinting;
   }

   public void setInLiquid(boolean inLiquid) {
      this.inLiquid = inLiquid;
   }

   public void setOnGround(boolean onGround) {
      this.onGround = onGround;
   }

   public void setOnStairs(boolean onStairs) {
      this.onStairs = onStairs;
   }

   public void setBanWave(boolean banWave) {
      this.banWave = banWave;
   }

   public void setPlacing(boolean placing) {
      this.placing = placing;
   }

   public void setBanning(boolean banning) {
      this.banning = banning;
   }

   public void setDigging(boolean digging) {
      this.digging = digging;
   }

   public void setInWeb(boolean inWeb) {
      this.inWeb = inWeb;
   }

   public void setOnIce(boolean onIce) {
      this.onIce = onIce;
   }

   public void setLastGroundY(double lastGroundY) {
      this.lastGroundY = lastGroundY;
   }

   public void setVelocityX(double velocityX) {
      this.velocityX = velocityX;
   }

   public void setVelocityY(double velocityY) {
      this.velocityY = velocityY;
   }

   public void setVelocityZ(double velocityZ) {
      this.velocityZ = velocityZ;
   }

   public void setLastDelayedMovePacket(long lastDelayedMovePacket) {
      this.lastDelayedMovePacket = lastDelayedMovePacket;
   }

   public void setLastAttackPacket(long lastAttackPacket) {
      this.lastAttackPacket = lastAttackPacket;
   }

   public void setLastTeleportTime(long lastTeleportTime) {
      this.lastTeleportTime = lastTeleportTime;
   }

   public void setLastVelocity(long lastVelocity) {
      this.lastVelocity = lastVelocity;
   }

   public void setPing(long ping) {
      this.ping = ping;
   }

   public void setVelocityV(int velocityV) {
      this.velocityV = velocityV;
   }

   public void setCps(int cps) {
      this.cps = cps;
   }

   static {
      Class[] var0 = CHECKS;
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         Class check = var0[var2];

         try {
            CONSTRUCTORS.put(check, check.getConstructor(AntiGamingChair.class, PlayerData.class));
         } catch (NoSuchMethodException var5) {
            var5.printStackTrace();
         }
      }

   }
}
