package net.itsite.login;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.itsite.utils.MD5;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.util.ImageUtils;
import net.simpleframework.util.JSONUtils;
import net.simpleframework.util.StringUtils;

public class LoginServlet extends HttpServlet {

	final Map<String, String> idCode = new HashMap<String, String>();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html");
			final String uid = request.getParameter("uid");
			final String username = request.getParameter("user");
			final String pass = request.getParameter("pass");
			if (StringUtils.hasText(username) && StringUtils.hasText(pass)) {//登入验证 
				final Map<String, String> json = new HashMap<String, String>();
				final String validate = request.getParameter("validate");
				if (StringUtils.hasText(validate) && validate.equalsIgnoreCase(idCode.get(uid))) {
					final IUser user = OrgUtils.um().getUserByName(username);
					if (user != null) {
						if (user.account().getPassword().equals(MD5.getHashString(pass))) {
							json.put("name", username);
							json.put("text", user.getText());
							idCode.remove(uid);
						} else {
							json.put("pass", "");
						}
					} else {
						json.put("user", "");
					}
				} else {
					json.put("validate", "");
				}
				final PrintWriter pw = response.getWriter();
				pw.append(JSONUtils.toJSON(json));
				pw.flush();
			} else {//获得校验码
				final String puid = request.getParameter("puid");
				idCode.remove(puid);
				final String code = ImageUtils.genCode(response.getOutputStream());
				idCode.put(uid, code);
			}
		} catch (Exception ex) {
			response.setStatus(404);
			ex.printStackTrace();
		} finally {
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
