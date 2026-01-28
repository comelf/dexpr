package com.github.comelf.dexpr.operation;


import com.github.comelf.dexpr.util.CastUtil;
import com.github.comelf.dexpr.Dexpr;
import com.github.comelf.dexpr.DexprType;
import com.github.comelf.dexpr.err.DexprException;
import com.github.comelf.dexpr.function.Function;
import com.github.comelf.dexpr.val.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DexprFunction extends DexprEvaluatable {
    private String name;
    private List<Dexpr> args;
    private Function implementation;

    public DexprFunction(Function function, List<Dexpr> args) {
        super(DexprType.FUNCTION);
        this.implementation = function;
        this.name = function.getClass().getSimpleName();
        this.args = args;
    }

    public String getName() {
        return name;
    }

    public int size() {
        return args.size();
    }

    public Dexpr getArg(int index) {
        return args.get(index);
    }

    public List<Dexpr> getArgs() {
        return args;
    }

    public void setImplementation(Function function) {
        this.implementation = function;
    }

    public Function getImplementation() {
        return implementation;
    }

    public Dexpr evaluate(Object param) throws DexprException {
        List<Object> list = new ArrayList<>();
        for (Dexpr expr : args) {
            Dexpr e = evaluateExpr(expr, param);
            if (e instanceof DexprString) {
                list.add(e.toString());
            } else if (e instanceof DexprVariable) {
                list.add(e.produce(param));
            } else if (e instanceof DexprEvaluatable) {
                list.add(e.produce(param));
            } else if (e instanceof DexprNull) {
                list.add(null);
            } else if (e instanceof DexprNumber) {
                list.add(((DexprNumber) e).doubleValue());
            } else {
                throw new DexprException("Invalid args. " + e);
            }
        }

        Object o = implementation.process(list);
        return toExpression(o);
    }

    @Override
    public Object produce(Object param) throws DexprException {
        return evaluate(param).produce(param);
    }

    @Override
    public boolean isExpectable(DexprType type) {
        if (type == null) {
            return false;
        }
        return type.getId() > 91 && type.getId() < 1000;
    }

    @Override
    public List<Dexpr> findAll(DexprType type) {
        if (this.type.equals(type)) {
            return Arrays.asList(this);
        }
        return Collections.emptyList();
    }

    private Dexpr evaluateExpr(Dexpr expr, Object param) throws DexprException {
        if (expr instanceof DexprEvaluatable) {
            return ((DexprEvaluatable) expr).evaluate(param);
        } else if (expr instanceof DexprNumber) {
            return expr;
        } else if (expr instanceof DexprVariable) {
            return ((DexprVariable) expr).evaluate(param);
        } else if (expr instanceof DexprEvaluatable) {
            return ((DexprEvaluatable) expr).evaluate(param);
        } else if (expr instanceof DexprNull) {
            return new DexprNull();
        } else {
            throw new DexprException("Invalid args. " + expr);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append("(");
        for (int i = 0; i < args.size(); i++) {
            if (i > 0)
                sb.append(",");
            sb.append(args.get(i));
        }
        sb.append(")");
        return sb.toString();
    }

    public void validate() throws DexprException {
        if (name == null)
            throw new RuntimeException("Function name cannot be empty");
        if (args == null || args.isEmpty()) {
            throw new DexprException("Function '" + name + "' requires at least one argument");
        }
        for (int i = 0; i < args.size(); i++) {
            args.get(i).validate();
        }
    }

    public static Dexpr toExpression(Object value) {
        if (value == null)
            return new DexprNull();
        else if (value instanceof Double || value instanceof Float || value instanceof Long)
            return new DexprDouble(CastUtil.toDouble(value));
        else if (value instanceof Integer)
            return new DexprInteger(CastUtil.toInt(value));
        else if (value instanceof Boolean)
            return new DexprBoolean(CastUtil.toBoolean(value));
        else if (value instanceof String)
            return new DexprString(CastUtil.toStr(value));
        return null;
    }

}
