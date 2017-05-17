package sys.beans;

import java.io.Serializable;

/**
 * SerialBean
 * 
 * @author Fantasy
 * 
 */
@SuppressWarnings("serial")
public class SerialBean extends BaseBean implements Serializable {

	String id;

	String pid;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

}
