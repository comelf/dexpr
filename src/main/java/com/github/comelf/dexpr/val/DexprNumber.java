package com.github.comelf.dexpr.val;


import com.github.comelf.dexpr.DexprType;

public abstract class DexprNumber extends DexprValue {
    DexprNumber(DexprType type) {
        super(type);
    }

    @Override
    public boolean isVolatile() {
        return true;
    }

    @Override
    public Object produce(Object param) {
        return doubleValue();
    }

    public void validate() {
    }

    public boolean booleanValue() {
        return intValue() != 0;
    }

    public abstract int intValue();

    public abstract double doubleValue();
}
