package com.github.comelf.dexpr;

import com.github.comelf.dexpr.err.DexprException;

public enum DexprType {

    OPERATOR_MUL(0), // *
    OPERATOR_DIV(1), // /
    OPERATOR_MOD(2), // %

    OPERATOR_ADD(10), // +
    OPERATOR_SUB(11), // -
    OPERATOR_GT(20), // < greater than
    OPERATOR_LT(21), // > lower than
    OPERATOR_GTE(22), // <= greater than or equal
    OPERATOR_LTE(23), // >= lower than or equal
    OPERATOR_EQUAL(30), // ==
    OPERATOR_NOT_EQUAL(31), // !=
    OPERATOR_LIKE(32), // like
    OPERATOR_NOT_LIKE(33), // not like
    OPERATOR_AND(40), // && and
    OPERATOR_OR(41), // || or , Short Circuit Operation

    OPERATOR_TERNARY_QUESTION(42), // ?, 3항 연산자
    OPERATOR_TERNARY_COLON(43), // :, 3항 연산자
    OPERATOR_TERNARY(49),

    OPERATOR_EXPONENTIATION(57),

    FUNCTION(91),
    BASE_VAL(100),
    NULL(101),
    BOOLEAN(102),
    DECIMAL(103),
    REAL(104),
    OBJECT(105),
    STRING(106),
    DOUBLE(107),
    INTEGER(108),

    ARRAY(110),

    VARIABLE(120),

    PARENTHESIS_OPEN(50), // '('
    PARENTHESIS_CLOSE(51), // ')'
    BRACKET_OPEN(1001),  // '['
    BRACKET_CLOSE(1002),  // ']'
    COMMA(1003),

    STRING_START(1004),  // 문자열 시작
    STRING_END(1005),    // 문자열 종료
    STRING_PART(1006),   // 문자열 일부
    VAR_START(1007),     // 변수 시작 ${
    VAR_END(1008),       // 변수 종료 }

    EXPRESSION(2000),


    OPERATOR_MATH(5001),        // +, -, *, /, %
    OPERATOR_COMPARISON(5002),  // <, <=, >, >=, ==, !=
    OPERATOR_LOGICAL(5003),     // &&, ||
    OPERATOR_EQUALITY(5004),    // ==, !=
    OPERATOR_INEQUALITY(5005)   // <, <=, >, >=
    ;


    private final int id;

    DexprType(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static void assertType(Dexpr e, DexprType... types) throws DexprException {
        for (DexprType t : types) {
            if (t.equals(e.type))
                return;
        }
        throw new DexprException("Unexpected type: " + e.type);
    }

    public boolean equalType(DexprType type) {
        switch (type) {
            case OPERATOR_MATH:
                return isMath();
            case OPERATOR_COMPARISON:
                return isComparison();
            case OPERATOR_LOGICAL:
                return isLogical();
            case OPERATOR_EQUALITY:
                return isEquality();
            case OPERATOR_INEQUALITY:
                return isInequality();
            default:
                return this.equals(type);
        }
    }

    private boolean isInequality() {
        switch (this) {
            case OPERATOR_GT:
            case OPERATOR_GTE:
            case OPERATOR_LT:
            case OPERATOR_LTE:
                return true;
            default:
                return false;
        }
    }

    private boolean isEquality() {
        switch (this) {
            case OPERATOR_EQUAL:
            case OPERATOR_NOT_EQUAL:
                return true;
            default:
                return false;
        }
    }

    private boolean isLogical() {
        switch (this) {
            case OPERATOR_AND:
            case OPERATOR_OR:
                return true;
            default:
                return false;
        }
    }

    private boolean isComparison() {
        switch (this) {
            case OPERATOR_GT:
            case OPERATOR_GTE:
            case OPERATOR_LT:
            case OPERATOR_LTE:
            case OPERATOR_EQUAL:
            case OPERATOR_NOT_EQUAL:
                return true;
            default:
                return false;
        }
    }

    private boolean isMath() {
        switch (this) {
            case OPERATOR_MUL:
            case OPERATOR_DIV:
            case OPERATOR_MOD:
            case OPERATOR_ADD:
            case OPERATOR_SUB:
                return true;
            default:
                return false;
        }
    }

}
