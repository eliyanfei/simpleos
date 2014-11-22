package net.simpleos.openapi;

import net.simpleframework.applets.openid.OpenIDUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleos.SimpleosUtil;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import weibo4j.Oauth;
import weibo4j.Users;
import weibo4j.Weibo;
import weibo4j.http.AccessToken;
import weibo4j.model.User;
import weibo4j.model.WeiboException;
import weibo4j.org.json.JSONObject;
import weibo4j.util.WeiboConfig;

/**
 * 
 * @author 李岩飞 
 * @email eliyanfei@126.com
 * @date 2014年11月20日 上午11:52:11 
 * @Description: TODO(这里用一句话描述这个类的作用)
 *
 */
public class OpenapiUtils {
	public final static String fileName = "/jsp/openapi_return.jsp";
	static {
		WeiboConfig.updateProperties("client_ID", "897761991");
		WeiboConfig.updateProperties("client_SERCRET", "e42cc52e1926a5562577e5eddda7869a");
		WeiboConfig.updateProperties("baseURL", "https://api.weibo.com/2/");
		WeiboConfig.updateProperties("accessTokenURL", "https://api.weibo.com/2/oauth2/access_token");
		WeiboConfig.updateProperties("authorizeURL", "https://api.weibo.com/2/oauth2/authorize");
	}

	/**
	 * 新浪微博
	 * @param requestResponse
	 * @return
	 */
	public static String authorizeWeibo(final PageRequestResponse requestResponse) {
		WeiboConfig.updateProperties("redirect_URI", SimpleosUtil.url + OpenIDUtils.deployPath + fileName);
		try {
			return new Oauth().authorize("code");
		} catch (WeiboException e) {
		}
		return "";
	}

	/**
	 * QQ账号
	 * @param requestResponse
	 * @return
	 */
	public static String authorizeQQ(final PageRequestResponse requestResponse) {
		return "https://graph.qq.com/oauth2.0/authorize?response_type=token&client_id=100237132&redirect_uri="
				+ (SimpleosUtil.url + OpenIDUtils.deployPath + "/jsp/qq_return.jsp") + "&scope=get_info";
	}

	public static OpenapiBean getOpenId(final PageRequestResponse requestResponse) {
		final String code = requestResponse.getRequestParameter("code");
		if (StringUtils.hasText(code)) {
			return getSinaOpenId(requestResponse);
		} else {
			return getQQOpenId(requestResponse);
		}
	}

	/**
	 * 获得新浪用户信息
	 * @param requestResponse
	 * @return
	 */
	private static OpenapiBean getSinaOpenId(final PageRequestResponse requestResponse) {
		final Oauth oauth = new Oauth();
		try {
			final AccessToken access = oauth.getAccessTokenByCode(requestResponse.getRequestParameter("code"));
			access.getAccessToken();
			final Weibo weibo = new Weibo();
			weibo.setToken(access.getAccessToken());
			weibo4j.Account am = new weibo4j.Account();
			try {
				final JSONObject json = am.getUid();
				final Users um = new Users();
				final User user = um.showUserById(json.getString("uid"));
				if (user != null) {
					OpenapiBean apiBean = OpenapiAppModule.applicationModule.getOpenapiBean(EOpenapi.sina_t, user.getName());
					if (apiBean == null) {
						apiBean = new OpenapiBean();
						apiBean.setName(user.getId() + "_weibo");
						apiBean.setOpenId(user.getId() + "_weibo");
						apiBean.setOpenapi(EOpenapi.sina_t);
						OpenapiAppModule.applicationModule.doUpdate(apiBean);
					}
					apiBean.setText(user.getScreenName());
					return apiBean;
				}
			} catch (Exception e) {
			}
		} catch (WeiboException e) {
		}
		return null;
	}

	/**
	* 获得QQ用户信息
	* @param requestResponse
	* @return
	*/
	private static OpenapiBean getQQOpenId(final PageRequestResponse requestResponse) {
		final String access_token = StringUtils.text(requestResponse.getRequestParameter("access_token"),
				requestResponse.getRequestParameter("#access_token"));
		Connection conn = Jsoup.connect("https://graph.qq.com/oauth2.0/me?access_token=" + access_token);
		if (conn != null) {
			try {
				JSONObject jsonMap = new JSONObject(conn.get().body().text());
				String client_id = (String) jsonMap.get("client_id");
				String openid = (String) jsonMap.get("openid");
				conn = Jsoup.connect("https://graph.qq.com/user/get_user_info?access_token=" + access_token + "&oauth_consumer_key=" + client_id
						+ "&openid=" + openid);
				if (conn != null) {
					jsonMap = new JSONObject(conn.get().body().text());
					OpenapiBean apiBean = OpenapiAppModule.applicationModule.getOpenapiBean(EOpenapi.qq, openid);
					if (apiBean == null) {
						apiBean = new OpenapiBean();
						apiBean.setName(openid);
						apiBean.setOpenId(openid);
						apiBean.setOpenapi(EOpenapi.qq);
						OpenapiAppModule.applicationModule.doUpdate(apiBean);
					}
					apiBean.setText((String) jsonMap.get("nickname"));
					return apiBean;
				}
			} catch (Exception e) {
			}
		}
		return null;

	}
}
