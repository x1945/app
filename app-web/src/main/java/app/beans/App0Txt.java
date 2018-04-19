package app.beans;

import java.io.Serializable;
import java.util.Date;

import fantasy.ibatis.annotation.Table;
import sys.beans.BaseBean;

@SuppressWarnings("serial")
@Table(pk = { "pid" })
public class App0Txt extends BaseBean implements Serializable {

	String pid;

	Integer sort;

	String content;

	String updater;

	Date updtime;

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

}
