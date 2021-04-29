package club.mineman.antigamingchair.check.impl.entity;

import club.mineman.antigamingchair.AntiGamingChair;
import club.mineman.antigamingchair.check.checks.PacketCheck;
import club.mineman.antigamingchair.data.PlayerData;
import club.mineman.antigamingchair.util.dummy.DummyPlayer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntity;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity.EnumEntityUseAction;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntity.PacketPlayOutRelEntityMove;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PlayerGuard extends PacketCheck {
   private final Map<UUID, Set<Integer>> playerGuardMap = new HashMap();

   public PlayerGuard(AntiGamingChair plugin, PlayerData playerData) {
      super(plugin, playerData);
   }

   public void handleCheck(Player player, Packet packet) {
      if (packet instanceof PacketPlayInUseEntity) {
         PacketPlayInUseEntity useEntity = (PacketPlayInUseEntity)packet;
         if (useEntity.a() == EnumEntityUseAction.ATTACK) {
            Entity entity = useEntity.a(((CraftPlayer)player).getHandle().world);
            if (entity == null) {
               return;
            }

            if (entity instanceof EntityPlayer) {
            }
         }
      }

   }

   public void handleOutboundPacket(Player player, Packet packet) {
      if (!(packet instanceof PacketPlayOutRelEntityMove) && !(packet instanceof PacketPlayOutRelEntityMoveLook)) {
         if (packet instanceof PacketPlayOutNamedEntitySpawn) {
         }
      } else {
         Entity targetEntity = ((CraftPlayer)player).getHandle().getWorld().a(((PacketPlayOutEntity)packet).getA());
         if (targetEntity instanceof EntityPlayer) {
            Set<Integer> playerGuard = (Set)this.playerGuardMap.get(targetEntity.getUniqueID());
            if (playerGuard == null) {
               this.createPlayerGuard(player, targetEntity);
               return;
            }

            PacketPlayOutEntity movePacket = (PacketPlayOutEntity)packet;
            Iterator var6 = playerGuard.iterator();

            while(true) {
               while(var6.hasNext()) {
                  int id = (Integer)var6.next();
                  Iterator var8 = ((CraftPlayer)player).getHandle().getWorld().entityList.iterator();

                  while(var8.hasNext()) {
                     Entity entity = (Entity)var8.next();
                     if (entity.getBukkitEntity().getEntityId() == id) {
                        PacketPlayOutRelEntityMove move = new PacketPlayOutRelEntityMove(id, movePacket.getB(), movePacket.getC(), movePacket.getD(), false);
                        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(move);
                        break;
                     }
                  }
               }

               return;
            }
         }
      }

   }

   private void createPlayerGuard(Player player, Entity entity) {
      Set<Integer> playerGuard = new HashSet();

      for(int i = 0; i < 6; ++i) {
         DummyPlayer entityPlayer = new DummyPlayer(entity, "nigger_" + i);
         entityPlayer.setInvisible(true);
         PacketPlayOutNamedEntitySpawn spawn = new PacketPlayOutNamedEntitySpawn(entityPlayer);
         ((CraftPlayer)player).getHandle().playerConnection.sendPacket(spawn);
         playerGuard.add(entityPlayer.getId());
      }

      this.playerGuardMap.put(entity.getUniqueID(), playerGuard);
   }
}
