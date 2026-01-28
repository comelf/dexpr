package com.github.comelf.dexpr.operation;

import com.github.comelf.dexpr.Dexpr;
import com.github.comelf.dexpr.DexprType;
import com.github.comelf.dexpr.err.DexprException;
import com.github.comelf.dexpr.val.DexprDouble;
import com.github.comelf.dexpr.val.DexprNumber;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DexprUnaryMinus extends DexprEvaluatable {
    private Dexpr operand;

    public DexprUnaryMinus(Dexpr operand) {
        super(DexprType.OPERATOR_SUB);
        this.operand = operand;
    }

    public Dexpr getOperand() {
        return operand;
    }

    public void setOperand(Dexpr operand) {
        this.operand = operand;
    }

    @Override
    public Dexpr evaluate(Object param) throws DexprException {
        Dexpr evaluated = operand;
        if (operand instanceof DexprEvaluatable) {
            evaluated = ((DexprEvaluatable) operand).evaluate(param);
        }

        if (!(evaluated instanceof DexprNumber)) {
            throw new DexprException("Unary minus requires numeric operand");
        }

        double value = ((DexprNumber) evaluated).doubleValue();
        return new DexprDouble(-value);
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
        return type.getId() >= 100 && type.getId() <= 1000;
    }

    @Override
    public List<Dexpr> findAll(DexprType type) {
        if (this.type.equals(type)) {
            return Arrays.asList(this);
        }
        if (operand != null) {
            return operand.findAll(type);
        }
        return Collections.emptyList();
    }

    @Override
    public void validate() throws DexprException {
        if (operand == null) {
            throw new DexprException("Unary minus operand missing");
        }
        operand.validate();
    }

    @Override
    public String toString() {
        return "-" + operand;
    }

    @Override
    public int getOrder() {
        // 단항 마이너스는 높은 우선순위 (곱셈/나눗셈보다 높음)
        return 2;
    }
}