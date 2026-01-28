package com.github.comelf.dexpr.val;

import com.github.comelf.dexpr.Dexpr;
import com.github.comelf.dexpr.DexprType;
import com.github.comelf.dexpr.err.DexprException;
import com.github.comelf.dexpr.operation.DexprEvaluatable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DexprExpression extends DexprEvaluatable {
    private Dexpr child;

    public DexprExpression(Dexpr child) {
        super(DexprType.EXPRESSION);
        this.child = child;
    }

    public Dexpr evaluate(Object param) throws DexprException {
        if (child instanceof DexprEvaluatable)
            return ((DexprEvaluatable) child).evaluate(param);
        else
            return child;
    }

    @Override
    public Object produce(Object param) throws DexprException {
        Dexpr e = evaluate(param);
        return e.produce(param);
    }

    public Dexpr optimize() throws DexprException {
        return child.optimize();
    }

    public List<Dexpr> getArgs() {
        return Arrays.asList(child);
    }

    public Dexpr getChild() {
        return child;
    }

    public String toString() {
        return "(" + child + ")";
    }

    public void validate() throws DexprException {
        if (child == null) {
            throw new DexprException("Empty expression (empty parentheses)");
        }
        child.validate();
    }

    @Override
    public boolean isExpectable(DexprType type) {
        if (this.child == null) {
            return false;
        }
        return child.isExpectable(type);
    }

    @Override
    public List<Dexpr> findAll(DexprType type) {
        List<Dexpr> list = new ArrayList<>();
        if (this.type.equalType(type)) {
            list.add(this);
        }
        if (this.child != null) {
            list.addAll(child.findAll(type));
        }
        return list;
    }

    @Override
    public int getOrder() {
        // Expression (괄호)는 가장 높은 우선순위를 가짐 (가장 작은 숫자)
        return Integer.MIN_VALUE;
    }

}
