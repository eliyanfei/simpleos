package net.simpleframework.organization.component.login;

import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.ETextAlign;
import net.simpleframework.web.page.PageDocument;
import net.simpleframework.web.page.component.AbstractContainerBean;
import net.simpleframework.web.page.component.IComponentHandle;
import net.simpleframework.web.page.component.IComponentRegistry;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class LoginBean extends AbstractContainerBean {

	private boolean showAutoLogin = true;

	private boolean showResetAction = true;

	private boolean showGetPassword = true;

	private String registAction;

	private ETextAlign actionAlign;

	private String loginForward = "/";

	private String loginCallback;

	public LoginBean(final IComponentRegistry componentRegistry, final PageDocument pageDocument,
			final Element element) {
		super(componentRegistry, pageDocument, element);
	}

	@Override
	protected Class<? extends IComponentHandle> getDefaultHandleClass() {
		return DefaultLoginHandle.class;
	}

	public boolean isShowAutoLogin() {
		return showAutoLogin;
	}

	public void setShowAutoLogin(final boolean showAutoLogin) {
		this.showAutoLogin = showAutoLogin;
	}

	public boolean isShowResetAction() {
		return showResetAction;
	}

	public void setShowResetAction(final boolean showResetAction) {
		this.showResetAction = showResetAction;
	}

	public String getLoginForward() {
		return loginForward;
	}

	public void setLoginForward(final String loginForward) {
		this.loginForward = loginForward;
	}

	public ETextAlign getActionAlign() {
		return actionAlign == null ? ETextAlign.right : actionAlign;
	}

	public void setActionAlign(final ETextAlign actionAlign) {
		this.actionAlign = actionAlign;
	}

	public boolean isShowGetPassword() {
		return showGetPassword;
	}

	public void setShowGetPassword(final boolean showGetPassword) {
		this.showGetPassword = showGetPassword;
	}

	static final String defaultRegistAction = "$Actions['registWindow']();";

	public String getRegistAction() {
		return StringUtils.hasText(registAction) ? registAction : defaultRegistAction;
	}

	public void setRegistAction(final String registAction) {
		this.registAction = registAction;
	}

	public String getLoginCallback() {
		return loginCallback;
	}

	public void setLoginCallback(final String loginCallback) {
		this.loginCallback = loginCallback;
	}

	@Override
	protected String[] elementAttributes() {
		return new String[] { "loginForward", "loginCallback" };
	}
}
