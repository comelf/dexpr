package com.github.comelf.dexpr.operation;

import com.github.comelf.dexpr.Dexpr;
import com.github.comelf.dexpr.DexprType;
import com.github.comelf.dexpr.val.DexprDouble;

public class DexprAddition extends AbstractMathOperator {

    public DexprAddition(Dexpr left, Dexpr right) {
        super(DexprType.OPERATOR_ADD, left, right);
    }

    protected Dexpr evaluate(double left, double right) {
        return new DexprDouble(left + right);
    }

    @Override
    public String operatorString() {
        return "+";
    }

    @Override
    public int getOrder() {
        return 4;
    }


    public String toString() {
        if (left == null)
            return operatorString() + right;
        else
            return left + operatorString() + right;
    }
}
