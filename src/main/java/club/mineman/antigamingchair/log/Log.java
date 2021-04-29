package club.mineman.antigamingchair.log;

import java.beans.ConstructorProperties;

public class Log {
   private final long timestamp = System.currentTimeMillis();
   private final int minemanId;
   private final String log;

   public long getTimestamp() {
      return this.timestamp;
   }

   public int getMinemanId() {
      return this.minemanId;
   }

   public String getLog() {
      return this.log;
   }

   @ConstructorProperties({"minemanId", "log"})
   public Log(int minemanId, String log) {
      this.minemanId = minemanId;
      this.log = log;
   }
}
