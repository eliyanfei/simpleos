package net.simpleframework.content;

import net.simpleframework.ado.lucene.AbstractLuceneManager;
import net.simpleframework.organization.account.IGetAccountAware;
import net.simpleframework.web.EFunctionModule;
import net.simpleframework.web.FilePathWrapper;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.pager.db.IDbTablePagerHandle;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IContentPagerHandle extends IDbTablePagerHandle {

	static final String _VTYPE = "vtype";

	EFunctionModule getFunctionModule();

	IGetAccountAware getAccountAware();

	/************** Lucene **************/
	FilePathWrapper getFileCache(ComponentParameter compParameter);

	AbstractLuceneManager createLuceneManager(ComponentParameter compParameter);

	/************** Attention **************/
	void contentAttention(ComponentParameter compParameter, IAttentionsBeanAware attentionsBean,
			boolean deleteAttention);

	void doAttentionSent(final ComponentParameter compParameter, Object... beans);

	/************** Utils **************/
	String getNavigateHTML(ComponentParameter compParameter);

	String getActionsHTML(ComponentParameter compParameter, AbstractContentBase contentBase);

	String getHtmlEditorToolbar(ComponentParameter compParameter);

	boolean isNew(ComponentParameter compParameter, final AbstractContent content);
}