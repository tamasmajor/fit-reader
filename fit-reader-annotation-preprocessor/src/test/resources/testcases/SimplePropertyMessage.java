package testcases;

import java.lang.Integer;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class SimplePropertyMessage {
    private final Integer aProperty;

    public static SimplePropertyMessage of(Map<Integer, byte[]> values) {
        SimplePropertyMessageBuilder builder = SimplePropertyMessage.builder();
        values.forEach((key, value) -> {
            if (key == 2) {
                builder.aProperty(testcases.util.Converter.convert(value, java.lang.Integer.class));
            }
        });
        return builder.build();
    }
}
