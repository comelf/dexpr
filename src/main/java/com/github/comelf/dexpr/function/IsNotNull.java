package com.github.comelf.dexpr.function;

import java.util.List;

public class IsNotNull implements Function {

    public String desc() {
        return "(any)";
    }

    public Object process(List param) {
        if (param.size() != 1)
            throw new RuntimeException("isNotNull() invalid param size ");

        try {
            if (param.get(0) == null)
                return Boolean.FALSE;
            else
                return Boolean.TRUE;
        } catch (RuntimeException e) {
            throw new RuntimeException("isNotNull() : " + e);
        } catch (Exception e) {
            throw new RuntimeException("isNotNull() : " + e);
        }
    }

}
