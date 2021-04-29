package club.mineman.antigamingchair.request.banwave;

import club.mineman.antigamingchair.request.AGCRequest;
import com.google.common.collect.ImmutableMap.Builder;
import java.util.Map;

public class AGCAddBanWaveRequest extends AGCRequest {
   private final int id;
   private final String reason;

   public AGCAddBanWaveRequest(int id, String reason) {
      super("add-ban-wave");
      this.id = id;
      this.reason = reason;
   }

   public Map<String, Object> toMap() {
      return (new Builder()).put("id", this.id).put("reason", this.reason).putAll(super.toMap()).build();
   }
}
