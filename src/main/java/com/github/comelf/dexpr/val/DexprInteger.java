package com.github.comelf.dexpr.val;


import com.github.comelf.dexpr.DexprType;

public class DexprInteger extends DexprNumber {
    public static final DexprInteger ZERO = new DexprInteger(0);

    public final int value;

    public DexprInteger(int value) {
        super(DexprType.INTEGER);
        this.value = value;
    }

    public int intValue() {
        return value;
    }

    public double doubleValue() {
        return value;
    }

    public int hashCode() {
        return value;
    }

    public boolean equals(Object obj) {
        return obj instanceof DexprInteger && value == ((DexprInteger) obj).value;
    }

    public String toString() {
        return Integer.toString(value);
    }

    @Override
    public boolean isExpectable(DexprType type) {
        return DexprType.INTEGER.equals(type);
    }
}
