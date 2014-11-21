package net.simpleframework.workflow;

import net.simpleframework.util.StringUtils;
import net.simpleframework.workflow.schema.ProcessDocument;
import net.simpleframework.workflow.schema.ProcessNode;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class ProcessModelBean extends AbstractWorkflowBean {

	private String title;

	private EProcessModelStatus status;

	public String getTitle() {
		if (StringUtils.hasText(title)) {
			return title;
		}
		final ProcessNode node = document().getProcessNode();
		return StringUtils.text(node.getText(), node.getId());
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public EProcessModelStatus getStatus() {
		return status != null ? status : EProcessModelStatus.edit;
	}

	public void setStatus(final EProcessModelStatus status) {
		this.status = status;
	}

	/*----------------------------------关联操作 --------------------------------*/

	private ProcessDocument document;

	public ProcessDocument document() {
		if (document == null) {
			final ProcessModelLobBean lob = WorkflowUtils.getTableEntityManager(
					ProcessModelLobBean.class).queryForObjectById(getId(), ProcessModelLobBean.class);
			if (lob != null) {
				document = new ProcessDocument(lob.getProcessSchema());
			}
		}
		return document;
	}

	private static final long serialVersionUID = 6413648325601228584L;
}