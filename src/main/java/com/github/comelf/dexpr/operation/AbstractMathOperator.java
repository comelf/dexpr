package com.github.comelf.dexpr.operation;


import com.github.comelf.dexpr.Dexpr;
import com.github.comelf.dexpr.DexprType;
import com.github.comelf.dexpr.err.DexprException;
import com.github.comelf.dexpr.val.DexprDouble;
import com.github.comelf.dexpr.val.DexprNumber;
import com.github.comelf.dexpr.val.DexprVariable;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractMathOperator extends AbstractBinaryOperator {
    private Dexpr[] args;

    public AbstractMathOperator(DexprType type, Dexpr left, Dexpr right) {
        super(type, left, right);
        args = new Dexpr[]{left, right};
    }

    public Dexpr optimize() throws DexprException {
        left = left == null ? DexprDouble.ZERO : left.optimize();
        right = right.optimize();
        if (left instanceof DexprNumber && right instanceof DexprNumber) {
            return evaluate(null);
        }
        return this;
    }

    protected double evaluateExpr(Dexpr e, Object param) throws DexprException {
        Dexpr c = eval(e, param);
        if (c == null || DexprType.NULL.equals(c.type))
            throw new DexprException("Failed to operation on null value. " + e.type + ": " + e);
        else if (DexprType.STRING.equals(c.type)) {
            c = new DexprVariable(c.toString()).evaluate(param);
        }

        DexprType.assertType(c, DexprType.INTEGER, DexprType.DOUBLE);
        return ((DexprNumber) c).doubleValue();
    }

    public Dexpr evaluate(Object param) throws DexprException {
        Dexpr l = eval(left, param);
        if (DexprType.NULL.equals(l.type)) {
            throw new DexprException("Failed to operation on null value. " + left.type + ": " + left);
        }
        Dexpr r = eval(right, param);
        if (DexprType.NULL.equals(r.type)) {
            throw new DexprException("Failed to operation on null value. " + right.type + ": " + right);
        }
        return evaluate(evaluateExpr(l, param), evaluateExpr(r, param));
    }

    @Override
    public Object produce(Object param) throws DexprException {
        DexprDouble e = (DexprDouble) evaluate(param);
        return e.value;
    }

    public List<Dexpr> getArgs() {
        return Arrays.asList(args);
    }

    protected abstract Dexpr evaluate(double left, double right) throws DexprException;
}
