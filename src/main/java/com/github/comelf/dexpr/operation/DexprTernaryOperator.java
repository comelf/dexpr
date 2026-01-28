package com.github.comelf.dexpr.operation;

import com.github.comelf.dexpr.Dexpr;
import com.github.comelf.dexpr.DexprType;
import com.github.comelf.dexpr.err.DexprException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DexprTernaryOperator extends AbstractBinaryOperator {

    protected Dexpr condition;

    public DexprTernaryOperator(Dexpr condition, Dexpr left, Dexpr right) {
        super(DexprType.OPERATOR_TERNARY, left, right);
        this.condition = condition;
    }

    public Dexpr getCondition() {
        return condition;
    }

    public void setCondition(Dexpr condition) {
        this.condition = condition;
    }

    @Override
    public void printTree() {

    }

    @Override
    public int getOrder() {
        return 13;
    }

    @Override
    public Object produce(Object param) throws DexprException {
        return evaluate(param).produce(param);
    }

    @Override
    public Dexpr evaluate(Object param) throws DexprException {
        boolean c = condition.produceToBoolean(param);
        if (c) {
            if (left instanceof DexprEvaluatable) {
                return ((DexprEvaluatable) left).evaluate(param);
            } else {
                return left;
            }
        } else {
            if (right instanceof DexprEvaluatable) {
                return ((DexprEvaluatable) right).evaluate(param);
            } else {
                return right;
            }
        }
    }

    @Override
    public boolean isExpectable(DexprType type) {
        if (type == null) {
            return false;
        }
        return type.getId() > 100 && type.getId() < 1000;
    }

    @Override
    public String operatorString() {
        return "?";
    }

    @Override
    public List<Dexpr> findAll(DexprType type) {
        if (this.type.equals(type)) {
            return Arrays.asList(this);
        }
        return Collections.emptyList();
    }

    @Override
    public String toString() {
        return condition + " ? " + left + " : " + right;
    }
}
