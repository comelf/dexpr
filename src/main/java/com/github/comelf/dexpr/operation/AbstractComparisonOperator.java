package com.github.comelf.dexpr.operation;


import com.github.comelf.dexpr.Dexpr;
import com.github.comelf.dexpr.DexprType;
import com.github.comelf.dexpr.err.DexprException;
import com.github.comelf.dexpr.val.DexprBoolean;
import com.github.comelf.dexpr.val.DexprNumber;
import com.github.comelf.dexpr.val.DexprString;
import com.github.comelf.dexpr.val.DexprVariable;

public abstract class AbstractComparisonOperator extends AbstractBinaryOperator {
    public AbstractComparisonOperator(DexprType type, Dexpr left, Dexpr right) {
        super(type, left, right);
    }

    @Override
    public int getOrder() {
        return 6;
    }

    public boolean isExpectable(DexprType type){
        return DexprType.BOOLEAN.equals(type);
    }

    @Override
    public Object produce(Object param) throws DexprException {
        DexprBoolean e = (DexprBoolean) evaluate(param);
        return e.value;
    }

    protected Dexpr evaluateExpr(Dexpr e, Object param) throws DexprException {
        Dexpr c = eval(e, param);
        if (c == null || DexprType.NULL.equals(c.type))
            throw new DexprException("Failed to operation on null value. " + e.type + ": " + e);
        else if (DexprType.STRING.equals(c.type)) {
            Dexpr val = new DexprVariable(c.toString()).evaluate(param);
            if (val != null && !DexprType.NULL.equals(val.type)) {
                return val;
            }
        }
        return c;
    }

    protected double compare(Object param) throws DexprException {
        Dexpr l = eval(left, param);
        Dexpr r = eval(right, param);

        if (l instanceof DexprString && r instanceof DexprNumber) {
            l = evaluateExpr(l, param);
        } else if (l instanceof DexprNumber && r instanceof DexprString) {
            r = evaluateExpr(r, param);
        }

        if (l instanceof DexprString || r instanceof DexprString) {
            return l.toString().compareTo(r.toString());
        }

        if (l instanceof DexprNumber && r instanceof DexprNumber) {
            return (((DexprNumber) l).doubleValue() - ((DexprNumber) r)
                    .doubleValue());
        }

        return 0;
    }
}
