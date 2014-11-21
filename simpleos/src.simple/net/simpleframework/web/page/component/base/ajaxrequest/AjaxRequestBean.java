package net.simpleframework.web.page.component.base.ajaxrequest;

import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.PageDocument;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.AbstractComponentRegistry;
import net.simpleframework.web.page.component.IComponentRegistry;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class AjaxRequestBean extends AbstractComponentBean {
	private String handleMethod;

	private String encoding;

	private String updateContainerId;

	private boolean updateContainerCache;

	private String confirmMessage;

	private EThrowException throwException;

	private boolean showLoading = true, loadingModal = false, parallel = false,
			disabledTriggerAction = true;

	private String jsCompleteCallback;

	private String urlForward;

	private String ajaxRequestId;

	private String jobExecute;

	public AjaxRequestBean(final IComponentRegistry componentRegistry,
			final PageDocument pageDocument, final Element element) {
		super(componentRegistry, pageDocument, element);
		setRunImmediately(false);
	}

	public AjaxRequestBean(final PageDocument pageDocument, final Element element) {
		this(AbstractComponentRegistry.getRegistry(AjaxRequestRegistry.ajaxRequest), pageDocument,
				element);
	}

	public AjaxRequestBean(final PageDocument pageDocument) {
		this(pageDocument, null);
	}

	public String getHandleMethod() {
		return handleMethod;
	}

	public void setHandleMethod(final String handleMethod) {
		this.handleMethod = handleMethod;
	}

	public boolean isUpdateContainerCache() {
		return updateContainerCache;
	}

	public void setUpdateContainerCache(final boolean updateContainerCache) {
		this.updateContainerCache = updateContainerCache;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(final String encoding) {
		this.encoding = encoding;
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

	public boolean isParallel() {
		return parallel;
	}

	public void setParallel(final boolean parallel) {
		this.parallel = parallel;
	}

	public boolean isDisabledTriggerAction() {
		return disabledTriggerAction;
	}

	public void setDisabledTriggerAction(final boolean disabledTriggerAction) {
		this.disabledTriggerAction = disabledTriggerAction;
	}

	public String getUrlForward() {
		return urlForward;
	}

	public void setUrlForward(final String urlForward) {
		this.urlForward = urlForward;
	}

	public String getConfirmMessage() {
		return confirmMessage;
	}

	public void setConfirmMessage(final String confirmMessage) {
		this.confirmMessage = confirmMessage;
	}

	public EThrowException getThrowException() {
		return throwException == null ? EThrowException.window : throwException;
	}

	public void setThrowException(final EThrowException throwException) {
		this.throwException = throwException;
	}

	public String getUpdateContainerId() {
		return updateContainerId;
	}

	public void setUpdateContainerId(final String updateContainerId) {
		this.updateContainerId = updateContainerId;
	}

	public String getJsCompleteCallback() {
		return jsCompleteCallback;
	}

	public void setJsCompleteCallback(final String jsCompleteCallback) {
		this.jsCompleteCallback = jsCompleteCallback;
	}

	public String getAjaxRequestId() {
		return ajaxRequestId;
	}

	public void setAjaxRequestId(final String ajaxRequestId) {
		this.ajaxRequestId = ajaxRequestId;
	}

	public String getJobExecute() {
		return jobExecute;
	}

	public void setJobExecute(final String jobExecute) {
		this.jobExecute = jobExecute;
	}

	@Override
	public String getHandleClass() {
		final String handleClass = super.getHandleClass();
		if (StringUtils.hasText(getUrlForward())) {
			return UrlAjaxRequestHandle.class.getName();
		}
		return handleClass;
	}

	@Override
	protected String[] elementAttributes() {
		return new String[] { "confirmMessage", "jsCompleteCallback", "urlForward" };
	}
}
