package net.simpleframework.content.component.topicpager;

import java.io.File;
import java.io.IOException;

import net.simpleframework.content.AbstractContentLuceneManager;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.component.ComponentParameter;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter;
import org.jsoup.Jsoup;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class TopicLuceneManager extends AbstractContentLuceneManager {
	public TopicLuceneManager(final ComponentParameter compParameter, final File indexPath) {
		super(compParameter, indexPath);
	}

	@Override
	protected String[] getQueryParserFields() {
		return new String[] { "subject", "content" };
	}

	@Override
	protected void objectToDocument(final Object object, final IndexWriter indexWriter,
			final Document doc) throws IOException {
		TopicBean topicBean = null;
		if (object instanceof PostsBean) {
			topicBean = (TopicBean) queryForObject(((PostsBean) object).getTopicId());
		} else if (object instanceof TopicBean) {
			topicBean = (TopicBean) object;
		}
		if (topicBean == null) {
			return;
		}
		super.objectToDocument(topicBean, indexWriter, doc);
		doc.add(new Field("subject", StringUtils.blank(topicBean.getTopic()), Store.NO,
				Index.ANALYZED));
		doc.add(new Field("content", Jsoup.parse(
				TopicPagerUtils.getAllTopicContent(getComponentParameter(), topicBean)).text(),
				Store.NO, Index.ANALYZED));
	}
}
