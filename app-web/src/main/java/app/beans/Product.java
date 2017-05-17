package app.beans;

import java.io.Serializable;

import sys.beans.BaseBean;
import sys.mybatis.Table;

@SuppressWarnings("serial")
@Table(pk = { "product_uuid" })
public class Product extends BaseBean implements Serializable {
	String product_uuid; // 商品UUID character varying
	String product_id; // 系統商編碼 character varying
	String product_name; // 商品名稱 character varying
	String product_name_eng; // 商品英文名稱 character varying
	String first_industry_code_ver_oid; // 主產業版本OID character varying
	String first_industry; // 商品主產業 character varying
	String second_industry_code_ver_oid; // 次產業版本OID character varying
	String second_industry; // 商品次產業 character varying
	String export_name; // 出品報關之英文名稱 character varying
	String hs_code; // 商品海關編碼 character varying
	String mpn_code; // MPN_CODE character varying
	String gs1_code; // GS1_CODE character varying
	String product_unit; // 最少配送單位商品單位 character varying
	Integer product_qty; // 最少配送單位主商品數量 numeric
	Long create_time; // 建立時間 numeric
	String create_user; // 建立者 character varying
	String product_status; // 商品狀態 character
	String check_type; // 審核狀態0.可調價狀態1尚未寄倉不需審查2.編修截止日後出航前不可調價2.審查中不可調價 character

	public String getProduct_uuid() {
		return product_uuid;
	}

	public void setProduct_uuid(String product_uuid) {
		this.product_uuid = product_uuid;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public String getProduct_name_eng() {
		return product_name_eng;
	}

	public void setProduct_name_eng(String product_name_eng) {
		this.product_name_eng = product_name_eng;
	}

	public String getFirst_industry_code_ver_oid() {
		return first_industry_code_ver_oid;
	}

	public void setFirst_industry_code_ver_oid(String first_industry_code_ver_oid) {
		this.first_industry_code_ver_oid = first_industry_code_ver_oid;
	}

	public String getFirst_industry() {
		return first_industry;
	}

	public void setFirst_industry(String first_industry) {
		this.first_industry = first_industry;
	}

	public String getSecond_industry_code_ver_oid() {
		return second_industry_code_ver_oid;
	}

	public void setSecond_industry_code_ver_oid(String second_industry_code_ver_oid) {
		this.second_industry_code_ver_oid = second_industry_code_ver_oid;
	}

	public String getSecond_industry() {
		return second_industry;
	}

	public void setSecond_industry(String second_industry) {
		this.second_industry = second_industry;
	}

	public String getExport_name() {
		return export_name;
	}

	public void setExport_name(String export_name) {
		this.export_name = export_name;
	}

	public String getHs_code() {
		return hs_code;
	}

	public void setHs_code(String hs_code) {
		this.hs_code = hs_code;
	}

	public String getMpn_code() {
		return mpn_code;
	}

	public void setMpn_code(String mpn_code) {
		this.mpn_code = mpn_code;
	}

	public String getGs1_code() {
		return gs1_code;
	}

	public void setGs1_code(String gs1_code) {
		this.gs1_code = gs1_code;
	}

	public String getProduct_unit() {
		return product_unit;
	}

	public void setProduct_unit(String product_unit) {
		this.product_unit = product_unit;
	}

	public Integer getProduct_qty() {
		return product_qty;
	}

	public void setProduct_qty(Integer product_qty) {
		this.product_qty = product_qty;
	}

	public Long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Long create_time) {
		this.create_time = create_time;
	}

	public String getCreate_user() {
		return create_user;
	}

	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}

	public String getProduct_status() {
		return product_status;
	}

	public void setProduct_status(String product_status) {
		this.product_status = product_status;
	}

	public String getCheck_type() {
		return check_type;
	}

	public void setCheck_type(String check_type) {
		this.check_type = check_type;
	}

}
