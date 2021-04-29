package club.mineman.antigamingchair.util;

import club.mineman.antigamingchair.location.CustomLocation;
import java.util.Iterator;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class MathUtil {
   public static CustomLocation getLocationInFrontOfPlayer(Player player, double distance) {
      return new CustomLocation(0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
   }

   public static CustomLocation getLocationInFrontOfLocation(double x, double y, double z, float yaw, float pitch, double distance) {
      return new CustomLocation(0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
   }

   public static boolean isMouseOverEntity(Player player) {
      return rayTrace(player, 6.0D) != null;
   }

   public static Entity rayTrace(Player player, double distance) {
      CustomLocation playerLocation = CustomLocation.fromBukkitLocation(player.getLocation());
      Entity currentTarget = null;
      float lowestFov = Float.MAX_VALUE;
      Iterator var6 = player.getNearbyEntities(distance, distance, distance).iterator();

      while(var6.hasNext()) {
         Entity entity = (Entity)var6.next();
         CustomLocation entityLocation = CustomLocation.fromBukkitLocation(entity.getLocation());
         float fov = getRotationFromPosition(playerLocation, entityLocation)[0] - playerLocation.getYaw();
         double groundDistance = playerLocation.getGroundDistanceTo(entityLocation);
         if (lowestFov < fov && (double)fov < groundDistance + 2.0D) {
            currentTarget = entity;
            lowestFov = fov;
         }
      }

      return currentTarget;
   }

   public static float[] getRotationFromPosition(CustomLocation param0, CustomLocation param1) {
      // $FF: Couldn't be decompiled
   }

   public static double getDistanceBetweenAngles(float angle1, float angle2) {
      float distance = Math.abs(angle1 - angle2) % 360.0F;
      if (distance > 180.0F) {
         distance = 360.0F - distance;
      }

      return (double)distance;
   }

   public static int pingFormula(long ping) {
      return (int)Math.ceil((double)ping / 50.0D);
   }
}
