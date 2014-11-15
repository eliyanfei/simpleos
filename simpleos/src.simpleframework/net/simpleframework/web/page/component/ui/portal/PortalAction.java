package net.simpleframework.web.page.component.ui.portal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.HTMLUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.ETextAlign;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.UrlForward;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.base.ajaxrequest.AbstractAjaxRequestHandle;
import net.simpleframework.web.page.component.ui.portal.module.IPortalModuleHandle;
import net.simpleframework.web.page.component.ui.portal.module.PortalModuleRegistryFactory;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class PortalAction extends AbstractAjaxRequestHandle {

	public IForward portalRequest(final ComponentParameter compParameter) {
		final ComponentParameter nComponentParameter = PortalUtils.getComponentParameter(compParameter);
		return new UrlForward(((PortalRender) nComponentParameter.componentBean.getComponentRender()).getResponseUrl(nComponentParameter));
	}

	public IForward contentRequest(final ComponentParameter compParameter) {
		final ComponentParameter nComponentParameter = PortalUtils.getComponentParameter(compParameter);
		final PageletBean pagelet = PortalUtils.getPageletBean(nComponentParameter);
		if (pagelet == null) {
			return null;
		}
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				final String pageletId = pagelet.id();
				json.put("li", pageletId);
				final IPortalModuleHandle layoutModuleHandle = PortalModuleRegistryFactory.getInstance().getModuleHandle(pagelet);
				if (layoutModuleHandle == null) {
					return;
				}
				String responseText = null;
				try {
					final IForward forward = layoutModuleHandle.getPageletContent(nComponentParameter);
					if (forward != null) {
						responseText = forward.getResponseText(nComponentParameter);
					}
				} catch (final Exception e) {
					responseText = HTMLUtils.convertHtmlLines(e.toString());
				}
				json.put("text", StringUtils.blank(responseText));
				json.put("ajaxRequestId", compParameter.componentBean.hashId());
				final String fontStyle = pagelet.getFontStyle();
				if (StringUtils.hasText(fontStyle)) {
					json.put("fontStyle", fontStyle);
				}
			}
		});
	}

	public IForward optionSave(final ComponentParameter compParameter) {
		final ComponentParameter nComponentParameter = PortalUtils.getComponentParameter(compParameter);
		final PageletBean pagelet = PortalUtils.getPageletBean(nComponentParameter);
		final IPortalModuleHandle mh = PortalModuleRegistryFactory.getInstance().getModuleHandle(pagelet);
		if (mh != null) {
			mh.optionSave(nComponentParameter);
		}
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				final String title = mh.getOptionUITitle(nComponentParameter);
				if (StringUtils.hasText(title)) {
					json.put("title", title);
				}
			}
		});
	}

	public IForward draggableSave(final ComponentParameter compParameter) {
		final ComponentParameter nComponentParameter = PortalUtils.getComponentParameter(compParameter);
		PortalUtils.savePortal(nComponentParameter, null, !ConvertUtils.toBoolean(nComponentParameter.getRequestParameter("checked")));
		return null;
	}

	public IForward uiOptionSave(final ComponentParameter compParameter) {
		final ComponentParameter nComponentParameter = PortalUtils.getComponentParameter(compParameter);
		final PageletBean pagelet = PortalUtils.getPageletBean(nComponentParameter);
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				final String pageletId = pagelet.id();
				json.put("li", pageletId);
				final PageletTitle pageletTitle = pagelet.getTitle();

				pageletTitle.setValue(nComponentParameter.getRequestParameter("ui_options_title"));
				pageletTitle.setLink(nComponentParameter.getRequestParameter("ui_options_link"));
				pageletTitle.setIcon(nComponentParameter.getRequestParameter("ui_options_icon"));
				pageletTitle.setFontStyle(nComponentParameter.getRequestParameter("ui_options_fontstyle"));
				pageletTitle.setDescription(nComponentParameter.getRequestParameter("ui_options_desc"));
				json.put("ui_title", PortalUtils.getTitleString(nComponentParameter, pagelet));

				// pagelet
				final int h = ConvertUtils.toInt(nComponentParameter.getRequestParameter("ui_options_height"), 0);
				pagelet.setHeight(h);
				String style = "height:" + (h > 0 ? (h + "px") : "auto") + ";";
				try {
					final ETextAlign ta = ETextAlign.valueOf(nComponentParameter.getRequestParameter("ui_options_align"));
					pagelet.setAlign(ta);
					style += "text-align: " + pagelet.getAlign() + ";";
				} catch (final Exception e) {
				}
				json.put("ui_c_style", style);

				final String fontStyle = nComponentParameter.getRequestParameter("ui_options_c_fontstyle");
				pagelet.setFontStyle(fontStyle);
				if (StringUtils.hasText(fontStyle)) {
					json.put("ui_c_fontstyle", fontStyle);
				}
				pagelet.setSync(ConvertUtils.toBoolean(nComponentParameter.getRequestParameter("ui_options_sync"), false));
				PortalUtils.savePortal(nComponentParameter);
			}
		});
	}

	public IForward columnSizeSave(final ComponentParameter compParameter) {
		final ComponentParameter nComponentParameter = PortalUtils.getComponentParameter(compParameter);
		final int columnCount = ConvertUtils.toInt(nComponentParameter.getRequestParameter("_columns_select"), 1);
		final ArrayList<ColumnBean> columns = new ArrayList<ColumnBean>(PortalUtils.getColumns(nComponentParameter));
		final ArrayList<PageletBean> removes = new ArrayList<PageletBean>();
		int size;
		while ((size = columns.size()) != columnCount) {
			if (size > columnCount) {
				final ColumnBean column = columns.remove(size - 1);
				removes.addAll(column.getPagelets());
			} else {
				columns.add(new ColumnBean(null, (PortalBean) nComponentParameter.componentBean));
			}
		}
		if (removes.size() > 0) {
			columns.get(size - 1).getPagelets().addAll(removes);
		}
		for (int i = 0; i < columns.size(); i++) {
			final ColumnBean column = columns.get(i);
			column.setWidth(nComponentParameter.getRequestParameter("_cw" + (i + 1)));
		}
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				PortalUtils.savePortal(nComponentParameter, columns);
				json.put("layout", nComponentParameter.getBeanProperty("name"));
			}
		});
	}

	public IForward positionSave(final ComponentParameter compParameter) {
		final String[] ulArr = StringUtils.split(compParameter.getRequestParameter("ul"), "#");
		if (ulArr == null) {
			return null;
		}
		final ComponentParameter nComponentParameter = PortalUtils.getComponentParameter(compParameter);
		final ArrayList<ColumnBean> columns = new ArrayList<ColumnBean>();
		final Map<ColumnBean, Collection<PageletBean>> pagelets = new HashMap<ColumnBean, Collection<PageletBean>>();
		for (final String ul : ulArr) {
			final ColumnBean column = PortalUtils.getColumnBeanByHashId(nComponentParameter, ul);
			final String[] liArr = StringUtils.split(compParameter.getRequestParameter(ul), "#");
			if (liArr != null) {
				final Collection<PageletBean> coll = new ArrayList<PageletBean>();
				pagelets.put(column, coll);
				for (final String li : liArr) {
					coll.add(PortalUtils.getPageletByHashId(nComponentParameter, li));
				}
			}
			columns.add(column);
		}

		for (final Map.Entry<ColumnBean, Collection<PageletBean>> entry : pagelets.entrySet()) {
			final Collection<PageletBean> _pagelets = entry.getKey().getPagelets();
			_pagelets.clear();
			_pagelets.addAll(entry.getValue());
		}

		PortalUtils.savePortal(nComponentParameter, columns);
		return null;
	}

	public IForward addModule(final ComponentParameter compParameter) {
		final ComponentParameter nComponentParameter = PortalUtils.getComponentParameter(compParameter);
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				ColumnBean column = null;
				PageletBean pagelet = PortalUtils.getPageletBean(nComponentParameter);
				if (pagelet == null) {
					final Collection<ColumnBean> coll = PortalUtils.getColumns(nComponentParameter);
					if (coll != null && coll.size() > 0) {
						column = coll.iterator().next();
						final ArrayList<PageletBean> al = column.getPagelets();
						if (al.size() > 0) {
							pagelet = al.get(0);
						}
					} else {
						return;
					}
				}
				if (pagelet != null && column == null) {
					column = pagelet.getColumnBean();
				}
				final Collection<PageletBean> pagelets = column.getPagelets();
				final ArrayList<PageletBean> al = new ArrayList<PageletBean>(pagelets);
				final int j = pagelet != null ? al.indexOf(pagelet) : -1;
				final PageletBean created = new PageletBean(column);
				created.setModule(nComponentParameter.getRequestParameter("module"));
				al.add(j + 1, created);
				pagelets.clear();
				pagelets.addAll(al);
				json.put("created", PortalUtils.createPagelet(nComponentParameter, created));
				json.put("column", column.id());
				json.put("draggable", nComponentParameter.getBeanProperty("draggable"));
				PortalUtils.savePortal(nComponentParameter);
			}
		});
	}
}
