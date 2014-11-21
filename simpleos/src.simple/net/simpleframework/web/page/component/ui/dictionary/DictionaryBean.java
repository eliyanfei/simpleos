package net.simpleframework.web.page.component.ui.dictionary;

import net.simpleframework.core.AbstractElementBean;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.PageDocument;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.IComponentRegistry;
import net.simpleframework.web.page.component.ui.window.WindowBean;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DictionaryBean extends WindowBean {
	private String bindingId;

	private String bindingText;

	private String clearAction, refreshAction;

	private boolean showHelpTooltip = true;

	private AbstractDictionaryTypeBean dictionaryTypeBean;

	private String jsSelectCallback;

	public DictionaryBean(final IComponentRegistry componentRegistry,
			final PageDocument pageDocument, final Element element) {
		super(componentRegistry, pageDocument, element);
		setDestroyOnClose(false);
		setWidth(240);
	}

	public String getJsSelectCallback() {
		return jsSelectCallback;
	}

	public void setJsSelectCallback(final String jsSelectCallback) {
		this.jsSelectCallback = jsSelectCallback;
	}

	public AbstractDictionaryTypeBean getDictionaryTypeBean() {
		return dictionaryTypeBean;
	}

	public void setDictionaryTypeBean(final AbstractDictionaryTypeBean dictionaryTypeBean) {
		this.dictionaryTypeBean = dictionaryTypeBean;
	}

	@Override
	public boolean isPopup() {
		return true;
	}

	@Override
	public boolean isModal() {
		return false;
	}

	@Override
	public String getUrl() {
		return null;
	}

	public String getBindingId() {
		return bindingId;
	}

	public void setBindingId(final String bindingId) {
		this.bindingId = bindingId;
	}

	public String getBindingText() {
		return bindingText;
	}

	public void setBindingText(final String bindingText) {
		this.bindingText = bindingText;
	}

	public String getClearAction(final ComponentParameter compParameter) {
		if ("false".equalsIgnoreCase(clearAction)) {
			return null;
		}
		if (!StringUtils.hasText(clearAction)) {
			final StringBuilder sb = new StringBuilder();
			sb.append("var o;");
			if (StringUtils.hasText(bindingId)) {
				sb.append("o=$(\"").append(bindingId).append("\");if(o) o.clear();");
			}
			if (StringUtils.hasText(bindingText)) {
				sb.append("o=$(\"").append(bindingText).append("\");if(o) o.clear();");
			}
			sb.append("$Actions[\"").append(compParameter.getBeanProperty("name"))
					.append("\"].close();");
			return sb.toString();
		}
		return clearAction;
	}

	public void setClearAction(final String clearAction) {
		this.clearAction = clearAction;
	}

	public String getRefreshAction(final ComponentParameter compParameter) {
		if ("false".equalsIgnoreCase(refreshAction)) {
			return null;
		}
		if (!StringUtils.hasText(refreshAction)) {
			final StringBuilder sb = new StringBuilder();
			final AbstractDictionaryTypeBean dictionaryType = getDictionaryTypeBean();
			if (dictionaryType instanceof DictionaryTreeBean) {
				sb.append("$Actions[\"").append(((DictionaryTreeBean) dictionaryType).getRef());
				sb.append("\"].refresh();");
			}
			return sb.toString();
		}
		return refreshAction;
	}

	public void setRefreshAction(final String refreshAction) {
		this.refreshAction = refreshAction;
	}

	public boolean isShowHelpTooltip() {
		return showHelpTooltip;
	}

	public void setShowHelpTooltip(final boolean showHelpTooltip) {
		this.showHelpTooltip = showHelpTooltip;
	}

	public static class DictionaryListBean extends AbstractDictionaryTypeBean {

		private String ref;

		public DictionaryListBean(final DictionaryBean dictionaryBean, final Element element) {
			super(dictionaryBean, element);
		}

		public String getRef() {
			return ref;
		}

		public void setRef(final String ref) {
			this.ref = ref;
		}
	}

	public static class DictionaryTreeBean extends AbstractDictionaryTypeBean {

		private String ref;

		private boolean updateImmediately = true;

		public DictionaryTreeBean(final DictionaryBean dictionaryBean, final Element element) {
			super(dictionaryBean, element);
		}

		public String getRef() {
			return ref;
		}

		public void setRef(final String ref) {
			this.ref = ref;
		}

		public boolean isUpdateImmediately() {
			return updateImmediately;
		}

		public void setUpdateImmediately(final boolean updateImmediately) {
			this.updateImmediately = updateImmediately;
		}
	}

	public static class DictionaryColorBean extends AbstractDictionaryTypeBean {

		private String ref;

		public DictionaryColorBean(final DictionaryBean dictionaryBean, final Element element) {
			super(dictionaryBean, element);
		}

		public String getRef() {
			return ref;
		}

		public void setRef(final String ref) {
			this.ref = ref;
		}
	}

	public static class DictionaryFontBean extends AbstractDictionaryTypeBean {

		public DictionaryFontBean(final DictionaryBean dictionaryBean, final Element element) {
			super(dictionaryBean, element);
		}
	}

	public static class DictionarySmileyBean extends AbstractDictionaryTypeBean {

		public DictionarySmileyBean(final DictionaryBean dictionaryBean, final Element element) {
			super(dictionaryBean, element);
		}
	}

	static abstract class AbstractDictionaryTypeBean extends AbstractElementBean {
		private final DictionaryBean dictionaryBean;

		public AbstractDictionaryTypeBean(final DictionaryBean dictionaryBean, final Element element) {
			super(element);
			dictionaryBean.setDictionaryTypeBean(this);
			this.dictionaryBean = dictionaryBean;
		}

		public DictionaryBean getDictionaryBean() {
			return dictionaryBean;
		}
	}

	@Override
	protected String[] elementAttributes() {
		return new String[] { "jsSelectCallback", "clearAction", "refreshAction", "jsShownCallback",
				"jsHiddenCallback" };
	}
}
