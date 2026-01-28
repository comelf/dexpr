package com.github.comelf.dexpr;


import com.github.comelf.dexpr.err.DexprException;
import com.github.comelf.dexpr.operation.DexprEvaluatable;
import com.github.comelf.dexpr.util.CastUtil;
import com.github.comelf.dexpr.val.DexprBoolean;

import java.util.List;
import java.util.Objects;

public abstract class Dexpr {

    public final DexprType type;
    public final boolean evaluatable;

    public Dexpr(DexprType type, boolean evaluatable) {
        this.type = type;
        this.evaluatable = evaluatable;
    }


    public boolean isVolatile() {
        return false;
    }

    public void validate() throws DexprException {
    }

    public Dexpr optimize() throws DexprException {
        return this;
    }

    public abstract Object produce(Object param) throws DexprException;


    public <T> T produce(Object param, Class<T> resultType) throws DexprException {
        Objects.requireNonNull(resultType, "'resultType' must not be null");
        Object o = produce(param);

        if (o == null) {
            return null;
        } else if (o.getClass().equals(resultType)) {
            return (T) o;
        } else {
            throw new DexprException("resultType not match error");
        }
    }

    public double produceToDouble(Object param) throws DexprException {
        Object o = produce(param);
        return CastUtil.toDouble(o);
    }

    public int produceToInt(Object param) throws DexprException {
        Object o = produce(param);
        return CastUtil.toInt(o);
    }

    public long produceToLong(Object param) throws DexprException {
        Object o = produce(param);
        return CastUtil.toLong(o);
    }

    public boolean produceToBoolean(Object param) throws DexprException {
        Object o = produce(param);
        return CastUtil.toBoolean(o);
    }

    protected Dexpr eval(Dexpr expr, Object param) throws DexprException {
        if (expr instanceof DexprEvaluatable) {
            return ((DexprEvaluatable) expr).evaluate(param);
        }
        return expr;
    }

    public int getOrder() {
        return 0;
    }

    protected DexprBoolean bool(boolean bool) {
        return bool ? DexprBoolean.TRUE : DexprBoolean.FALSE;
    }

    public abstract boolean isExpectable(DexprType type);

    public abstract List<Dexpr> findAll(DexprType type);

}
