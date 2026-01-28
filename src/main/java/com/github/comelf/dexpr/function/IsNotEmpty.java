package com.github.comelf.dexpr.function;

import java.util.List;

public class IsNotEmpty implements Function {

    public String desc() {
        return "(text)";
    }

    public Object process(List param) {
        if (param.size() != 1)
            throw new RuntimeException("isNotEmpty() invalid param size ");

        try {
            if (param.get(0) == null || "".equals(param.get(0)))
                return Boolean.FALSE;
            else
                return Boolean.TRUE;
        } catch (RuntimeException e) {
            throw new RuntimeException("isNotEmpty() : " + e);
        } catch (Exception e) {
            throw new RuntimeException("isNotEmpty() : " + e);
        }
    }

}
