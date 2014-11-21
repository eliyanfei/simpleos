package net.simpleframework.content;

import java.util.Map;

import net.simpleframework.core.IApplicationModuleAware;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.base.ajaxrequest.AbstractAjaxRequestHandle;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractMgrToolsAction extends AbstractAjaxRequestHandle implements IApplicationModuleAware {
	@Override
	public abstract IContentApplicationModule getApplicationModule();

	protected abstract void doStatRebuild();

	public IForward statRebuild(final ComponentParameter compParameter) {
		doStatRebuild();
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				json.put("info", LocaleI18n.getMessage("manager_tools.3"));
			}
		});
	}

	public IForward indexRebuild(final ComponentParameter compParameter) {
		final ComponentParameter nComponentParameter = ComponentParameter.get(compParameter, getApplicationModule().getComponentBean(compParameter));
		((AbstractContentPagerHandle) nComponentParameter.getComponentHandle()).createLuceneManager(nComponentParameter).rebuildAll(true);
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				json.put("info", LocaleI18n.getMessage("manager_tools.6"));
			}
		});
	}
}
