package net.simpleframework.content.component.newspager;

import java.io.File;
import java.io.IOException;

import net.simpleframework.ado.lucene.LuceneQuery;
import net.simpleframework.content.AbstractContentLuceneManager;
import net.simpleframework.content.EContentStatus;
import net.simpleframework.util.BeanUtils;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ado.IDbComponentHandle;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.FieldCacheTermsFilter;
import org.apache.lucene.search.FilteredQuery;
import org.apache.lucene.search.Query;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class NewsLuceneManager extends AbstractContentLuceneManager {
	public NewsLuceneManager(final ComponentParameter compParameter, final File indexPath) {
		super(compParameter, indexPath);
	}

	@Override
	protected String[] getQueryParserFields() {
		return new String[] { "topic", "topic2", "keywords", "description" };
	}

	@Override
	protected void objectToDocument(final Object object, final IndexWriter indexWriter, final Document doc) throws IOException {
		super.objectToDocument(object, indexWriter, doc);
		final NewsBean news = (NewsBean) object;
		doc.add(new Field("status", news.getStatus().name(), Field.Store.YES, Field.Index.NOT_ANALYZED));
		for (final String field : getQueryParserFields()) {
			try {
				final Object value = BeanUtils.getProperty(object, field);
				if (value != null) {
					doc.add(new Field(field, String.valueOf(value), Store.NO, Index.ANALYZED));
				}
			} catch (final Exception e) {
				logger.warn(e);
			}
		}
	}

	@Override
	protected LuceneQuery<?> createLuceneQuery(final Query query) {
		try {
			if (IDbComponentHandle.Utils.isManager(getComponentParameter())) {
				return super.createLuceneQuery(query);
			}
		} catch (final Exception e) {
		}
		// my
		return super.createLuceneQuery(new FilteredQuery(query, new FieldCacheTermsFilter("status", EContentStatus.publish.name())));
	}
}
