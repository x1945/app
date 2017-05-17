package sys.beans;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * BaseBean
 * 
 * @author Fantasy
 * 
 */
public class BaseBean {

	/**
	 * toString
	 */
	@Override
	public String toString() {

		// return ReflectionToStringBuilder.toString(this,
		// ToStringStyle.DEFAULT_STYLE);
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

	/**
	 * get
	 * 
	 * @param name
	 * @return
	 */
	public String get(String name) {
		try {
			return BeanUtils.getProperty(this, name);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * set
	 * 
	 * @param name
	 * @param value
	 */
	public void set(String name, Object value) {
		try {
			BeanUtils.setProperty(this, name, value);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

	}
}
