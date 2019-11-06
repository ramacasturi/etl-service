package com.myswiftly.etl.parsers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class FixedWidthFieldSpecificationTest {

	@ParameterizedTest
	@MethodSource("argumentsProvider")
	public void testParseField(String str, int startIdx, int endIdx, FixedWidthFieldType fieldType, Object result,
			boolean expectException) {
		try {
			FixedWidthFieldSpecification spec = new FixedWidthFieldSpecification(startIdx, endIdx, fieldType);
			Object value = spec.parse(str.toCharArray());
			assertEquals(result, value);
		} catch (Exception e) {
			if (!expectException) {
				fail("Unexpected exception");
			}
		}
	}

	static Stream<Arguments> argumentsProvider() {
		return Stream.of(Arguments.of("HelloWorldToAll", -10, 9, FixedWidthFieldType.String, "World", true),
				Arguments.of("HelloWorldToAll", 50, 9, FixedWidthFieldType.String, "World", true),
				Arguments.of("HelloWorldToAll", 5, -10, FixedWidthFieldType.String, "World", true),
				Arguments.of("HelloWorldToAll", 5, 50, FixedWidthFieldType.String, "World", true),
				Arguments.of("HelloWorldToAll", 50, 9, FixedWidthFieldType.String, "World", true),
				Arguments.of("", 0, 0, FixedWidthFieldType.String, null, true),
				Arguments.of("Hello 1234 World", 0, 4, FixedWidthFieldType.Number, 1234, true),
				Arguments.of("Hello 000349 World", 0, 5, FixedWidthFieldType.Currency, 3.49f, true),

				Arguments.of("HelloWorldToAll", 0, 14, FixedWidthFieldType.String, "HelloWorldToAll", false),
				Arguments.of("HelloWorldToAll", 0, 0, FixedWidthFieldType.String, "H", false),
				Arguments.of("HelloWorldToAll", 5, 9, FixedWidthFieldType.String, "World", false),

				Arguments.of("Hello " + Integer.MIN_VALUE + " World", 6,
						6 + new Integer(Integer.MIN_VALUE).toString().length() - 1, FixedWidthFieldType.Number,
						Integer.MIN_VALUE, false),
				Arguments.of("Hello " + Integer.MAX_VALUE + " World", 6,
						6 + new Integer(Integer.MAX_VALUE).toString().length() - 1, FixedWidthFieldType.Number,
						Integer.MAX_VALUE, false),
				Arguments.of("Hello 1234 World", 6, 9, FixedWidthFieldType.Number, 1234, false),

				Arguments.of("Hello 000349 World", 6, 11, FixedWidthFieldType.Currency, 3.49f, false),
				Arguments.of("Hello -00349 World", 6, 11, FixedWidthFieldType.Currency, -3.49f, false));
	}
}
