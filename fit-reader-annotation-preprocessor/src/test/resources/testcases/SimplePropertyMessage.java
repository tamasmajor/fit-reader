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
        return builder.build();
    }
}
