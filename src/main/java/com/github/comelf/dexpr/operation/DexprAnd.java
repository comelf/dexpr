package com.github.comelf.dexpr.operation;

import com.github.comelf.dexpr.Dexpr;
import com.github.comelf.dexpr.DexprType;
import com.github.comelf.dexpr.err.DexprException;
import com.github.comelf.dexpr.val.DexprBoolean;

public class DexprAnd extends AbstractLogicalOperator {

    public DexprAnd(Dexpr left, Dexpr right) {
        super(DexprType.OPERATOR_AND, left, right);
    }

    public Dexpr evaluate(Object param) throws DexprException {
        // Short-circuit evaluation: evaluate left first
        Dexpr l = eval(left, param);

        if (!(l instanceof DexprBoolean)) {
            throw new DexprException("invalid and operation - left is not boolean");
        }

        // AND short-circuit: if left is false, don't evaluate right
        if (!((DexprBoolean) l).value) {
            return DexprBoolean.FALSE;
        }

        // Left is true, evaluate right
        Dexpr r = eval(right, param);
        if (!(r instanceof DexprBoolean)) {
            throw new DexprException("invalid and operation - right is not boolean");
        }

        // Return singleton instance
        return ((DexprBoolean) r).value ? DexprBoolean.TRUE : DexprBoolean.FALSE;
    }


    @Override
    public int getOrder() {
        return 11;
    }

    @Override
    public String operatorString() {
        return "&&";
    }

    public String toString() {
        return left + operatorString() + right;
    }
}
