package com.github.comelf.dexpr.util;

import java.text.DecimalFormat;

public class CastUtil {
    public static int toInt(Object value) {
        if (value == null) {
            return 0;
        } else if (value instanceof Number) {
            return ((Number) value).intValue();
        } else {
            try {
                return Integer.parseInt(toStringValue(value));
            } catch (NumberFormatException e) {
                return 0;
            } catch (Exception e) {
                return 0;
            }
        }
    }

    public static Integer toInteger(Object value) {
        return toInt(value);
    }

    public static long toLong(Object value) {
        if (value == null) {
            return 0;
        } else if (value instanceof Number) {
            return ((Number) value).longValue();
        } else {
            try {
                return Long.parseLong(toStringValue(value));
            } catch (NumberFormatException e) {
                return 0L;
            } catch (Exception e) {
                return 0L;
            }
        }
    }

    public static Long toLongBoxed(Object value) {
        return toLong(value);
    }

    public static float toFloat(Object value) {
        if (value == null) {
            return 0f;
        } else if (value instanceof Number) {
            return ((Number) value).floatValue();
        } else {
            try {
                return Float.parseFloat(toStringValue(value));
            } catch (RuntimeException e) {
                return 0f;
            } catch (Exception e) {
                return 0f;
            }
        }
    }

    public static Float toFloatBoxed(Object value) {
        return toFloat(value);
    }

    public static double toDouble(Object value) {
        if (value == null) {
            return 0;
        } else if (value instanceof Number) {
            return ((Number) value).doubleValue();
        } else {
            try {
                return Double.parseDouble(toStringValue(value));
            } catch (RuntimeException e) {
                return 0;
            } catch (Exception e) {
                return 0;
            }
        }
    }

    public static Double toDoubleBoxed(Object value) {
        return toDouble(value);
    }

    private static String toStringValue(Object value) {
        if (value instanceof String)
            return (String) value;
        String val = value.toString();
        if (val == null)
            return "";
        return val;
    }

    public static String toStr(Object value) {
        if (value == null) {
            return "";
        } else if (value instanceof Number) {
            if (value instanceof Double || value instanceof Float) {
                return new DecimalFormat("#0.0#######").format(value);
            } else {
                return value.toString();
            }
        } else {
            return toStringValue(value);
        }
    }

    public static boolean toBoolean(Object value) {
        if (value == null) {
            return false;
        } else if (value instanceof Boolean) {
            return ((Boolean) value).booleanValue();
        } else {
            return "true".equalsIgnoreCase(toStringValue(value));
        }
    }

    public static Boolean toBooleanBoxed(Object value) {
        return toBoolean(value);
    }

    public static Number toNumber(String value) {
        if (value == null) {
            return 0;
        } else if (value.contains(".")) {
            return toDoubleBoxed(value);
        } else {
            return toLongBoxed(value);
        }
    }

    public static double getDouble(Object o) {
        if (o instanceof Number) {
            return ((Number) o).doubleValue();
        }
        return 0;
    }
}
