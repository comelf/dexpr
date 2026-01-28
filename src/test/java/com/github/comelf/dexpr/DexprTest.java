package com.github.comelf.dexpr;

import com.github.comelf.dexpr.err.DexprException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class DexprTest {

    @Nested
    @DisplayName("산술 연산 테스트")
    class ArithmeticOperationTest {

        @Test
        @DisplayName("덧셈 연산")
        public void testAddition() throws Exception {
            Dexpr w = DexprParser.parse("${a} + ${b}");
            Map<String, Object> vars = new HashMap<>();
            vars.put("a", 10);
            vars.put("b", 20);
            assertEquals(30.0, w.produce(vars));
        }

        @Test
        @DisplayName("뺄셈 연산")
        public void testSubtraction() throws Exception {
            Dexpr w = DexprParser.parse("${a} - ${b}");
            Map<String, Object> vars = new HashMap<>();
            vars.put("a", 50);
            vars.put("b", 30);
            assertEquals(20.0, w.produce(vars));
        }

        @Test
        @DisplayName("곱셈 연산")
        public void testMultiplication() throws Exception {
            Dexpr w = DexprParser.parse("${a} * ${b}");
            Map<String, Object> vars = new HashMap<>();
            vars.put("a", 5);
            vars.put("b", 4);
            assertEquals(20.0, w.produce(vars));
        }

        @Test
        @DisplayName("나눗셈 연산")
        public void testDivision() throws Exception {
            Dexpr w = DexprParser.parse("${a} / ${b}");
            Map<String, Object> vars = new HashMap<>();
            vars.put("a", 100);
            vars.put("b", 4);
            assertEquals(25.0, w.produce(vars));
        }

        @Test
        @DisplayName("나머지 연산")
        public void testModular() throws Exception {
            Dexpr w = DexprParser.parse("${a} % ${b}");
            Map<String, Object> vars = new HashMap<>();
            vars.put("a", 17);
            vars.put("b", 5);
            assertEquals(2.0, w.produce(vars));
        }

        @Test
        @DisplayName("복합 산술 연산 (우선순위)")
        public void testComplexArithmetic() throws Exception {
            Dexpr w = DexprParser.parse("${a} + ${b} * ${c}");
            Map<String, Object> vars = new HashMap<>();
            vars.put("a", 10);
            vars.put("b", 5);
            vars.put("c", 2);
            assertEquals(20.0, w.produce(vars)); // 10 + (5 * 2)
        }

        @Test
        @DisplayName("괄호를 사용한 연산 우선순위 변경")
        public void testParenthesis() throws Exception {
            Dexpr w = DexprParser.parse("(${a} + ${b}) * ${c}");
            Map<String, Object> vars = new HashMap<>();
            vars.put("a", 10);
            vars.put("b", 5);
            vars.put("c", 2);
            assertEquals(30.0, w.produce(vars)); // (10 + 5) * 2
        }
    }

    @Nested
    @DisplayName("비교 연산 테스트")
    class ComparisonOperationTest {

        @Test
        @DisplayName("크다 비교 (>)")
        public void testGreaterThan() throws Exception {
            Dexpr w = DexprParser.parse("${a} > ${b}");
            Map<String, Object> vars = new HashMap<>();
            vars.put("a", 10);
            vars.put("b", 5);
            assertTrue((Boolean) w.produce(vars));

            vars.put("a", 3);
            vars.put("b", 5);
            assertFalse((Boolean) w.produce(vars));
        }

        @Test
        @DisplayName("크거나 같다 비교 (>=)")
        public void testGreaterThanEqual() throws Exception {
            Dexpr w = DexprParser.parse("${a} >= ${b}");
            Map<String, Object> vars = new HashMap<>();
            vars.put("a", 10);
            vars.put("b", 10);
            assertTrue((Boolean) w.produce(vars));

            vars.put("a", 9);
            assertFalse((Boolean) w.produce(vars));
        }

        @Test
        @DisplayName("작다 비교 (<)")
        public void testLessThan() throws Exception {
            Dexpr w = DexprParser.parse("${a} < ${b}");
            Map<String, Object> vars = new HashMap<>();
            vars.put("a", 5);
            vars.put("b", 10);
            assertTrue((Boolean) w.produce(vars));

            vars.put("a", 15);
            assertFalse((Boolean) w.produce(vars));
        }

        @Test
        @DisplayName("작거나 같다 비교 (<=)")
        public void testLessThanEqual() throws Exception {
            Dexpr w = DexprParser.parse("${a} <= ${b}");
            Map<String, Object> vars = new HashMap<>();
            vars.put("a", 5);
            vars.put("b", 5);
            assertTrue((Boolean) w.produce(vars));

            vars.put("a", 6);
            assertFalse((Boolean) w.produce(vars));
        }

        @Test
        @DisplayName("같다 비교 (==)")
        public void testEqual() throws Exception {
            Dexpr w = DexprParser.parse("${a} == ${b}");
            Map<String, Object> vars = new HashMap<>();
            vars.put("a", 5);
            vars.put("b", 5);
            assertTrue((Boolean) w.produce(vars));

            vars.put("b", 10);
            assertFalse((Boolean) w.produce(vars));
        }

        @Test
        @DisplayName("같지 않다 비교 (!=)")
        public void testNotEqual() throws Exception {
            Dexpr w = DexprParser.parse("${a} != ${b}");
            Map<String, Object> vars = new HashMap<>();
            vars.put("a", 5);
            vars.put("b", 10);
            assertTrue((Boolean) w.produce(vars));

            vars.put("b", 5);
            assertFalse((Boolean) w.produce(vars));
        }
    }

    @Nested
    @DisplayName("논리 연산 테스트")
    class LogicalOperationTest {

        @Test
        @DisplayName("AND 연산 (&&)")
        public void testAnd() throws Exception {
            Dexpr w = DexprParser.parse("${a} > 5 && ${b} < 10");
            Map<String, Object> vars = new HashMap<>();
            vars.put("a", 7);
            vars.put("b", 8);
            assertTrue((Boolean) w.produce(vars));

            vars.put("a", 3);
            assertFalse((Boolean) w.produce(vars));
        }

        @Test
        @DisplayName("OR 연산 (||)")
        public void testOr() throws Exception {
            Dexpr w = DexprParser.parse("${a} > 10 || ${b} < 5");
            Map<String, Object> vars = new HashMap<>();
            vars.put("a", 15);
            vars.put("b", 8);
            assertTrue((Boolean) w.produce(vars));

            vars.put("a", 8);
            vars.put("b", 3);
            assertTrue((Boolean) w.produce(vars));

            vars.put("a", 8);
            vars.put("b", 8);
            assertFalse((Boolean) w.produce(vars));
        }

        @Test
        @DisplayName("복합 논리 연산")
        public void testComplexLogical() throws Exception {
            Dexpr w = DexprParser.parse("(${a} > 5 && ${b} < 10) || ${c} == 100");
            Map<String, Object> vars = new HashMap<>();
            vars.put("a", 7);
            vars.put("b", 8);
            vars.put("c", 50);
            assertTrue((Boolean) w.produce(vars));

            vars.put("a", 3);
            vars.put("c", 100);
            assertTrue((Boolean) w.produce(vars));

            vars.put("c", 50);
            assertFalse((Boolean) w.produce(vars));
        }
    }

    @Nested
    @DisplayName("삼항 연산자 테스트")
    class TernaryOperationTest {

        @Test
        @DisplayName("삼항 연산자 - true 케이스")
        public void testTernaryTrue() throws Exception {
            Dexpr w = DexprParser.parse("${a} > 5 ? ${b} : ${c}");
            Map<String, Object> vars = new HashMap<>();
            vars.put("a", 10);
            vars.put("b", 100);
            vars.put("c", 200);
            assertEquals(100.0, w.produce(vars));
        }

        @Test
        @DisplayName("삼항 연산자 - false 케이스")
        public void testTernaryFalse() throws Exception {
            Dexpr w = DexprParser.parse("${a} > 5 ? ${b} : ${c}");
            Map<String, Object> vars = new HashMap<>();
            vars.put("a", 3);
            vars.put("b", 100);
            vars.put("c", 200);
            assertEquals(200.0, w.produce(vars));
        }

        @Test
        @DisplayName("중첩 삼항 연산자")
        public void testNestedTernary() throws Exception {
            Dexpr w = DexprParser.parse("${a} > 10 ? ${b} : ${a} > 5 ? ${c} : ${d}");
            Map<String, Object> vars = new HashMap<>();
            vars.put("a", 7);
            vars.put("b", 100);
            vars.put("c", 200);
            vars.put("d", 300);
            assertEquals(200.0, w.produce(vars));
        }
    }

    @Nested
    @DisplayName("문자열 연산 테스트")
    class StringOperationTest {

        @Test
        @DisplayName("문자열 비교 (==)")
        public void testStringEqual() throws Exception {
            Dexpr w = DexprParser.parse("${name} == 'admin'");
            Map<String, Object> vars = new HashMap<>();
            vars.put("name", "admin");
            assertTrue((Boolean) w.produce(vars));

            vars.put("name", "user");
            assertFalse((Boolean) w.produce(vars));
        }

        @Test
        @DisplayName("문자열 Like 연산")
        public void testStringLike() throws Exception {
            Dexpr w = DexprParser.parse("${name} like 'ad%'");
            Map<String, Object> vars = new HashMap<>();
            vars.put("name", "admin");
            assertTrue((Boolean) w.produce(vars));

            vars.put("name", "user");
            assertFalse((Boolean) w.produce(vars));
        }
    }

    @Nested
    @DisplayName("배열 연산 테스트")
    class ArrayOperationTest {

        @Test
        @DisplayName("배열 생성")
        public void testArrayCreation() throws Exception {
            Dexpr w = DexprParser.parse("[1, 2, 3, 4, 5]");
            Object result = w.produce(null);
            assertNotNull(result);
        }

        @Test
        @DisplayName("변수를 포함한 배열")
        public void testArrayWithVariables() throws Exception {
            Dexpr w = DexprParser.parse("[${a}, ${b}, ${c}]");
            Map<String, Object> vars = new HashMap<>();
            vars.put("a", 10);
            vars.put("b", 20);
            vars.put("c", 30);
            Object result = w.produce(vars);
            assertNotNull(result);
        }
    }

    @Nested
    @DisplayName("특수 값 테스트")
    class SpecialValueTest {

        @Test
        @DisplayName("Boolean 값")
        public void testBooleanValue() throws Exception {
            Dexpr w = DexprParser.parse("${flag} == true");
            Map<String, Object> vars = new HashMap<>();
            vars.put("flag", true);
            assertTrue((Boolean) w.produce(vars));

            vars.put("flag", false);
            assertFalse((Boolean) w.produce(vars));
        }

        @Test
        @DisplayName("Null 값")
        public void testNullValue() throws Exception {
            Dexpr w = DexprParser.parse("${value} == null");
            Map<String, Object> vars = new HashMap<>();
            vars.put("value", null);
            assertTrue((Boolean) w.produce(vars));
        }

        @Test
        @DisplayName("실수 연산")
        public void testDoubleValue() throws Exception {
            Dexpr w = DexprParser.parse("${a} + ${b}");
            Map<String, Object> vars = new HashMap<>();
            vars.put("a", 10.5);
            vars.put("b", 20.3);
            assertEquals(30.8, (Double) w.produce(vars), 0.01);
        }
    }

    @Nested
    @DisplayName("복합 표현식 테스트")
    class ComplexExpressionTest {

        @Test
        @DisplayName("복잡한 조건식")
        public void testComplexCondition() throws Exception {
            Dexpr w = DexprParser.parse("(${cpu} > 80 && ${memory} > 70) || ${errorCount} > 100");
            Map<String, Object> vars = new HashMap<>();
            vars.put("cpu", 85);
            vars.put("memory", 75);
            vars.put("errorCount", 50);
            assertTrue((Boolean) w.produce(vars));
        }

        @Test
        @DisplayName("임계값 체크 표현식")
        public void testThresholdCheck() throws Exception {
            Dexpr w = DexprParser.parse("${value} >= ${min} && ${value} <= ${max}");
            Map<String, Object> vars = new HashMap<>();
            vars.put("value", 50);
            vars.put("min", 10);
            vars.put("max", 100);
            assertTrue((Boolean) w.produce(vars));

            vars.put("value", 150);
            assertFalse((Boolean) w.produce(vars));
        }

        @Test
        @DisplayName("비율 계산 표현식")
        public void testRatioCalculation() throws Exception {
            Dexpr w = DexprParser.parse("(${success} / (${success} + ${failure})) * 100 > 95");
            Map<String, Object> vars = new HashMap<>();
            vars.put("success", 98);
            vars.put("failure", 2);
            assertTrue((Boolean) w.produce(vars));

            vars.put("success", 90);
            vars.put("failure", 10);
            assertFalse((Boolean) w.produce(vars));
        }

        @Test
        @DisplayName("중첩된 괄호 표현식")
        public void testNestedParenthesis() throws Exception {
            Dexpr w = DexprParser.parse("((${a} + ${b}) * ${c}) - (${d} / ${e})");
            Map<String, Object> vars = new HashMap<>();
            vars.put("a", 10);
            vars.put("b", 5);
            vars.put("c", 2);
            vars.put("d", 20);
            vars.put("e", 4);
            assertEquals(25.0, w.produce(vars)); // ((10+5)*2) - (20/4) = 30 - 5 = 25
        }

        @Test
        @DisplayName("다중 조건 조합")
        public void testMultipleConditions() throws Exception {
            Dexpr w = DexprParser.parse("${status} == 'active' && ${count} > 0 && ${rate} >= 0.8");
            Map<String, Object> vars = new HashMap<>();
            vars.put("status", "active");
            vars.put("count", 10);
            vars.put("rate", 0.85);
            assertTrue((Boolean) w.produce(vars));

            vars.put("rate", 0.7);
            assertFalse((Boolean) w.produce(vars));
        }
    }

    @Nested
    @DisplayName("실전 사용 케이스")
    class RealWorldUseCaseTest {

        @Test
        @DisplayName("APM 알림 조건 - CPU 임계값")
        public void testApmCpuAlert() throws Exception {
            Dexpr w = DexprParser.parse("${cpu_usage} > ${threshold} && ${duration} >= 300");
            Map<String, Object> vars = new HashMap<>();
            vars.put("cpu_usage", 85);
            vars.put("threshold", 80);
            vars.put("duration", 350);
            assertTrue((Boolean) w.produce(vars));
        }

        @Test
        @DisplayName("응답시간 체크")
        public void testResponseTimeCheck() throws Exception {
            Dexpr w = DexprParser.parse("${response_time} > ${warning} ? 'slow' : 'normal'");
            Map<String, Object> vars = new HashMap<>();
            vars.put("response_time", 5000);
            vars.put("warning", 3000);
            Object result = w.produce(vars);
            assertNotNull(result);
        }

        @Test
        @DisplayName("에러율 체크")
        public void testErrorRateCheck() throws Exception {
            Dexpr w = DexprParser.parse("(${error_count} / ${total_count}) * 100 > ${error_threshold}");
            Map<String, Object> vars = new HashMap<>();
            vars.put("error_count", 15);
            vars.put("total_count", 100);
            vars.put("error_threshold", 10);
            assertTrue((Boolean) w.produce(vars)); // 15% > 10%
        }

        @Test
        @DisplayName("메모리 사용률 알림")
        public void testMemoryUsageAlert() throws Exception {
            Dexpr w = DexprParser.parse("(${used_memory} / ${total_memory}) * 100 > 90");
            Map<String, Object> vars = new HashMap<>();
            vars.put("used_memory", 9500);
            vars.put("total_memory", 10000);
            assertTrue((Boolean) w.produce(vars)); // 95% > 90%
        }

        @Test
        @DisplayName("트랜잭션 성능 평가")
        public void testTransactionPerformance() throws Exception {
            Dexpr w = DexprParser.parse("${tps} < ${min_tps} || ${avg_response_time} > ${max_response_time}");
            Map<String, Object> vars = new HashMap<>();
            vars.put("tps", 50);
            vars.put("min_tps", 100);
            vars.put("avg_response_time", 2000);
            vars.put("max_response_time", 3000);
            assertTrue((Boolean) w.produce(vars)); // tps가 낮음
        }

        @Test
        @DisplayName("복합 헬스체크")
        public void testComplexHealthCheck() throws Exception {
            Dexpr w = DexprParser.parse(
                    "(${cpu} > 80 || ${memory} > 90 || ${disk} > 95) && ${status} == 'running'"
            );
            Map<String, Object> vars = new HashMap<>();
            vars.put("cpu", 85);
            vars.put("memory", 70);
            vars.put("disk", 60);
            vars.put("status", "running");
            assertTrue((Boolean) w.produce(vars));
        }
    }

    @Nested
    @DisplayName("디버그 케이스 - 수정된 파싱 검증")
    class DebugCaseTest {

        @Test
        @DisplayName("배열 파싱 - 변수 포함 (COMMA 처리)")
        public void testArrayParsingWithVariables() throws Exception {
            // 이전에 "Expected operator not found" 에러가 발생했던 케이스
            Dexpr w = DexprParser.parse("[${a}, ${b}, ${c}]");
            Map<String, Object> vars = new HashMap<>();
            vars.put("a", 10);
            vars.put("b", 20);
            vars.put("c", 30);
            Object result = w.produce(vars);
            assertNotNull(result);
            // 결과는 [10.0, 20.0, 30.0] 형태의 배열
        }

        @Test
        @DisplayName("삼항 연산자 파싱 - true 분기")
        public void testTernaryOperatorParsingTrue() throws Exception {
            // 이전에 "Expected operator not found" 에러가 발생했던 케이스
            Dexpr w = DexprParser.parse("${a} > 5 ? ${b} : ${c}");
            Map<String, Object> vars = new HashMap<>();
            vars.put("a", 10);
            vars.put("b", 100);
            vars.put("c", 200);
            assertEquals(100.0, w.produce(vars));
        }

        @Test
        @DisplayName("삼항 연산자 파싱 - false 분기")
        public void testTernaryOperatorParsingFalse() throws Exception {
            Dexpr w = DexprParser.parse("${a} > 5 ? ${b} : ${c}");
            Map<String, Object> vars = new HashMap<>();
            vars.put("a", 3);
            vars.put("b", 100);
            vars.put("c", 200);
            assertEquals(200.0, w.produce(vars));
        }

        @Test
        @DisplayName("괄호 우선순위 - 단순 표현식")
        public void testParenthesisPrioritySimple() throws Exception {
            // 이전에 20.0 (10 + 5*2)으로 잘못 계산되던 케이스
            // 현재는 30.0 ((10+5)*2)로 정확하게 계산됨
            Dexpr w = DexprParser.parse("(${a} + ${b}) * ${c}");
            Map<String, Object> vars = new HashMap<>();
            vars.put("a", 10);
            vars.put("b", 5);
            vars.put("c", 2);
            assertEquals(30.0, w.produce(vars)); // (10 + 5) * 2 = 30
        }

        @Test
        @DisplayName("괄호 우선순위 - 중첩 표현식")
        public void testParenthesisPriorityNested() throws Exception {
            // 이전에 5.0으로 잘못 계산되던 케이스
            // 현재는 25.0으로 정확하게 계산됨
            Dexpr w = DexprParser.parse("((${a} + ${b}) * ${c}) - (${d} / ${e})");
            Map<String, Object> vars = new HashMap<>();
            vars.put("a", 10);
            vars.put("b", 5);
            vars.put("c", 2);
            vars.put("d", 20);
            vars.put("e", 4);
            assertEquals(25.0, w.produce(vars)); // ((10+5)*2) - (20/4) = 30 - 5 = 25
        }

        @Test
        @DisplayName("Like 연산자 - % 와일드카드")
        public void testLikeOperatorWildcard() throws Exception {
            // 이전에 false로 잘못 평가되던 케이스
            // 현재는 % → * 변환으로 정확하게 작동
            Dexpr w = DexprParser.parse("${name} like 'ad%'");
            Map<String, Object> vars = new HashMap<>();

            vars.put("name", "admin");
            assertTrue((Boolean) w.produce(vars)); // "admin"은 "ad%"와 매칭됨

            vars.put("name", "user");
            assertFalse((Boolean) w.produce(vars)); // "user"는 "ad%"와 매칭 안됨
        }

        @Test
        @DisplayName("Like 연산자 - 다양한 패턴")
        public void testLikeOperatorVariousPatterns() throws Exception {
            Map<String, Object> vars = new HashMap<>();

            // 시작 패턴
            Dexpr w1 = DexprParser.parse("${text} like 'test%'");
            vars.put("text", "testing");
            assertTrue((Boolean) w1.produce(vars));

            // 끝 패턴
            Dexpr w2 = DexprParser.parse("${text} like '%ing'");
            vars.put("text", "testing");
            assertTrue((Boolean) w2.produce(vars));

            // 중간 패턴
            Dexpr w3 = DexprParser.parse("${text} like '%est%'");
            vars.put("text", "testing");
            assertTrue((Boolean) w3.produce(vars));
        }

        @Test
        @DisplayName("복합 케이스 - 괄호 + 삼항 연산자")
        public void testComplexCaseParenthesisAndTernary() throws Exception {
            // 괄호와 삼항 연산자를 함께 사용하는 복합 케이스
            Dexpr w = DexprParser.parse("(${a} + ${b}) > 10 ? ${x} : ${y}");
            Map<String, Object> vars = new HashMap<>();
            vars.put("a", 7);
            vars.put("b", 5);
            vars.put("x", 100);
            vars.put("y", 200);
            assertEquals(100.0, w.produce(vars)); // (7+5)=12 > 10, 따라서 x 반환
        }

        @Test
        @DisplayName("복합 케이스 - 배열 + 괄호")
        public void testComplexCaseArrayAndParenthesis() throws Exception {
            // 배열 내에서 괄호 표현식 사용
            Dexpr w = DexprParser.parse("[(${a} + ${b}), (${c} * ${d})]");
            Map<String, Object> vars = new HashMap<>();
            vars.put("a", 10);
            vars.put("b", 5);
            vars.put("c", 3);
            vars.put("d", 4);
            Object result = w.produce(vars);
            assertNotNull(result);
            // 결과는 [15.0, 12.0] 형태의 배열
        }
    }

    @Nested
    @DisplayName("공백 및 포맷 테스트")
    class WhitespaceAndFormatTest {

        @Test
        @DisplayName("여러 공백이 포함된 표현식")
        public void testMultipleSpaces() throws Exception {
            Dexpr w = DexprParser.parse("  ${a}   +   ${b}  ");
            Map<String, Object> vars = new HashMap<>();
            vars.put("a", 10);
            vars.put("b", 20);
            assertEquals(30.0, w.produce(vars));
        }

        @Test
        @DisplayName("탭과 공백이 섞인 표현식")
        public void testMixedWhitespace() throws Exception {
            Dexpr w = DexprParser.parse("${a}\t+\t${b}");
            Map<String, Object> vars = new HashMap<>();
            vars.put("a", 5);
            vars.put("b", 3);
            assertEquals(8.0, w.produce(vars));
        }

        @Test
        @DisplayName("줄바꿈이 포함된 복잡한 표현식")
        public void testMultilineExpression() throws Exception {
            String expr = "(${cpu} > 80 && ${memory} > 70) || \n" +
                    "${errorCount} > 100";
            Dexpr w = DexprParser.parse(expr);
            Map<String, Object> vars = new HashMap<>();
            vars.put("cpu", 85);
            vars.put("memory", 75);
            vars.put("errorCount", 50);
            assertTrue((Boolean) w.produce(vars));
        }

        @Test
        @DisplayName("괄호와 공백의 다양한 조합")
        public void testParenthesisWithSpaces() throws Exception {
            Dexpr w = DexprParser.parse("( ( ${a} + ${b} ) * ${c} )");
            Map<String, Object> vars = new HashMap<>();
            vars.put("a", 2);
            vars.put("b", 3);
            vars.put("c", 4);
            assertEquals(20.0, w.produce(vars));
        }
    }

    @Nested
    @DisplayName("숫자 타입 및 변환 테스트")
    class NumberTypeTest {

        @Test
        @DisplayName("정수와 실수 혼합 연산")
        public void testMixedIntegerAndDouble() throws Exception {
            Dexpr w = DexprParser.parse("${int} + ${double}");
            Map<String, Object> vars = new HashMap<>();
            vars.put("int", 10);
            vars.put("double", 5.5);
            assertEquals(15.5, (Double) w.produce(vars), 0.01);
        }

        @Test
        @DisplayName("음수 처리")
        public void testNegativeNumbers() throws Exception {
            Dexpr w = DexprParser.parse("${a} + ${b}");
            Map<String, Object> vars = new HashMap<>();
            vars.put("a", -10);
            vars.put("b", 5);
            assertEquals(-5.0, w.produce(vars));
        }

        @Test
        @DisplayName("0으로 나누기")
        public void testDivisionByZero() throws Exception {
            Dexpr w = DexprParser.parse("${a} / ${b}");
            Map<String, Object> vars = new HashMap<>();
            vars.put("a", 10);
            vars.put("b", 0);
            // Division by zero should throw DexprException
            try {
                Object result = w.produce(vars);
                // If we get here without exception, fail the test
                assertTrue(false, "Expected DexprException for division by zero");
            } catch (DexprException e) {
                // Expected exception
                assertTrue(e.getMessage().contains("division") || e.getMessage().contains("zero"));
            }
        }

        @Test
        @DisplayName("매우 큰 숫자 연산")
        public void testLargeNumbers() throws Exception {
            Dexpr w = DexprParser.parse("${a} * ${b}");
            Map<String, Object> vars = new HashMap<>();
            vars.put("a", 1000000);
            vars.put("b", 1000000);
            assertEquals(1000000000000.0, w.produce(vars));
        }

        @Test
        @DisplayName("매우 작은 숫자 연산")
        public void testSmallNumbers() throws Exception {
            Dexpr w = DexprParser.parse("${a} + ${b}");
            Map<String, Object> vars = new HashMap<>();
            vars.put("a", 0.0001);
            vars.put("b", 0.0002);
            assertEquals(0.0003, (Double) w.produce(vars), 0.0001);
        }
    }

    @Nested
    @DisplayName("변수명 및 문자열 테스트")
    class VariableAndStringTest {

        @Test
        @DisplayName("언더스코어가 포함된 변수명")
        public void testUnderscoreInVariableName() throws Exception {
            Dexpr w = DexprParser.parse("${user_name} == 'admin'");
            Map<String, Object> vars = new HashMap<>();
            vars.put("user_name", "admin");
            assertTrue((Boolean) w.produce(vars));
        }

        @Test
        @DisplayName("숫자가 포함된 변수명")
        public void testNumberInVariableName() throws Exception {
            Dexpr w = DexprParser.parse("${value1} + ${value2}");
            Map<String, Object> vars = new HashMap<>();
            vars.put("value1", 10);
            vars.put("value2", 20);
            assertEquals(30.0, w.produce(vars));
        }

        @Test
        @DisplayName("빈 문자열 비교")
        public void testEmptyStringComparison() throws Exception {
            Dexpr w = DexprParser.parse("${text} == ''");
            Map<String, Object> vars = new HashMap<>();
            vars.put("text", "");
            assertTrue((Boolean) w.produce(vars));
        }

        @Test
        @DisplayName("특수문자가 포함된 문자열")
        public void testSpecialCharactersInString() throws Exception {
            Dexpr w = DexprParser.parse("${path} like '/api/%'");
            Map<String, Object> vars = new HashMap<>();
            vars.put("path", "/api/users");
            assertTrue((Boolean) w.produce(vars));
        }

        @Test
        @DisplayName("공백이 포함된 문자열")
        public void testStringWithSpaces() throws Exception {
            Dexpr w = DexprParser.parse("${message} == 'Hello World'");
            Map<String, Object> vars = new HashMap<>();
            vars.put("message", "Hello World");
            assertTrue((Boolean) w.produce(vars));
        }
    }

    @Nested
    @DisplayName("경계값 및 특수 케이스 테스트")
    class EdgeCaseTest {

        @Test
        @DisplayName("단일 변수 표현식")
        public void testSingleVariable() throws Exception {
            Dexpr w = DexprParser.parse("${value}");
            Map<String, Object> vars = new HashMap<>();
            vars.put("value", 42);
            assertEquals(42.0, w.produce(vars));
        }

        @Test
        @DisplayName("단일 상수 표현식")
        public void testSingleConstant() throws Exception {
            Dexpr w = DexprParser.parse("100");
            Object result = w.produce(null);
            assertEquals(100.0, result);
        }

        @Test
        @DisplayName("빈 배열")
        public void testEmptyArray() throws Exception {
            Dexpr w = DexprParser.parse("[]");
            Object result = w.produce(null);
            assertNotNull(result);
        }

        @Test
        @DisplayName("중첩된 삼항 연산자 - 깊은 중첩")
        public void testDeeplyNestedTernary() throws Exception {
            Dexpr w = DexprParser.parse("${a} > 10 ? ${b} : ${a} > 5 ? ${c} : ${a} > 0 ? ${d} : ${e}");
            Map<String, Object> vars = new HashMap<>();
            vars.put("a", 3);
            vars.put("b", 100);
            vars.put("c", 200);
            vars.put("d", 300);
            vars.put("e", 400);
            assertEquals(300.0, w.produce(vars)); // a=3이므로 d 반환
        }

        @Test
        @DisplayName("매우 긴 표현식")
        public void testVeryLongExpression() throws Exception {
            String expr = "${a} + ${b} + ${c} + ${d} + ${e} + ${f} + ${g} + ${h}";
            Dexpr w = DexprParser.parse(expr);
            Map<String, Object> vars = new HashMap<>();
            for (char c = 'a'; c <= 'h'; c++) {
                vars.put(String.valueOf(c), 1);
            }
            assertEquals(8.0, w.produce(vars));
        }

        @Test
        @DisplayName("연속된 비교 연산")
        public void testChainedComparisons() throws Exception {
            Dexpr w = DexprParser.parse("${a} > ${b} && ${b} > ${c}");
            Map<String, Object> vars = new HashMap<>();
            vars.put("a", 10);
            vars.put("b", 5);
            vars.put("c", 1);
            assertTrue((Boolean) w.produce(vars));
        }
    }

    @Nested
    @DisplayName("타입 혼합 테스트")
    class MixedTypeTest {

        @Test
        @DisplayName("숫자와 문자열 비교")
        public void testNumberAndStringComparison() throws Exception {
            Dexpr w = DexprParser.parse("${num} == ${str}");
            Map<String, Object> vars = new HashMap<>();
            vars.put("num", 123);
            vars.put("str", "123");
            // Dexpr performs type conversion, so numeric string "123" equals 123
            assertTrue((Boolean) w.produce(vars));

            // Test with non-numeric string
            vars.put("str", "abc");
            assertFalse((Boolean) w.produce(vars));
        }

        @Test
        @DisplayName("Boolean과 숫자 비교")
        public void testBooleanAndNumberComparison() throws Exception {
            Dexpr w = DexprParser.parse("${flag} == ${num}");
            Map<String, Object> vars = new HashMap<>();
            vars.put("flag", true);
            vars.put("num", 1);
            // Dexpr performs type conversion, true equals 1
            assertTrue((Boolean) w.produce(vars));

            // Test false with 0
            vars.put("flag", false);
            vars.put("num", 0);
            assertTrue((Boolean) w.produce(vars));

            // Test true with non-1 number
            vars.put("flag", true);
            vars.put("num", 2);
            assertFalse((Boolean) w.produce(vars));
        }

        @Test
        @DisplayName("Null 처리 - 다양한 연산")
        public void testNullInOperations() throws Exception {
            Map<String, Object> vars = new HashMap<>();
            vars.put("a", null);
            vars.put("b", 10);

            // Null 비교
            Dexpr w1 = DexprParser.parse("${a} == null");
            assertTrue((Boolean) w1.produce(vars));

            // Null이 아닌 값과 비교
            Dexpr w2 = DexprParser.parse("${a} != null");
            assertFalse((Boolean) w2.produce(vars));
        }
    }

    @Nested
    @DisplayName("복잡한 실전 시나리오")
    class ComplexRealWorldScenarioTest {

        @Test
        @DisplayName("다단계 알림 조건")
        public void testMultiLevelAlertCondition() throws Exception {
            String expr = "${severity} == 'critical' ? 1 : " +
                    "${severity} == 'warning' ? 2 : " +
                    "${severity} == 'info' ? 3 : 4";
            Dexpr w = DexprParser.parse(expr);
            Map<String, Object> vars = new HashMap<>();

            vars.put("severity", "critical");
            assertEquals(1.0, w.produce(vars));

            vars.put("severity", "warning");
            assertEquals(2.0, w.produce(vars));

            vars.put("severity", "info");
            assertEquals(3.0, w.produce(vars));

            vars.put("severity", "debug");
            assertEquals(4.0, w.produce(vars));
        }


        @Test
        @DisplayName("다단계 알림 조건 + 괄호")
        public void testMultiLevelAlertConditionWith() throws Exception {
            // 괄호로 복잡하게 우선순위를 섞은 중첩 삼항 연산자
            // (level > 3 ? A : B) ? C : (level > 1 ? D : E) 형태로 조건과 결과가 뒤섞임
            String expr = "(${level} > 3 ? ${level} : ${level} + 1) > 4 ? " +
                    "(${status} == 'ok' ? 100 : 200) : " +
                    "(${level} > 1 ? " +
                    "    (${status} == 'ok' ? 300 : 400) : " +
                    "    (${status} == 'ok' ? 500 : 600))";
            Dexpr w = DexprParser.parse(expr);
            Map<String, Object> vars = new HashMap<>();

            // Case 1: level=5, status='ok' -> (5) > 4 ? 100 : ...  = 100
            vars.put("level", 5);
            vars.put("status", "ok");
            assertEquals(100.0, w.produce(vars));

            // Case 2: level=5, status='bad' -> (5) > 4 ? 200 : ... = 200
            vars.put("level", 5);
            vars.put("status", "bad");
            assertEquals(200.0, w.produce(vars));

            // Case 3: level=3, status='ok' -> (4) > 4 ? ... : (3>1 ? 300 : ...) = 300
            vars.put("level", 3);
            vars.put("status", "ok");
            assertEquals(300.0, w.produce(vars));

            // Case 4: level=3, status='bad' -> (4) > 4 ? ... : (3>1 ? 400 : ...) = 400
            vars.put("level", 3);
            vars.put("status", "bad");
            assertEquals(400.0, w.produce(vars));

            // Case 5: level=1, status='ok' -> (2) > 4 ? ... : (1>1 ? ... : 500) = 500
            vars.put("level", 1);
            vars.put("status", "ok");
            assertEquals(500.0, w.produce(vars));

            // Case 6: level=0, status='bad' -> (1) > 4 ? ... : (0>1 ? ... : 600) = 600
            vars.put("level", 0);
            vars.put("status", "bad");
            assertEquals(600.0, w.produce(vars));
        }

        @Test
        @DisplayName("복합 비즈니스 규칙")
        public void testComplexBusinessRule() throws Exception {
            // 할인 적용: (주문액 > 100000 && 회원등급 == 'VIP') || (주문액 > 50000 && 이벤트 참여)
            String expr = "(${orderAmount} > 100000 && ${memberGrade} == 'VIP') || " +
                    "(${orderAmount} > 50000 && ${eventParticipant} == true)";
            Dexpr w = DexprParser.parse(expr);
            Map<String, Object> vars = new HashMap<>();

            // VIP 고객, 고액 주문
            vars.put("orderAmount", 150000);
            vars.put("memberGrade", "VIP");
            vars.put("eventParticipant", false);
            assertTrue((Boolean) w.produce(vars));

            // 일반 고객, 중액 주문, 이벤트 참여
            vars.put("orderAmount", 70000);
            vars.put("memberGrade", "NORMAL");
            vars.put("eventParticipant", true);
            assertTrue((Boolean) w.produce(vars));

            // 조건 미달
            vars.put("orderAmount", 30000);
            vars.put("memberGrade", "NORMAL");
            vars.put("eventParticipant", false);
            assertFalse((Boolean) w.produce(vars));
        }

        @Test
        @DisplayName("SLA 체크 복합 조건")
        public void testSLACheckComplexCondition() throws Exception {
            String expr = "(${successRate} >= 99.9 && ${avgResponseTime} <= 100) || " +
                    "(${successRate} >= 99.5 && ${avgResponseTime} <= 200 && ${p95ResponseTime} <= 500)";
            Dexpr w = DexprParser.parse(expr);
            Map<String, Object> vars = new HashMap<>();

            // 최상위 SLA 충족
            vars.put("successRate", 99.95);
            vars.put("avgResponseTime", 80);
            vars.put("p95ResponseTime", 300);
            assertTrue((Boolean) w.produce(vars));

            // 중간 SLA 충족
            vars.put("successRate", 99.7);
            vars.put("avgResponseTime", 150);
            vars.put("p95ResponseTime", 400);
            assertTrue((Boolean) w.produce(vars));

            // SLA 미달
            vars.put("successRate", 99.3);
            vars.put("avgResponseTime", 250);
            vars.put("p95ResponseTime", 600);
            assertFalse((Boolean) w.produce(vars));
        }

        @Test
        @DisplayName("리소스 최적화 판단")
        public void testResourceOptimizationDecision() throws Exception {
            // 스케일 아웃 조건: CPU > 80% && (메모리 > 70% || 대기 큐 > 1000)
            String expr = "${cpuUsage} > 80 && (${memoryUsage} > 70 || ${queueSize} > 1000)";
            Dexpr w = DexprParser.parse(expr);
            Map<String, Object> vars = new HashMap<>();

            // 스케일 아웃 필요 - CPU 높고 메모리도 높음
            vars.put("cpuUsage", 85);
            vars.put("memoryUsage", 75);
            vars.put("queueSize", 500);
            assertTrue((Boolean) w.produce(vars));

            // 스케일 아웃 필요 - CPU 높고 큐 대기 많음
            vars.put("cpuUsage", 90);
            vars.put("memoryUsage", 60);
            vars.put("queueSize", 1500);
            assertTrue((Boolean) w.produce(vars));

            // 스케일 아웃 불필요
            vars.put("cpuUsage", 70);
            vars.put("memoryUsage", 65);
            vars.put("queueSize", 800);
            assertFalse((Boolean) w.produce(vars));
        }

        @Test
        @DisplayName("동적 가격 계산")
        public void testDynamicPricing() throws Exception {
            // 가격 = 기본가격 * (수요율에 따른 승수) * (시즌 할인)
            String expr = "${basePrice} * (${demandRate} > 0.8 ? 1.5 : 1.0) * (${isSeason} == true ? 0.9 : 1.0)";
            Dexpr w = DexprParser.parse(expr);
            Map<String, Object> vars = new HashMap<>();
            System.out.println(w);
            // 성수기, 높은 수요
            vars.put("basePrice", 10000);
            vars.put("demandRate", 0.9);
            vars.put("isSeason", true);
            assertEquals(13500.0, (Double) w.produce(vars), 0.1); // 10000 * 1.5 * 0.9

            // 비수기, 낮은 수요
            vars.put("demandRate", 0.5);
            vars.put("isSeason", false);
            assertEquals(10000.0, (Double) w.produce(vars), 0.1); // 10000 * 1.0 * 1.0
        }

        @Test
        @DisplayName("반복 계산")
        public void testMultiDynamicPricing() throws Exception {
            // 가격 계산을 단계별로 분리
            // 먼저 수요율 승수 계산
            String expr1 = "${basePrice} * ${demandMultiplier} * ${seasonDiscount}";
            Dexpr w1 = DexprParser.parse(expr1);
            Map<String, Object> vars = new HashMap<>();

            // 성수기, 높은 수요
            vars.put("basePrice", 10000);
            vars.put("demandMultiplier", 1.5);
            vars.put("seasonDiscount", 0.9);
            assertEquals(13500.0, (Double) w1.produce(vars), 0.1); // 10000 * 1.5 * 0.9

            // 비수기, 낮은 수요
            vars.put("demandMultiplier", 1.0);
            vars.put("seasonDiscount", 1.0);
            assertEquals(10000.0, (Double) w1.produce(vars), 0.1); // 10000 * 1.0 * 1.0

            // 삼항 연산자를 사용한 승수 결정
            String expr2 = "${demandRate} > 0.8 ? 1.5 : 1.0";
            Dexpr w2 = DexprParser.parse(expr2);

            vars.put("demandRate", 0.9);
            assertEquals(1.5, (Double) w2.produce(vars), 0.01);

            vars.put("demandRate", 0.5);
            assertEquals(1.0, (Double) w2.produce(vars), 0.01);
        }
    }

    @Nested
    @DisplayName("Like 연산자 고급 패턴")
    class LikeOperatorAdvancedTest {

        @Test
        @DisplayName("여러 와일드카드 조합 - 접두사")
        public void testMultipleWildcardsPrefix() throws Exception {
            Dexpr w = DexprParser.parse("${path} like '%api%'");
            Map<String, Object> vars = new HashMap<>();

            vars.put("path", "/v1/api/v2/users");
            assertTrue((Boolean) w.produce(vars));

            vars.put("path", "/api/users");
            assertTrue((Boolean) w.produce(vars));

            vars.put("path", "/users/test");
            assertFalse((Boolean) w.produce(vars));
        }

        @Test
        @DisplayName("와일드카드 없는 완전 일치")
        public void testExactMatchWithoutWildcard() throws Exception {
            Dexpr w = DexprParser.parse("${value} like 'exact'");
            Map<String, Object> vars = new HashMap<>();

            vars.put("value", "exact");
            assertTrue((Boolean) w.produce(vars));

            vars.put("value", "exactly");
            assertFalse((Boolean) w.produce(vars));
        }

        @Test
        @DisplayName("URL 프로토콜 패턴")
        public void testUrlProtocolPattern() throws Exception {
            Dexpr w = DexprParser.parse("${url} like 'https:%'");
            Map<String, Object> vars = new HashMap<>();

            vars.put("url", "https://example.com/api/users");
            assertTrue((Boolean) w.produce(vars));

            vars.put("url", "http://example.com/api/users");
            assertFalse((Boolean) w.produce(vars));
        }

        @Test
        @DisplayName("파일 확장자 패턴")
        public void testFileExtensionPattern() throws Exception {
            Dexpr w = DexprParser.parse("${filename} like '%.jpg'");
            Map<String, Object> vars = new HashMap<>();

            vars.put("filename", "photo.jpg");
            assertTrue((Boolean) w.produce(vars));

            vars.put("filename", "document.pdf");
            assertFalse((Boolean) w.produce(vars));
        }
    }
}