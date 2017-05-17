package sys.beans;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * UidBean
 * 
 * @author Fantasy
 * 
 */
@SuppressWarnings("serial")
public class UidBean extends BaseBean implements Serializable {

	String uid;

	String create_id;

	Timestamp create_time;

	String update_id;

	Timestamp update_time;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getCreate_id() {
		return create_id;
	}

	public void setCreate_id(String create_id) {
		this.create_id = create_id;
	}

	public Timestamp getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Timestamp create_time) {
		this.create_time = create_time;
	}

	public String getUpdate_id() {
		return update_id;
	}

	public void setUpdate_id(String update_id) {
		this.update_id = update_id;
	}

	public Timestamp getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Timestamp update_time) {
		this.update_time = update_time;
	}

}
