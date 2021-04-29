package club.mineman.antigamingchair.manager;

import club.mineman.antigamingchair.log.Log;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class LogManager {
   private final Queue<Log> logQueue = new ConcurrentLinkedQueue();

   public void addToLogQueue(Log log) {
      this.logQueue.add(log);
   }

   public void clearLogQueue() {
      this.logQueue.clear();
   }

   public Queue<Log> getLogQueue() {
      return this.logQueue;
   }
}
