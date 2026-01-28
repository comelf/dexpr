package com.github.comelf.dexpr.operation;


import com.github.comelf.dexpr.Dexpr;
import com.github.comelf.dexpr.DexprType;
import com.github.comelf.dexpr.err.DexprException;
import com.github.comelf.dexpr.val.DexprDouble;

public class DexprModular extends AbstractMathOperator {

    public DexprModular(Dexpr left, Dexpr right) {
        super(DexprType.OPERATOR_MOD, left, right);
    }

    protected Dexpr evaluate(double left, double right) throws DexprException {
        if (right == 0.)
            throw new DexprException("modular (%) by zero (0)");
        return new DexprDouble(left % right);
    }

    @Override
    public int getOrder() {
        return 3;
    }

    @Override
    public String operatorString() {
        return "%";
    }

    public String toString() {
        return left + operatorString() + right;
    }

}
