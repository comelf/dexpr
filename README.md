# DExpr

Dynamic Expression Parser for Java

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java](https://img.shields.io/badge/Java-8%2B-blue.svg)](https://www.oracle.com/java/)

## Overview

DExpr is a lightweight, high-performance expression parser and evaluator for Java. It allows you to parse and evaluate dynamic expressions at runtime with support for variables, operators, and functions.

## Features

- **Arithmetic Operations**: `+`, `-`, `*`, `/`, `%`
- **Comparison Operations**: `<`, `<=`, `>`, `>=`, `==`, `!=`
- **Logical Operations**: `&&`, `||`, `and`, `or`
- **Ternary Operator**: `condition ? trueValue : falseValue`
- **Pattern Matching**: `like`, `not like` (SQL-style wildcards with `%`)
- **Variables**: `${variableName}` syntax
- **String Interpolation**: `"Hello ${name}!"`
- **Arrays**: `[1, 2, 3]`, `[${a}, ${b}, ${c}]`
- **Functions**: Extensible function system
- **Parentheses**: Full support for expression grouping

## Installation

### Maven

```xml
<dependency>
    <groupId>com.github.comelf</groupId>
    <artifactId>dexpr</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Quick Start

```java
import com.github.comelf.dexpr.Dexpr;
import com.github.comelf.dexpr.DexprParser;
import java.util.HashMap;
import java.util.Map;

public class Example {
    public static void main(String[] args) throws Exception {
        // Simple arithmetic
        Dexpr expr = DexprParser.parse("${a} + ${b} * 2");

        Map<String, Object> vars = new HashMap<>();
        vars.put("a", 10);
        vars.put("b", 5);

        Object result = expr.produce(vars);
        System.out.println(result); // 20.0
    }
}
```

## Usage Examples

### Arithmetic Operations

```java
Dexpr expr = DexprParser.parse("(${a} + ${b}) * ${c}");
vars.put("a", 10);
vars.put("b", 5);
vars.put("c", 2);
expr.produce(vars); // 30.0
```

### Comparison Operations

```java
Dexpr expr = DexprParser.parse("${cpu} > 80 && ${memory} > 70");
vars.put("cpu", 85);
vars.put("memory", 75);
expr.produce(vars); // true
```

### Ternary Operator

```java
Dexpr expr = DexprParser.parse("${score} >= 60 ? 'pass' : 'fail'");
vars.put("score", 75);
expr.produce(vars); // "pass"
```

### Pattern Matching (like)

```java
Dexpr expr = DexprParser.parse("${path} like '/api/%'");
vars.put("path", "/api/users");
expr.produce(vars); // true
```

### String Interpolation

```java
Dexpr expr = DexprParser.parse("'Hello ${name}!'");
vars.put("name", "World");
expr.produce(vars); // "Hello World!"
```

### Complex Conditions

```java
// APM alert condition example
String condition = "(${cpu} > 80 && ${memory} > 70) || ${errorCount} > 100";
Dexpr expr = DexprParser.parse(condition);

vars.put("cpu", 85);
vars.put("memory", 75);
vars.put("errorCount", 50);
expr.produce(vars); // true
```

### Type Conversion Methods

```java
Dexpr expr = DexprParser.parse("${value} * 2");
vars.put("value", 25);

// Get result as specific type
double d = expr.produceToDouble(vars);  // 50.0
int i = expr.produceToInt(vars);        // 50
long l = expr.produceToLong(vars);      // 50
boolean b = expr.produceToBoolean(vars); // true (non-zero)
```

## Custom Functions

DExpr supports extensible custom functions through the `FunctionLoader` system.

### Implementing a Custom Function

Create a class that implements the `Function` interface:

```java
package com.example.myapp.functions;

import com.github.comelf.dexpr.function.Function;
import java.util.List;

public class Max implements Function {

    @Override
    public String desc() {
        return "Returns the maximum value from arguments";
    }

    @Override
    public Object process(List param) {
        if (param == null || param.isEmpty()) {
            return 0.0;
        }
        double max = Double.MIN_VALUE;
        for (Object o : param) {
            if (o instanceof Number) {
                double val = ((Number) o).doubleValue();
                if (val > max) {
                    max = val;
                }
            }
        }
        return max;
    }
}
```

### Registering Custom Functions

Register your custom function package using `FunctionLoader.addPackage()`:

```java
import com.github.comelf.dexpr.function.FunctionLoader;

// Register custom function package (call once at application startup)
FunctionLoader.getInstance().addPackage("com.example.myapp.functions");
```

### Using Custom Functions

Once registered, use your custom functions in expressions:

```java
Dexpr expr = DexprParser.parse("max(${a}, ${b}, ${c})");
vars.put("a", 10);
vars.put("b", 25);
vars.put("c", 15);
expr.produce(vars); // 25.0
```

### Function Naming Convention

- Class name is converted to camelCase for the function name
- `Max` class -> `max()` function
- `MyCustomFunc` class -> `myCustomFunc()` function
- Classes starting with `_` have the underscore removed: `_Sum` -> `sum()`

## Operator Precedence

| Priority | Operators |
|----------|-----------|
| 1 (highest) | `*`, `/`, `%` |
| 2 | `+`, `-` |
| 3 | `<`, `<=`, `>`, `>=` |
| 4 | `==`, `!=`, `like`, `not like` |
| 5 | `&&`, `and` |
| 6 (lowest) | `\|\|`, `or` |

## Requirements

- Java 8 or higher
- Maven 3.x

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.
