package net.simpleframework.organization.web;

import java.util.ArrayList;
import java.util.Collection;

import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.content.component.catalog.Catalog;
import net.simpleframework.content.component.catalog.DefaultCatalogHandle;
import net.simpleframework.core.IApplicationModule;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.core.bean.IIdBeanAware;
import net.simpleframework.organization.EJobType;
import net.simpleframework.organization.IJob;
import net.simpleframework.organization.IJobChart;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.web.WebUtils;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.propeditor.EComponentType;
import net.simpleframework.web.page.component.ui.propeditor.FieldComponent;
import net.simpleframework.web.page.component.ui.propeditor.PropEditorBean;
import net.simpleframework.web.page.component.ui.propeditor.PropField;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeBean;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeNode;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class JobTreeHandle extends DefaultCatalogHandle {

	@Override
	public Class<? extends IIdBeanAware> getEntityBeanClass() {
		return OrgUtils.jm().getBeanClass();
	}

	@Override
	public IApplicationModule getApplicationModule() {
		return OrgUtils.applicationModule;
	}

	@Override
	protected ExpressionValue getBeansSQL(final ComponentParameter compParameter,
			final Object parentId) {
		final ArrayList<Object> al = new ArrayList<Object>();
		final StringBuilder sql = new StringBuilder();
		final Object documentId = getDocumentId(compParameter);
		if (documentId == null) {
			sql.append("1=2");
		} else {
			sql.append("jobchartid=?");
			al.add(documentId);
			sql.append(" and ");
			if (parentId == null) {
				sql.append(Table.nullExpr(getTableEntityManager(compParameter).getTable(), "parentid"));
			} else {
				sql.append("parentid=?");
				al.add(parentId);
			}
		}
		return new ExpressionValue(sql.toString(), al.toArray());
	}

	@Override
	public IDataObjectQuery<? extends Catalog> catalogs(final ComponentParameter compParameter,
			final Object parentId) {
		return beans(compParameter, parentId, "order by oorder");
	}

	@Override
	public Object getDocumentId(final ComponentParameter compParameter) {
		final String jcId = compParameter
				.getRequestParameter(getDocumentIdParameterName(compParameter));
		return ConvertUtils.toLong(jcId);
	}

	@Override
	public String getDocumentIdParameterName(final ComponentParameter compParameter) {
		return OrgUtils.jcm().getJobChartIdParameterName();
	}

	@Override
	protected boolean hideOwnerMgrMenu() {
		return true;
	}

	@Override
	public Collection<? extends AbstractTreeNode> getCatalogTreenodes(
			final ComponentParameter compParameter, final AbstractTreeBean treeBean,
			final AbstractTreeNode treeNode, final boolean dictionary) {
		final Collection<? extends AbstractTreeNode> coll = super.getCatalogTreenodes(compParameter,
				treeBean, treeNode, dictionary);
		for (final AbstractTreeNode treeNode2 : coll) {
			if (treeNode == null && !dictionary) {
				treeNode2.setImage(OrgUtils.getCssPath(compParameter) + "/images/chart.png");
				final IJobChart jobChart = OrgUtils.jcm().queryForObjectById(
						getDocumentId(compParameter));
				if (jobChart != null) {
					treeNode2.setPostfixText(WebUtils.enclose(jobChart.getText()));
				}
			}
			treeNode2.setOpened(true);
			final IJob job = (IJob) treeNode2.getDataObject();
			if (job != null) {
				if (!dictionary) {
					treeNode2.setJsClickCallback("$Actions['ajaxJobDetail']('"
							+ OrgUtils.jm().getJobIdParameterName() + "=" + job.getId() + "')");
				}
				treeNode2.setImage(OrgUtils.getCssPath(compParameter) + "/images/job.png");
			}
		}
		return coll;
	}

	@Override
	public Collection<PropField> getPropFields(final ComponentParameter compParameter,
			final PropEditorBean formEditor) {
		final ArrayList<PropField> al = new ArrayList<PropField>(formEditor.getFormFields());
		final PropField propField = new PropField();
		propField.setLabel(LocaleI18n.getMessage("JobTreeHandle.0"));

		final FieldComponent fieldComponent = new FieldComponent();
		propField.getFieldComponents().add(fieldComponent);

		fieldComponent.setName("catalog_jobType");
		fieldComponent.setType(EComponentType.select);

		final StringBuilder sb = new StringBuilder();
		sb.append(EJobType.normal).append("=");
		sb.append(LocaleI18n.getMessage("JobTreeHandle.1")).append(";");
		sb.append(EJobType.handle).append("=");
		sb.append(LocaleI18n.getMessage("JobTreeHandle.2")).append(";");
		sb.append(EJobType.script).append("=");
		sb.append(LocaleI18n.getMessage("JobTreeHandle.3"));
		fieldComponent.setDefaultValue(sb.toString());

		al.add(3, propField);
		return al;
	}

	@Override
	public String getJavascriptCallback(final ComponentParameter compParameter,
			final String jsAction, final Object bean) {
		String jsCallback = super.getJavascriptCallback(compParameter, jsAction, bean);
		if ("add".equals(jsAction) || "delete".equals(jsAction)) {
			jsCallback += "$Actions['jcTree'].refresh();";
			if ("delete".equals(jsAction)) {
				jsCallback += "$('__job_detail').update('')";
			}
		}
		return jsCallback;
	}
}
