package net.itsite.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.itsite.ItSiteOrganizationApplicationModule.AccountExt;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.web.page.SkinUtils;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.propeditor.AbstractPropEditorHandle;
import net.simpleframework.web.page.component.ui.propeditor.FieldComponent;
import net.simpleframework.web.page.component.ui.propeditor.PropEditorBean;
import net.simpleframework.web.page.component.ui.propeditor.PropField;

public class UserSpacePropEditorHandle extends AbstractPropEditorHandle {
	@Override
	public Map<String, Object> getFormParameters(ComponentParameter compParameter) {
		return super.getFormParameters(compParameter);
	}

	@Override
	public Collection<PropField> getFormFields(ComponentParameter compParameter) {
		final PropEditorBean formEditor = (PropEditorBean) compParameter.componentBean;
		final Collection<PropField> colls = new ArrayList<PropField>(formEditor.getFormFields());
		for (final PropField pf : colls) {
			if ("选择皮肤".equals(pf.getLabel())) {
				final StringBuffer buf = new StringBuffer();
				int i = 1;
				for (final String skin : SkinUtils.getSkinList().keySet()) {
					buf.append(skin).append("=").append(SkinUtils.getSkinList().get(skin));
					if (i++ != SkinUtils.getSkinList().size()) {
						buf.append(";");
					}
				}
				getFieldComponent(pf.getFieldComponents(), 0).setDefaultValue(buf.toString());
			} else if ("样式预览".equals(pf.getLabel())) {
				final HttpServletRequest request = compParameter.request;
				Object userId = request.getParameter(OrgUtils.um().getUserIdParameterName());
				if (userId == null) {
					userId = AccountSession.getLogin(compParameter.getSession()).getId().getValue();
				}
				final IUser user = OrgUtils.um().queryForObjectById(userId);
				String skin = SkinUtils.DEFAULT_SKIN;
				if (user != null) {
					final AccountExt account = (AccountExt) user.account();
					if (account != null) {
						skin = account.getSkin();
					}
				}
				getFieldComponent(pf.getFieldComponents(), 0).setDefaultValue(
						"<img height='450' width='500' src='/$resource/default/userPager/jsp/ue/images/" + skin + ".png' alt='" + skin + "'/>");
			}
		}
		return colls;
	}

	private FieldComponent getFieldComponent(Collection<FieldComponent> fieldComponents, final int p) {
		return ((List<FieldComponent>) fieldComponents).get(p);
	}
}
