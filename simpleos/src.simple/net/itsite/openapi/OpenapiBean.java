package net.itsite.openapi;

import java.util.HashMap;
import java.util.Map;

import net.simpleframework.ado.db.IEntityBeanAware;
import net.simpleframework.core.ado.db.Column;
import net.simpleframework.core.bean.AbstractIdDataObjectBean;

/**
 * 
 * @author 李岩飞 
 * @email eliyanfei@126.com
 * @date 2014年11月20日 上午11:52:35 
 * @Description: 用户扩展信息
 *
 */
public class OpenapiBean extends AbstractIdDataObjectBean implements IEntityBeanAware {
	String email;
	String pass;
	String text;
	String name;
	String openId;
	EOpenapi openapi;
	private final static Map<String, Column> columns;
	static {
		columns = new HashMap<String, Column>();
		columns.put("id", new Column("id"));
		columns.put("name", new Column("name"));
		columns.put("openId", new Column("openId"));
		columns.put("openapi", new Column("openapi"));
	}

	@Override
	public Map<String, Column> getTableColumnDefinition() {
		return columns;
	}

	public String getEmail() {
		return email == null ? "" : email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public void setOpenapi(EOpenapi openapi) {
		this.openapi = openapi;
	}

	public EOpenapi getOpenapi() {
		return openapi;
	}

	public String getOpenId() {
		return openId;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
