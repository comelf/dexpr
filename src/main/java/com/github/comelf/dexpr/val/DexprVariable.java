package com.github.comelf.dexpr.val;

import com.github.comelf.dexpr.Dexpr;
import com.github.comelf.dexpr.DexprType;
import com.github.comelf.dexpr.err.DexprException;
import com.github.comelf.dexpr.operation.IBinaryOperator;
import com.github.comelf.dexpr.operation.DexprEvaluatable;
import com.github.comelf.dexpr.operation.DexprFunction;

import java.util.*;

public class DexprVariable extends DexprEvaluatable {

    private String name;

    public DexprVariable(String name) {
        super(DexprType.VARIABLE);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean isVolatile() {
        return name != null;
    }

    public Dexpr evaluate(Object param) {
        Object value = findValue(name, param);
        Dexpr wexl = DexprFunction.toExpression(value);
        return wexl;
    }

    @Override
    public Object produce(Object param) throws DexprException {
        Dexpr e = evaluate(param);
        if (e == null) {
            return null;
        }
        return e.produce(param);
    }

    public String toString() {
        return name;
    }

    public int hashCode() {
        return name.hashCode();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof DexprVariable))
            return false;

        DexprVariable ev = (DexprVariable) obj;
        return ev.name.equals(name);
    }

    public static DexprVariable[] findVariables(Dexpr expr) {
        List<DexprVariable> vars = new ArrayList();
        findVariables(expr, vars);
        return vars.toArray(new DexprVariable[0]);
    }

    public static void findVariables(Dexpr expr, List<DexprVariable> vars) {
        if (expr instanceof DexprFunction) {
            DexprFunction f = (DexprFunction) expr;
            for (int i = 0; i < f.size(); i++) {
                findVariables(f.getArg(i), vars);
            }
        } else if (expr instanceof DexprExpression) {
            findVariables(((DexprExpression) expr).getChild(), vars);
        } else if (expr instanceof IBinaryOperator) {
            IBinaryOperator bo = (IBinaryOperator) expr;
            findVariables(bo.getLeft(), vars);
            findVariables(bo.getRight(), vars);
        } else if (expr instanceof DexprVariable) {
            vars.add(((DexprVariable) expr));
        }
    }

    public void validate() throws DexprException {
        if (name == null)
            throw new DexprException("Variable name is empty");
    }

    private Object findValue(String key, Object param) {
        try {
//            if (param instanceof TagCountPack) {
//                return get(key, (TagCountPack) param);
//            } else if (param instanceof LogSinkPack) {
//                return get(key, (LogSinkPack) param);
//            } else if (param instanceof StringKeyLinkedMap) {
//                return ((StringKeyLinkedMap) param).get(key);
//            } else if (param instanceof MapValue) {
//                return ((MapValue) param).get(key);
//            } else
                if (param instanceof Map) {
                return ((HashMap<String, Object>) param).get(key);
            }
        } catch (RuntimeException t) {
//            ExceptionUtil.ignore(t);
        } catch (Throwable t) {
//            ExceptionUtil.ignore(t);
        }
        return null;
    }

//    private Object get(String key, LogSinkPack param) {
//        switch (key) {
//            case "oid":
//                return param.oid;
//            case "okind":
//                return param.okind;
//            case "onode":
//                return param.onode;
//            case "time":
//                return param.time;
//            case "$oid": // tag 또는 field에서 oid 정보를 조회하고 싶은 경우 $oid로 rule 지정
//                return getTagOrField("oid", param);
//            case "$okind":
//                return getTagOrField("okind", param);
//            case "$onode":
//                return getTagOrField("onode", param);
//        }
//        return getTagOrField(key, param);
//    }
//
//    private Object get(String key, TagCountPack param) {
//        switch (key) {
//            case "oid":
//                return param.oid;
//            case "okind":
//                return param.okind;
//            case "onode":
//                return param.onode;
//            case "time":
//                return param.time;
//            case "$oid": // tag 또는 field에서 oid 정보를 조회하고 싶은 경우 $oid로 rule 지정
//                return getTagOrField("oid", param);
//            case "$okind":
//                return getTagOrField("okind", param);
//            case "$onode":
//                return getTagOrField("onode", param);
//        }
//        return getTagOrField(key, param);
//    }
//
//    private Value getTagOrField(String key, TagCountPack param) {
//        // get in tag
//        Value v = param.getTag(key);
//        if (v != null) {
//            return v;
//        }
//        // get in field
//        return param.get(key);
//    }
//
//    private Value getTagOrField(String key, LogSinkPack param) {
//        // get in tag
//        Value v = param.tags.get(key);
//        if (v != null) {
//            return v;
//        }
//        // get in field
//        return param.fields.get(key);
//    }

    @Override
    public boolean isExpectable(DexprType type) {
        return DexprType.VARIABLE.equals(type);
    }

    @Override
    public List<Dexpr> findAll(DexprType type) {
        if (this.type.equals(type)) {
            return Arrays.asList(this);
        }
        return Collections.emptyList();
    }

}
