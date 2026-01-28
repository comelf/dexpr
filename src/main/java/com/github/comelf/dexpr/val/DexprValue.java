package com.github.comelf.dexpr.val;

import com.github.comelf.dexpr.Dexpr;
import com.github.comelf.dexpr.DexprType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class DexprValue extends Dexpr {
    public DexprValue(DexprType type) {
        super(type, false);
    }

    @Override
    public List<Dexpr> findAll(DexprType type) {
        if(this.type.equalType(type)){
            return Arrays.asList(this);
        }
        return Collections.emptyList();
    }

}
