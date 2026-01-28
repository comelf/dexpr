package com.github.comelf.dexpr.function;

import java.util.List;

public class IsNull implements Function {

    public String desc() {
        return "(any)";
    }

    public Object process(List param) {
        if (param.size() != 1)
            throw new RuntimeException("isNull() invalid param size ");

        try {
            if (param.get(0) == null)
                return Boolean.TRUE;
            else
                return Boolean.FALSE;
        } catch (RuntimeException e) {
            throw new RuntimeException("isNull() : " + e);
        } catch (Exception e) {
            throw new RuntimeException("isNull() : " + e);
        }
    }

}
