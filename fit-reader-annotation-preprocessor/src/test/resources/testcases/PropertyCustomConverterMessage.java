package testcases;

import java.lang.Integer;
import java.lang.Long;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class PropertyCustomConverterMessage {
    private final Long aProperty;

    public static PropertyCustomConverterMessage of(Map<Integer, byte[]> values) {
        PropertyCustomConverterMessageBuilder builder = PropertyCustomConverterMessage.builder();
        values.forEach((key, value) -> {
            if (key == 2) {
                builder.aProperty(testcases.util.SimpleUnitConverter.doubleIt(testcases.util.Converter.convert(value, java.lang.Integer.class)));
            }
        });
        return builder.build();
    }
}
