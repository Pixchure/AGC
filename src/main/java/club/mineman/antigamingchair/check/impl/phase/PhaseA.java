package club.mineman.antigamingchair.check.impl.phase;

import club.mineman.antigamingchair.AntiGamingChair;
import club.mineman.antigamingchair.check.checks.PositionCheck;
import club.mineman.antigamingchair.data.PlayerData;
import club.mineman.antigamingchair.location.CustomLocation;
import club.mineman.paper.event.PlayerUpdatePositionEvent;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class PhaseA extends PositionCheck {
   private static final List<Material> PHASE_BLOCKS;
   private static final boolean TELEPORT_ON_FAIL = false;
   private CustomLocation lastNotInBlockLocation;
   private boolean inBlock = false;
   private int blocksPhased = 0;

   public PhaseA(AntiGamingChair plugin, PlayerData playerData) {
      super(plugin, playerData);
   }

   public void handleCheck(Player player, PlayerUpdatePositionEvent event) {
      double vl = this.playerData.getCheckVl(this);
      boolean inBlock = this.inBlock;
      Location to = event.getTo();

      try {
         if (PHASE_BLOCKS.contains(to.getBlock().getType())) {
            this.inBlock = false;
            return;
         }

         if (!to.getBlock().getType().name().contains("FENCE") && !to.getBlock().getType().name().contains("DOOR") && to.getBlock().getType().isSolid()) {
            if (this.playerData.getLastTeleportTime() + 1000L > System.currentTimeMillis()) {
               this.inBlock = false;
               return;
            }

            this.inBlock = true;
            Location from = event.getFrom();
            if (!inBlock || from.getBlockX() == to.getBlockX() && from.getBlockY() == to.getBlockY() && from.getBlockZ() == to.getBlockZ()) {
               return;
            }

            vl += 1.0D + (double)(++this.blocksPhased) / 10.0D;
            if (vl > 5.0D) {
               this.plugin.getAlertsManager().forceAlert("failed Phase Check A (Development). " + this.blocksPhased + ". VL " + vl + ".", player);
            }

            return;
         }

         this.inBlock = false;
      } finally {
         if (inBlock && !this.inBlock) {
            this.lastNotInBlockLocation = CustomLocation.fromBukkitLocation(to);
            this.blocksPhased = 0;
            vl -= 0.45D;
         }

         this.playerData.setCheckVl(vl, this);
      }

   }

   static {
      PHASE_BLOCKS = Arrays.asList(Material.LAVA, Material.STATIONARY_LAVA, Material.WATER, Material.STATIONARY_WATER, Material.WATER_LILY, Material.LADDER, Material.AIR, Material.ANVIL, Material.RAILS, Material.ACTIVATOR_RAIL, Material.DETECTOR_RAIL, Material.POWERED_RAIL, Material.TORCH, Material.BED, Material.BED_BLOCK, Material.BREWING_STAND, Material.BREWING_STAND_ITEM);
   }
}
