package net.itsite.utils;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.util.BeanUtils;
import net.simpleframework.util.StringUtils;

import org.springframework.web.bind.ServletRequestUtils;

/**
 * 
 * @author 李岩飞 
 * @email eliyanfei@126.com
 * @date 2014年11月20日 下午12:00:44 
 * @Description: TODO(这里用一句话描述这个类的作用)
 *
 */
public abstract class RequestUtils extends ServletRequestUtils {

	public static final String DEFAULT_DEP = ";";

	public static Map<String, Object> requet2Map(final HttpServletRequest request) {
		return requet2Map(request, DEFAULT_DEP);
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> requet2Map(final HttpServletRequest request, String sep) {
		final Map<String, Object> params = new HashMap<String, Object>();
		final Enumeration<String> ele = request.getParameterNames();
		String[] values;
		sep = StringUtils.hasText(sep) ? sep : DEFAULT_DEP;
		while (ele.hasMoreElements()) {
			try {
				final String name = ele.nextElement();
				values = request.getParameterValues(name);
				if (values == null || values.length <= 0) {
					continue;
				}
				if (values.length == 1) {
					params.put(name, values[0]);
				} else {
					final StringBuffer sBuffer = new StringBuffer(100);
					for (final String value : values) {
						if (StringUtils.hasText(value)) {
							sBuffer.append(value).append(sep);
						}
					}
					params.put(name, sBuffer.substring(0, sBuffer.length() - 1));
				}
			} catch (final Exception e) {
				// e.printStackTrace();
			}
		}
		return params;
	}

	public static Object requet2Bean(final HttpServletRequest request, final Object obj) {
		return requet2Bean(request, obj, DEFAULT_DEP);
	}

	@SuppressWarnings("unchecked")
	public static Object requet2Bean(final HttpServletRequest request, final Object bean, String sep) {
		if (bean == null || request == null) {
			return bean;
		}
		String retValue;
		sep = StringUtils.hasText(sep) ? sep : DEFAULT_DEP;
		final Enumeration<String> ele = request.getParameterNames();

		String[] values;
		while (ele.hasMoreElements()) {
			try {
				final String name = ele.nextElement();
				values = request.getParameterValues(name);
				if (values == null || values.length <= 0) {
					continue;
				}
				if (values.length == 1) {
					retValue = values[0];
				} else {
					final StringBuffer sBuffer = new StringBuffer(100);
					for (final String value : values) {
						if (StringUtils.hasText(value)) {
							sBuffer.append(value).append(sep);
						}
					}
					retValue = sBuffer.substring(0, sBuffer.length() - 1);
				}
				BeanUtils.setProperty(bean, name, retValue);
			} catch (final Exception e) {
			}
		}
		return bean;
	}

	public static String getRequestParameter(final HttpServletRequest request, final String parameter) {
		final String value = request.getParameter(parameter);
		return org.apache.commons.lang3.StringUtils.isBlank(value) ? "" : value;
	}

	public static ExpressionValue request2SQL(final HttpServletRequest request) {
		return request2SQL(request, null);
	}

	public static ExpressionValue request2SQL(final HttpServletRequest request, final String tableAlias) {
		String conditionSql = "";
		String conditionValue = "";
		String requestParameter = "";
		for (final Object requestParameterObject : request.getParameterMap().keySet()) {
			requestParameter = String.valueOf(requestParameterObject);
			String requestParameterValue = request.getParameter(requestParameter);
			if (StringUtils.hasText(requestParameterValue)) {
				final int lens = requestParameter.length();
				if (lens <= 4) {
					continue;
				}
				String inputName = requestParameter.substring(0, lens - 4);
				if (StringUtils.hasText(tableAlias)) {
					inputName = tableAlias + "." + inputName;
				}
				// 模糊查询的后缀的参数名字为_name、_title、_content
				if (requestParameter.endsWith("__li")) {
					conditionSql += " and " + inputName + " like ?";
					conditionValue += "%" + requestParameterValue + "%;";
				} else if (requestParameter.endsWith("_ngt")) {
					conditionSql += " and " + inputName + " >= ?";
					conditionValue += inputName + ";";
				} else if (requestParameter.endsWith("_nlt")) {
					conditionSql += " and " + inputName + " <= ?";
					conditionValue += requestParameterValue + ";";
				} else if (requestParameter.endsWith("_dgt")) {
					conditionSql += " and " + inputName + " >= ?";
					conditionValue += getRealStartDateValue(requestParameterValue) + ";";
				} else if (requestParameter.endsWith("_dlt")) {
					conditionSql += " and " + inputName + " <= ?";
					conditionValue += getRealEndDateValue(requestParameterValue) + ";";
				} else if (requestParameter.endsWith("__eq")) {
					conditionSql += " and " + inputName + " = ?";
					conditionValue += requestParameterValue + ";";
				}
				if (conditionSql.startsWith(" and")) {
					conditionSql = "1=1 " + conditionSql;
				}
			}
		}
		return new ExpressionValue(conditionSql, StringUtils.hasText(conditionValue) ? conditionValue.split(";") : null);
	}

	/**
	 * 结束时间
	 * @param requestParameterValue
	 * @return
	 */
	public static String getRealEndDateValue(String requestParameterValue) {
		if (!StringUtils.hasText(requestParameterValue)) {
			return "";
		}
		if (requestParameterValue.length() == 10) {
			requestParameterValue = requestParameterValue + " 23:59:59";
		}
		return requestParameterValue;
	}

	/**
	 * 开始时间
	 * @param requestParameterValue
	 * @return
	 */
	public static String getRealStartDateValue(String requestParameterValue) {
		if (!StringUtils.hasText(requestParameterValue)) {
			return "";
		}
		if (requestParameterValue.length() == 10) {
			requestParameterValue = requestParameterValue + " 00:00:00";
		}
		return requestParameterValue;
	}

}
