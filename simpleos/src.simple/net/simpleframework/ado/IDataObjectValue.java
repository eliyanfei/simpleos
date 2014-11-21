package net.simpleframework.ado;

import java.io.Serializable;

import net.simpleframework.core.IAttributeAware;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IDataObjectValue extends Serializable, IAttributeAware {

	Object[] getValues();

	String key();
}
