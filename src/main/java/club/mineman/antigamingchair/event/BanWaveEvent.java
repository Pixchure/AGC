package club.mineman.antigamingchair.event;

import java.beans.ConstructorProperties;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BanWaveEvent extends Event {
   private static final HandlerList HANDLER_LIST = new HandlerList();
   private final String instigator;

   public static HandlerList getHandlerList() {
      return HANDLER_LIST;
   }

   public HandlerList getHandlers() {
      return HANDLER_LIST;
   }

   @ConstructorProperties({"instigator"})
   public BanWaveEvent(String instigator) {
      this.instigator = instigator;
   }

   public String getInstigator() {
      return this.instigator;
   }
}
