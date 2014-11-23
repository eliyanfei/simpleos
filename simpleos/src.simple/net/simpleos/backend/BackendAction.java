package net.simpleos.backend;

import java.util.Map;

import net.simpleframework.core.ado.db.Column;
import net.simpleframework.sysmgr.dict.DictUtils;
import net.simpleframework.sysmgr.dict.SysDict;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.base.ajaxrequest.AbstractAjaxRequestHandle;
import net.simpleos.SimpleosUtil;
import net.simpleos.backend.slide.IndexSlideBean;
import net.simpleos.utils.LangUtils;
import net.simpleos.utils.StringsUtils;

/**
 * @author yanfei.li
 * @email eliyanfei@126.com
 * 2013-12-3下午03:26:11
 */
public class BackendAction extends AbstractAjaxRequestHandle {
	/**
	 * 保存友情链接底部显示的数量
	 * 
	 * @param compParameter
	 * @return
	 */
	public IForward saveDictLinks(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {

			@Override
			public void doAction(Map<String, Object> json) throws Exception {
				SysDict sysDict = DictUtils.getSysDictByName("links");
				if (sysDict == null) {
					sysDict = new SysDict();
					sysDict.setParentId(DictUtils.getSysDictByName("base").getId());
					sysDict.setName("links");
					sysDict.setText(StringsUtils.trimNull(compParameter.getParameter("dict_links"), "10"));
					DictUtils.getTableEntityManager(SysDict.class).insert(sysDict);
				} else {
					sysDict.setText(StringsUtils.trimNull(compParameter.getParameter("dict_links"), "10"));
					DictUtils.getTableEntityManager(SysDict.class).update(sysDict);
				}
				json.put("rs", "true");
			}
		});
	}

	/**
	 * 保存自已表的数据
	 * @param compParameter
	 * @return
	 */
	public IForward saveCustom(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {

			@Override
			public void doAction(Map<String, Object> json) throws Exception {
				final String type = compParameter.getRequestParameter("type");
				BackendUtils.saveCustom(type, BackendUtils.applicationModule, compParameter);
				if ("site".equals(type)) {
					SimpleosUtil.url = StringsUtils.trimNull(SimpleosUtil.attrMap.get("site.site_url"), "");
					SimpleosUtil.title = StringsUtils.trimNull(SimpleosUtil.attrMap.get("site.site_name"), "");
				}
			}
		});
	}

	/**
	 * 保存自定义变量
	 * @param compParameter
	 * @return
	 */
	public IForward saveTemplate(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {

			@Override
			public void doAction(Map<String, Object> json) throws Exception {
				final String name = "template";
				final StringBuffer sb = new StringBuffer();
				sb.append("name=").append(compParameter.getParameter("templateId"));
				sb.append(";fixedHeader=").append(compParameter.getParameter("fixedHeader"));
				sb.append(";fixedFooter=").append(compParameter.getParameter("fixedFooter"));
				sb.append(";fullScreen=").append(compParameter.getParameter("fullScreen"));
				CustomBean customBean = BackendUtils.applicationModule.getBeanByExp(CustomBean.class, "name=? and type=?",
						new Object[] { name, name });
				if (customBean == null) {
					customBean = new CustomBean();
					customBean.setType(name);
					customBean.setName(name);
				}
				customBean.setValue(sb.toString());
				BackendUtils.applicationModule.doUpdate(customBean);
				SimpleosUtil.attrMap.put("template.template", sb.toString());
			}
		});
	}

	/**
	 * 保存幻灯片
	 * @param compParameter
	 * @return
	 */
	public IForward saveNav(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {

			@Override
			public void doAction(Map<String, Object> json) throws Exception {
				final String navId = compParameter.getParameter("navId");
				final String title = compParameter.getParameter("ds_title");
				final String url = compParameter.getParameter("ds_url");
				IndexSlideBean navBean = BackendUtils.applicationModule.getBean(IndexSlideBean.class, navId);
				if (navBean == null) {
					navBean = new IndexSlideBean();
					navBean.setOorder(BackendUtils.applicationModule.nextId());
					navBean.setImage("");
				}
				navBean.setTitle(title);
				navBean.setUrl(url);
				BackendUtils.applicationModule.doUpdate(navBean);
			}
		});
	}

	/**
	 * 删除幻灯片
	 * @param compParameter
	 * @return
	 */
	public IForward deleteNav(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {

			@Override
			public void doAction(Map<String, Object> json) throws Exception {
				final String navId = compParameter.getParameter("navId");
				BackendUtils.applicationModule.doDelete(IndexSlideBean.class, navId);
			}
		});
	}

	/**
	 * 保存幻灯片
	 * @param compParameter
	 * @return
	 */
	public IForward exchangeNav(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {

			@Override
			public void doAction(Map<String, Object> json) throws Exception {
				final String navId1 = compParameter.getParameter("navId1");
				final String navId2 = compParameter.getParameter("navId2");
				final IndexSlideBean navBean1 = BackendUtils.applicationModule.getBean(IndexSlideBean.class, navId1);
				final IndexSlideBean navBean2 = BackendUtils.applicationModule.getBean(IndexSlideBean.class, navId2);
				final boolean up = LangUtils.toBoolean(compParameter.getParameter("up"), false);
				BackendUtils.applicationModule.getDataObjectManager(IndexSlideBean.class).exchange(navBean1, navBean2, new Column("oorder"), up);
			}
		});
	}
}
