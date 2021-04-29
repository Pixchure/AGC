package club.mineman.antigamingchair.request;

import com.google.common.collect.ImmutableMap.Builder;
import java.util.Map;
import org.json.simple.JSONArray;

public class AGCLogRequest extends AGCRequest {
   private final JSONArray data;

   public AGCLogRequest(JSONArray data) {
      super("insert-logs");
      this.data = data;
   }

   public Map<String, Object> toMap() {
      return (new Builder()).put("data", this.data.toJSONString()).putAll(super.toMap()).build();
   }
}
