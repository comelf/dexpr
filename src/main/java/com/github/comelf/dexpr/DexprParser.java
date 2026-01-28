package com.github.comelf.dexpr;

import com.github.comelf.dexpr.err.DexprException;
import com.github.comelf.dexpr.err.DexprTokenException;
import com.github.comelf.dexpr.function.Function;
import com.github.comelf.dexpr.function.FunctionLoader;
import com.github.comelf.dexpr.operation.*;
import com.github.comelf.dexpr.util.CastUtil;
import com.github.comelf.dexpr.val.*;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Objects;

public class DexprParser {
    private static final int MAX_RULE_LENGTH = 1000;

    protected Dexpr current;

    public static Dexpr parse(String rule) throws Exception {
        if (Objects.requireNonNull(rule).length() > MAX_RULE_LENGTH) {
            throw new DexprException(
                String.format("Rule exceeds maximum length: %d > %d",
                    rule.length(),
                    MAX_RULE_LENGTH)
            );
        }

        return parse(rule, true);
    }

    public static Dexpr parse(String rule, boolean useCache) throws Exception {
        Dexpr e = null;
//        if (useCache) {
//            e = DexprCache.getCache(rule);
//            if (e != null) {
//                return e;
//            }
//        }

        DexprParser p = new DexprParser();
        p.parse(new DexprFlex(new StringReader(rule)));
        e = p.get();
//        if (useCache && e != null) {
//            DexprCache.putCache(rule, e);
//        }
        return e;
    }

    public void parse(DexprFlex lexer) throws Exception {
        DexprToken t = null;
        while ((t = lexer.yylex()) != null) {
            parseToken(lexer, t);
        }
    }

    protected void parseToken(DexprFlex lexer, DexprToken token) throws DexprException, DexprTokenException, IOException {
        switch (token.type) {
            case OPERATOR_MUL:
            case OPERATOR_DIV:
            case OPERATOR_MOD:
            case OPERATOR_ADD:
            case OPERATOR_LIKE:
            case OPERATOR_NOT_LIKE:
            case OPERATOR_AND:
            case OPERATOR_OR:
            case OPERATOR_EQUAL:
            case OPERATOR_NOT_EQUAL:
            case OPERATOR_GT:
            case OPERATOR_LT:
            case OPERATOR_GTE:
            case OPERATOR_LTE:
                parseOperator(token);
                break;
            case OPERATOR_SUB:
                parseMinusOperator(lexer, token);
                break;
            case DECIMAL:
            case BASE_VAL:
            case NULL:
            case BOOLEAN:
            case REAL:
            case OBJECT:
            case STRING:
            case DOUBLE:
            case INTEGER:
                parseValue(token);
                break;
            case STRING_START:
                parseString(token, lexer);
                break;
            case BRACKET_OPEN:
                parseArray(lexer);
                break;
            case FUNCTION:
                parseFunction(token, lexer);
                break;
            case VARIABLE:
                parseVariable(token, lexer);
                break;
            case PARENTHESIS_OPEN:
                parseExpression(lexer);
                break;
            case OPERATOR_EXPONENTIATION:
                break;
            case OPERATOR_TERNARY_QUESTION:
                parseTernaryOperator(lexer);
                break;
            default:
                throw new DexprException("Unexpected " + token.type + " found");
        }
    }

    private void parseTernaryOperator(DexprFlex lexer) throws DexprException, DexprTokenException, IOException {
        if (current == null) {
            throw new DexprException("Invalid operation condition");
        }
        if (current instanceof AbstractMathOperator) {
            throw new DexprException("Mathematical operations cannot be conditions.");
        }

        ArrayDeque<Dexpr> stack = new ArrayDeque<>();
        while (current instanceof DexprTernaryOperator) {
            stack.push(current);
            current = ((DexprTernaryOperator) current).getRight();
        }

        Dexpr condition = current;
        current = null;

        Dexpr trueValue = null;
        Dexpr falseValue = null;

        DexprToken e = null;
        while ((e = lexer.yylex()) != null) {
            if (e.typeOf(DexprType.OPERATOR_TERNARY_COLON)) {
                if (current == null) {
                    throw new DexprException("Ternary operator true branch cannot be empty");
                }
                trueValue = current;
                current = null;
                break;
            } else if (e.typeOf(DexprType.VARIABLE)) {
                setValue(new DexprVariable(CastUtil.toStr(e.value)));
            } else {
                parseToken(lexer, e);
            }
        }

        if (trueValue == null) {
            throw new DexprException("Ternary operator true branch not found");
        }

        while ((e = lexer.yylex()) != null) {
            if (e.typeOf(DexprType.VARIABLE)) {
                setValue(new DexprVariable(CastUtil.toStr(e.value)));
                falseValue = current;
                current = null;
                break;
            } else if (e.isNumber() || e.isNotOperator()) {
                parseToken(lexer, e);
                falseValue = current;
                current = null;
                break;
            }
        }

        if (falseValue == null) {
            falseValue = current;
        }

        if (falseValue == null) {
            throw new DexprException("Ternary operator false branch cannot be empty");
        }
        current = new DexprTernaryOperator(condition, trueValue, falseValue);

        while (!stack.isEmpty()) {
            Dexpr prev = stack.pop();
            current = new DexprTernaryOperator(((DexprTernaryOperator) prev).getCondition(), ((DexprTernaryOperator) prev).getLeft(), current);
        }
    }

    private void parseVariable(DexprToken token, DexprFlex lexer) throws DexprException, DexprTokenException, IOException {
        DexprToken e = lexer.yylex();
        if (e != null && (e.typeOf(DexprType.PARENTHESIS_OPEN) || e.typeOf(DexprType.BRACKET_OPEN))) {
            parseFunction(token, lexer);
        } else {
            setValue(new DexprVariable(CastUtil.toStr(token.value)));
            if (e != null) {
                if (e.typeOf(DexprType.PARENTHESIS_CLOSE) || e.typeOf(DexprType.BRACKET_CLOSE) || e.typeOf(DexprType.OPERATOR_TERNARY_COLON) || e.typeOf(DexprType.COMMA)) {
                    lexer.revertPosition();
                } else {
                    parseToken(lexer, e);
                }
            }
        }
    }

    private void parseString(DexprToken token, DexprFlex lexer) throws DexprTokenException, IOException, DexprException {
        StringBuilder sb = new StringBuilder();
        ArrayList<Object> parts = new ArrayList<>();

        DexprToken e = null;
        while ((e = lexer.yylex()) != null) {
            if (e.typeOf(DexprType.STRING_END)) {
                // 문자열 종료 - 마지막으로 누적된 텍스트가 있으면 추가
                if (sb.length() > 0) {
                    parts.add(sb.toString());
                    sb.setLength(0);
                }
                break;
            } else if (e.typeOf(DexprType.STRING_PART)) {
                // 일반 문자열 부분 - StringBuilder에 누적
                sb.append(CastUtil.toStr(e.value));
            } else if (e.typeOf(DexprType.VARIABLE)) {
                // 변수 발견 - 지금까지 누적된 텍스트를 parts에 추가하고 변수 추가
                if (sb.length() > 0) {
                    parts.add(sb.toString());
                    sb.setLength(0);
                }
                // $name 형식의 경우 $ 제거
                String varName = CastUtil.toStr(e.value);
                if (varName.startsWith("$")) {
                    varName = varName.substring(1);
                }
                parts.add(new DexprVariable(varName));
            } else if (e.typeOf(DexprType.VAR_START)) {
                // ${...} 형식의 변수 시작
                if (sb.length() > 0) {
                    parts.add(sb.toString());
                    sb.setLength(0);
                }

                // VAR_START 다음 토큰은 VARIABLE이어야 함
                DexprToken varToken = lexer.yylex();
                if (varToken == null || !varToken.typeOf(DexprType.VARIABLE)) {
                    throw new DexprException("Expected variable name after ${");
                }
                parts.add(new DexprVariable(CastUtil.toStr(varToken.value)));

                // VAR_END 확인
                DexprToken endToken = lexer.yylex();
                if (endToken == null || !endToken.typeOf(DexprType.VAR_END)) {
                    throw new DexprException("Expected } after variable in string");
                }
            } else {
                throw new DexprException("Unexpected token in string: " + e.type);
            }
        }

        if (e == null || !e.typeOf(DexprType.STRING_END)) {
            throw new DexprException("Unclosed string");
        }

        // 결과를 DexprString으로 변환
        if (parts.isEmpty()) {
            // 빈 문자열
            setValue(DexprString.EMPTY);
        } else if (parts.size() == 1 && parts.get(0) instanceof String) {
            // 단순 문자열 (변수 보간 없음)
            setValue(new DexprString((String) parts.get(0)));
        } else {
            // 변수 보간이 있는 문자열
            setValue(new DexprStringInterpolation(parts));
        }
    }

    /**
     * Parse variable or function call with lookahead.
     * Returns the next token if it's a terminator, otherwise processes it.
     *
     * @param token The VARIABLE token
     * @param lexer The lexer
     * @return The next token if it's a terminator (caller should handle it), null otherwise
     */
    private DexprToken parseVariableOrFunction(DexprToken token, DexprFlex lexer) throws DexprException, DexprTokenException, IOException {
        DexprToken next = lexer.yylex();
        if (next != null && (next.typeOf(DexprType.PARENTHESIS_OPEN) || next.typeOf(DexprType.BRACKET_OPEN))) {
            // Function call
            parseFunction(token, lexer);
            return null;
        } else {
            // Variable
            setValue(new DexprVariable(CastUtil.toStr(token.value)));
            return next;
        }
    }

    // ========== Helper Methods for Code Deduplication ==========

    /**
     * Check if token is a terminator (comma, closing brackets/parenthesis, or colon)
     */
    private boolean isTerminator(DexprToken token) {
        return token.typeOf(DexprType.COMMA)
            || token.typeOf(DexprType.BRACKET_CLOSE)
            || token.typeOf(DexprType.PARENTHESIS_CLOSE)
            || token.typeOf(DexprType.OPERATOR_TERNARY_COLON);
    }

    /**
     * Check if token is a closing delimiter (closing brackets or parenthesis)
     */
    private boolean isClosingDelimiter(DexprToken token) {
        return token.typeOf(DexprType.BRACKET_CLOSE)
            || token.typeOf(DexprType.PARENTHESIS_CLOSE);
    }

    /**
     * Add current value to collection and reset current to null
     */
    private void addCurrentToCollection(ArrayList<Dexpr> collection) {
        if (current != null) {
            collection.add(current);
            current = null;
        }
    }

    /**
     * Process VARIABLE token with lookahead for function call detection.
     * Handles the common pattern of variable/function parsing in collections.
     *
     * @param varToken The VARIABLE token to process
     * @param lexer The lexer for lookahead
     * @param collection The collection to add results to
     * @return The lookahead token for caller to handle, or null if already processed
     */
    private DexprToken handleVariableToken(DexprToken varToken, DexprFlex lexer, ArrayList<Dexpr> collection)
            throws DexprException, DexprTokenException, IOException {
        DexprToken next = parseVariableOrFunction(varToken, lexer);

        if (next == null) {
            // parseVariableOrFunction handled everything (function call)
            return null;
        }

        // Handle common terminator patterns
        if (next.typeOf(DexprType.COMMA)) {
            addCurrentToCollection(collection);
            return next; // Return COMMA so caller knows we hit a separator
        } else if (isClosingDelimiter(next)) {
            addCurrentToCollection(collection);
            return next; // Return closing delimiter so caller can break
        }

        // For other tokens, caller should process them
        return next;
    }

    private void parseFunction(DexprToken token, DexprFlex lexer) throws DexprException, DexprTokenException, IOException {
        Dexpr c = current;
        current = null;

        DexprToken e = null;
        ArrayList<Dexpr> args = new ArrayList<>(8);  // Initial capacity for typical function calls
        while ((e = lexer.yylex()) != null) {
            if (e.typeOf(DexprType.COMMA)) {
                addCurrentToCollection(args);
            } else if (isClosingDelimiter(e)) {
                addCurrentToCollection(args);
                break;
            } else if (e.typeOf(DexprType.VARIABLE)) {
                // Support nested function calls: sum(isNull(x), getValue(y))
                DexprToken next = handleVariableToken(e, lexer, args);
                if (next != null && isClosingDelimiter(next)) {
                    e = next;
                    break;
                } else if (next != null && !next.typeOf(DexprType.COMMA)) {
                    // handleVariableToken didn't process this token, so we need to
                    parseToken(lexer, next);
                }
            } else {
                parseToken(lexer, e);
            }
        }

        if (e == null || !isClosingDelimiter(e)) {
            throw new DexprException("Unclosed function parenthesis: " + token.value);
        }

        Function function = FunctionLoader.getInstance().getFunction((String) token.value);
        if (function == null) {
            throw new DexprException("function not found: " + token.value);
        }
        this.current = c;
        setValue(new DexprFunction(function, args));
    }

    private void parseExpression(DexprFlex lexer) throws DexprException, DexprTokenException, IOException {
        Dexpr c = current;
        current = null;

        DexprToken e = null;
        while ((e = lexer.yylex()) != null) {
            if (e.typeOf(DexprType.PARENTHESIS_CLOSE)) {
                finalizeExpression(c);
                break;
            } else if (e.typeOf(DexprType.VARIABLE)) {
                // Support function calls in expressions: (isNotNull(x) && isNull(y))
                DexprToken next = parseVariableOrFunction(e, lexer);
                if (next != null && next.typeOf(DexprType.PARENTHESIS_CLOSE)) {
                    // This is the closing parenthesis - finalize expression
                    e = next;
                    finalizeExpression(c);
                    break;
                } else if (next != null) {
                    // Process the next token
                    parseToken(lexer, next);
                }
            } else {
                parseToken(lexer, e);
            }
        }
        if (e == null || !e.typeOf(DexprType.PARENTHESIS_CLOSE)) {
            throw new DexprException("Unclosed parenthesis");
        }
    }

    /**
     * Helper method to finalize expression parsing
     */
    private void finalizeExpression(Dexpr savedCurrent) throws DexprException {
        Dexpr expr = current;
        current = savedCurrent;
        setValue(new DexprExpression(expr));
    }

    private void parseArray(DexprFlex lexer) throws DexprException, DexprTokenException, IOException {
        Dexpr c = current;
        current = null;

        DexprToken e = null;
        int cols = -1;
        ArrayList<Dexpr> args = new ArrayList<>(4);  // Initial capacity for typical arrays
        while ((e = lexer.yylex()) != null) {
            if (e.typeOf(DexprType.COMMA)) {
                // Arrays cannot have empty values, so check before adding
                if (current == null) {
                    throw new DexprException("Arrays cannot contain empty values");
                }
                addCurrentToCollection(args);
            } else if (e.typeOf(DexprType.BRACKET_CLOSE)) {
                addCurrentToCollection(args);
                break;
            } else if (e.typeOf(DexprType.VARIABLE)) {
                // Support function calls in arrays: [isNull(x), getValue(y), 123]
                DexprToken next = handleVariableToken(e, lexer, args);
                if (next != null) {
                    // Check for empty values when we hit a comma
                    if (next.typeOf(DexprType.COMMA) && args.isEmpty() && current == null) {
                        throw new DexprException("Arrays cannot contain empty values");
                    } else if (next.typeOf(DexprType.BRACKET_CLOSE)) {
                        e = next;
                        break;
                    } else if (!next.typeOf(DexprType.COMMA)) {
                        parseToken(lexer, next);
                    }
                }
            } else {
                parseToken(lexer, e);
            }
        }

        if (e == null || !e.typeOf(DexprType.BRACKET_CLOSE)) {
            throw new DexprException("Unclosed array bracket");
        }

        int rows = 1;
        if (cols == -1)
            cols = args.size();
        else
            rows = args.size() / cols;
        DexprArray a = new DexprArray(rows, cols);
        for (int i = 0; i < args.size(); i++) {
            a.set(0, i, args.get(i));
        }

        current = c;
        setValue(a);
    }

    private void parseValue(DexprToken e) throws DexprException {
        Dexpr value = null;
        switch (e.type) {
            case BASE_VAL:
            case DOUBLE:
            case DECIMAL:
                value = new DexprDouble(CastUtil.toDouble(e.value));
                break;
            case BOOLEAN:
                value = CastUtil.toBoolean(e.value) ? DexprBoolean.TRUE : DexprBoolean.FALSE;
                break;
            case REAL:
                break;
            case OBJECT:
            case STRING:
                value = isNumberic(e.value) ? new DexprDouble(CastUtil.toDouble(e.value)) : new DexprString(CastUtil.toStr(e.value));
                break;
            case VARIABLE:
                value = new DexprVariable(CastUtil.toStr(e.value));
                break;
            case NULL:
                value = new DexprNull();
        }
        setValue(value);
    }

    private boolean isNumberic(Object value) {
        if (value == null) {
            return false;
        }
        try {
            double dv = Double.parseDouble(value.toString());
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    private void setValue(Dexpr value) throws DexprException {
        if (current == null) {
            current = value;
            return;
        } else {
            Dexpr c = current;
            do {
                if (!(c instanceof IBinaryOperator))
                    throw new DexprException("Expected operator not found");

                Dexpr right = ((IBinaryOperator) c).getRight();
                if (right == null) {
                    ((IBinaryOperator) c).setRight(value);
                    return;
                } else {
                    c = right;
                }
            } while (c != null);

            throw new DexprException("Unexpected token found");
        }
    }

    private void parseMinusOperator(DexprFlex lexer, DexprToken token) throws DexprException, DexprTokenException, IOException {
        // Determine if this is unary or binary minus based on context
        boolean isUnary = false;

        if (current == null) {
            // At the beginning of expression: unary
            isUnary = true;
        } else if (current instanceof IBinaryOperator) {
            // After an operator, check if it expects a right operand
            IBinaryOperator binOp = (IBinaryOperator) current;
            if (binOp.getRight() == null) {
                // Operator is waiting for right operand: unary
                isUnary = true;
            } else {
                // Navigate to the rightmost operator to check
                Dexpr c = binOp.getRight();
                while (c instanceof IBinaryOperator && ((IBinaryOperator) c).getRight() != null) {
                    c = ((IBinaryOperator) c).getRight();
                }
                if (c instanceof IBinaryOperator && ((IBinaryOperator) c).getRight() == null) {
                    isUnary = true;
                }
            }
        }

        if (isUnary) {
            // Parse as unary minus
            DexprToken nextToken = lexer.yylex();
            if (nextToken == null) {
                throw new DexprException("Unary minus requires an operand");
            }

            // Save current and temporarily set to null to parse the operand
            Dexpr savedCurrent = current;
            current = null;

            // Parse the operand
            parseToken(lexer, nextToken);

            if (current == null) {
                throw new DexprException("Unary minus operand missing");
            }

            // Create unary minus with the parsed operand
            DexprUnaryMinus unaryMinus = new DexprUnaryMinus(current);

            // Restore context and set the unary minus as value
            current = savedCurrent;
            setValue(unaryMinus);
        } else {
            // Parse as binary minus
            parseOperator(token);
        }
    }

    private void parseOperator(DexprToken e) throws DexprException {
        // Use OperatorFactory for cleaner and more efficient operator creation
        // EnumMap provides O(1) lookup, faster than switch for many cases
        AbstractBinaryOperator operator = OperatorFactory.createOperator(e.type);
        setOperator(operator);
    }

    protected void setOperator(AbstractBinaryOperator op) {
        Dexpr c = current;
        // DexprExpression은 값처럼 처리되므로 operator 재배치 대상이 아님
        if (c instanceof DexprExpression) {
            currentToLeft(op);
            return;
        }

        if (c instanceof AbstractBinaryOperator && c.getOrder() > op.getOrder()) {
            AbstractBinaryOperator prev = null;
            while (c != null) {
                // DexprExpression은 값이므로 건너뛰기
                if (c instanceof DexprExpression) {
                    break;
                }
                if (c instanceof AbstractBinaryOperator && c.getOrder() > op.getOrder()) {
                    prev = (AbstractBinaryOperator) c;
                    c = ((IBinaryOperator) c).getRight();
                } else {
                    if (prev == null) {
                        currentToLeft(op);
                        return;
                    } else {
                        if (prev.getOrder() > op.getOrder()) {
                            op.setLeft(prev.getRight());
                            prev.setRight(op);
                        } else {
                            op.setLeft(prev);
                            ((IBinaryOperator) current).setRight(op);
                        }
                        return;
                    }
                }
            }
            // 여기까지 왔다면 DexprExpression을 만난 경우
            if (prev == null) {
                currentToLeft(op);
            } else {
                op.setLeft(prev.getRight());
                prev.setRight(op);
            }
        } else {
            currentToLeft(op);
        }
    }

    private void currentToLeft(AbstractBinaryOperator md) {
        md.setLeft(current);
        current = md;
    }

    public Dexpr get() throws DexprException {
        if (this.current == null) {
            throw new DexprException("Expression is empty or incomplete");
        }
        this.current.validate();
        return current;
    }

}
