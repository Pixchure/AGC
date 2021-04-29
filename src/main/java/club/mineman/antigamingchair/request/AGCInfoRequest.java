package club.mineman.antigamingchair.request;

import com.google.common.collect.ImmutableMap.Builder;
import java.sql.Timestamp;
import java.util.Map;

public class AGCInfoRequest extends AGCRequest {
   private final String name;
   private final Timestamp time;

   public AGCInfoRequest(String name, Timestamp time) {
      super("get-info");
      this.name = name;
      this.time = time;
   }

   public Map<String, Object> toMap() {
      return (new Builder()).put("time", this.time).put("name", this.name).putAll(super.toMap()).build();
   }
}
