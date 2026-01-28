package com.github.comelf.dexpr.val;

import com.github.comelf.dexpr.DexprType;

public class DexprString extends DexprValue {
    public static final DexprString EMPTY = new DexprString("");

    public final String str;

    public DexprString(String str) {
        super(DexprType.STRING);
        this.str = str;
    }

    @Override
    public boolean isVolatile() {
        return str != null;
    }

    @Override
    public Object produce(Object param) {
        return this.str;
    }

    public String toString() {
        return str;
    }

    public int hashCode() {
        return str.hashCode();
    }

    public boolean equals(Object obj) {
        return obj instanceof DexprString && str.equals(((DexprString) obj).str);
    }

    @Override
    public boolean isExpectable(DexprType type) {
        return DexprType.STRING.equals(type);
    }
}
