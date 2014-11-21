package net.itsite.docu;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.lucene.LuceneQuery;
import net.simpleframework.content.AbstractContentLuceneManager;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.core.bean.IDataObjectBean;
import net.simpleframework.util.BeanUtils;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ado.IDbComponentHandle;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.FieldCacheTermsFilter;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;

public class DocuLuceneManager extends AbstractContentLuceneManager {
	public DocuLuceneManager(final ComponentParameter compParameter, final File indexPath) {
		super(compParameter, indexPath);
	}

	@Override
	protected String[] getQueryParserFields() {
		return new String[] { "title", "createDate", "point", "downCounter", "totalGrade", "fileSize", "views" };
	}

	@Override
	protected void objectToDocument(final Object object, final IndexWriter indexWriter, final Document doc) throws IOException {
		super.objectToDocument(object, indexWriter, doc);
		final DocuBean docuBean = (DocuBean) object;
		doc.add(new Field("status", docuBean.getStatus().name(), Field.Store.YES, Field.Index.NOT_ANALYZED));
		for (final String field : getQueryParserFields()) {
			try {
				final Object value = BeanUtils.getProperty(object, field);
				if (value != null) {
					if ("createDate".equals(field)) {
						doc.add(new Field(field, String.valueOf(((Date) value).getTime()), Store.NO, Index.ANALYZED));
					} else {
						doc.add(new Field(field, String.valueOf(value), Store.NO, Index.ANALYZED));
					}
				}
			} catch (final Exception e) {
				logger.warn(e);
			}
		}
	}

	@Override
	public Sort getSort() {
		final String sort_ = StringUtils.text(getComponentParameter().getRequestParameter("sort"), "default");
		if ("default".equals(sort_)) {
			return null;
		}
		final boolean revert = ConvertUtils.toBoolean(getComponentParameter().getRequestParameter("revert"),
				!ConvertUtils.toBoolean(getComponentParameter().getSession().getAttribute(sort_), false));
		int type = SortField.LONG;
		if ("totalGrade".equals(sort_)) {
			type = SortField.FLOAT;
		} else if ("point".equals(sort_)) {
			type = SortField.INT;
		}
		getComponentParameter().getSession().setAttribute(sort_, revert);
		final SortField sortF = new SortField(sort_, type, ConvertUtils.toBoolean(getComponentParameter().getSession().getAttribute(sort_), false));
		final Sort sort = new Sort(sortF);
		return sort;
	}

	@Override
	protected LuceneQuery<?> createLuceneQuery(Query query) {
		if (query == null) {
			return null;
		}
		try {
			if (IDbComponentHandle.Utils.isManager(getComponentParameter())) {
				return super.createLuceneQuery(query);
			}
		} catch (final Exception e) {
		}
		query = addFilter(query, new FieldCacheTermsFilter("status", EDocuStatus.publish.name()));
		final String docu_ = StringUtils.text(getComponentParameter().getRequestParameter("docu"), "all");
		if (!"all".equals(docu_)) {
			query = addFilter(query, new FieldCacheTermsFilter("docuFunction", docu_));
		}
		// my
		return super.createLuceneQuery(query);
	}

	@Override
	protected IDataObjectBean queryForObject(Object id) {
		return DocuUtils.applicationModule.getBean(DocuBean.class, id);
	}

	@Override
	protected IDataObjectQuery<?> getAllData() {
		final ITableEntityManager tMgr = DocuUtils.applicationModule.getDataObjectManager();
		return tMgr.query(new ExpressionValue("status=?", new Object[] { EDocuStatus.publish }), DocuBean.class);
	}
}
