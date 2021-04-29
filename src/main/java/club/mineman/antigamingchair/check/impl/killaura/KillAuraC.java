package club.mineman.antigamingchair.check.impl.killaura;

import club.mineman.antigamingchair.AntiGamingChair;
import club.mineman.antigamingchair.check.checks.PacketCheck;
import club.mineman.antigamingchair.data.PlayerData;
import club.mineman.antigamingchair.event.player.PlayerAlertEvent;
import club.mineman.antigamingchair.location.CustomLocation;
import club.mineman.antigamingchair.util.MathUtil;
import java.util.UUID;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity.EnumEntityUseAction;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class KillAuraC extends PacketCheck {
   private UUID lastTarget;

   public KillAuraC(AntiGamingChair var1, PlayerData var2) {
      super(var1, var2);
   }

   public void handleCheck(Player var1, Packet var2) {
      if (var2 instanceof PacketPlayInFlying && !this.playerData.isAllowTeleport()) {
         PacketPlayInFlying var11 = (PacketPlayInFlying)var2;
         if (var11.h() && var11.g() && this.lastTarget != null) {
            CustomLocation var12 = this.playerData.getLastMovePacket();
            CustomLocation var13 = this.playerData.getLastPlayerPacket(this.lastTarget, MathUtil.pingFormula(this.playerData.getPing()) + 2);
            if (var13 == null) {
               return;
            }

            double var6 = MathUtil.getDistanceBetweenAngles(var12.getYaw(), MathUtil.getRotationFromPosition(this.playerData.getLastMovePacket(), var13)[1]);
            double var8 = MathUtil.getDistanceBetweenAngles(var12.getPitch(), MathUtil.getRotationFromPosition(this.playerData.getLastMovePacket(), var13)[0]);
            if ((var6 < 0.0D || var8 > 0.0D) && this.alert(PlayerAlertEvent.AlertType.RELEASE, var1, String.format("failed Kill Aura Check C. BY %.2f. BP %.2f.", var6, var8))) {
               int var10 = this.playerData.getViolations(this, 60000L);
               if (!this.playerData.isBanning() && var10 > 6) {
                  this.ban(var1, "Kill Aura Check C");
               }
            }
         }
      } else if (var2 instanceof PacketPlayInUseEntity) {
         PacketPlayInUseEntity var3 = (PacketPlayInUseEntity)var2;
         if (var3.a() == EnumEntityUseAction.ATTACK) {
            Entity var4 = var3.a(((CraftPlayer)var1).getHandle().getWorld());
            if (var4 instanceof EntityPlayer) {
               Player var5 = (Player)var4.getBukkitEntity();
               this.lastTarget = var5.getUniqueId();
            }
         }
      }

   }
}
