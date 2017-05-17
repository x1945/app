package sys.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	@Autowired
	SystemService systemService;

	@Autowired
	I18nService i18n;

	/**
	 * 新增一個使用者
	 * 
	 * @param params
	 */
	public void add(Map<String, Object> params) {
//		Org_member_data bean = new Org_member_data();
//		Util.mapToBean(params, bean);
//
//		Org_member_data data = findByEmail(bean.getEmail());
//		if (data == null) {
//			long currTime = DateUtil.getTime();
//			// md5 加密
//			bean.setPassword(CodingUtil.md5(bean.getPassword()));
//			bean.setOrg_uuid(PropUtil.getOrg_uuid());
//			bean.setMember_uuid(Util.getUUID());
//			bean.setRegistration_date(currTime);
//			// bean.setAccount_status(SysConfig.Account_status.尚未開通);
//			bean.setAccount_status(SysConfig.Account_status.正常使用);
//			bean.setCreate_user(bean.getMember_uuid());
//			bean.setCreate_date(currTime);
//			bean.setUpdate_user(bean.getMember_uuid());
//			bean.setUpdate_date(currTime);
//			org_member_dataDao.save(bean);
//		} else {
//			String msg = MessageFormat.format(i18n.getMessage("already.registered"), bean.getEmail());
//			throw new WebException(msg);
//		}
	}

	/**
	 * 依E-Mail搜尋
	 * 
	 * @param email
	 * @return
	 */
//	public Org_member_data findByEmail(String email) {
//		SQLParams sp = new SQLParams();
//		sp.put("email", email);
//		Org_member_data bean = org_member_dataDao.query(sp);
//		return bean;
//	}


	/**
	 * find by member_uuid
	 * 
	 * @param member_uuid
	 * @return
	 */
//	public Org_member_data findById(String member_uuid) {
//		SQLParams sp = new SQLParams();
//		sp.put("org_uuid", PropUtil.getOrg_uuid());
//		sp.put("member_uuid", member_uuid);
//		return org_member_dataDao.queryByPk(sp);
//	}

	/**
	 * 會員變更密碼
	 * 
	 * @param params
	 */
//	public void changePassword(Map<String, Object> params) {
//		User user = systemService.getLoginUser();
//
//		Org_member_data bean = new Org_member_data();
//		Util.mapToBean(params, bean);
//
//		Org_member_data data = findById(user.getId());
//		if (data != null) {
//
//			long currTime = DateUtil.getTime();
//			data.setPassword(CodingUtil.md5(bean.getPassword()));
//			data.setUpdate_user(systemService.getUserId());
//			data.setUpdate_date(currTime);
//			org_member_dataDao.save(data);
//		} else {
//			// 查無此會員
//			throw new WebException(i18n.getMessage("no.such.member"));
//		}
//
//	}

	/**
	 * 會員修改個人資料
	 * 
	 * @param params
	 */
//	public void modifyMemberData(Map<String, Object> params) {
//		User user = systemService.getLoginUser();
//
//		Org_member_data data = findById(user.getId());
//		if (data != null) {
//			Util.mapToBean(params, data);
//			long currTime = DateUtil.getTime();
//			data.setUpdate_user(user.getId());
//			data.setUpdate_date(currTime);
//			org_member_dataDao.save(data);
//		} else {
//			// 查無此會員
//			throw new WebException(i18n.getMessage("no.such.member"));
//		}
//
//	}

	/**
	 * 會員付款方式設定
	 * 
	 * @param params
	 */
//	public void setPayment(Map<String, Object> params) {
//		User user = systemService.getLoginUser();
//		Org_member_data data = findById(user.getId());
//		if (data != null) {
//			Util.mapToBean(params, data);
//			long currTime = DateUtil.getTime();
//			data.setUpdate_user(user.getId());
//			data.setUpdate_date(currTime);
//			org_member_dataDao.save(data);
//		} else {
//			// 查無此會員
//			throw new WebException(i18n.getMessage("no.such.member"));
//		}
//
//	}
}
