package net.simpleframework.content.bbs;

import net.simpleframework.ado.DataObjectManagerFactory;
import net.simpleframework.content.AbstractMgrToolsAction;
import net.simpleframework.content.IContentApplicationModule;
import net.simpleframework.web.WebUtils;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.UrlForward;
import net.simpleframework.web.page.component.ComponentParameter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class BbsAction extends AbstractMgrToolsAction {

	@Override
	protected void doStatRebuild() {
		BbsUtils.doStatRebuild();

		// test cache
		DataObjectManagerFactory.resetAll();
		System.gc();
	}

	public IForward tabUrl(ComponentParameter compParameter) {
		return new UrlForward("/app/bbs/bbs_right_data.jsp?" + WebUtils.toQueryString(compParameter.request.getParameterMap()));
	}

	@Override
	public IContentApplicationModule getApplicationModule() {
		return BbsUtils.applicationModule;
	}
}
