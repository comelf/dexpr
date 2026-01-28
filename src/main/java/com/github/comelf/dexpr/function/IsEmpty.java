package com.github.comelf.dexpr.function;

import java.util.List;

public class IsEmpty implements Function {

    public String desc() {
        return "(text)";
    }

    public Object process(List param) {
        if (param.size() != 1)
            throw new RuntimeException("isEmpty() invalid param size ");

        try {
            if (param.get(0) == null || "".equals(param.get(0)))
                return Boolean.TRUE;
            else
                return Boolean.FALSE;
        } catch (RuntimeException e) {
            throw new RuntimeException("isEmpty() : " + e);
        } catch (Exception e) {
            throw new RuntimeException("isEmpty() : " + e);
        }
    }

}
