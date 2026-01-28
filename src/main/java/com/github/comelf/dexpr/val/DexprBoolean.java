package com.github.comelf.dexpr.val;


import com.github.comelf.dexpr.DexprType;

public class DexprBoolean extends DexprNumber {

    public static final DexprBoolean TRUE = new DexprBoolean(true);
    public static final DexprBoolean FALSE = new DexprBoolean(false);

    public final boolean value;

    public DexprBoolean(boolean value) {
        super(DexprType.BOOLEAN);
        this.value = value;
    }

    public boolean booleanValue() {
        return value;
    }

    public double doubleValue() {
        return intValue();
    }

    public int intValue() {
        return value ? 1 : 0;
    }

    public int hashCode() {
        return value ? 1 : 0;
    }

    public boolean equals(Object obj) {
        return obj instanceof DexprBoolean && value == ((DexprBoolean) obj).value;
    }

    public String toString() {
        return Boolean.toString(value).toUpperCase();
    }

    @Override
    public Object produce(Object param) {
        return this.value;
    }

    @Override
    public boolean isExpectable(DexprType type) {
        return DexprType.BOOLEAN.equals(type);
    }
}
