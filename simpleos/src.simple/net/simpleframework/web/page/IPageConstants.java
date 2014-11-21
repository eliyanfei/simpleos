package net.simpleframework.web.page;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IPageConstants {
	/* page system */
	final static String THROWABLE = "__throwable";

	final static String XMLPATH_PARAMETER = "__xmlpath";

	final static String POSTFIX_XML = ".xml";

	final static String PROTOCAL_FILE_PREFIX = "file:///";

	final static String DEFAULT_PAGE_RESOURCE_PROVIDER = "default";

	static final String NONE = "none";

	/* page tag const */

	final static String TAG_HANDLE_CLASS = "handleClass";

	final static String TAG_COMPONENTS = "components";

	final static String TAG_SCRIPT_INIT = "scriptInit";

	final static String TAG_SCRIPT_EVAL = "scriptEval";

	final static String TAG_IMPORT_PAGE = "importPage";

	final static String TAG_IMPORT_JAVASCRIPT = "importJavascript";

	final static String TAG_IMPORT_CSS = "importCSS";

	final static String TAG_VALUE = "value";

	/* parser */

	final static String SCRIPT_TYPE = "text/javascript";

	/* session */
	final static String SESSION_PAGE_DOCUMENT = "__page_document";

	/* other */
	final static String DATA_HOME = "/$data";
}
