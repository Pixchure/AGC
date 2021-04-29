package club.mineman.antigamingchair.check.checks;

import club.mineman.antigamingchair.AntiGamingChair;
import club.mineman.antigamingchair.check.AbstractCheck;
import club.mineman.antigamingchair.data.PlayerData;
import club.mineman.paper.event.PlayerUpdateRotationEvent;

public abstract class RotationCheck extends AbstractCheck<PlayerUpdateRotationEvent> {
   public RotationCheck(AntiGamingChair plugin, PlayerData playerData) {
      super(plugin, playerData, PlayerUpdateRotationEvent.class);
   }
}
