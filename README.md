[![FIT reader CI](https://github.com/tamasmajor/fit-reader/actions/workflows/ci.yml/badge.svg)](https://github.com/tamasmajor/fit-reader/actions/workflows/ci.yml)

# Garmin FIT File Parser

This is a project driven by personal interest, specifically aimed at parsing selected properties from Garmin FIT files. 
Currently, it primarily parses the properties of the Record message type that meets my requirements. While future 
extensions are planned, it isn't aimed to encompass the entirety of the FIT specification.

The repository is compartmentalized into four main modules 
- `fit-file-reader`
- `fit-model`
- `fit-reader-annotation-preprocessor`
- `fit-test-app`

## fit-file-reader
The `fit-file-reader` module stands at the centerpiece of the project, managing the reading and parsing of Garmin FIT files. 
It operates closely with the `fit-model` module to recognize and manipulate Garmin message types.

## fit-model
The `fit-model` module bears the responsibility of outlining the model for various Garmin message types read by 
the `fit-file-reader`. It incorporates custom annotations for defining custom converters and utilizes
the `fit-reader-annotation-preprocessor` for constructing enriched Java classes at compile time.

## fit-reader-annotation-preprocessor
Working in tandem with the `fit-model`, the `fit-reader-annotation-preprocessor` module examines the `fit-model` 
for custom annotations and procedurally generates enriched model objects incorporated with custom converters during 
compile time. This process essentially reduces the reliance on conditional statements, such as multiple if/else conditions, 
fulfilling one of the primary objectives of the project. Fundamentally, this module uses the properties from the annotated 
model to transform the read byte array into the respective type. It then employs a generated builder to assign these properties 
into an immutable object.


Model:
```java
@DataDefinition
// Converter to be used to do the byte[] -> type mapping
@ValueConverter(converter = ByteValueConverter.class)
// Defines the global message number
@MessageNumber(20) 
public class Record {
    
    // Describes the field for the given message type
    @FieldNumber(3)
    private Integer heartRate;

    // Describes the field for the given message type
    @FieldNumber(5)
    // If custom conversion is required
    // sourceType defines that the FIT file represents it as an int => byte[] will be converted to int
    // after that the divideBy100 method will be executed (result will be stored as a double in the model)
    @UnitConverter(converter = Scaler.class, method = "divideBy100", sourceType = Integer.class)
    private Double distance;
}
```

**Generated** by the annotation preprocessor (imports are simplified in the snippet)
```java
@Getter
@Builder
@ToString
public class RecordMessage {

    private final Integer heartRate;

    private final Double distance;
    
    public static RecordMessage of(Map<Integer, byte[]> values) {
        RecordMessageBuilder builder = RecordMessage.builder();
        values.forEach((key, value) -> {
        	if (key == 3) {
        		builder.heartRate(ByteValueConverter.convert(value, Integer.class));
        	}
        	if (key == 5) {
        		builder.distance(Scaler.divideBy100(ByteValueConverter.convert(value, Integer.class)));
        	}
        });
        return builder.build();
    }
}
```

## fit-test-app
Serving as a practical implementation of the overall system, the `fit-test-app` module is a basic standalone application. 
It demonstrates the interaction and functionality of the other three modules, providing a glimpse into the comprehensive 
reading, parsing, and processing stages in a real-world application. It displays the parsed example FIT file 
in both JSON and CSV formats. It's noteworthy, however, that the demonstration uses a Garmin example, 
which may not fully replicate real-world use cases. For instance, the heart rate in the example varies 
between 0 and 250, including significantly low values that may not be typically encountered in actual scenarios.