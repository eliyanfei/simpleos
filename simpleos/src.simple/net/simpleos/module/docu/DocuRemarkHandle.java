package net.simpleos.module.docu;

import java.util.Map;

import net.simpleframework.ado.IDataObjectValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.content.component.remark.DefaultRemarkHandle;
import net.simpleframework.core.IApplicationModule;
import net.simpleframework.core.bean.IDataObjectBean;
import net.simpleframework.core.bean.IIdBeanAware;
import net.simpleframework.core.id.ID;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleos.i.ICommonBeanAware;

/**
 * 
 * @author 李岩飞 
 * @email eliyanfei@126.com
 * @date 2014年11月21日 下午4:15:13 
 * @Description: 文档评论处理
 *
 */
public class DocuRemarkHandle extends DefaultRemarkHandle {

	@Override
	public Object getDocumentId(ComponentParameter compParameter) {
		final Object o = compParameter.getRequestParameter(DocuUtils.docuId);
		if (o != null) {
			return o;
		}
		return super.getDocumentId(compParameter);
	}

	public IApplicationModule getApplicationModule() {
		return DocuUtils.applicationModule;
	}

	@Override
	public Class<? extends IIdBeanAware> getEntityBeanClass() {
		return DocuRemark.class;
	}

	@Override
	public <T extends IDataObjectBean> void doAddCallback(ComponentParameter compParameter, ITableEntityManager temgr, T t, Map<String, Object> data,
			Class<T> beanClazz) {
		super.doAddCallback(compParameter, temgr, t, data, beanClazz);
		final DocuRemark remark = (DocuRemark) t;

		ICommonBeanAware.Utils.updateRemarks(compParameter, DocuUtils.applicationModule.getDataObjectManager(), DocuBean.class,
				remark.getDocumentId());
	}

	@Override
	public <T extends IDataObjectBean> void doDeleteCallback(ComponentParameter compParameter, IDataObjectValue dataObjectValue, Class<T> beanClazz) {
		super.doDeleteCallback(compParameter, dataObjectValue, beanClazz);
		final ITableEntityManager tMgr = DocuUtils.applicationModule.getDataObjectManager();
		final DocuBean docuBean = tMgr.queryForObjectById(getDocumentId(compParameter), DocuBean.class);
		if (docuBean != null) {
			docuBean.setRemarks(docuBean.getRemarks() - 1);
			tMgr.update(new String[] { "remarks" }, docuBean);
		}
	}

	@Override
	protected String getTopic(final ComponentParameter compParameter, ID documentId) {
		final ITableEntityManager tMgr = DocuUtils.applicationModule.getDataObjectManager();
		final DocuBean docuBean = tMgr.queryForObjectById(documentId, DocuBean.class);
		if (docuBean != null) {
			compParameter.setRequestAttribute("userId", docuBean.getUserId());
			return "-" + LocaleI18n.getMessage("Docu.0") + "《" + DocuUtils.wrapOpenLink(compParameter, docuBean) + "》";
		}
		return null;
	}

	@Override
	public Map<String, Object> getFormParameters(ComponentParameter compParameter) {
		final Map<String, Object> parameters = super.getFormParameters(compParameter);
		putParameter(compParameter, parameters, DocuUtils.docuId);
		return parameters;
	}
}
