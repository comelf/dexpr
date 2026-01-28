package com.github.comelf.dexpr.operation;


import com.github.comelf.dexpr.Dexpr;

public interface IBinaryOperator {
    Dexpr getLeft();

    void setLeft(Dexpr left);

    Dexpr getRight();

    void setRight(Dexpr right);

    void printTree();
}
