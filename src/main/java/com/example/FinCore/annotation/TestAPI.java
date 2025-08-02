package com.example.FinCore.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 該 Annotation 僅可作用在方法上，被標記的方法（通常是API方法）將會被鎖定
 * 為測試用 API，此時將需要添加特定標頭（RequestHeader）以及指定Token才可被
 * 呼叫。
 */
@Documented
@Retention(RUNTIME)
@Target(METHOD)
public @interface TestAPI {

}
