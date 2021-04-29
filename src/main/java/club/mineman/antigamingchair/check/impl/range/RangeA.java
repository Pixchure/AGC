package club.mineman.antigamingchair.check.impl.range;

import club.mineman.antigamingchair.AntiGamingChair;
import club.mineman.antigamingchair.check.checks.PacketCheck;
import club.mineman.antigamingchair.data.PlayerData;
import club.mineman.antigamingchair.event.player.PlayerAlertEvent;
import club.mineman.antigamingchair.location.CustomLocation;
import club.mineman.antigamingchair.util.MathUtil;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity.EnumEntityUseAction;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class RangeA extends PacketCheck {
   private boolean sameTick;

   public RangeA(AntiGamingChair plugin, PlayerData playerData) {
      super(plugin, playerData);
   }

   public void handleCheck(Player player, Packet packet) {
      if (packet instanceof PacketPlayInUseEntity && !player.getGameMode().equals(GameMode.CREATIVE) && System.currentTimeMillis() - this.playerData.getLastDelayedMovePacket() > 110L && System.currentTimeMillis() - this.playerData.getLastMovePacket().getTimestamp() < 110L && !this.sameTick) {
         PacketPlayInUseEntity useEntity = (PacketPlayInUseEntity)packet;
         if (useEntity.a() == EnumEntityUseAction.ATTACK) {
            Entity targetEntity = useEntity.a(((CraftPlayer)player).getHandle().getWorld());
            if (targetEntity instanceof EntityPlayer) {
               Player target = (Player)targetEntity.getBukkitEntity();
               CustomLocation latestLocation = this.playerData.getLastPlayerPacket(target.getUniqueId(), 1);
               if (latestLocation == null || System.currentTimeMillis() - latestLocation.getTimestamp() > 100L) {
                  return;
               }

               CustomLocation targetLocation = this.playerData.getLastPlayerPacket(target.getUniqueId(), MathUtil.pingFormula(this.playerData.getPing()) + 2);
               if (targetLocation == null) {
                  return;
               }

               CustomLocation playerLocation = this.playerData.getLastMovePacket();
               PlayerData targetData = this.plugin.getPlayerDataManager().getPlayerData(target);
               if (targetData == null) {
                  return;
               }

               double range = Math.hypot(playerLocation.getX() - targetLocation.getX(), playerLocation.getZ() - targetLocation.getZ());
               if (range > 6.5D) {
                  return;
               }

               double threshold = 3.3D;
               if (!targetData.isSprinting() || MathUtil.getDistanceBetweenAngles(playerLocation.getYaw(), targetLocation.getYaw()) < 90.0D) {
                  threshold += 0.5D;
               }

               double vl = this.playerData.getCheckVl(this);
               if (range > threshold && ++vl >= 12.5D) {
                  if (this.alert(PlayerAlertEvent.AlertType.RELEASE, player, String.format("failed Range Check A. E %.2f. R %.3f. T %.2f. VL %.2f.", range - threshold + 3.0D, range, threshold, vl))) {
                     if (!this.playerData.isBanning() && vl >= this.plugin.getRangeVl()) {
                        this.ban(player, "Range Check A");
                     }
                  } else {
                     --vl;
                  }
               } else if (range >= 2.0D) {
                  vl -= 0.225D;
               }

               this.playerData.setCheckVl(vl, this);
               this.sameTick = true;
            }
         }
      } else if (packet instanceof PacketPlayInFlying) {
         this.sameTick = false;
      }

   }
}
