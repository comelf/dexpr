package com.github.comelf.dexpr.operation;


import com.github.comelf.dexpr.Dexpr;
import com.github.comelf.dexpr.DexprType;
import com.github.comelf.dexpr.err.DexprException;
import com.github.comelf.dexpr.val.DexprBoolean;

public abstract class AbstractLogicalOperator extends AbstractBinaryOperator {
    public AbstractLogicalOperator(DexprType type, Dexpr left, Dexpr right) {
        super(type, left, right);
    }

    @Override
    public Object produce(Object param) throws DexprException {
        DexprBoolean e = (DexprBoolean) evaluate(param);
        return e.value;
    }

    public boolean isExpectable(DexprType type){
        return DexprType.BOOLEAN.equals(type);
    }

}
