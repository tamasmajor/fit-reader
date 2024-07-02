package com.tamasmajor.fitreader.model.definition;

import com.tamasmajor.fitreader.model.converter.ByteValueConverter;
import com.tamasmajor.fitreader.model.converter.unit.CommonUnitConverter;
import com.tamasmajor.fitreader.model.converter.unit.Scaler;
import com.tamasmajor.fitreader.preprocessing.annotation.*;

@DataDefinition
@ValueConverter(converter = ByteValueConverter.class)
@MessageNumber(20)
public class Record {

    @FieldNumber(0)
    @UnitConverter(converter = CommonUnitConverter.class, method = "semicirclesToDegrees", sourceType = Integer.class)
    private Double latitude;

    @FieldNumber(1)
    @UnitConverter(converter = CommonUnitConverter.class, method = "semicirclesToDegrees", sourceType = Integer.class)
    private Double longitude;

    @FieldNumber(2)
    @UnitConverter(converter = CommonUnitConverter.class, method = "fromFitAltitude", sourceType = Integer.class)
    private Double altitude;

    @FieldNumber(3)
    private Integer heartRate;

    @FieldNumber(4)
    private Integer cadence;

    @FieldNumber(5)
    @UnitConverter(converter = Scaler.class, method = "divideBy100", sourceType = Integer.class)
    private Double distance;

    @FieldNumber(6)
    private Integer speed;

    @FieldNumber(7)
    private Integer power;

    @FieldNumber(41)
    @UnitConverter(converter = Scaler.class, method = "divideBy10", sourceType = Integer.class)
    private Double groundContactTime;

    @FieldNumber(253)
    private Long timestamp;

}
