package club.mineman.antigamingchair.request;

import club.mineman.core.api.APIMessage;
import com.google.common.collect.ImmutableMap;
import java.beans.ConstructorProperties;
import java.util.Map;

public abstract class AGCRequest implements APIMessage {
   private final String subChannel;

   public String getChannel() {
      return "AGC";
   }

   public Map<String, Object> toMap() {
      return ImmutableMap.of("sub-channel", this.subChannel, "agc-key", "#EH3geeftrlfroT4J9yvgD)k#*h#ndxJ!8f");
   }

   @ConstructorProperties({"subChannel"})
   public AGCRequest(String subChannel) {
      this.subChannel = subChannel;
   }
}
