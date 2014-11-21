package net.simpleframework.workflow;

import net.simpleframework.core.bean.AbstractIdDataObjectBean;
import net.simpleframework.core.id.ID;
import net.simpleframework.workflow.schema.EVariableType;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class VariableBean extends AbstractIdDataObjectBean {
	private EVariableSource variableSource;

	private ID sourceId;

	private EVariableType variableType;

	private String variableName;

	private String stringValue;

	private byte[] blobValue;

	public EVariableSource getVariableSource() {
		return variableSource;
	}

	public void setVariableSource(final EVariableSource variableSource) {
		this.variableSource = variableSource;
	}

	public ID getSourceId() {
		return sourceId;
	}

	public void setSourceId(final ID sourceId) {
		this.sourceId = sourceId;
	}

	public EVariableType getVariableType() {
		return variableType;
	}

	public void setVariableType(final EVariableType variableType) {
		this.variableType = variableType;
	}

	public String getVariableName() {
		return variableName;
	}

	public void setVariableName(final String variableName) {
		this.variableName = variableName;
	}

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(final String stringValue) {
		this.stringValue = stringValue;
	}

	public byte[] getBlobValue() {
		return blobValue;
	}

	public void setBlobValue(final byte[] blobValue) {
		this.blobValue = blobValue;
	}

	private static final long serialVersionUID = -3897086526014759768L;
}
