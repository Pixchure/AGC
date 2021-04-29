package club.mineman.antigamingchair.event.player;

import java.beans.ConstructorProperties;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerAlertEvent extends Event implements Cancellable {
   private static final HandlerList HANDLER_LIST = new HandlerList();
   private final PlayerAlertEvent.AlertType alertType;
   private final Player player;
   private final String alert;
   private boolean cancelled;

   public static HandlerList getHandlerList() {
      return HANDLER_LIST;
   }

   public HandlerList getHandlers() {
      return HANDLER_LIST;
   }

   public void setCancelled(boolean cancelled) {
      this.cancelled = cancelled;
   }

   public PlayerAlertEvent.AlertType getAlertType() {
      return this.alertType;
   }

   public Player getPlayer() {
      return this.player;
   }

   public String getAlert() {
      return this.alert;
   }

   public boolean isCancelled() {
      return this.cancelled;
   }

   @ConstructorProperties({"alertType", "player", "alert"})
   public PlayerAlertEvent(PlayerAlertEvent.AlertType alertType, Player player, String alert) {
      this.alertType = alertType;
      this.player = player;
      this.alert = alert;
   }

   public static enum AlertType {
      RELEASE,
      EXPERIMENTAL,
      DEVELOPMENT;
   }
}
