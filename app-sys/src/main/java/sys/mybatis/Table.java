package sys.mybatis;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * BaseDaoSQL Table
 * 
 * @author Fantasy
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Table {

	// table schema
	String schema() default "TEST";

	// table name
	String name() default "";

	// primaryKey, update delete where keys
	String[] pk() default { "uid" };

	// 略過非table中的欄位
	String[] ignore() default {};

	// 指定排序欄位 預設seq
	String seq() default "seq";

	// 是否將table name和欄位名稱轉大寫
	boolean upperCase() default false;
}
