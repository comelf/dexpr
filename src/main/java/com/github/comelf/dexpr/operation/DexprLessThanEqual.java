package com.github.comelf.dexpr.operation;


import com.github.comelf.dexpr.Dexpr;
import com.github.comelf.dexpr.DexprType;
import com.github.comelf.dexpr.err.DexprException;

public class DexprLessThanEqual extends AbstractComparisonOperator {
    public DexprLessThanEqual(Dexpr left, Dexpr right) {
        super(DexprType.OPERATOR_LTE, left, right);
    }

    public Dexpr evaluate(Object param) throws DexprException {
        return bool(compare(param) >= 0);
    }

    @Override
    public String operatorString() {
        return ">=";
    }

    public String toString() {
        return left + operatorString() + right;
    }
}
