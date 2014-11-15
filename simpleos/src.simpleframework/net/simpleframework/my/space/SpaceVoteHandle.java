package net.simpleframework.my.space;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.content.component.vote.DefaultVoteHandle;
import net.simpleframework.content.component.vote.Vote;
import net.simpleframework.content.component.vote.VoteItem;
import net.simpleframework.content.component.vote.VoteItemGroup;
import net.simpleframework.content.component.vote.VoteUtils;
import net.simpleframework.core.bean.IDataObjectBean;
import net.simpleframework.core.id.ID;
import net.simpleframework.core.id.LongID;
import net.simpleframework.web.EFunctionModule;
import net.simpleframework.web.page.component.ComponentParameter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class SpaceVoteHandle extends DefaultVoteHandle {
	static final String SESSION_VOTE = "__space_voteId";

	static final String SESSION_VOTE_BINDING_LISTENER = "__space_vote_bindinglistener";

	@Override
	public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
		if ("documentId".equals(beanProperty)) {
			return "spacevote".hashCode();
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	@Override
	public ISpaceApplicationModule getApplicationModule() {
		return MySpaceUtils.applicationModule;
	}

	@Override
	public <T extends IDataObjectBean> void doBeforeAdd(final ComponentParameter compParameter, final ITableEntityManager temgr, final T t,
			final Map<String, Object> data, final Class<T> beanClazz) {
		super.doBeforeAdd(compParameter, temgr, t, data, beanClazz);
		if (t instanceof Vote) {
			final Vote vote = (Vote) t;
			vote.setDocumentId(ID.Utils.newID("spacevote".hashCode()));
		}
	}

	@Override
	public String getJavascriptCallback(ComponentParameter compParameter, String jsAction, Object bean) {
		final StringBuffer buf = new StringBuffer();
		buf.append(super.getJavascriptCallback(compParameter, jsAction, bean));
		for (final Object key : compParameter.request.getParameterMap().keySet()) {
			final String s = key.toString();
			if (s.startsWith("__vg_")) {
				try {
					VoteItemGroup itemGroup = getTableEntityManager(compParameter, VoteItemGroup.class).queryForObjectById(
							s.substring("__vg_".length(), s.length()), VoteItemGroup.class);
					if (itemGroup != null) {
						Object v = compParameter.request.getParameterMap().get(s);
						String v1;
						if (v instanceof String) {
							v1 = v.toString();
						} else {
							v1 = ((String[]) v)[0];
						}
						VoteItem item = getTableEntityManager(compParameter, VoteItem.class).queryForObjectById(v1, VoteItem.class);
						MySpaceUtils.addSapceLog(compParameter, "参与了投票《" + itemGroup.getText() + "》,选择了-" + (item == null ? "" : item.getText()),
								EFunctionModule.space_log, new LongID("spacevote".hashCode()));
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return buf.toString();
	}

	@Override
	public <T extends IDataObjectBean> void doAddCallback(final ComponentParameter compParameter, final ITableEntityManager temgr, final T t,
			final Map<String, Object> data, final Class<T> beanClazz) {
		super.doAddCallback(compParameter, temgr, t, data, beanClazz);
		if (t instanceof Vote) {
			final ID voteId = ((Vote) t).getId();
			compParameter.setSessionAttribute(SESSION_VOTE, voteId);
			compParameter.setSessionAttribute(SESSION_VOTE_BINDING_LISTENER, new HttpSessionBindingListener() {
				@Override
				public void valueBound(final HttpSessionBindingEvent bindingEvent) {
				}

				@Override
				public void valueUnbound(final HttpSessionBindingEvent bindingEvent) {
					final Map<Class<?>, String> tablenames = new HashMap<Class<?>, String>();
					for (final Class<?> clazz : VoteUtils.VOTE_CLASS) {
						tablenames.put(clazz, MySpaceUtils.getTableEntityManager(clazz).getTablename());
					}
					temgr.execute(VoteUtils.getDeleteSQLs(tablenames, Vote.class, new Object[] { voteId }));
				}
			});
		}
	}
}
