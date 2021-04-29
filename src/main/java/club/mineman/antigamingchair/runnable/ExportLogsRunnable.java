package club.mineman.antigamingchair.runnable;

import club.mineman.antigamingchair.AntiGamingChair;
import club.mineman.antigamingchair.log.Log;
import club.mineman.antigamingchair.request.AGCLogRequest;
import club.mineman.core.CorePlugin;
import club.mineman.core.api.request.RequestCallback;
import club.mineman.core.util.finalutil.CC;
import java.beans.ConstructorProperties;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ExportLogsRunnable implements Runnable {
   private final AntiGamingChair plugin;

   public void run() {
      if (!this.plugin.getLogManager().getLogQueue().isEmpty()) {
         Set<JSONArray> data = new HashSet();
         JSONArray current = new JSONArray();
         Iterator var3 = this.plugin.getLogManager().getLogQueue().iterator();

         while(var3.hasNext()) {
            Log log = (Log)var3.next();
            JSONObject object = new JSONObject();
            object.put("timestamp", (new Timestamp(log.getTimestamp())).toString());
            object.put("player-id", log.getMinemanId());
            object.put("log", log.getLog());
            current.add(object.toJSONString());
            if (current.toJSONString().length() >= 1000) {
               data.add(current);
               current = new JSONArray();
            }
         }

         if (current.size() > 0) {
            data.add(current);
         }

         var3 = data.iterator();

         while(var3.hasNext()) {
            JSONArray array = (JSONArray)var3.next();
            CorePlugin.getInstance().getRequestManager().sendRequest(new AGCLogRequest(array), new RequestCallback() {
               public void callback(JSONObject data) {
                  String response = (String)data.get("response");
                  if (!response.equals("success")) {
                     ExportLogsRunnable.this.onError(data.toJSONString());
                  }

               }

               public void error(String message) {
                  ExportLogsRunnable.this.onError(message);
               }
            });
         }

         this.plugin.getLogManager().clearLogQueue();
      }
   }

   private void onError(String message) {
      this.plugin.getAlertsManager().forceAlert(CC.D_RED + "ERROR SAVING LOGS! Check console for details.");
      this.plugin.getLogger().severe(message);
   }

   @ConstructorProperties({"plugin"})
   public ExportLogsRunnable(AntiGamingChair plugin) {
      this.plugin = plugin;
   }
}
