package net.simpleframework.my.friends;

import java.util.Map;

import net.simpleframework.core.ExecutorRunnable;
import net.simpleframework.core.IInitializer;
import net.simpleframework.core.ITaskExecutorAware;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.util.DateUtils;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.AbstractWebApplicationModule;
import net.simpleframework.web.page.PageRequestResponse;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DefaultFriendsApplicationModule extends AbstractWebApplicationModule implements
		IFriendsApplicationModule {
	static final String deployName = "my/friends";

	@Override
	protected void putTables(final Map<Class<?>, Table> tables) {
		tables.put(Friends.class, new Table("simple_my_friends"));
		tables.put(FriendsGroup.class, new Table("simple_my_friends_group"));
		tables.put(FriendsRequest.class, new Table("simple_my_friends_request"));
	}

	@Override
	public void init(final IInitializer initializer) {
		super.init(initializer);
		doInit(FriendsUtils.class, deployName);

		((ITaskExecutorAware) getApplication()).getTaskExecutor().addScheduledTask(60 * 5,
				DateUtils.DAY_PERIOD, new ExecutorRunnable() {
					@Override
					public void task() {
						FriendsUtils.doStatRebuild();
					}
				});
	}

	private String friendsRequestUrl;

	@Override
	public String getFriendsRequestUrl(final PageRequestResponse requestResponse) {
		return friendsRequestUrl;
	}

	public void setFriendsRequestUrl(final String friendsRequestUrl) {
		this.friendsRequestUrl = friendsRequestUrl;
	}

	private String friendsSearchUrl;

	@Override
	public String getFriendsSearchUrl(final PageRequestResponse requestResponse) {
		return friendsSearchUrl;
	}

	public void setFriendsSearchUrl(final String friendsSearchUrl) {
		this.friendsSearchUrl = friendsSearchUrl;
	}

	@Override
	public String getApplicationText() {
		return StringUtils.text(super.getApplicationText(),
				LocaleI18n.getMessage("DefaultFriendsApplicationModule.0"));
	}
}
