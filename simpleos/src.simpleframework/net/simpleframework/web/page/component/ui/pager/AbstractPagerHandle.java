package net.simpleframework.web.page.component.ui.pager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.simpleframework.ado.db.SQLValue;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.web.page.component.AbstractComponentHandle;
import net.simpleframework.web.page.component.ComponentParameter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractPagerHandle extends AbstractComponentHandle implements IPagerHandle {

	@Override
	public Map<String, Object> getFormParameters(final ComponentParameter compParameter) {
		final Map<String, Object> parameters = super.getFormParameters(compParameter);
		putParameter(compParameter, parameters, PagerUtils.BEAN_ID_NAME);
		putParameter(compParameter, parameters, getBeanIdName());
		putParameter(compParameter, parameters, (String) compParameter.getBeanProperty("pageNumberParameterName"));
		putParameter(compParameter, parameters, (String) compParameter.getBeanProperty("pageItemsParameterName"));
		return parameters;
	}

	@Override
	public IDataObjectQuery<?> createDataObjectQuery(final ComponentParameter compParameter) {
		return null;
	}

	protected String getBeanIdName() {
		return PagerUtils.BEAN_ID;
	}

	protected static final String QUERY_CACHE = "__query_cache";

	/**
	 * count和查询语句不通
	 * @return
	 */
	protected boolean dataSplit() {
		return false;
	}

	protected IDataObjectQuery<?> getDataObjectQuery(final ComponentParameter compParameter) {
		IDataObjectQuery<?> dataObjectQuery = (IDataObjectQuery<?>) compParameter.getRequestAttribute(QUERY_CACHE);
		if (dataObjectQuery == null) {
			compParameter.setRequestAttribute(QUERY_CACHE, dataObjectQuery = createDataObjectQuery(compParameter));
			if (dataObjectQuery != null) {
				dataObjectQuery.setFetchSize(Math.min(((PagerBean) compParameter.componentBean).getPageItems(), 100));
			}
		}
		return dataObjectQuery;
	}

	protected List<Object> getData(final ComponentParameter compParameter, final IDataObjectQuery<?> dataQuery, final int start) {
		dataQuery.move(start - 1);
		final List<Object> data = new ArrayList<Object>();
		Object object;
		int i = 0;
		while ((object = dataQuery.next()) != null) {
			data.add(object);
			if (++i == Math.min(PagerUtils.getPageItems(compParameter), 100)) {
				break;
			}
		}
		return data;
	}

	@Override
	public int getCount(final ComponentParameter compParameter) {
		final IDataObjectQuery<?> dataQuery = getDataObjectQuery(compParameter);
		doCount(compParameter, dataQuery);
		return dataQuery == null ? 0 : dataQuery.getCount();
	}

	protected void doCount(final ComponentParameter compParameter, final IDataObjectQuery<?> dataQuery) {
	}

	private static final String PROCESS_TIMES = "__process_times";

	@Override
	public void process(final ComponentParameter compParameter, final int start) {
		final long l = System.currentTimeMillis();
		compParameter.setStart(start);
		final IDataObjectQuery<?> dataQuery = getDataObjectQuery(compParameter);
		if (dataQuery != null) {
			if (dataSplit()) {
				dataQuery.setSqlValue(createSqlValue(compParameter));
			}
			doResult(compParameter, dataQuery, start);
		}
		compParameter.setSessionAttribute(PROCESS_TIMES, System.currentTimeMillis() - l);
	}

	protected SQLValue createSqlValue(final ComponentParameter compParameter) {
		return null;
	}

	protected void wrapNavImage(final ComponentParameter compParameter, final StringBuilder sb) {
		final Object times = compParameter.getSessionAttribute(PROCESS_TIMES);
		if (times != null) {
			sb.append("<span style=\"margin-left: 8px;\">( ");
			sb.append(times).append("ms )</span>");
			compParameter.removeSessionAttribute(PROCESS_TIMES);
		}
		sb.insert(0, "<div class=\"nav0_image\">");
		sb.append("</div>");
	}

	protected void doResult(final ComponentParameter compParameter, final IDataObjectQuery<?> dataQuery, final int start) {
		compParameter.setRequestAttribute(PAGER_LIST, getData(compParameter, dataQuery, start));
	}

	@Override
	public String getPagerUrl(final ComponentParameter compParameter, final EPagerPosition pagerPosition, final int pageItems,
			final Map<String, Integer> pageVar) {
		// final String lastUrl = (String) compParameter
		// .getSessionAttribute(IPageConstants.SESSION_LAST_URL);
		// if (StringUtils.hasText(lastUrl)) {
		// return WebUtils.addParameters(lastUrl,
		// IPagerHandle.Helper.getPageNumberParameter(compParameter,
		// pagerPosition, pageVar));
		// }
		final StringBuilder sb = new StringBuilder();
		sb.append("javascript:$Actions['").append(compParameter.getBeanProperty("name"));
		sb.append("'].doPager(this, '");
		if (pagerPosition == EPagerPosition.pageNumber) {
			sb.append(compParameter.getBeanProperty("pageNumberParameterName")).append("=");
		} else {
			sb.append(compParameter.getBeanProperty("pageItemsParameterName")).append("=");
		}
		if (pagerPosition == EPagerPosition.pageItems) {
			sb.append("' + ").append("$F(this)").append(" + '");
		} else if (pagerPosition == EPagerPosition.pageNumber) {
			sb.append("' + ").append("$F(this)").append(" + '");
		} else {
			sb.append(pageItems);
		}
		sb.append("&");
		sb.append(getPageNumberParameter(compParameter, pagerPosition, pageVar));
		sb.append("');");
		return sb.toString();
	}

	protected String getPageNumberParameter(final ComponentParameter compParameter, final EPagerPosition pagerPosition,
			final Map<String, Integer> pageVar) {
		final StringBuilder sb = new StringBuilder();
		sb.append(compParameter.getBeanProperty("pageNumberParameterName"));
		sb.append("=");
		final int pageNumber = ConvertUtils.toInt(pageVar.get("pageNumber"), 0);
		final int currentPageNumber = ConvertUtils.toInt(pageVar.get("currentPageNumber"), 0);
		final int pageCount = ConvertUtils.toInt(pageVar.get("pageCount"), 0);
		if (pagerPosition == EPagerPosition.left2) {
			sb.append(1);
		} else if (pagerPosition == EPagerPosition.left) {
			sb.append(currentPageNumber > 1 ? (currentPageNumber - 1) : 1);
		} else if (pagerPosition == EPagerPosition.number) {
			sb.append(pageNumber);
		} else if (pagerPosition == EPagerPosition.right) {
			sb.append(currentPageNumber >= pageCount ? pageCount : currentPageNumber + 1);
		} else if (pagerPosition == EPagerPosition.right2) {
			sb.append(pageCount);
		} else if (pagerPosition == EPagerPosition.pageItems) {
			sb.append(1);
		}
		return sb.toString();
	}
}
