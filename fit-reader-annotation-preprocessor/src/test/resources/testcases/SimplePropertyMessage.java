package testcases;

import java.lang.Integer;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class SimplePropertyMessage {
    private final Integer aProperty;
}
