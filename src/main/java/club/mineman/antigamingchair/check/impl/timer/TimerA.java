package club.mineman.antigamingchair.check.impl.timer;

import club.mineman.antigamingchair.AntiGamingChair;
import club.mineman.antigamingchair.check.checks.PacketCheck;
import club.mineman.antigamingchair.data.PlayerData;
import club.mineman.antigamingchair.event.player.PlayerAlertEvent;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.entity.Player;

public class TimerA extends PacketCheck {
   private Deque<Long> delays = new LinkedList();
   private long lastPacketTime;

   public TimerA(AntiGamingChair plugin, PlayerData playerData) {
      super(plugin, playerData);
   }

   public void handleCheck(Player player, Packet packet) {
      if (packet instanceof PacketPlayInFlying && !this.playerData.isAllowTeleport() && System.currentTimeMillis() - this.playerData.getLastDelayedMovePacket() > 110L) {
         this.delays.add(System.currentTimeMillis() - this.lastPacketTime);
         if (this.delays.size() == 120) {
            double average = 0.0D;

            long l;
            for(Iterator var5 = this.delays.iterator(); var5.hasNext(); average += (double)l) {
               l = (Long)var5.next();
            }

            average /= (double)this.delays.size();
            double vl = this.playerData.getCheckVl(this);
            if (average <= 49.0D) {
               if (++vl > 4.0D && this.alert(PlayerAlertEvent.AlertType.EXPERIMENTAL, player, String.format("failed Timer Check A. AVG %.3f. VL %.2f.", average, vl)) && vl >= 20.0D) {
                  if (average >= 35.714285714285715D) {
                     if (!this.playerData.isBanWave()) {
                        this.banWave(player, "Timer Check A");
                     }
                  } else if (!this.playerData.isBanning()) {
                     this.ban(player, "Timer Check A");
                  }
               }
            } else {
               vl -= 0.5D;
            }

            this.playerData.setCheckVl(vl, this);
            this.delays.clear();
         }

         this.lastPacketTime = System.currentTimeMillis();
      }

   }
}
