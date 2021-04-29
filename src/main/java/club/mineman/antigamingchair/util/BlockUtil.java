package club.mineman.antigamingchair.util;

import java.util.HashSet;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.World;

public class BlockUtil {
   private static Set<Byte> blockSolidPassSet = new HashSet();
   private static Set<Byte> blockStairsSet = new HashSet();
   private static Set<Byte> blockLiquidsSet = new HashSet();
   private static Set<Byte> blockWebsSet = new HashSet();
   private static Set<Byte> blockIceSet = new HashSet();

   public static boolean isOnGround(Location location, int down) {
      double posX = location.getX();
      double posZ = location.getZ();
      double fracX = posX % 1.0D > 0.0D ? Math.abs(posX % 1.0D) : 1.0D - Math.abs(posX % 1.0D);
      double fracZ = posZ % 1.0D > 0.0D ? Math.abs(posZ % 1.0D) : 1.0D - Math.abs(posZ % 1.0D);
      int blockX = location.getBlockX();
      int blockY = location.getBlockY() - down;
      int blockZ = location.getBlockZ();
      World world = location.getWorld();
      if (!blockSolidPassSet.contains((byte)world.getBlockAt(blockX, blockY, blockZ).getTypeId())) {
         return true;
      } else {
         if (fracX < 0.3D) {
            if (!blockSolidPassSet.contains((byte)world.getBlockAt(blockX - 1, blockY, blockZ).getTypeId())) {
               return true;
            }

            if (fracZ < 0.3D) {
               if (!blockSolidPassSet.contains((byte)world.getBlockAt(blockX - 1, blockY, blockZ - 1).getTypeId())) {
                  return true;
               }

               if (!blockSolidPassSet.contains((byte)world.getBlockAt(blockX, blockY, blockZ - 1).getTypeId())) {
                  return true;
               }

               if (!blockSolidPassSet.contains((byte)world.getBlockAt(blockX + 1, blockY, blockZ - 1).getTypeId())) {
                  return true;
               }
            } else if (fracZ > 0.7D) {
               if (!blockSolidPassSet.contains((byte)world.getBlockAt(blockX - 1, blockY, blockZ + 1).getTypeId())) {
                  return true;
               }

               if (!blockSolidPassSet.contains((byte)world.getBlockAt(blockX, blockY, blockZ + 1).getTypeId())) {
                  return true;
               }

               if (!blockSolidPassSet.contains((byte)world.getBlockAt(blockX + 1, blockY, blockZ + 1).getTypeId())) {
                  return true;
               }
            }
         } else if (fracX > 0.7D) {
            if (!blockSolidPassSet.contains((byte)world.getBlockAt(blockX + 1, blockY, blockZ).getTypeId())) {
               return true;
            }

            if (fracZ < 0.3D) {
               if (!blockSolidPassSet.contains((byte)world.getBlockAt(blockX - 1, blockY, blockZ - 1).getTypeId())) {
                  return true;
               }

               if (!blockSolidPassSet.contains((byte)world.getBlockAt(blockX, blockY, blockZ - 1).getTypeId())) {
                  return true;
               }

               if (!blockSolidPassSet.contains((byte)world.getBlockAt(blockX + 1, blockY, blockZ - 1).getTypeId())) {
                  return true;
               }
            } else if (fracZ > 0.7D) {
               if (!blockSolidPassSet.contains((byte)world.getBlockAt(blockX - 1, blockY, blockZ + 1).getTypeId())) {
                  return true;
               }

               if (!blockSolidPassSet.contains((byte)world.getBlockAt(blockX, blockY, blockZ + 1).getTypeId())) {
                  return true;
               }

               if (!blockSolidPassSet.contains((byte)world.getBlockAt(blockX + 1, blockY, blockZ + 1).getTypeId())) {
                  return true;
               }
            }
         } else if (fracZ < 0.3D) {
            if (!blockSolidPassSet.contains((byte)world.getBlockAt(blockX, blockY, blockZ - 1).getTypeId())) {
               return true;
            }
         } else if (fracZ > 0.7D && !blockSolidPassSet.contains((byte)world.getBlockAt(blockX, blockY, blockZ + 1).getTypeId())) {
            return true;
         }

         return false;
      }
   }

   public static boolean isOnStairs(Location location, int down) {
      return isUnderBlock(location, blockStairsSet, down);
   }

   private static boolean isUnderBlock(Location location, Set<Byte> itemIDs, int down) {
      double posX = location.getX();
      double posZ = location.getZ();
      double fracX = posX % 1.0D > 0.0D ? Math.abs(posX % 1.0D) : 1.0D - Math.abs(posX % 1.0D);
      double fracZ = posZ % 1.0D > 0.0D ? Math.abs(posZ % 1.0D) : 1.0D - Math.abs(posZ % 1.0D);
      int blockX = location.getBlockX();
      int blockY = location.getBlockY() - down;
      int blockZ = location.getBlockZ();
      World world = location.getWorld();
      if (itemIDs.contains((byte)world.getBlockAt(blockX, blockY, blockZ).getTypeId())) {
         return true;
      } else {
         if (fracX < 0.3D) {
            if (itemIDs.contains((byte)world.getBlockAt(blockX - 1, blockY, blockZ).getTypeId())) {
               return true;
            }

            if (fracZ < 0.3D) {
               if (itemIDs.contains((byte)world.getBlockAt(blockX - 1, blockY, blockZ - 1).getTypeId())) {
                  return true;
               }

               if (itemIDs.contains((byte)world.getBlockAt(blockX, blockY, blockZ - 1).getTypeId())) {
                  return true;
               }

               if (itemIDs.contains((byte)world.getBlockAt(blockX + 1, blockY, blockZ - 1).getTypeId())) {
                  return true;
               }
            } else if (fracZ > 0.7D) {
               if (itemIDs.contains((byte)world.getBlockAt(blockX - 1, blockY, blockZ + 1).getTypeId())) {
                  return true;
               }

               if (itemIDs.contains((byte)world.getBlockAt(blockX, blockY, blockZ + 1).getTypeId())) {
                  return true;
               }

               if (itemIDs.contains((byte)world.getBlockAt(blockX + 1, blockY, blockZ + 1).getTypeId())) {
                  return true;
               }
            }
         } else if (fracX > 0.7D) {
            if (itemIDs.contains((byte)world.getBlockAt(blockX + 1, blockY, blockZ).getTypeId())) {
               return true;
            }

            if (fracZ < 0.3D) {
               if (itemIDs.contains((byte)world.getBlockAt(blockX - 1, blockY, blockZ - 1).getTypeId())) {
                  return true;
               }

               if (itemIDs.contains((byte)world.getBlockAt(blockX, blockY, blockZ - 1).getTypeId())) {
                  return true;
               }

               if (itemIDs.contains((byte)world.getBlockAt(blockX + 1, blockY, blockZ - 1).getTypeId())) {
                  return true;
               }
            } else if (fracZ > 0.7D) {
               if (itemIDs.contains((byte)world.getBlockAt(blockX - 1, blockY, blockZ + 1).getTypeId())) {
                  return true;
               }

               if (itemIDs.contains((byte)world.getBlockAt(blockX, blockY, blockZ + 1).getTypeId())) {
                  return true;
               }

               if (itemIDs.contains((byte)world.getBlockAt(blockX + 1, blockY, blockZ + 1).getTypeId())) {
                  return true;
               }
            }
         } else if (fracZ < 0.3D) {
            if (itemIDs.contains((byte)world.getBlockAt(blockX, blockY, blockZ - 1).getTypeId())) {
               return true;
            }
         } else if (fracZ > 0.7D && itemIDs.contains((byte)world.getBlockAt(blockX, blockY, blockZ + 1).getTypeId())) {
            return true;
         }

         return false;
      }
   }

   public static boolean isOnLiquid(Location location, int down) {
      return isUnderBlock(location, blockLiquidsSet, down);
   }

   public static boolean isOnWeb(Location location, int down) {
      return isUnderBlock(location, blockWebsSet, down);
   }

   public static boolean isOnIce(Location location, int down) {
      return isUnderBlock(location, blockIceSet, down);
   }

   static {
      blockSolidPassSet.add((byte)0);
      blockSolidPassSet.add((byte)6);
      blockSolidPassSet.add((byte)8);
      blockSolidPassSet.add((byte)9);
      blockSolidPassSet.add((byte)10);
      blockSolidPassSet.add((byte)11);
      blockSolidPassSet.add((byte)27);
      blockSolidPassSet.add((byte)28);
      blockSolidPassSet.add((byte)30);
      blockSolidPassSet.add((byte)31);
      blockSolidPassSet.add((byte)32);
      blockSolidPassSet.add((byte)37);
      blockSolidPassSet.add((byte)38);
      blockSolidPassSet.add((byte)39);
      blockSolidPassSet.add((byte)40);
      blockSolidPassSet.add((byte)50);
      blockSolidPassSet.add((byte)51);
      blockSolidPassSet.add((byte)55);
      blockSolidPassSet.add((byte)59);
      blockSolidPassSet.add((byte)63);
      blockSolidPassSet.add((byte)66);
      blockSolidPassSet.add((byte)68);
      blockSolidPassSet.add((byte)69);
      blockSolidPassSet.add((byte)70);
      blockSolidPassSet.add((byte)72);
      blockSolidPassSet.add((byte)75);
      blockSolidPassSet.add((byte)76);
      blockSolidPassSet.add((byte)77);
      blockSolidPassSet.add((byte)78);
      blockSolidPassSet.add((byte)83);
      blockSolidPassSet.add((byte)90);
      blockSolidPassSet.add((byte)104);
      blockSolidPassSet.add((byte)105);
      blockSolidPassSet.add((byte)115);
      blockSolidPassSet.add((byte)119);
      blockSolidPassSet.add(-124);
      blockSolidPassSet.add(-113);
      blockSolidPassSet.add(-81);
      blockStairsSet.add((byte)53);
      blockStairsSet.add((byte)67);
      blockStairsSet.add((byte)108);
      blockStairsSet.add((byte)109);
      blockStairsSet.add((byte)114);
      blockStairsSet.add(-128);
      blockStairsSet.add(-122);
      blockStairsSet.add(-121);
      blockStairsSet.add(-120);
      blockStairsSet.add(-100);
      blockStairsSet.add(-93);
      blockStairsSet.add(-92);
      blockStairsSet.add((byte)126);
      blockStairsSet.add(-76);
      blockLiquidsSet.add((byte)8);
      blockLiquidsSet.add((byte)9);
      blockLiquidsSet.add((byte)10);
      blockLiquidsSet.add((byte)11);
      blockWebsSet.add((byte)30);
      blockIceSet.add((byte)79);
      blockIceSet.add(-82);
   }
}
