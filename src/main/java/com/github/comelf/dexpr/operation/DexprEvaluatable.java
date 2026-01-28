package com.github.comelf.dexpr.operation;

import com.github.comelf.dexpr.Dexpr;
import com.github.comelf.dexpr.DexprType;
import com.github.comelf.dexpr.err.DexprException;

public abstract class DexprEvaluatable extends Dexpr {

    public DexprEvaluatable(DexprType type) {
        super(type, true);
    }

    public boolean isVolatile() {
        return true;
    }

    public abstract Dexpr evaluate(Object param) throws DexprException;
}
