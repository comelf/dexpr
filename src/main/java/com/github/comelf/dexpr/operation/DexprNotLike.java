package com.github.comelf.dexpr.operation;

import com.github.comelf.dexpr.Dexpr;
import com.github.comelf.dexpr.DexprType;
import com.github.comelf.dexpr.err.DexprException;

import java.util.regex.Pattern;

public class DexprNotLike extends AbstractLogicalOperator {

    private Pattern cachedPattern;
    private String cachedPatternString;

    public DexprNotLike(Dexpr left, Dexpr right) {
        super(DexprType.OPERATOR_NOT_LIKE, left, right);
    }

    @Override
    public int getOrder() {
        return 0;
    }

    private boolean compare(Object param) throws DexprException {
        Dexpr l = eval(left, param);
        Dexpr r = eval(right, param);

        if (l == null || r == null) {
            throw new DexprException("invalid not like operation");
        }

        Object lv = l.produce(param);
        Object rv = r.produce(param);

        String ls = lv != null ? lv.toString() : "";
        String rs = rv != null ? rv.toString() : "";

        // Convert SQL LIKE wildcards (%) to pattern wildcards (*)
        rs = rs.replace('%', '*');

        if (ls.isEmpty() && rs.isEmpty()) {
            return false;  // "" not like ""
        }
        if (ls.isEmpty() && rs.replace("*", "").isEmpty()) {
            return false;  // "" not like "%" → false (SQL 표준)
        }
        if (ls.isEmpty()) {
            return true;   // "" not like "something"
        }
        if (rs.isEmpty()) {
            return true;   // "something" not like ""
        }

        // Cache pattern only if pattern string hasn't changed
        if (cachedPattern == null || !rs.equals(cachedPatternString)) {
            String regex = convertWildcardToRegex(rs);
            cachedPattern = Pattern.compile(regex);
            cachedPatternString = rs;
        }
        return !cachedPattern.matcher(ls).matches();  // NOT like - invert the result
    }

    /**
     * Convert wildcard pattern (* for any characters) to Java regex
     * Special regex characters are escaped except *
     */
    private String convertWildcardToRegex(String pattern) {
        StringBuilder regex = new StringBuilder();
        for (int i = 0; i < pattern.length(); i++) {
            char ch = pattern.charAt(i);
            if (ch == '*') {
                regex.append(".*");
            } else if ("\\[]{}()+?.^$|".indexOf(ch) >= 0) {
                // Escape special regex characters
                regex.append('\\').append(ch);
            } else {
                regex.append(ch);
            }
        }
        return regex.toString();
    }

    @Override
    public String operatorString() {
        return "not like";
    }

    @Override
    public Dexpr evaluate(Object param) throws DexprException {
        return bool(compare(param));
    }

    public String toString() {
        return left + operatorString() + right;
    }
}
