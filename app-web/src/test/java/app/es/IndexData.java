package app.es;

import java.io.Serializable;

import sys.beans.BaseBean;

@SuppressWarnings("serial")
public class IndexData extends BaseBean implements Serializable {

	private String pid;
	/** 計畫編號 **/
	private String cpid;
	/** 中文計畫名稱 **/
	private String cname;
	/** 年度 **/
	private String yr;
	/** 計畫類別 **/
	private String category;
	/** 計畫型式 **/
	private String type;
	/** 計畫主持人姓名 **/
	private String director_name;
	/** 計畫主持人單位 **/
	private String director_dept;
	/** 主辦專家機關 **/
	private String divisioin_id;
	/** 主辦專家單位 **/
	private String segid;
	/** 主辦專家 **/
	private String eid;
	/** 領域 **/
	private String real_domain_id;
	/** 綱要 **/
	private String domain_id;
	/** 推動小組 **/
	private String promote_id;
	/** 計畫內容 **/
	private String content;

	private String filePath;

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getCpid() {
		return cpid;
	}

	public void setCpid(String cpid) {
		this.cpid = cpid;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public String getYr() {
		return yr;
	}

	public void setYr(String yr) {
		this.yr = yr;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDirector_name() {
		return director_name;
	}

	public void setDirector_name(String director_name) {
		this.director_name = director_name;
	}

	public String getDirector_dept() {
		return director_dept;
	}

	public void setDirector_dept(String director_dept) {
		this.director_dept = director_dept;
	}

	public String getDivisioin_id() {
		return divisioin_id;
	}

	public void setDivisioin_id(String divisioin_id) {
		this.divisioin_id = divisioin_id;
	}

	public String getSegid() {
		return segid;
	}

	public void setSegid(String segid) {
		this.segid = segid;
	}

	public String getEid() {
		return eid;
	}

	public void setEid(String eid) {
		this.eid = eid;
	}

	public String getReal_domain_id() {
		return real_domain_id;
	}

	public void setReal_domain_id(String real_domain_id) {
		this.real_domain_id = real_domain_id;
	}

	public String getDomain_id() {
		return domain_id;
	}

	public void setDomain_id(String domain_id) {
		this.domain_id = domain_id;
	}

	public String getPromote_id() {
		return promote_id;
	}

	public void setPromote_id(String promote_id) {
		this.promote_id = promote_id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

}
