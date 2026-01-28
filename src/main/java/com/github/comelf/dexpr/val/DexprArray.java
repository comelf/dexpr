package com.github.comelf.dexpr.val;

import com.github.comelf.dexpr.Dexpr;
import com.github.comelf.dexpr.DexprType;
import com.github.comelf.dexpr.err.DexprException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DexprArray extends DexprValue {
    private int columns;
    private int rows;
    private Dexpr[] array;

    public DexprArray(int rows, int columns) {
        super(DexprType.ARRAY);
        this.array = new Dexpr[rows * columns];
        this.columns = columns;
        this.rows = rows;
    }

    public int rows() {
        return rows;
    }

    public int columns() {
        return columns;
    }

    public int length() {
        return array.length;
    }

    public Dexpr get(int index) {
        return array[index];
    }

    public Dexpr get(int row, int column) {
        return array[row * columns + column];
    }

    public void set(int index, Dexpr value) {
        array[index] = value;
    }

    public void set(int row, int column, Dexpr value) {
        array[row * columns + column] = value;
    }

    public void set(int row, int column, String value) {
        set(row, column, new DexprString(value));
    }

    public void set(int row, int column, double value) {
        set(row, column, new DexprDouble(value));
    }

    public void set(int row, int column, int value) {
        set(row, column, new DexprInteger(value));
    }

    public void set(int row, int column, boolean value) {
        set(row, column, new DexprBoolean(value));
    }

    public List<Dexpr> getArgs() {
        return Arrays.asList(array);
    }

    public int hashCode() {
        return 567 ^ rows ^ columns ^ array.length;
    }

    @Override
    public Object produce(Object param) throws DexprException {
        List<Object> result = new ArrayList<>(this.array.length);
        for (Dexpr e : array) {
            result.add(e.produce(param));
        }
        return result;
    }

    @Override
    public boolean isExpectable(DexprType type) {
        return DexprType.ARRAY.equals(type);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof DexprArray))
            return false;

        DexprArray a = (DexprArray) obj;
        return a.rows == rows && a.columns == columns &&
                Arrays.equals(a.array, array);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            sb.append("[");
            for (int j = 0; j < columns; j++) {
                if (j > 0)
                    sb.append(",");
                sb.append(get(i, j));
            }
            sb.append("]\n");
        }
        return sb.toString();
    }
}
