package com.alibaba.fastjson2.schema;

import java.math.BigDecimal;
import java.math.BigInteger;

public class ConstSchema
        extends JSONSchema {

    static final BigInteger BIGINT_INT64_MIN = BigInteger.valueOf(Long.MIN_VALUE);
    static final BigInteger BIGINT_INT64_MAX = BigInteger.valueOf(Long.MAX_VALUE);

    static final BigInteger BIGINT_INT32_MIN = BigInteger.valueOf(Integer.MIN_VALUE);
    static final BigInteger BIGINT_INT32_MAX = BigInteger.valueOf(Integer.MAX_VALUE);
    Object value;

    ConstSchema(Object value) {
        super(null, null);
        if (value instanceof BigDecimal) {
            BigDecimal decimal = ((BigDecimal) value).stripTrailingZeros();
            if (decimal.scale() == 0) {
                BigInteger bigInt = decimal.toBigInteger();
                if (bigInt.compareTo(BIGINT_INT32_MIN) >= 0 && bigInt.compareTo(BIGINT_INT32_MAX) <= 0) {
                    value = bigInt.intValue();
                } else if (bigInt.compareTo(BIGINT_INT64_MIN) >= 0 && bigInt.compareTo(BIGINT_INT64_MAX) <= 0) {
                    value = bigInt.longValue();
                } else {
                    value = bigInt;
                }
            } else {
                value = decimal;
            }
        }
        this.value = value;
    }

    @Override
    public Type getType() {
        return Type.Const;
    }

    @Override
    public ValidateResult validate(Object value) {
        if (this.value == null) {
            if (value == null) {
                return SUCCESS;
            }
            return new ValidateResult(false, "expect value null, but %s",  value);
        }

        if (value == null) {
            return FAIL_INPUT_NULL;
        }

        if (value instanceof BigDecimal) {
            BigDecimal decimal = (BigDecimal) value;
            value = decimal.stripTrailingZeros();

            long longValue = decimal.longValue();
            if (decimal.compareTo(BigDecimal.valueOf(longValue)) == 0) {
                value = longValue;
            } else if (decimal.scale() == 0) {
                value = decimal.unscaledValue();
            }
        } else if (value instanceof BigInteger) {
            BigInteger bigInt = (BigInteger) value;
            if (bigInt.compareTo(BIGINT_INT64_MIN) >= 0 && bigInt.compareTo(BIGINT_INT64_MAX) <= 0) {
                value = bigInt.longValue();
            }
        }

        if (value instanceof Long) {
            long longValue = (long) value;
            if (longValue >= Integer.MIN_VALUE && longValue <= Integer.MAX_VALUE) {
                value = (int) longValue;
            }
        }

        if (!this.value.equals(value)) {
            return new ValidateResult(false, "expect value %s, but %s", this.value, value);
        }

        return SUCCESS;
    }
}
