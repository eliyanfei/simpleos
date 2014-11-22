package net.simpleframework.organization.component.userpager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.simpleframework.ado.IDataObjectValue;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.event.TableEntityAdapter;
import net.simpleframework.applets.notification.MailMessageNotification;
import net.simpleframework.applets.notification.NotificationUtils;
import net.simpleframework.core.ExecutorRunnable;
import net.simpleframework.core.IApplicationModule;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.core.bean.IDataObjectBean;
import net.simpleframework.core.bean.IIdBeanAware;
import net.simpleframework.core.bean.ITreeBeanAware;
import net.simpleframework.organization.EUserStatus;
import net.simpleframework.organization.IDepartment;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.account.EAccountStatus;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.util.BeanUtils;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.HTMLUtils;
import net.simpleframework.util.JavascriptUtils;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.util.script.ScriptEvalUtils;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.HandleException;
import net.simpleframework.web.page.component.ui.pager.AbstractTablePagerData;
import net.simpleframework.web.page.component.ui.pager.db.AbstractDbTablePagerHandle;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DefaultUserPagerHandle extends AbstractDbTablePagerHandle implements IUserPagerHandle {

	@Override
	public IApplicationModule getApplicationModule() {
		return OrgUtils.applicationModule;
	}

	@Override
	public Class<? extends IIdBeanAware> getEntityBeanClass() {
		return OrgUtils.um().getBeanClass();
	}

	@Override
	protected String getBeanIdName() {
		return UserPagerUtils.BEAN_ID;
	}

	@Override
	public ITableEntityManager getTableEntityManager(final ComponentParameter compParameter, final Class<?> beanClazz) {
		return OrgUtils.getTableEntityManager(beanClazz);
	}

	@Override
	public <T extends IDataObjectBean> void doBeforeEdit(final ComponentParameter compParameter, final ITableEntityManager temgr, final T t,
			final Map<String, Object> data, final Class<T> beanClazz) {
		final Object v = data.get("department");
		if (v != null) {
			final IUser user = (IUser) t;
			if ("none".equalsIgnoreCase(v.toString())) {
				user.setDepartmentId(null);
			} else {
				final IDepartment dept = OrgUtils.dm().queryForObjectById(v);
				user.setDepartmentId(dept.getId());
			}
		}
	}

	@Override
	public IDataObjectQuery<?> createDataObjectQuery(final ComponentParameter compParameter) {
		final ExpressionValue ev = new ExpressionValue("status <> ? order by oorder desc", new Object[] { EUserStatus.delete });
		return OrgUtils.um().query(ev);
	}

	@Override
	protected void doResult(final ComponentParameter compParameter, final IDataObjectQuery<?> dataQuery, final int start) {
		super.doResult(compParameter, dataQuery, start);
		if (DefaultUserPagerHandle.class.equals(getClass())) {
			addFirstAndLastDataItem(compParameter, dataQuery);
		}
	}

	@Override
	public <T extends IDataObjectBean> void doDelete(final ComponentParameter compParameter, final IDataObjectValue ev, final Class<T> beanClazz) {
		final int result = updateStatus(compParameter, EUserStatus.delete, ev, beanClazz);
		if (result == 0) {
			super.doDelete(compParameter, ev, beanClazz);
		}
	}

	@Override
	public <T extends IDataObjectBean> void doUnDelete(final ComponentParameter compParameter, final IDataObjectValue ev, final Class<T> beanClazz) {
		updateStatus(compParameter, EUserStatus.normal, ev, beanClazz);
	}

	private <T extends IDataObjectBean> int updateStatus(final ComponentParameter compParameter, final EUserStatus updateState,
			final IDataObjectValue ev, final Class<T> beanClazz) {
		final ITableEntityManager temgr = getTableEntityManager(compParameter, beanClazz);
		final IQueryEntitySet<T> qs = temgr.query(ev, beanClazz);
		if (qs.getCount() == 0) {
			return 0;
		} else {
			final ArrayList<T> al = new ArrayList<T>();
			T t;
			while ((t = qs.next()) != null) {
				final IUser user = (IUser) t;
				if (user.getStatus() != updateState) {
					user.setStatus(updateState);
					al.add(t);
				}
			}
			return temgr.updateTransaction(new String[] { "status" }, al.toArray(), new TableEntityAdapter() {
				@Override
				public void afterUpdate(final ITableEntityManager manager, final Object[] objects) {
					for (final Object object : objects) {
						final IAccount account = ((IUser) object).account();
						if (account != null) {
							if (updateState == EUserStatus.delete) {
								account.setStatus(EAccountStatus.delete);
								if (((IUser) object).isBuildIn())
									throw HandleException.wrapException(LocaleI18n.getMessage("buildin.0"));
							} else if (updateState == EUserStatus.normal) {
								account.setStatus(EAccountStatus.normal);
							}
							OrgUtils.am().update(new String[] { "status" }, account);
						}
					}
				}
			});
		}
	}

	@Override
	public String getCatalogIdName(final PageRequestResponse requestResponse) {
		return OrgUtils.dm().getDepartmentIdParameterName();
	}

	@Override
	public Collection<? extends ITreeBeanAware> getMove2Catalogs(final ComponentParameter compParameter, final ITreeBeanAware parent) {
		return OrgUtils.dm().children((IDepartment) parent);
	}

	@Override
	public Collection<? extends ITreeBeanAware> getSelectedCatalogs(final ComponentParameter compParameter, final ITreeBeanAware parent) {
		return OrgUtils.dm().children((IDepartment) parent);
	}

	@Override
	public AbstractTablePagerData createTablePagerData(final ComponentParameter compParameter) {
		return new UserTablePagerData(compParameter);
	}

	protected class UserTablePagerData extends AbstractTablePagerData {
		public UserTablePagerData(final ComponentParameter compParameter) {
			super(compParameter);
		}

		protected final String[][] otherAttributes = new String[][] { { "email", "#(DefaultUserPagerHandle.0)" },
				{ "mobile", "#(DefaultUserPagerHandle.1)" }, { "msn", "#(DefaultUserPagerHandle.2)" }, { "address", "#(DefaultUserPagerHandle.3)" } };

		@Override
		protected Map<Object, Object> getRowData(final Object dataObject) {
			final IUser user = (IUser) dataObject;
			final IAccount account = user.account();
			if (account == null) {
				return null;
			}
			final Map<Object, Object> rowData = new HashMap<Object, Object>();
			final StringBuilder photo = new StringBuilder();
			final Object id = user.getId();
			photo.append("<img class=\"photo_icon\" id=\"user_img_");
			photo.append(id).append("\" src=\"");
			photo.append(OrgUtils.getPhotoSRC(compParameter.request, id)).append("\" />");
			rowData.put("photo", photo.toString());
			rowData.put("text", "<a tipparam='userId=" + user.getId() + "' class='a2 user_navTooltip_class' href='/space/" + user.getId()
					+ ".html' >" + user.getText() + "</a>");
			final String aAction = "<a onclick=\"__pager_action(this).account_stat(this);\">" + user.getName() + "</a>";
			rowData.put("name", aAction);
			rowData.put("birthday", ConvertUtils.toDateString(user.getBirthday(), IUser.birthdayDateFormat));
			rowData.put("sex", user.getSex());
			final StringBuilder other = new StringBuilder();
			other.append("<table style=\"width: 100%;\" cellpadding=\"2\" cellspacing=\"0\">");
			for (final String[] attri : otherAttributes) {
				other.append("<tr>");
				other.append("<td class=\"l\">").append(attri[1]).append("</td>");
				other.append("<td class=\"v\">");
				final String v = StringUtils.blank(BeanUtils.getProperty(user, attri[0]));
				if ("email".equals(attri[0])) {
					other.append("<a href=\"mailto:").append(v).append("\">");
				}
				other.append(v).append("</td>");
				if ("email".equals(attri[0])) {
					other.append("</a>");
				}
				other.append("</tr>");
			}
			///总登入次数
			other.append("<tr>");
			other.append("<td class=\"l\">登入次数</td>");
			other.append("<td class=\"v\">");
			other.append(account.getLoginTimes()).append("</td>");
			other.append("</tr>");

			other.append("</table>");
			final String desc = user.getDescription();
			if (StringUtils.hasText(desc)) {
				other.append("<script type=\"text/javascript\">");
				other.append("new Tip($('user_img_").append(id).append("'), '").append(JavascriptUtils.escape(HTMLUtils.convertHtmlLines(desc)))
						.append("', { delay: 0.5 });");
				other.append("</script>");
			}
			rowData.put("other", other.toString());
			rowData.put("action", AbstractTablePagerData.ACTIONc);
			return rowData;
		}
	}

	@Override
	public String getIdParameterName(final ComponentParameter compParameter) {
		return OrgUtils.um().getUserIdParameterName();
	}

	/********************************* mail **********************************/

	@Override
	public void doSentMail(final ComponentParameter compParameter, final Collection<IUser> users, final String topic, final String content) {
		if (users == null || users.size() == 0 || !StringUtils.hasText(topic) || !StringUtils.hasText(content)) {
			return;
		}

		final ArrayList<IUser> list = new ArrayList<IUser>(users);
		final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
		executor.scheduleAtFixedRate(new ExecutorRunnable() {
			@Override
			protected void task() {
				final int size = list.size();
				if (size == 0) {
					executor.shutdown();
					return;
				}
				final IUser user = list.remove(size - 1);
				final MailMessageNotification mailMessage = new MailMessageNotification();
				mailMessage.setHtmlContent(true);
				mailMessage.getTo().add(user);
				mailMessage.setSubject(topic);
				final Map<String, Object> variable = new HashMap<String, Object>();
				variable.put("user", user);
				variable.put("account", user.account());
				mailMessage.setTextBody(ScriptEvalUtils.replaceExpr(variable, content));
				NotificationUtils.sendMessage(mailMessage);
			}
		}, 0, 10, TimeUnit.SECONDS);
	}
}
