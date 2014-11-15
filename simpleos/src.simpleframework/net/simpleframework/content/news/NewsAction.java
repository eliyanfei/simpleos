package net.simpleframework.content.news;

import net.simpleframework.content.AbstractMgrToolsAction;
import net.simpleframework.content.IContentApplicationModule;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class NewsAction extends AbstractMgrToolsAction {
	@Override
	public IContentApplicationModule getApplicationModule() {
		return NewsUtils.applicationModule;
	}

	@Override
	protected void doStatRebuild() {
		NewsUtils.doStatRebuild();
	}
}
