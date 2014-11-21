package net.simpleframework.web.page.component.base.validation;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public enum EValidatorMethod {
	required,

	number,

	digits,

	alpha,

	alphanum,

	date,

	email,

	url,

	min_value,

	max_value,

	min_length,

	max_length,

	int_range,

	float_range,

	length_range,

	file,

	chinese,

	ip,

	phone,

	mobile_phone,

	equals,

	less_than,

	great_than
}
