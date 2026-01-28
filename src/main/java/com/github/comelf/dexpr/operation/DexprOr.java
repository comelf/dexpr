package com.github.comelf.dexpr.operation;


import com.github.comelf.dexpr.Dexpr;
import com.github.comelf.dexpr.DexprType;
import com.github.comelf.dexpr.err.DexprException;
import com.github.comelf.dexpr.val.DexprBoolean;

public class DexprOr extends AbstractLogicalOperator {

    public DexprOr(Dexpr left, Dexpr right) {
        super(DexprType.OPERATOR_OR, left, right);
    }

    public Dexpr evaluate(Object param) throws DexprException {
        // Short-circuit evaluation: evaluate left first
        Dexpr l = eval(left, param);

        if (!(l instanceof DexprBoolean)) {
            throw new DexprException("invalid or operation - left is not boolean");
        }

        // OR short-circuit: if left is true, don't evaluate right
        if (((DexprBoolean) l).value) {
            return DexprBoolean.TRUE;
        }

        // Left is false, evaluate right
        Dexpr r = eval(right, param);
        if (!(r instanceof DexprBoolean)) {
            throw new DexprException("invalid or operation - right is not boolean");
        }

        // Return singleton instance
        return ((DexprBoolean) r).value ? DexprBoolean.TRUE : DexprBoolean.FALSE;
    }

    @Override
    public int getOrder() {
        return 12;
    }

    @Override
    public String operatorString() {
        return "||";
    }

    public String toString() {
        return left + operatorString() + right;
    }
}
