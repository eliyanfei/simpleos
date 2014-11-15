package net.simpleframework.content.component.vote;

import net.simpleframework.content.IContentHandle;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.web.page.component.ComponentParameter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IVoteHandle extends IContentHandle {

	Vote getVoteByDocumentId(ComponentParameter compParameter);

	Vote getVoteByDocumentId(ComponentParameter compParameter, Object documentId);

	IDataObjectQuery<VoteItemGroup> getItemGroups(ComponentParameter compParameter, Vote vote);

	IDataObjectQuery<VoteItem> getVoteItems(ComponentParameter compParameter, VoteItemGroup itemGroup);

	VoteResult[] submitVote(ComponentParameter compParameter, Object[] itemIds);

	IDataObjectQuery<VoteResult> getResults(ComponentParameter compParameter, Object itemId);

	String getManagerToolbar(ComponentParameter compParameter);
}
