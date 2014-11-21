package net.itsite.docu;

import java.util.List;
import java.util.Map;

import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.AbstractTitleAwarePageLoad;
import net.simpleframework.web.page.PageParameter;

public class DocuPageLoad extends AbstractTitleAwarePageLoad {
	@Override
	public Object getBeanProperty(PageParameter pageParameter, String beanProperty) {
		if ("title".equals(beanProperty)) {
			final String docuId = pageParameter.getRequestParameter(DocuUtils.docuId);
			if (StringUtils.hasText(docuId)) {
				final DocuBean docuBean = DocuUtils.applicationModule.getBean(DocuBean.class, docuId);
				if (docuBean != null) {
					return wrapApplicationTitle(docuBean.getTitle() + " - " + LocaleI18n.getMessage("Docu.0"));
				}
			}
			return wrapApplicationTitle(LocaleI18n.getMessage("Docu.0"));
		}
		return super.getBeanProperty(pageParameter, beanProperty);
	}

	/**
	 * 高级属性
	 * 
	 * @throws Exception
	 */
	public void docuAttrLoad(final PageParameter pageParameter, final Map<String, Object> dataBinding, final List<String> visibleToggleSelector,
			final List<String> readonlySelector, final List<String> disabledSelector) throws Exception {
		final String docuId = pageParameter.getRequestParameter(DocuUtils.docuId);
		final DocuBean docuBean = DocuUtils.applicationModule.getBean(DocuBean.class, docuId);
		if (docuBean != null) {
			dataBinding.put("docu_status", docuBean.getStatus().name());
			dataBinding.put("docu_ttype", docuBean.getTtype().name());
			dataBinding.put("docu_grade", docuBean.getAdminGrade());
			dataBinding.put("docu_run", docuBean.isCanRun());
		}
		dataBinding.put("docuId", docuId);
	}

	/**
	 * 文档编辑
	 */
	public void docuLoad(final PageParameter pageParameter, final Map<String, Object> dataBinding, final List<String> visibleToggleSelector,
			final List<String> readonlySelector, final List<String> disabledSelector) throws Exception {
		final String docuId = pageParameter.getRequestParameter(DocuUtils.docuId);
		final DocuBean docuBean = DocuUtils.applicationModule.getBean(DocuBean.class, docuId);
		if (docuBean != null) {
			dataBinding.put("docu_title", docuBean.getTitle());
			dataBinding.put("docu_content", docuBean.getContent());
			dataBinding.put("docu_keyworks", docuBean.getKeyworks());
			dataBinding.put("docu_point", docuBean.getPoint());
			dataBinding.put("docu_free_page",
					(docuBean.getAllowRead() >= 1 || docuBean.getAllowRead() == 0) ? docuBean.getAllowRead() : docuBean.getAllowRead() * 100 + "%");
			dataBinding.put("docu_catalog", docuBean.getCatalogId());
			final StringBuffer catalogTitle = new StringBuffer();
			final DocuCatalog catalog = DocuUtils.applicationModule.getBean(DocuCatalog.class, docuBean.getCatalogId());
			if (catalog != null) {
				final DocuCatalog pCatalog = (DocuCatalog) catalog.parent(DocuUtils.applicationModule);
				if (pCatalog != null) {
					catalogTitle.append(pCatalog.getText()).append("-");
				}
				catalogTitle.append(catalog.getText());
			}
			dataBinding.put("docu_catalog_text", catalogTitle.toString());
			dataBinding.put("code_language", docuBean.getLanguage());
		}
		dataBinding.put("docuId", docuId);
	}

	/**
	* 上传文档编辑
	*/
	public void docuLoad1(final PageParameter pageParameter, final Map<String, Object> dataBinding, final List<String> visibleToggleSelector,
			final List<String> readonlySelector, final List<String> disabledSelector) throws Exception {
		final String docuId = pageParameter.getRequestParameter(DocuUtils.docuId);
		final DocuBean docuBean = DocuUtils.applicationModule.getBean(DocuBean.class, docuId);
		if (docuBean != null) {
			dataBinding.put("docu_title", docuBean.getTitle());
		}
		dataBinding.put("docuId", docuId);
	}
}
