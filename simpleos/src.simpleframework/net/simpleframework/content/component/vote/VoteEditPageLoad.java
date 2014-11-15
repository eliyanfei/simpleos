package net.simpleframework.content.component.vote;

import java.util.Date;
import java.util.List;
import java.util.Map;

import net.simpleframework.util.ConvertUtils;
import net.simpleframework.web.page.DefaultPageHandle;
import net.simpleframework.web.page.PageParameter;
import net.simpleframework.web.page.component.ComponentParameter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class VoteEditPageLoad extends DefaultPageHandle {

	public void ve1Loaded(final PageParameter pageParameter, final Map<String, Object> dataBinding,
			final List<String> visibleToggleSelector, final List<String> readonlySelector,
			final List<String> disabledSelector) {
		final ComponentParameter nComponentParameter = VoteUtils.getComponentParameter(pageParameter);
		final Vote vote = ((IVoteHandle) nComponentParameter.getComponentHandle())
				.getEntityBeanByRequest(nComponentParameter);
		dataBinding.put("ve1_text", vote.getText());
		dataBinding.put("ve1_status", vote.getStatus());
		final Date expiredDate = vote.getExpiredDate();
		if (expiredDate != null) {
			dataBinding.put("ve1_expiredDate",
					ConvertUtils.toDateString(expiredDate, Vote.expiredDateFormat));
		}
		dataBinding.put("ve1_description", vote.getDescription());
	}

	public void ve2Loaded(final PageParameter pageParameter, final Map<String, Object> dataBinding,
			final List<String> visibleToggleSelector, final List<String> readonlySelector,
			final List<String> disabledSelector) {
		final ComponentParameter nComponentParameter = VoteUtils.getComponentParameter(pageParameter);
		final VoteItemGroup itemGroup = ((IVoteHandle) nComponentParameter.getComponentHandle())
				.getEntityBeanById(nComponentParameter,
						pageParameter.getRequestParameter(VoteUtils.VOTE_GROUP_ID), VoteItemGroup.class);
		if (itemGroup != null) {
			dataBinding.put("ve2_text", itemGroup.getText());
			dataBinding.put("ve2_multiple", itemGroup.getMultiple());
		}
	}

	public void ve3Loaded(final PageParameter pageParameter, final Map<String, Object> dataBinding,
			final List<String> visibleToggleSelector, final List<String> readonlySelector,
			final List<String> disabledSelector) {
		final ComponentParameter nComponentParameter = VoteUtils.getComponentParameter(pageParameter);
		final VoteItem voteItem = ((IVoteHandle) nComponentParameter.getComponentHandle())
				.getEntityBeanById(nComponentParameter,
						pageParameter.getRequestParameter(VoteUtils.VOTE_ITEM_ID), VoteItem.class);
		if (voteItem != null) {
			dataBinding.put("ve3_text", voteItem.getText());
		}
	}
}
