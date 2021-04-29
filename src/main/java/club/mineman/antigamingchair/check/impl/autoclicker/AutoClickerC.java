package club.mineman.antigamingchair.check.impl.autoclicker;

import club.mineman.antigamingchair.AntiGamingChair;
import club.mineman.antigamingchair.check.checks.PacketCheck;
import club.mineman.antigamingchair.data.PlayerData;
import club.mineman.antigamingchair.event.player.PlayerAlertEvent;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInArmAnimation;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.entity.Player;

public class AutoClickerC extends PacketCheck {
   private PlayerData playerData;
   private boolean failed;
   private boolean sent;

   public AutoClickerC(AntiGamingChair var1, PlayerData var2) {
      super(var1, var2);
      this.playerData = var2;
   }

   public void handleCheck(Player var1, Packet var2) {
      if (var2 instanceof PacketPlayInArmAnimation && System.currentTimeMillis() - this.playerData.getLastDelayedMovePacket() > 110L && System.currentTimeMillis() - this.playerData.getLastMovePacket().getTimestamp() < 110L && !this.playerData.isDigging() && !this.playerData.isPlacing()) {
         if (this.sent) {
            if (!this.failed) {
               int var3 = (int)this.playerData.getCheckVl(this);
               ++var3;
               if (var3 >= 5) {
                  var3 = 0;
                  this.alert(PlayerAlertEvent.AlertType.EXPERIMENTAL, var1, "failed Auto Clicker Check C (Experimental).");
               }

               this.playerData.setCheckVl((double)var3, this);
               this.failed = true;
            }
         } else {
            this.sent = true;
         }
      } else if (var2 instanceof PacketPlayInFlying) {
         this.failed = false;
         this.sent = false;
      }

   }
}
