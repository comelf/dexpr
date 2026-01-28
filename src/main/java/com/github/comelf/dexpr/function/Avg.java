package com.github.comelf.dexpr.function;

import com.github.comelf.dexpr.util.CastUtil;
import com.github.comelf.dexpr.util.CollectionUtil;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;

public class Avg implements Function {

    public String desc() {
        return "(list)";
    }

    public Object process(List param) {
        if (param == null)
            return new Double(0);
        double d = 0;
        int cnt = 0;
        try {

            for (int i = 0; i < param.size(); i++) {
                Object o = param.get(i);
                if (o == null)
                    continue;
                if (o instanceof List) {
                    d += CollectionUtil.sum((List) o);
                    cnt += ((List) o).size();
                } else if (o instanceof Map) {
                    d += CollectionUtil.sum((Map) o);
                    cnt += ((Map) o).size();
                } else if (o.getClass().isArray()) {
                    d += CollectionUtil.sumArr(o);
                    cnt += Array.getLength(o);
                } else {
                    d += CastUtil.getDouble(o);
                    cnt++;
                }
            }

            return cnt == 0 ? 0 : new Double(d / cnt);
        } catch (RuntimeException e) {
            throw new RuntimeException("avg() : " + e);
        } catch (Exception e) {
            throw new RuntimeException("avg() : " + e);
        }
    }
}
