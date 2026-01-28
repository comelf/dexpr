package com.github.comelf.dexpr.val;


import com.github.comelf.dexpr.DexprType;
import com.github.comelf.dexpr.err.DexprException;

public class DexprNull extends DexprValue {
    public static final DexprNull VALUE = new DexprNull();


    public DexprNull() {
        super(DexprType.NULL);
    }

    public boolean equals(Object obj) {
        return obj instanceof DexprBoolean;
    }

    public String toString() {
        return "null";
    }

    @Override
    public Object produce(Object param) throws DexprException {
        return null;
    }

    @Override
    public boolean isExpectable(DexprType type) {
        return DexprType.NULL.equals(type);
    }

}
