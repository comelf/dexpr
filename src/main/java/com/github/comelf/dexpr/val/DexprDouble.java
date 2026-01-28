package com.github.comelf.dexpr.val;


import com.github.comelf.dexpr.DexprType;

public class DexprDouble extends DexprNumber {

    public static final DexprDouble ZERO = new DexprDouble(0);
    public static final DexprDouble PI = new DexprDouble(Math.PI);
    public static final DexprDouble E = new DexprDouble(Math.E);

    public final double value;

    public DexprDouble(double value) {
        super(DexprType.DOUBLE);
        this.value = value;
    }

    public int intValue() {
        return (int) value;
    }

    public double doubleValue() {
        return value;
    }

    public String toString() {
        if ((value == Math.floor(value)) && !Double.isInfinite(value)) {
            return Long.toString((long) value);
        }
        return Double.toString(value);
    }

    public int hashCode() {
        return (int) value * 100;
    }

    public boolean equals(Object obj) {
        return obj instanceof DexprDouble &&
                Math.abs(value - ((DexprDouble) obj).value) < 1.0e-10;
    }

    @Override
    public boolean isExpectable(DexprType type) {
        return DexprType.DOUBLE.equals(type);
    }

}
