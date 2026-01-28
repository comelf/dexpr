package com.github.comelf.dexpr.operation;

import com.github.comelf.dexpr.Dexpr;
import com.github.comelf.dexpr.DexprType;
import com.github.comelf.dexpr.err.DexprException;
import com.github.comelf.dexpr.val.DexprDouble;

public class DexprSubtraction extends AbstractMathOperator {

    public DexprSubtraction(Dexpr left, Dexpr right) {
        super(DexprType.OPERATOR_SUB, left, right);
    }

    protected Dexpr evaluate(double lhs, double rhs) throws DexprException {
        return new DexprDouble(lhs - rhs);
    }

    @Override
    public int getOrder() {
        return 4;
    }

    @Override
    public String operatorString() {
        return "-";
    }

    public String toString() {
        if (left == null)
            return operatorString() + right;
        else
            return left + operatorString() + right;
    }

}
