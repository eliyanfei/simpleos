package net.simpleos.openapi;

import net.itsite.ItSiteUtil;
import net.simpleframework.applets.openid.OpenIDUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.PageRequestResponse;

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

import com.tencent.weibo.beans.Account;
import com.tencent.weibo.beans.OAuth;
import com.tencent.weibo.utils.OAuthClient;
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
		WeiboConfig.updateProperties("redirect_URI", ItSiteUtil.url + OpenIDUtils.deployPath + fileName);
		try {
			return new Oauth().authorize("code");
		} catch (WeiboException e) {
		}
		return "";
	}

	/**
	 * 腾讯微博
	 * @return
	 */
	public static String authorizeQQWeibo(final PageRequestResponse requestResponse) {
		OAuth oauth = new OAuth();
		oauth.setOauth_consumer_key("801081880");
		oauth.setOauth_consumer_secret("e687b4e2bdb745b4e90d0b25d9db44a6");
		oauth.setOauth_callback(ItSiteUtil.url + OpenIDUtils.deployPath + fileName);
		OAuthClient auth = new OAuthClient();
		// 获取request token
		try {
			oauth = auth.requestToken(oauth);
		} catch (Exception e) {
		}
		requestResponse.setSessionAttribute("oauth_token_secret", oauth.getOauth_token_secret());
		String oauth_token = oauth.getOauth_token();
		return "http://open.t.qq.com/cgi-bin/authorize?oauth_token=" + oauth_token;
	}

	/**
	 * QQ账号
	 * @param requestResponse
	 * @return
	 */
	public static String authorizeQQ(final PageRequestResponse requestResponse) {
		return "https://graph.qq.com/oauth2.0/authorize?response_type=token&client_id=100237132&redirect_uri="
				+ (ItSiteUtil.url + OpenIDUtils.deployPath + "/jsp/qq_return.jsp") + "&scope=get_info";
	}

	public static OpenapiBean getOpenId(final PageRequestResponse requestResponse) {
		final String code = requestResponse.getRequestParameter("code");
		if (StringUtils.hasText(code)) {
			return getSinaOpenId(requestResponse);
		} else if (requestResponse.getSession().getAttribute("oauth_token_secret") != null) {
			return getQQWeiboOpenId(requestResponse);
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
	* 获得腾讯微博用户信息
	* @param requestResponse
	* @return
	*/
	private static OpenapiBean getQQWeiboOpenId(final PageRequestResponse requestResponse) {
		OAuth oauth = new OAuth();
		oauth.setOauth_consumer_key("801081880");
		oauth.setOauth_consumer_secret("e687b4e2bdb745b4e90d0b25d9db44a6");
		oauth.setOauth_callback(OpenIDUtils.deployPath + fileName);
		//获取request token
		OAuthClient auth = new OAuthClient();
		oauth.setOauth_verifier(requestResponse.getRequestParameter("oauth_verifier"));
		oauth.setOauth_token(requestResponse.getRequestParameter("oauth_token"));
		oauth.setOauth_token_secret((String) requestResponse.getSession().getAttribute("oauth_token_secret"));
		try {
			oauth = auth.accessToken(oauth);
			if (oauth.getStatus() != 2) {
				final Account account = auth.getAccount(oauth);
				if (account != null) {
					OpenapiBean apiBean = OpenapiAppModule.applicationModule.getOpenapiBean(EOpenapi.qq_t, account.getName());
					if (apiBean == null) {
						apiBean = new OpenapiBean();
						apiBean.setName(account.getName() + "_tqq");
						apiBean.setOpenId(account.getName() + "_tqq");
						apiBean.setOpenapi(EOpenapi.qq_t);
						OpenapiAppModule.applicationModule.doUpdate(apiBean);
					}
					apiBean.setText(account.getNick());
					return apiBean;
				}
			}
		} catch (Exception e1) {
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
