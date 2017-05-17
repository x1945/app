package app.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 頁面設定 Annotition
 * 
 * @author Fantasy
 * 
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface PageSet {

	/** value **/
	boolean value() default true;

	/** navbar **/
	boolean navbar() default true;

	/** footer **/
	boolean footer() default true;
}
