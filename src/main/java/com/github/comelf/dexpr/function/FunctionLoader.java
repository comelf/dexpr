package com.github.comelf.dexpr.function;

import com.github.comelf.dexpr.util.Scanner;

import java.util.*;

public class FunctionLoader {

    private HashMap<String, Function> funcTable = new HashMap<String, Function>();

    private static FunctionLoader instance = null;

    public static synchronized FunctionLoader getInstance() {
        if (instance == null) {
            instance = new FunctionLoader();
        }
        return instance;
    }

    private List<String> packages = new ArrayList<>();

    static {
        FunctionLoader.getInstance().addPackage("com.github.comelf.dexpr.function");
    }

    private FunctionLoader() {
    }

    private void load() {
        for (String packageName : packages) {
            Set<String> classes = new Scanner(packageName).process();

            Iterator<String> itr = classes.iterator();
            while (itr.hasNext()) {
                try {
                    Class c = Class.forName(itr.next());
                    if (Function.class != c && Function.class.isAssignableFrom(c)) {
                        funcTable.put(getFunctionName(c), (Function) c.newInstance());
                    }
                } catch (RuntimeException e) {
                    // ignore
                } catch (Exception e) {
                    // ignore
                }
            }
        }
    }

    public synchronized void addPackage(String packageName) {
        if (packageName == null || packageName.isEmpty()) {
            return;
        }
        this.packages.add(packageName);
        load();
    }

    public Set<String> getFunctionSet() {
        return this.funcTable.keySet();
    }

    private String getFunctionName(Class c) {
        String className = c.getName();
        className = className.substring(className.lastIndexOf(".") + 1);
        if (className.startsWith("_")) {
            className = className.substring(1);
        }
        return lowerFirst(className);
    }

    public Function getFunction(String name) throws RuntimeException {
        return funcTable.get(lowerFirst(name));
    }

    public static String lowerFirst(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        char[] buffer = str.toCharArray();
        buffer[0] = Character.toLowerCase(buffer[0]);
        return new String(buffer);
    }
}