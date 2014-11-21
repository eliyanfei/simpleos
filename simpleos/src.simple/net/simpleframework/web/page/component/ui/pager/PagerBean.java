package net.simpleframework.web.page.component.ui.pager;

import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.PageDocument;
import net.simpleframework.web.page.ReflectUtils;
import net.simpleframework.web.page.component.AbstractContainerBean;
import net.simpleframework.web.page.component.IComponentRegistry;
import net.simpleframework.web.page.component.ado.IJobPropertyAware;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class PagerBean extends AbstractContainerBean implements IJobPropertyAware {

	private EPagerBarLayout pagerBarLayout;

	private String title;

	private int pageItems;

	private int indexPages;

	private String dataPath;

	private String noResultDesc;

	private String jsLoadedCallback;

	private boolean showEditPageItems = true, showEditPageNumber = true;

	private String exportAction;

	private boolean showLoading = true, loadingModal = false;

	private String jobView;

	private String pageNumberParameterName, pageItemsParameterName;

	private String catalogId;

	private boolean assertCatalogNull;

	private String jobAdd, jobEdit, jobDelete, jobExchange;

	private boolean showHeader = true;

	public PagerBean(final IComponentRegistry componentRegistry, final PageDocument pageDocument, final Element element) {
		super(componentRegistry, pageDocument, element);
		setJobView(ReflectUtils.jobAnonymous);
		setNoResultDesc(LocaleI18n.getMessage("pager.0"));
	}

	public void setShowHeader(boolean showHeader) {
		this.showHeader = showHeader;
	}

	public boolean isShowHeader() {
		return showHeader;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public int getPageItems() {
		if (getPagerBarLayout() == EPagerBarLayout.none) {
			return Integer.MAX_VALUE;
		} else {
			return pageItems > 0 ? pageItems : 15;
		}
	}

	public void setPageItems(final int pageItems) {
		this.pageItems = pageItems;
	}

	public void setShowEditPageNumber(boolean showEditPageNumber) {
		this.showEditPageNumber = showEditPageNumber;
	}

	public boolean isShowEditPageNumber() {
		return showEditPageNumber;
	}

	public boolean isShowEditPageItems() {
		return showEditPageItems;
	}

	public void setShowEditPageItems(final boolean showEditPageItems) {
		this.showEditPageItems = showEditPageItems;
	}

	public int getIndexPages() {
		return indexPages > 0 ? indexPages : 5;
	}

	public void setIndexPages(final int indexPages) {
		this.indexPages = indexPages;
	}

	public EPagerBarLayout getPagerBarLayout() {
		return pagerBarLayout != null ? pagerBarLayout : EPagerBarLayout.both;
	}

	public void setPagerBarLayout(final EPagerBarLayout pagerBarLayout) {
		this.pagerBarLayout = pagerBarLayout;
	}

	public String getDataPath() {
		return dataPath;
	}

	public void setDataPath(final String dataPath) {
		this.dataPath = dataPath;
	}

	public String getNoResultDesc() {
		return noResultDesc;
	}

	public void setNoResultDesc(final String noResultDesc) {
		this.noResultDesc = noResultDesc;
	}

	public String getJsLoadedCallback() {
		return jsLoadedCallback;
	}

	public void setJsLoadedCallback(final String jsLoadedCallback) {
		this.jsLoadedCallback = jsLoadedCallback;
	}

	public String getJobView() {
		return jobView;
	}

	public void setJobView(final String jobView) {
		this.jobView = jobView;
	}

	public String getExportAction() {
		return exportAction;
	}

	public void setExportAction(final String exportAction) {
		this.exportAction = exportAction;
	}

	static final String PAGE_NUMBER = "pageNumber";

	static final String PAGE_ITEMs = "pageItems";

	public String getPageNumberParameterName() {
		return StringUtils.text(pageNumberParameterName, PAGE_NUMBER);
	}

	public void setPageNumberParameterName(final String pageNumberParameterName) {
		this.pageNumberParameterName = pageNumberParameterName;
	}

	public String getPageItemsParameterName() {
		return StringUtils.text(pageItemsParameterName, PAGE_ITEMs);
	}

	public void setPageItemsParameterName(final String pageItemsParameterName) {
		this.pageItemsParameterName = pageItemsParameterName;
	}

	public boolean isShowLoading() {
		return showLoading;
	}

	public void setShowLoading(final boolean showLoading) {
		this.showLoading = showLoading;
	}

	public boolean isLoadingModal() {
		return loadingModal;
	}

	public void setLoadingModal(final boolean loadingModal) {
		this.loadingModal = loadingModal;
	}

	@Override
	public String getJobAdd() {
		return jobAdd;
	}

	@Override
	public void setJobAdd(final String jobAdd) {
		this.jobAdd = jobAdd;
	}

	@Override
	public String getJobEdit() {
		return jobEdit;
	}

	@Override
	public void setJobEdit(final String jobEdit) {
		this.jobEdit = jobEdit;
	}

	@Override
	public String getJobDelete() {
		return jobDelete;
	}

	@Override
	public void setJobDelete(final String jobDelete) {
		this.jobDelete = jobDelete;
	}

	@Override
	public String getJobExchange() {
		return jobExchange;
	}

	@Override
	public void setJobExchange(final String jobExchange) {
		this.jobExchange = jobExchange;
	}

	public String getCatalogId() {
		return catalogId;
	}

	public void setCatalogId(final String catalogId) {
		this.catalogId = catalogId;
	}

	public boolean isAssertCatalogNull() {
		return assertCatalogNull;
	}

	public void setAssertCatalogNull(final boolean assertCatalogNull) {
		this.assertCatalogNull = assertCatalogNull;
	}

	@Override
	protected String[] elementAttributes() {
		return new String[] { "jsLoadedCallback" };
	}
}
