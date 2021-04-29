package club.mineman.antigamingchair.event.player;

import java.beans.ConstructorProperties;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerBanWaveEvent extends Event implements Cancellable {
   private static final HandlerList HANDLER_LIST = new HandlerList();
   private final Player player;
   private final String reason;
   private boolean cancelled;

   public static HandlerList getHandlerList() {
      return HANDLER_LIST;
   }

   public HandlerList getHandlers() {
      return HANDLER_LIST;
   }

   public Player getPlayer() {
      return this.player;
   }

   public String getReason() {
      return this.reason;
   }

   public boolean isCancelled() {
      return this.cancelled;
   }

   public void setCancelled(boolean cancelled) {
      this.cancelled = cancelled;
   }

   @ConstructorProperties({"player", "reason"})
   public PlayerBanWaveEvent(Player player, String reason) {
      this.player = player;
      this.reason = reason;
   }
}
