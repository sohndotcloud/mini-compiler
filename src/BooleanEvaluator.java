package org.kotlin.spring;

import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

public class BooleanEvaluator {
    public static boolean booleanEval(String expression) {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");

        try {
            Boolean result = (Boolean) engine.eval(expression);
            return result;
        } catch (ScriptException e) {
            e.printStackTrace();
        }

        return false;
    }
}