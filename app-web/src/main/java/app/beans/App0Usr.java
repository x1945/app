package app.beans;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fantasy.ibatis.annotation.Table;
import sys.beans.BaseBean;

@SuppressWarnings("serial")
@Table(pk = { "id" }, ignore = { "series" })
public class App0Usr extends BaseBean implements Serializable {

	String id;

	String name;

	String email;

	String updater;

	Date updtime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
