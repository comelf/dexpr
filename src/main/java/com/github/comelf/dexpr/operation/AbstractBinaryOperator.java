package com.github.comelf.dexpr.operation;


import com.github.comelf.dexpr.Dexpr;
import com.github.comelf.dexpr.DexprType;
import com.github.comelf.dexpr.err.DexprException;
import com.github.comelf.dexpr.util.CompareUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class AbstractBinaryOperator extends DexprEvaluatable implements IBinaryOperator {

    protected Dexpr left;
    protected Dexpr right;

    public AbstractBinaryOperator(DexprType type, Dexpr left, Dexpr right) {
        super(type);
        this.left = left;
        this.right = right;
    }

    public Dexpr getLeft() {
        return left;
    }

    public void setLeft(Dexpr left) {
        this.left = left;
    }

    public Dexpr getRight() {
        return right;
    }

    public void setRight(Dexpr right) {
        this.right = right;
    }

    public Dexpr popRight() {
        Dexpr r = this.right;
        this.right = null;
        return r;
    }

    public List<Dexpr> getArgs() {
        return Arrays.asList(left, right);
    }

    public void validate() throws DexprException {
        if (left == null)
            throw new DexprException("Left of operator missing");
        left.validate();
        if (right == null)
            throw new DexprException("Right of operator missing");
        right.validate();
    }

    public boolean isExpectable(DexprType type) {
        if (type == null) {
            return false;
        }
        if (type.getId() < 100 || type.getId() > 1000) {
            return false;
        }
        switch (type) {
            case BOOLEAN:
            case ARRAY:
            case VARIABLE:
            case OBJECT:
            case STRING:
                return false;
            default:
                return true;
        }
    }

    public int hashCode() {
        int hash = type.ordinal();
        if (left != null)
            hash ^= left.hashCode();
        if (right != null)
            hash ^= right.hashCode();
        return hash;
    }

    public boolean equals(Object obj) {
        if (!obj.getClass().equals(getClass()))
            return false;

        AbstractBinaryOperator b = (AbstractBinaryOperator) obj;
        return CompareUtil.equals(b.left, left) && CompareUtil.equals(b.right, right);
    }

    public void setRightOperator(AbstractBinaryOperator op) throws DexprException {
        if (this.right == null) {
            throw new DexprException("Unexpected right null");
        }
        if (getOrder() > op.getOrder()) {
            Dexpr c = right;
            Dexpr prev = null;
            while (c != null) {
                if (c instanceof AbstractBinaryOperator) {
                    prev = c;
                    c = ((AbstractBinaryOperator) c).getRight();
                } else {
                    if (prev == null) {
                        op.setLeft(right);
                        right = op;
                        return;
                    } else {
                        AbstractBinaryOperator b = (AbstractBinaryOperator) prev;
                        op.setLeft(b.getRight());
                        b.setRightOperator(op);
                        return;
                    }
                }
            }
        } else {
            op.setRight(this.right);
            this.right = op;
        }
    }

    @Override
    public void printTree() {
        printTree(0);
        System.out.println();
    }

    public abstract String operatorString();

    private void printTree(int depth) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            sb.append("  ");
        }
        String space = sb.toString();

        System.out.println(space + (depth != 0 ? "└ " : "") + "[" + operatorString() + "]");
        if (left != null) {
            if (left instanceof AbstractBinaryOperator) {
                ((AbstractBinaryOperator) left).printTree(depth + 1);
            } else {
                try {
                    System.out.println(space + "└ " + left.produce(null));
                } catch (DexprException e) {
                    throw new RuntimeException("left wexl is not BinaryOperator");
                }
            }
        }
        if (right != null) {
            if (right instanceof AbstractBinaryOperator) {
                ((AbstractBinaryOperator) right).printTree(depth + 1);
            } else {
                try {
                    System.out.println(space + "└ " + right.produce(null));
                } catch (DexprException e) {
                    throw new RuntimeException("right wexl is not BinaryOperator");
                }
            }
        }
    }

    public List<Dexpr> findAll(DexprType type) {
        if (type == null) {
            return Collections.emptyList();
        }
        List<Dexpr> list = new ArrayList<>();
        if (this.type.equalType(type)) {
            list.add(this);
        }
        if (left != null) {
            list.addAll(left.findAll(type));
        }
        if (right != null) {
            list.addAll(right.findAll(type));
        }
        return list;
    }

}
