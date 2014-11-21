package net.simpleframework.web.page;

import java.util.HashMap;
import java.util.Map;

import net.simpleframework.util.JSONUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class JsonForward extends TextForward {
	private Map<String, Object> map;

	public JsonForward(final Map<String, Object> map) {
		super(null);
		this.map = map;
	}

	public JsonForward() {
		this(null);
	}

	public Map<String, Object> getMap() {
		if (map == null) {
			map = new HashMap<String, Object>();
		}
		return map;
	}

	@Override
	public String getResponseText(final PageRequestResponse requestResponse) {
		return JSONUtils.toJSON(map);
	}
}
