package club.mineman.antigamingchair.check;

import org.bukkit.entity.Player;

public interface ICheck<T> {
   void handleCheck(Player var1, T var2);

   Class<? extends T> getType();
}
