package testcases;

import java.lang.Integer;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class SimpleNoPropertiesMessage {
    public static SimpleNoPropertiesMessage of(Map<Integer, byte[]> values) {
        SimpleNoPropertiesMessageBuilder builder = SimpleNoPropertiesMessage.builder();
        values.forEach((key, value) -> {
        });
        return builder.build();
    }
}
