package club.mineman.antigamingchair.check.checks;

import club.mineman.antigamingchair.AntiGamingChair;
import club.mineman.antigamingchair.check.AbstractCheck;
import club.mineman.antigamingchair.data.PlayerData;
import club.mineman.paper.event.PlayerUpdatePositionEvent;

public abstract class PositionCheck extends AbstractCheck<PlayerUpdatePositionEvent> {
   public PositionCheck(AntiGamingChair plugin, PlayerData playerData) {
      super(plugin, playerData, PlayerUpdatePositionEvent.class);
   }
}
