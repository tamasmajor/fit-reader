package testcases;

import testcases.util.Converter;
import testcases.util.SimpleUnitConverter;
import com.tamasmajor.fitreader.preprocessing.annotation.DataDefinition;
import com.tamasmajor.fitreader.preprocessing.annotation.FieldNumber;
import com.tamasmajor.fitreader.preprocessing.annotation.MessageNumber;
import com.tamasmajor.fitreader.preprocessing.annotation.UnitConverter;
import com.tamasmajor.fitreader.preprocessing.annotation.ValueConverter;

@DataDefinition
@MessageNumber(20)
@ValueConverter(converter = Converter.class)
public class PropertyCustomConverter {

    @FieldNumber(2)
    @UnitConverter(converter = SimpleUnitConverter.class, method = "doubleIt", sourceType = Integer.class)
    private Long aProperty;

}


