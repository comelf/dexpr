package com.github.comelf.dexpr.operation;

import com.github.comelf.dexpr.DexprType;
import com.github.comelf.dexpr.err.DexprException;

import java.util.EnumMap;
import java.util.Map;

public class OperatorFactory {

    /**
     * Interface for operator creation strategy
     */
    @FunctionalInterface
    private interface OperatorCreator {
        AbstractBinaryOperator create();
    }

    /**
     * EnumMap for fast operator type lookup
     * EnumMap is internally backed by array, providing O(1) performance
     */
    private static final Map<DexprType, OperatorCreator> OPERATOR_MAP;

    static {
        // Initialize with expected size for all operator types
        OPERATOR_MAP = new EnumMap<>(DexprType.class);

        // Mathematical operators
        OPERATOR_MAP.put(DexprType.OPERATOR_MUL, () -> new DexprMultiplication(null, null));
        OPERATOR_MAP.put(DexprType.OPERATOR_DIV, () -> new DexprDivision(null, null));
        OPERATOR_MAP.put(DexprType.OPERATOR_MOD, () -> new DexprModular(null, null));
        OPERATOR_MAP.put(DexprType.OPERATOR_ADD, () -> new DexprAddition(null, null));
        OPERATOR_MAP.put(DexprType.OPERATOR_SUB, () -> new DexprSubtraction(null, null));

        // Comparison operators
        OPERATOR_MAP.put(DexprType.OPERATOR_EQUAL, () -> new DexprEqual(null, null));
        OPERATOR_MAP.put(DexprType.OPERATOR_NOT_EQUAL, () -> new DexprNotEqual(null, null));
        OPERATOR_MAP.put(DexprType.OPERATOR_LT, () -> new DexprLessThan(null, null));
        OPERATOR_MAP.put(DexprType.OPERATOR_LTE, () -> new DexprLessThanEqual(null, null));
        OPERATOR_MAP.put(DexprType.OPERATOR_GT, () -> new DexprGreaterThan(null, null));
        OPERATOR_MAP.put(DexprType.OPERATOR_GTE, () -> new DexprGreaterThanEqual(null, null));

        // Logical operators
        OPERATOR_MAP.put(DexprType.OPERATOR_AND, () -> new DexprAnd(null, null));
        OPERATOR_MAP.put(DexprType.OPERATOR_OR, () -> new DexprOr(null, null));

        // String operators
        OPERATOR_MAP.put(DexprType.OPERATOR_LIKE, () -> new DexprLike(null, null));
        OPERATOR_MAP.put(DexprType.OPERATOR_NOT_LIKE, () -> new DexprNotLike(null, null));
    }

    /**
     * Creates a new operator instance based on the given type.
     *
     * @param type The operator type
     * @return A new instance of the corresponding operator
     *
     * @throws DexprException if the operator type is not supported
     */
    public static AbstractBinaryOperator createOperator(DexprType type) throws DexprException {
        OperatorCreator creator = OPERATOR_MAP.get(type);
        if (creator == null) {
            throw new DexprException("Unsupported operator type: " + type);
        }
        return creator.create();
    }

    /**
     * Checks if the given type is a supported operator.
     *
     * @param type The type to check
     * @return true if the type is a supported operator
     */
    public static boolean isSupportedOperator(DexprType type) {
        return OPERATOR_MAP.containsKey(type);
    }

    /**
     * Returns the number of registered operator types.
     * Useful for testing and validation.
     *
     * @return The number of registered operators
     */
    public static int getRegisteredOperatorCount() {
        return OPERATOR_MAP.size();
    }

    // Private constructor to prevent instantiation
    private OperatorFactory() {
        throw new AssertionError("OperatorFactory should not be instantiated");
    }
}