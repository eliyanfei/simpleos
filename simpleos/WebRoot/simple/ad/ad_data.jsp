<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page import="net.itsite.ad.AdUtils"%><%@page
	import="net.itsite.ad.EAd"%><%@page import="net.itsite.ad.AdBean"%>
<style type="text/css">
#adForm .l {
	width: 100px;
	text-align: right;
}

#adForm {
	background: #f7f7ff;
	padding: 6px 8px;
}
</style>
<%
	final EAd ad = EAd.valueOf(request.getParameter("adId"));
%>
<div id="adForm">
	<input type="hidden" id="adId" name="adId" value="<%=ad.name()%>">
	<table width="100%">
		<tbody>
			<tr>
				<td class="l">
				</td>
				<td>
					<input type="button" class="button2"
						onclick="$IT.A('adStartAct','adId=<%=ad.name()%>');" value="开始广告">
					<input type="button" onclick="$IT.A('adSaveAct');" value="保存广告"  class="button2">
					<input type="button"
						onclick="$IT.A('adStopAct','adId=<%=ad.name()%>');" value="取消广告">
				</td>
			</tr>
			<tr>
				<td class="l">
					显示位置：
				</td>
				<td>
					<%=ad.toString()%>
				</td>
			</tr>
			<tr>
				<td class="l" valign="top">
					广告状态：
				</td>
				<td>
					<input id="ad_status" readonly="readonly" type="text" style="border-width: 0; width: 99%; background-image: none; background: #f7f7ff;color: red;">
				</td>
			</tr>
			<tr>
				<td class="l">
					来源类型：
				</td>
				<td>
					<select id="ad_adType" name="ad_adType"
						onchange="selectAdType(this);">
						<option value="0">
							图片
						</option>
						<option value="1">
							文字
						</option>
						<option value="2">
							插件
						</option>
					</select>
				</td>
			</tr>
			<tr class="img_tr">
				<td class="l" valign="top">
					选择图片：
				</td>
				<td>
					<img id="adImg" name="adImg" src="/app/ad/ad_img.png"
						onclick="$Actions['fileUploadImgWindowAct']('refId=ad_src&refId1=adImg');"
						style="cursor: pointer;">
					<input type="hidden" id="ad_src" name="ad_src" />
				</td>
			</tr>
			<tr class="img_tr">
				<td class="l" valign="top">
					URL：
				</td>
				<td>
					<input type="text" style="width: 250px;" id="ad_url" name="ad_url" />
				</td>
			</tr>
			<tr class="plugin_tr" style="display: none;">
				<td class="l" valign="top">
					插件内容：
				</td>
				<td>
					<textarea id="ad_pcontent" name="ad_pcontent" style="width: 99%;" rows="6"></textarea>
				</td>
			</tr>
			<tr class="text_tr" style="display: none;">
				<td class="l" valign="top">
					文字内容：
				</td>
				<td>
					<div class="clear_float" style="padding-bottom: 3px;">
						<div style="float: left" id="ad_content_info"
							class="important-tip">
							#(CKEditor.0)
						</div>
					</div>
					<div>
						<textarea id="ad_content" name="ad_content" style="display: none;"></textarea>
					</div>
				</td>
			</tr>
			<tr>
				<td class="l">
					有效天数：
				</td>
				<td>
					<input type="text" style="width: 50px" id="ad_days" name="ad_days"
						value="-1"><span style="color: red;">-1表示永久有效</span>
				</td>
			</tr>
			<tr>
				<td class="l">
					开始时间：
				</td>
				<td>
					<div id="td_startDate" style="width: 120px;"></div>
					<input type="hidden" name="ad_startDate" id="ad_startDate">
				</td>
			</tr>
		</tbody>
	</table>
</div>
<script type="text/javascript">
$ready(function() {
	window.onbeforeunload = function() {
		return "#(CKEditor.1)";
	};
	addTextButton("ad_startDate", "calStartDate", "td_startDate", true);
	var i=0;
	new PeriodicalExecuter(function(executer) {
		var ad_src = $F('ad_src');
		if (ad_src != null && ad_src != "") {
			$('adImg').src = '<%=request.getContextPath()%>' + ad_src;
			executer.stop();
		}
		selectAdType($('ad_adType'));
		i++;
		if (i > 5) {
			executer.stop();
		}
	}, 0.1);
});
function selectAdType(t) {
	if ('0' == t.value) {
		$$('.text_tr').each(function(c) {
			c.style.display = 'none';
		});
		$$('.plugin_tr').each(function(c) {
			c.style.display = 'none';
		});
		$$('.img_tr').each(function(c) {
			c.style.display = '';
		});
	} else if ('1' == t.value) {
		$$('.text_tr').each(function(c) {
			c.style.display = '';
		});
		$$('.plugin_tr').each(function(c) {
			c.style.display = 'none';
		});
		$$('.img_tr').each(function(c) {
			c.style.display = 'none';
		});
	} else {
		$$('.img_tr').each(function(c) {
			c.style.display = 'none';
		});
		$$('.text_tr').each(function(c) {
			c.style.display = 'none';
		});
		$$('.plugin_tr').each(function(c) {
			c.style.display = '';
		});
	}
}
</script>
