package com.github.comelf.dexpr.val;

import com.github.comelf.dexpr.Dexpr;
import com.github.comelf.dexpr.err.DexprException;
import com.github.comelf.dexpr.util.CastUtil;

import java.util.ArrayList;
import java.util.List;

public class DexprStringInterpolation extends DexprString {
    private final List<Object> parts;

    /**
     * @param parts 문자열 부분과 DexprVariable이 교대로 들어있는 리스트
     */
    public DexprStringInterpolation(List<Object> parts) {
        super(null);
        this.parts = parts;
    }

    @Override
    public boolean isVolatile() {
        return true;
    }

    @Override
    public Object produce(Object param) {
        StringBuilder result = new StringBuilder();

        try {
            for (Object part : parts) {
                if (part instanceof String) {
                    // 일반 문자열 부분
                    result.append((String) part);
                } else if (part instanceof DexprVariable) {
                    // 변수를 평가하여 값을 가져옴
                    DexprVariable var = (DexprVariable) part;
                    Object value = var.produce(param);
                    if (value != null) {
                        result.append(CastUtil.toStr(value));
                    }
                } else if (part instanceof Dexpr) {
                    // 다른 Dexpr 표현식
                    Dexpr wexl = (Dexpr) part;
                    Object value = wexl.produce(param);
                    if (value != null) {
                        result.append(CastUtil.toStr(value));
                    }
                }
            }
        } catch (DexprException e) {
            // 예외 발생 시 에러 메시지를 문자열에 포함
            result.append("[Error: ").append(e.getMessage()).append("]");
        }

        return result.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("\"");
        for (Object part : parts) {
            if (part instanceof String) {
                sb.append(part);
            } else if (part instanceof DexprVariable) {
                sb.append("${").append(((DexprVariable) part).getName()).append("}");
            }
        }
        sb.append("\"");
        return sb.toString();
    }

    @Override
    public void validate() throws DexprException {
        if (parts == null || parts.isEmpty()) {
            throw new DexprException("String interpolation parts cannot be empty");
        }

        // 각 변수 부분을 검증
        for (Object part : parts) {
            if (part instanceof Dexpr) {
                ((Dexpr) part).validate();
            }
        }
    }

    /**
     * 이 문자열 보간에 포함된 모든 변수를 반환
     */
    public List<DexprVariable> getVariables() {
        List<DexprVariable> vars = new ArrayList<>();
        for (Object part : parts) {
            if (part instanceof DexprVariable) {
                vars.add((DexprVariable) part);
            }
        }
        return vars;
    }
}