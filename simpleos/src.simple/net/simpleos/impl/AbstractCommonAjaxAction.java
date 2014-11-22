package net.simpleos.impl;

import java.util.Map;

import net.simpleframework.applets.attention.AttentionUtils;
import net.simpleframework.content.AbstractMgrToolsAction;
import net.simpleframework.content.IAttentionsBeanAware;
import net.simpleframework.core.id.ID;
import net.simpleframework.core.id.LongID;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleos.i.ISimpleosApplicationModule;

public abstract class AbstractCommonAjaxAction extends AbstractMgrToolsAction {

	/**
	 * 关注的ID
	 * @return
	 */
	protected String getAttentionParameter() {
		return null;
	}

	/**
	 * 关注
	 * 
	 * @param compParameter
	 * @return
	 */
	public IForward attention(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {

			@Override
			public void doAction(final Map<String, Object> json) throws Exception {
				final ID id = new LongID(compParameter.getRequestParameter(getAttentionParameter()));
				final ISimpleosApplicationModule applicationModule = (ISimpleosApplicationModule) getApplicationModule();
				final IAttentionsBeanAware aBean = (IAttentionsBeanAware) applicationModule.getBean(applicationModule.getEntityBeanClass(), id);
				if (AttentionUtils.get(compParameter, applicationModule.getEFunctionModule(), id) == null) {
					AttentionUtils.insert(compParameter, applicationModule.getEFunctionModule(), id);
					aBean.setAttentions(aBean.getAttentions() + 1);
					json.put("act", "add");
				} else {
					json.put("act", "del");
					aBean.setAttentions(aBean.getAttentions() - 1);
					AttentionUtils.delete(compParameter, applicationModule.getEFunctionModule(), id);
				}
				applicationModule.doUpdate(new Object[] { "attentions" }, aBean);
			}
		});
	}

	@Override
	protected void doStatRebuild() {
	}

	@Override
	public IForward indexRebuild(ComponentParameter compParameter) {
		return super.indexRebuild(compParameter);
	}
}
