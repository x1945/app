package sys.beans;

import java.io.Serializable;

/**
 * AppBean
 * 
 * @author Fantasy
 * 
 */
@SuppressWarnings("serial")
public class AppBean extends BaseBean implements Serializable {

	Long create_date; // numeric(14,0) NOT NULL, -- 建立時間
	String create_user; // character varying(64) NOT NULL, -- 建立人員
	Long update_date; // numeric(14,0) NOT NULL, -- 更新時間
	String update_user; // character varying(64) NOT NULL, -- 更新人員

	public Long getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Long create_date) {
		this.create_date = create_date;
	}

	public String getCreate_user() {
		return create_user;
	}

	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}

	public Long getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(Long update_date) {
		this.update_date = update_date;
	}

	public String getUpdate_user() {
		return update_user;
	}

	public void setUpdate_user(String update_user) {
		this.update_user = update_user;
	}

}
