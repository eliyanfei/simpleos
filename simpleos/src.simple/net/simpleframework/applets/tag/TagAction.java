package net.simpleframework.applets.tag;

import java.util.Map;

import net.simpleframework.ado.IDataObjectValue;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.event.TableEntityAdapter;
import net.simpleframework.content.EContentType;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.UrlForward;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.base.ajaxrequest.AbstractAjaxRequestHandle;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class TagAction extends AbstractAjaxRequestHandle {
	@Override
	public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
		if ("jobExecute".equals(beanProperty)) {
			final String ajaxComponentName = compParameter.componentBean.getName();
			if ("ajaxTagsManagerPage".equals(ajaxComponentName)
					|| "ajaxTagRPage".equals(ajaxComponentName)) {
				return TagUtils.applicationModule.getManager(compParameter);
			}
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	public IForward tagDelete(final ComponentParameter compParameter) {
		final String tag = compParameter.getRequestParameter(ITagApplicationModule._TAG_ID);
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				TagUtils.getTableEntityManager(TagBean.class).deleteTransaction(
						new ExpressionValue("id=?", new Object[] { tag }), new TableEntityAdapter() {
							@Override
							public void afterDelete(final ITableEntityManager manager,
									final IDataObjectValue dataObjectValue) {
								TagUtils.getTableEntityManager(TagRBean.class).delete(
										new ExpressionValue("tagid=?", new Object[] { tag }));
							}
						});
			}
		});
	}

	public IForward tagRDelete(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				TagUtils.getTableEntityManager(TagRBean.class).delete(
						new ExpressionValue("tagid=? and rid=?", new Object[] {
								compParameter.getRequestParameter("t"),
								compParameter.getRequestParameter("c") }));
			}
		});
	}

	public IForward tagsManagerUrl(final ComponentParameter compParameter) {
		return new UrlForward(TagUtils.deployPath + "jsp/tags_manager.jsp");
	}

	public IForward tagRUrl(final ComponentParameter compParameter) {
		return new UrlForward(TagUtils.deployPath + "jsp/tags_r.jsp");
	}

	public IForward tagRebuild(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				TagUtils.applicationModule.reCreateAllTags(compParameter);
				json.put("result", LocaleI18n.getMessage("TagAction.0"));
			}
		});
	}

	public IForward tagOptionSave(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				final ITableEntityManager temgr = TagUtils.getTableEntityManager();
				final TagBean tag = temgr.queryForObjectById(
						compParameter.getRequestParameter(ITagApplicationModule._TAG_ID), TagBean.class);
				if (tag != null) {
					tag.setTtype(ConvertUtils.toEnum(EContentType.class,
							compParameter.getRequestParameter("to_type")));
					temgr.update(new Object[] { "ttype" }, tag);
				}
			}
		});
	}
}
