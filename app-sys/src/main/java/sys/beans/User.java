package sys.beans;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

@SuppressWarnings("serial")
public class User extends BaseBean implements Serializable {

	String id;

	String username;

	String email;

	String updater;

	Date updtime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUpdater() {
		return updater;
	}

	public void setUpdater(String updater) {
		this.updater = updater;
	}

	public Date getUpdtime() {
		return updtime;
	}

	public void setUpdtime(Date updtime) {
		this.updtime = updtime;
	}

	/** 刪除series persistent_logins用 **/
	@JsonIgnore
	String series;

	public String getSeries() {
		return series;
	}

	public void setSeries(String series) {
		this.series = series;
	}

}