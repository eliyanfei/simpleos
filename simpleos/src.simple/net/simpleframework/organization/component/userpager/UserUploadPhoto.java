package net.simpleframework.organization.component.userpager;

import java.io.IOException;

import net.simpleframework.organization.IUser;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.account.AccountContext;
import net.simpleframework.organization.impl.UserManager;
import net.simpleframework.util.AlgorithmUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.WebUtils;
import net.simpleframework.web.page.AbstractUrlForward;
import net.simpleframework.web.page.IMultipartFile;
import net.simpleframework.web.page.MultipartPageRequest;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.HandleException;
import net.simpleframework.web.page.component.base.submit.AbstractSubmitHandle;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class UserUploadPhoto extends AbstractSubmitHandle {

	@Override
	public AbstractUrlForward submit(final ComponentParameter compParameter) {
		final UserManager um = OrgUtils.um();
		final String userId = StringUtils.blank(compParameter.getRequestParameter(um
				.getUserIdParameterName()));
		final IUser user = um.queryForObjectById(userId);
		final MultipartPageRequest request = (MultipartPageRequest) compParameter.request;
		final IMultipartFile multipart = request.getFile("user_photo");
		if (multipart.getSize() > 1024 * 1024) {
			return AbstractUrlForward.componentUrl(UserPagerRegistry.userPager,
					"/jsp/ue/user_edit_photo_upload.jsp?" + um.getUserIdParameterName() + "=" + userId);
		} else {
			try {
				um.updateLob(user, multipart.getInputStream());
			} catch (final IOException e) {
				throw HandleException.wrapException(e);
			}
			OrgUtils.deletePhoto(compParameter, userId);
			AccountContext.update(compParameter.getSession(), "org_photo", user.getId());
			final String isrc = AlgorithmUtils.base64Encode(compParameter.wrapContextPath(
					WebUtils.addParameters(OrgUtils.getPhotoSRC(request, user, 164, 164), "c="
							+ StringUtils.hash(request))).getBytes());
			return AbstractUrlForward.componentUrl(
					UserPagerRegistry.userPager,
					"/jsp/ue/user_edit_photo_upload.jsp?isrc=" + isrc + "&"
							+ um.getUserIdParameterName() + "=" + userId);
		}
	}
}
