package club.mineman.antigamingchair.check.impl.badpackets;

import club.mineman.antigamingchair.AntiGamingChair;
import club.mineman.antigamingchair.check.checks.PacketCheck;
import club.mineman.antigamingchair.data.PlayerData;
import club.mineman.antigamingchair.event.player.PlayerAlertEvent;
import club.mineman.antigamingchair.location.CustomLocation;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying.PacketPlayInPosition;
import org.bukkit.entity.Player;

public class BadPacketsB extends PacketCheck {
   private CustomLocation lastPosition;

   public BadPacketsB(AntiGamingChair var1, PlayerData var2) {
      super(var1, var2);
   }

   public void handleCheck(Player var1, Packet var2) {
      if (var2 instanceof PacketPlayInPosition) {
         if (this.lastPosition != null) {
            CustomLocation var3 = this.playerData.getLastMovePacket();
            if (this.lastPosition.getX() == var3.getX() && this.lastPosition.getY() == var3.getY() && this.lastPosition.getZ() == var3.getZ()) {
               long var4 = var3.getTimestamp() - this.lastPosition.getTimestamp();
               long var6 = Math.abs(50L - var4);
               if (var6 < 4L) {
                  int var8 = this.playerData.getViolations(this, 60000L);
                  if (!this.playerData.isBanWave() && var8 > 5) {
                     this.banWave(var1, "Bad Packets Check F");
                  } else if (!this.playerData.isBanWave()) {
                     this.alert(PlayerAlertEvent.AlertType.RELEASE, var1, "failed Bad Packets Check B.");
                  }
               }
            }

            this.lastPosition = this.playerData.getLastMovePacket();
         } else {
            this.lastPosition = this.playerData.getLastMovePacket();
         }
      }

   }
}
