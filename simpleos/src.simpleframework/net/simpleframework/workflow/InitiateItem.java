package net.simpleframework.workflow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import net.simpleframework.core.AAttributeAware;
import net.simpleframework.core.id.ID;
import net.simpleframework.organization.IJob;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class InitiateItem extends AAttributeAware {
	/**
	 * 此处定义ID而不是具体对象，主要考虑到缓存很大，并且可能会持续化
	 */
	private final ID modelId;

	private final ArrayList<ID> initiators = new ArrayList<ID>();

	private IJob selected;

	public InitiateItem(final ID modelId) {
		this.modelId = modelId;
	}

	public ID getModelId() {
		return modelId;
	}

	public Collection<ID> getInitiators() {
		return initiators;
	}

	public ProcessModelBean model() {
		return WorkflowUtils.pmm().queryForObjectById(getModelId());
	}

	public Collection<IJob> jobs() {
		final Collection<IJob> jobs = new ArrayList<IJob>();
		for (final ID id : getInitiators()) {
			final IJob job = WorkflowUtils.org().job(id);
			if (job != null) {
				jobs.add(job);
			}
		}
		return jobs;
	}

	public IJob getSelected() {
		if (selected != null) {
			return selected;
		} else {
			Iterator<IJob> it;
			return (it = jobs().iterator()).hasNext() ? it.next() : null;
		}
	}

	public void setSelected(final IJob selected) {
		this.selected = selected;
	}
}
