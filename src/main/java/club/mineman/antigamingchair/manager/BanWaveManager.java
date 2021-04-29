package club.mineman.antigamingchair.manager;

import club.mineman.antigamingchair.AntiGamingChair;
import club.mineman.antigamingchair.request.banwave.AGCGetBanWaveRequest;
import club.mineman.core.CorePlugin;
import club.mineman.core.api.request.AbstractCallback;
import java.beans.ConstructorProperties;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class BanWaveManager {
   private final AntiGamingChair plugin;
   private final Deque<Long> cheaters = new LinkedList();
   private final Map<Long, String> cheaterNames = new HashMap();
   private boolean banWaveStarted;

   public void loadCheaters() {
      CorePlugin.getInstance().getRequestManager().sendRequest(new AGCGetBanWaveRequest(), new AbstractCallback("Error fetching the ban wave list.") {
         public void callback(JSONObject data) {
            JSONArray array = (JSONArray)data.get("data");
            List<Long> cheaters = new LinkedList();
            Iterator var4 = array.iterator();

            while(var4.hasNext()) {
               Object object = var4.next();
               JSONObject jsonObject = (JSONObject)object;
               long id = (Long)jsonObject.get("id");
               String name = (String)jsonObject.get("name");
               cheaters.add(id);
               BanWaveManager.this.cheaterNames.put(id, name);
            }

            cheaters.sort((integer, t1) -> {
               String name = (String)BanWaveManager.this.cheaterNames.get(integer);
               String otherName = (String)BanWaveManager.this.cheaterNames.get(t1);
               return name.compareToIgnoreCase(otherName);
            });
            BanWaveManager.this.cheaters.addAll(cheaters);
         }
      });
   }

   public void clearCheaters() {
      this.cheaters.clear();
      this.cheaterNames.clear();
   }

   public long queueCheater() {
      return (Long)this.cheaters.poll();
   }

   public String getCheaterName(long id) {
      return (String)this.cheaterNames.get(id);
   }

   public AntiGamingChair getPlugin() {
      return this.plugin;
   }

   public Deque<Long> getCheaters() {
      return this.cheaters;
   }

   public Map<Long, String> getCheaterNames() {
      return this.cheaterNames;
   }

   public boolean isBanWaveStarted() {
      return this.banWaveStarted;
   }

   @ConstructorProperties({"plugin"})
   public BanWaveManager(AntiGamingChair plugin) {
      this.plugin = plugin;
   }

   public void setBanWaveStarted(boolean banWaveStarted) {
      this.banWaveStarted = banWaveStarted;
   }
}
