package net.simpleframework.ado.lucene;

import java.io.IOException;

import net.simpleframework.core.ado.AbstractDataObjectQuery;
import net.simpleframework.core.ado.DataObjectException;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class LuceneQuery<T> extends AbstractDataObjectQuery<T> {
	protected AbstractLuceneManager lManager;

	protected Query query;

	protected Searcher searcher;

	public LuceneQuery(final AbstractLuceneManager lManager, final Query query) throws IOException {
		this.lManager = lManager;
		if (query != null) {
			this.searcher = new IndexSearcher(lManager.getLuceneDirectory());
			this.query = query;
		}
	}

	private int fetchSize = 100;

	@Override
	public int getFetchSize() {
		return fetchSize;
	}

	@Override
	public void setFetchSize(final int fetchSize) {
		this.fetchSize = fetchSize;
	}

	@Override
	public void close() {
		try {
			searcher.close();
		} catch (final IOException e) {
			throw DataObjectException.wrapException(e);
		}
	}

	@Override
	public void move(final int toIndex) {
		super.move(toIndex);
		topDocs = null;
	}

	private TopDocs topDocs;

	private int j = 0;

	@Override
	public T next() {
		if (query == null) {
			return null;
		}
		i++;
		if (i < 0 || i >= getCount()) {
			return null;
		}
		final int fetchSize = getFetchSize();
		try {
			if (topDocs == null || j >= fetchSize) {
				final int topNum = i + fetchSize;
				Sort sort = lManager.getSort();
				if (sort == null) {
					final TopScoreDocCollector collector = TopScoreDocCollector.create(topNum, false);
					searcher.search(query, collector);
					topDocs = collector.topDocs(i, topNum);
				} else {
					topDocs = searcher.search(query, null, 10, sort);
				}
				j = 0;
			}
			final ScoreDoc[] docs = topDocs.scoreDocs;
			if (j < docs.length) {
				final ScoreDoc scoreDoc = topDocs.scoreDocs[j++];
				T t = toBean(searcher.doc(scoreDoc.doc), scoreDoc.score);
				if (t == null) {
					t = next();
				}
				return t;
			} else {
				return null;
			}
		} catch (final Exception e) {
			throw DataObjectException.wrapException(e);
		}
	}

	protected abstract T toBean(final Document doc, final float score);

	@Override
	public int getCount() {
		if (query == null) {
			return 0;
		}
		if (count < 0) {
			final TopScoreDocCollector collector = TopScoreDocCollector.create(0, false);
			try {
				searcher.search(query, collector);
				count = collector.getTotalHits();
			} catch (final IOException e) {
				throw DataObjectException.wrapException(e);
			}
		}
		return count;
	}
}
