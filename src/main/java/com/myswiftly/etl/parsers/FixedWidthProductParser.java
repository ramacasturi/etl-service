package com.myswiftly.etl.parsers;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import com.myswiftly.etl.models.Product;
import com.myswiftly.etl.models.UnitOfMeasure;
import com.myswiftly.etl.util.Pair;

public class FixedWidthProductParser {

	public static final int LINE_WIDTH = 142;

	public static final FixedWidthFieldSpecification ProductId = new FixedWidthFieldSpecification(0, 7,
			FixedWidthFieldType.Number);
	public static final FixedWidthFieldSpecification ProductDescription = new FixedWidthFieldSpecification(9, 67,
			FixedWidthFieldType.String);
	public static final FixedWidthFieldSpecification RegularSingularPrice = new FixedWidthFieldSpecification(69, 76,
			FixedWidthFieldType.Currency);
	public static final FixedWidthFieldSpecification PromotionalSingularPrice = new FixedWidthFieldSpecification(78, 85,
			FixedWidthFieldType.Currency);
	public static final FixedWidthFieldSpecification RegularSplitPrice = new FixedWidthFieldSpecification(87, 94,
			FixedWidthFieldType.Currency);
	public static final FixedWidthFieldSpecification PromitionalSplitPrice = new FixedWidthFieldSpecification(96, 103,
			FixedWidthFieldType.Currency);
	public static final FixedWidthFieldSpecification RegularForX = new FixedWidthFieldSpecification(105, 112,
			FixedWidthFieldType.Number);
	public static final FixedWidthFieldSpecification PromitionalForX = new FixedWidthFieldSpecification(114, 121,
			FixedWidthFieldType.Number);
	public static final FixedWidthFieldSpecification Flags = new FixedWidthFieldSpecification(123, 131,
			FixedWidthFieldType.String);
	public static final FixedWidthFieldSpecification ProductSize = new FixedWidthFieldSpecification(133, 141,
			FixedWidthFieldType.String);

	public static final String ForEachSplitPriceFormat = "%d for %s";
	public static final String PerPoundSingularPriceFormat = "%s per lb";
	public static final String PerPoundSplitPriceFormat = "%s for %d lbs";

	public static final NumberFormat CurrencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "US"));
	public static final DecimalFormat DecimalFormat = new DecimalFormat("#.####");

	static {
		DecimalFormat.setRoundingMode(RoundingMode.HALF_DOWN);
	}

	public static Product parse(char[] line) {
		int productId = (int) ProductId.parse(line);
		String description = (String) ProductDescription.parse(line);
		float regularSingularPrice = (float) RegularSingularPrice.parse((line));
		float promotionalSingularPrice = (float) PromotionalSingularPrice.parse((line));
		float regularSplitPrice = (float) RegularSplitPrice.parse((line));
		float promotionalSplitPrice = (float) PromitionalSplitPrice.parse((line));
		int regularForX = (int) RegularForX.parse((line));
		int promotionalForX = (int) PromitionalForX.parse((line));
		String flags = (String) Flags.parse((line));
		String productSize = (String) ProductSize.parse((line));

		UnitOfMeasure unitOfMeasure = UnitOfMeasure.ForEach;
		if (flags.charAt(2) == 'Y') {
			unitOfMeasure = UnitOfMeasure.PerPound;
		}

		Pair<String, Float> regularPrices = CalculatePricing(regularSingularPrice, regularSplitPrice, regularForX,
				unitOfMeasure);
		Pair<String, Float> promotionalPrices = CalculatePricing(promotionalSingularPrice, promotionalSplitPrice,
				promotionalForX, unitOfMeasure);

		float taxRate = 0.0f;
		if (flags.charAt(4) == 'Y') {
			taxRate = 7.775f;
		}

		return new Product(productId, description, regularPrices.first, (float) regularPrices.second,
				promotionalPrices.first, (float) promotionalPrices.second, unitOfMeasure, productSize, taxRate);
	}

	private static Pair<String, Float> CalculatePricing(float singularPrice, float splitPrice, int forX,
			UnitOfMeasure unitOfMeasure) {
		float calculatorPrice = 0f;
		if (splitPrice != 0f) {
			float price = splitPrice / (float) forX;
			calculatorPrice = (float) Float.parseFloat(DecimalFormat.format(price));
		} else {
			calculatorPrice = (float) Float.parseFloat(DecimalFormat.format(singularPrice));
		}

		String displayPrice;
		if (unitOfMeasure == UnitOfMeasure.ForEach) {
			if (splitPrice != 0f) {
				displayPrice = String.format(ForEachSplitPriceFormat, forX, CurrencyFormat.format(splitPrice));
			} else {
				displayPrice = CurrencyFormat.format(singularPrice);
			}
		} else {
			if (splitPrice != 0f) {
				displayPrice = String.format(PerPoundSplitPriceFormat, CurrencyFormat.format(splitPrice), forX);
			} else {
				displayPrice = String.format(PerPoundSingularPriceFormat, CurrencyFormat.format(singularPrice));
			}
		}

		return new Pair<String, Float>(displayPrice, calculatorPrice);
	}
}