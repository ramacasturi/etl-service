package com.myswiftly.etl.parsers;

import org.springframework.util.StringUtils;

public class FixedWidthFieldSpecification {

	private int _startIndex;
	private int _endIndex;
	private FixedWidthFieldType _fieldType;

	public FixedWidthFieldSpecification(int _startIndex, int _endIndex, FixedWidthFieldType _fieldType) {
		this._startIndex = _startIndex;
		this._endIndex = _endIndex;
		this._fieldType = _fieldType;
	}

	public Object parse(char[] line) {

		if (line == null || StringUtils.isEmpty(line)) {
			return null;
		}

		int lineLength = line.length;
		if (this._startIndex < 0 || this._startIndex > lineLength - 1 || this._endIndex < 0
				|| this._endIndex > lineLength - 1 || this._startIndex > this._endIndex) {
			// Ideally want to throw a checked exception giving most appropriate error
			// message
			throw new IllegalArgumentException("invalid input");
		}

		int startIndex = this._startIndex;
		int endIndex = this._endIndex;
		switch (this._fieldType) {
		case Number:
			while (startIndex < endIndex && line[startIndex] == '0') {
				startIndex++;
			}
			// TODO Ideally parse start<->end of char[] as integer
			// instead of converting to String and then Integer
			String value = new String(line, startIndex, (endIndex - startIndex + 1));
			return Integer.parseInt(value);
		case String:
			while (startIndex < endIndex && line[startIndex] == ' ') {
				startIndex++;
			}
			while (startIndex < endIndex && line[endIndex] == ' ') {
				endIndex--;
			}
			return new String(line, startIndex, (endIndex - startIndex + 1));
		case Currency:
			boolean negativeValue = false;
			if (line[startIndex] == '-') {
				negativeValue = true;
				startIndex++;
			}
			while (startIndex < endIndex && line[startIndex] == '0') {
				startIndex++;
			}
			// TODO Ideally parse start<->end of char[] as float
			// instead of converting to String and then Float
			String val = new String(line, startIndex, (endIndex - startIndex + 1));
			float floatVal = (Float.parseFloat(val) / 100f);
			return negativeValue ? (-1f) * (floatVal) : floatVal;
		case Flags:
			return new String(line, startIndex, (endIndex - startIndex + 1));
		}
		return null;
	}
}
