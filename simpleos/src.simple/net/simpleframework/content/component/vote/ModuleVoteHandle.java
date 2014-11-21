package net.simpleframework.content.component.vote;

import java.util.Map;

import net.simpleframework.ado.IDataObjectValue;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.core.bean.IDataObjectBean;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.portal.PageletBean;
import net.simpleframework.web.page.component.ui.portal.PortalUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class ModuleVoteHandle extends DefaultVoteHandle {
	@Override
	public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
		if ("name".equals(beanProperty) || "containerId".equals(beanProperty)) {
			return "vote_" + compParameter.getRequestParameter(PortalUtils.PAGELET_ID);
		} else if ("jobDelete".equals(beanProperty) || "jobEdit".equals(beanProperty)) {
			final Vote vote = getTableEntityManager(compParameter).queryForObject(
					new ExpressionValue("documentid=?", new Object[] { getDocumentId(compParameter) }),
					Vote.class);
			if (vote != null) {
				return "#" + vote.getUserId();
			}
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	@Override
	public Map<String, Object> getFormParameters(final ComponentParameter compParameter) {
		final Map<String, Object> parameters = super.getFormParameters(compParameter);
		putParameter(compParameter, parameters, PortalUtils.BEAN_ID);
		putParameter(compParameter, parameters, PortalUtils.PAGELET_ID);
		return parameters;
	}

	@Override
	public <T extends IDataObjectBean> void doDeleteCallback(final ComponentParameter compParameter,
			final IDataObjectValue dataObjectValue, final Class<T> beanClazz) {
		super.doDeleteCallback(compParameter, dataObjectValue, beanClazz);
		final ComponentParameter nComponentParameter = PortalUtils
				.getComponentParameter(compParameter);
		final PageletBean pagelet = PortalUtils.getPageletBean(nComponentParameter);
		if (pagelet.removeOptionProperty("documentId")) {
			PortalUtils.savePortal(nComponentParameter);
		}
	}

	@Override
	public Object getDocumentId(final ComponentParameter compParameter) {
		final ComponentParameter nComponentParameter = PortalUtils
				.getComponentParameter(compParameter);
		final PageletBean pagelet = PortalUtils.getPageletBean(nComponentParameter);
		Long documentId = ConvertUtils.toLong(pagelet.getOptionProperty("documentId"));
		if (documentId == null) {
			documentId = getTableEntityManager(compParameter).nextIncValue("id");
			pagelet.setOptionProperty("documentId", documentId);
			PortalUtils.savePortal(nComponentParameter);
		}
		return documentId;
	}
}
