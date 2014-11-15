package net.simpleframework.content.component.newspager;

import java.io.File;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import net.a.ItSiteUtil;
import net.simpleframework.ado.lucene.AbstractLuceneManager;
import net.simpleframework.content.AbstractContentPagerHandle;
import net.simpleframework.content.blog.Blog;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.core.bean.IDataObjectBean;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.IoUtils;
import net.simpleframework.web.page.IPageConstants;
import net.simpleframework.web.page.component.ComponentParameter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractNewsPagerHandle extends AbstractContentPagerHandle implements INewsPagerHandle {
	public static final Table table_news = new Table("simple_news");

	@Override
	public void putTables(final Map<Class<?>, Table> tables) {
		tables.put(NewsBean.class, table_news);
	}

	@Override
	public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
		if ("title".equals(beanProperty)) {
			return getNavigateHTML(compParameter);
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	@Override
	protected String getBeanIdName() {
		return NewsPagerUtils.BEAN_ID;
	}

	@Override
	public Class<? extends IDataObjectBean> getEntityBeanClass() {
		return NewsBean.class;
	}

	static final String NEWS_ID = "__news_Id";

	@Override
	public String getIdParameterName(final ComponentParameter compParameter) {
		return NEWS_ID;
	}

	@Override
	public AbstractLuceneManager createLuceneManager(final ComponentParameter compParameter) {
		final File iPath = new File(compParameter.getServletContext().getRealPath(IPageConstants.DATA_HOME + "/" + getFunctionModule() + "_index"));
		IoUtils.createDirectoryRecursively(iPath);
		return new NewsLuceneManager(compParameter, iPath);
	}

	@Override
	public String getViewUrl(final ComponentParameter compParameter, final NewsBean news) {
		return null;
	}

	@Override
	public String wrapOpenLink(final ComponentParameter compParameter, final NewsBean news) {
		final StringWriter sb = new StringWriter();
		sb.append("<a class='nt'");
		sb.append(" target=\"_blank\"");
		sb.append(" href=\"");
		sb.append(compParameter.wrapContextPath(getViewUrl(compParameter, news))).append("\"");
		if (ConvertUtils.toBoolean(compParameter.getRequestAttribute("home"), false)) {
			sb.append(" title=\"").append(news.getTopic()).append("\"");
			sb.append(">");
			if (news instanceof Blog) {
				sb.append(ItSiteUtil.getShortString(news.getTopic(), 35, true));
			} else {
				sb.append(ItSiteUtil.getShortString(news.getTopic(), ConvertUtils.toBoolean(compParameter.getRequestAttribute("isNews"), false) ? 17
						: 19, true));
			}
		} else {
			sb.append(">");
			sb.append(news.getTopic());
		}
		sb.append("</a>");
		return sb.toString();
	}

	@Override
	public List<NewsCatalog> listNewsCatalog(final ComponentParameter compParameter) {
		return null;
	}

	@Override
	public String getRemarkHandleClass(final ComponentParameter compParameter) {
		return NewsPagerRemarkHandle.class.getName();
	}
}
