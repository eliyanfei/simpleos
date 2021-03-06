package net.simpleos.module.docu;

import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.pager.AbstractTablePagerData;

/**
 * 
 * @author 李岩飞 
 * @email eliyanfei@126.com
 * @date 2014年11月21日 下午5:38:01 
 *
 */
public abstract class AbstractDocuTablePagerData extends AbstractTablePagerData {
	public AbstractDocuTablePagerData(final ComponentParameter compParameter) {
		super(compParameter);
	}

	public String buildTitle(final DocuBean docuBean) {
		final StringBuffer sb = new StringBuffer();
		if (docuBean.getStatus() == EDocuStatus.audit) {
			sb.append("<span style='color:red;'>审核</span>");
		}
		sb.append(DocuUtils.wrapOpenLink(compParameter, docuBean));
		return sb.toString();
	}

	protected String wrapNum(final Object num) {
		final StringBuilder sb = new StringBuilder();
		sb.append("<span class=\"nnum\">").append(num).append("</span>");
		return sb.toString();
	}
}
