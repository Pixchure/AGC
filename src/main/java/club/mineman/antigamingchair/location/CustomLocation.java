package club.mineman.antigamingchair.location;

import club.mineman.core.CorePlugin;
import java.beans.ConstructorProperties;
import org.bukkit.Location;
import org.bukkit.World;

public class CustomLocation {
   private final long timestamp = System.currentTimeMillis();
   private double x;
   private double y;
   private double z;
   private float yaw;
   private float pitch;

   public static CustomLocation fromBukkitLocation(Location location) {
      return new CustomLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
   }

   public Location toBukkitLocation() {
      return new Location((World)CorePlugin.getInstance().getServer().getWorlds().get(0), this.x, this.y, this.z, this.yaw, this.pitch);
   }

   public double getGroundDistanceTo(CustomLocation location) {
      return Math.sqrt(Math.pow(this.x - location.x, 2.0D) + Math.pow(this.z - location.z, 2.0D));
   }

   public double getDistanceTo(CustomLocation location) {
      return Math.sqrt(Math.pow(this.x - location.x, 2.0D) + Math.pow(this.y - location.y, 2.0D) + Math.pow(this.z - location.z, 2.0D));
   }

   public long getTimestamp() {
      return this.timestamp;
   }

   public double getX() {
      return this.x;
   }

   public double getY() {
      return this.y;
   }

   public double getZ() {
      return this.z;
   }

   public float getYaw() {
      return this.yaw;
   }

   public float getPitch() {
      return this.pitch;
   }

   public void setX(double x) {
      this.x = x;
   }

   public void setY(double y) {
      this.y = y;
   }

   public void setZ(double z) {
      this.z = z;
   }

   public void setYaw(float yaw) {
      this.yaw = yaw;
   }

   public void setPitch(float pitch) {
      this.pitch = pitch;
   }

   @ConstructorProperties({"x", "y", "z", "yaw", "pitch"})
   public CustomLocation(double x, double y, double z, float yaw, float pitch) {
      this.x = x;
      this.y = y;
      this.z = z;
      this.yaw = yaw;
      this.pitch = pitch;
   }
}
