package club.mineman.antigamingchair.util.dummy;

import com.mojang.authlib.GameProfile;
import java.util.UUID;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.WorldServer;

public class DummyPlayer extends EntityPlayer {
   public DummyPlayer(Entity entity, String name) {
      super(MinecraftServer.getServer(), (WorldServer)entity.getWorld(), new GameProfile(UUID.randomUUID(), name), new DummyPlayerInteractManager(entity.getWorld()));
   }
}
