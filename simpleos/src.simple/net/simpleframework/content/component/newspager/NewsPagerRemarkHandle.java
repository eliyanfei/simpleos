package net.simpleframework.content.component.newspager;

import java.util.Map;

import net.simpleframework.ado.IDataObjectValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.content.component.remark.DefaultRemarkHandle;
import net.simpleframework.content.component.remark.RemarkItem;
import net.simpleframework.content.news.NewsUtils;
import net.simpleframework.core.IApplicationModule;
import net.simpleframework.core.bean.IDataObjectBean;
import net.simpleframework.web.page.component.ComponentParameter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class NewsPagerRemarkHandle extends DefaultRemarkHandle {

	@Override
	public IApplicationModule getApplicationModule() {
		return NewsUtils.applicationModule;
	}

	@Override
	public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
		if ("jobEdit".equals(beanProperty) || "jobDelete".equals(beanProperty)) {
			final ComponentParameter nComponentParameter = NewsPagerUtils
					.getComponentParameter(compParameter);
			final INewsPagerHandle nHandle = (INewsPagerHandle) nComponentParameter
					.getComponentHandle();
			return nHandle.getManager(compParameter);
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	@Override
	public Map<String, Object> getFormParameters(final ComponentParameter compParameter) {
		final Map<String, Object> parameters = super.getFormParameters(compParameter);
		final ComponentParameter nComponentParameter = NewsPagerUtils
				.getComponentParameter(compParameter);
		parameters.put(NewsPagerUtils.BEAN_ID, nComponentParameter.componentBean.hashId());
		return parameters;
	}

	@Override
	public String getDocumentIdParameterName(final ComponentParameter compParameter) {
		final ComponentParameter nComponentParameter = NewsPagerUtils
				.getComponentParameter(compParameter);
		final INewsPagerHandle nHandle = (INewsPagerHandle) nComponentParameter.getComponentHandle();
		return nHandle.getIdParameterName(compParameter);
	}

	@Override
	public <T extends IDataObjectBean> void doAddCallback(final ComponentParameter compParameter,
			final ITableEntityManager temgr, final T t, final Map<String, Object> data,
			final Class<T> beanClazz) {
		super.doAddCallback(compParameter, temgr, t, data, beanClazz);

		final ComponentParameter nComponentParameter = NewsPagerUtils
				.getComponentParameter(compParameter);
		final INewsPagerHandle nHandle = (INewsPagerHandle) nComponentParameter.getComponentHandle();
		final NewsBean news = nHandle.getEntityBeanById(nComponentParameter,
				((RemarkItem) t).getDocumentId());
		news.setRemarks(news.getRemarks() + 1);
		nHandle.getTableEntityManager(nComponentParameter).update(new String[] { "remarks" }, news);
	}

	@Override
	public <T extends IDataObjectBean> void doDeleteCallback(final ComponentParameter compParameter,
			final IDataObjectValue dataObjectValue, final Class<T> beanClazz) {
		super.doDeleteCallback(compParameter, dataObjectValue, beanClazz);

		final ComponentParameter nComponentParameter = NewsPagerUtils
				.getComponentParameter(compParameter);
		final INewsPagerHandle nHandle = (INewsPagerHandle) nComponentParameter.getComponentHandle();
		final NewsBean news = nHandle.getEntityBeanById(nComponentParameter,
				getDocumentId(compParameter));
		news.setRemarks(Math.max(0, news.getRemarks() - dataObjectValue.getValues().length));
		nHandle.getTableEntityManager(nComponentParameter).update(news);
	}
}
