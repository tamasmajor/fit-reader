package testcases;

import testcases.util.Converter;
import com.tamasmajor.fitreader.preprocessing.annotation.DataDefinition;
import com.tamasmajor.fitreader.preprocessing.annotation.MessageNumber;
import com.tamasmajor.fitreader.preprocessing.annotation.ValueConverter;

@DataDefinition
@MessageNumber(20)
@ValueConverter(converter = Converter.class)
public class SimpleNoProperties {
}
