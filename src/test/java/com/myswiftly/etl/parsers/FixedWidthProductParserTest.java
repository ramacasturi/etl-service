package com.myswiftly.etl.parsers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.myswiftly.etl.models.Product;
import com.myswiftly.etl.models.UnitOfMeasure;

public class FixedWidthProductParserTest {

	@ParameterizedTest
	@MethodSource("argumentsProvider")
	public void testParse(String line, Product result, boolean expectException) {
		try {
			Product p = FixedWidthProductParser.parse(line.toCharArray());
			assertEquals(result, p);
		} catch (Exception e) {
			if (!expectException) {
				e.printStackTrace();
				fail("Unexpected exception");
			}
		}
	}

	static Stream<Arguments> argumentsProvider() {
		return Stream.of(Arguments.of(
				"80000001 Kimchi-flavored white rice                                  00000567 00000000 00000000 00000000 00000000 00000000 NNNNNNNNN      18oz",
				new Product(80000001, "Kimchi-flavored white rice", "$5.67", 5.67f, "$0.00", 0f, UnitOfMeasure.ForEach,
						"18oz", 0f),
				false),
				(Arguments.of(
						"14963801 Generic Soda 12-pack                                        00000000 00000549 00001300 00000000 00000003 00000000 NNNNYNNNN   12x12oz",
						new Product(14963801, "Generic Soda 12-pack", "3 for $13.00", 4.3333f, "$5.49", 5.49f,
								UnitOfMeasure.ForEach, "12x12oz", 7.775f),
						false)),
				(Arguments.of(
						"50133333 Fuji Apples (Organic)                                       00001349 00000000 00000000 00000000 00000000 00000000 NNYNNNNNN        lb",
						new Product(50133333, "Fuji Apples (Organic)", "$13.49 per lb", 13.49f, "$0.00 per lb", 0f,
								UnitOfMeasure.PerPound, "lb", 0f),
						false)),
				(Arguments.of(
						"50133333 Fuji Apples (Organic)                                       00000000 00000000 00001350 00000000 00000002 00000000 NNYNNNNNN        lb",
						new Product(50133333, "Fuji Apples (Organic)", "$13.50 for 2 lbs", 6.75f, "$0.00 per lb", 0f,
								UnitOfMeasure.PerPound, "lb", 0f),
						false)));
	}
}
