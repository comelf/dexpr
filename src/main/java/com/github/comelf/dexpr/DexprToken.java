package com.github.comelf.dexpr;


public class DexprToken {

    public DexprToken(DexprType type, Object value) {
        this.type = type;
        this.value = value;
    }

    public DexprType type;
    public Object value;
    public int mux = 1;

    public String toString() {
        if (mux == 1) {
            return "YyToken (" + type + ": " + value + ")";
        } else {
            return "YyToken (" + type + ": " + value + " x=" + mux + ")";
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + type.getId();
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DexprToken other = (DexprToken) obj;
        if (!type.equals(other.type))
            return false;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }

    public boolean isNumber() {
        return typeOf(DexprType.DECIMAL) || typeOf(DexprType.REAL) || value instanceof Number;
    }

    public boolean typeOf(DexprType type) {
        if (type == null) {
            return false;
        }
        return this.type.equals(type);
    }

    public boolean isNotOperator() {
        if (type == null) {
            return true;
        }
        switch (type) {
            case OPERATOR_MUL:
            case OPERATOR_DIV:
            case OPERATOR_MOD:
            case OPERATOR_ADD:
            case OPERATOR_SUB:
            case OPERATOR_GT:
            case OPERATOR_GTE:
            case OPERATOR_LT:
            case OPERATOR_LTE:
            case OPERATOR_AND:
            case OPERATOR_OR:
            case OPERATOR_EQUAL:
            case OPERATOR_NOT_EQUAL:
            case OPERATOR_LIKE:
            case OPERATOR_TERNARY_QUESTION:
            case OPERATOR_TERNARY_COLON:
            case OPERATOR_TERNARY:
                return false;
        }
        return true;
    }
}
