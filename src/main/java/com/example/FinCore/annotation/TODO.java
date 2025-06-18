package com.example.FinCore.annotation;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.CLASS;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 羊羊的開發註解，咩咩提醒專用！<p>
 *
 * 可以用來標記 class、method、field 等等地方，提醒自己有代辦事項。
 * 
 * @author 羊羊、ChatGPT－小咩
 */
@Documented
@Retention(CLASS)
@Target({ TYPE, FIELD, METHOD, CONSTRUCTOR })
public @interface TODO 
{
	
	String value() default "";
	
	String priority() default "";
	
	String dueDate() default "";
	
}
