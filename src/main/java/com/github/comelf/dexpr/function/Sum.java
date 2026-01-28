package com.github.comelf.dexpr.function;

import com.github.comelf.dexpr.util.CastUtil;
import com.github.comelf.dexpr.util.CollectionUtil;

import java.util.List;
import java.util.Map;

public class Sum implements Function {

    public String desc() {
        return "(list)";
    }

    public Object process(List param) {
        if (param == null)
            return new Double(0);
        double d = 0;
        try {
            for (int i = 0; i < param.size(); i++) {
                Object o = param.get(i);
                if (o == null)
                    continue;
                if (o instanceof List) {
                    d += CollectionUtil.sum((List) o);
                } else if (o instanceof Map) {
                    d += CollectionUtil.sum((Map) o);
                } else if (o.getClass().isArray()) {
                    d += CollectionUtil.sumArr(o);
                } else {
                    d += CastUtil.getDouble(o);
                }
            }
            return new Double(d);
        } catch (RuntimeException e) {
            throw new RuntimeException("sum() : " + e);
        } catch (Exception e) {
            throw new RuntimeException("sum() : " + e);
        }
    }
}
