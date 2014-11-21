package net.simpleframework.content.component.topicpager;

import net.simpleframework.content.IContentBeanAware;
import net.simpleframework.core.bean.AbstractIdDataObjectBean;
import net.simpleframework.util.HTMLUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class PostsTextBean extends AbstractIdDataObjectBean implements IContentBeanAware {

	private static final long serialVersionUID = 6326894297076972593L;

	private String subject, content;

	public String getSubject() {
		return subject;
	}

	public void setSubject(final String subject) {
		this.subject = subject;
	}

	@Override
	public String getContent() {
		return content;
	}

	public void setContent(final String content) {
		this.content = HTMLUtils.stripScripts(content);
	}
}
