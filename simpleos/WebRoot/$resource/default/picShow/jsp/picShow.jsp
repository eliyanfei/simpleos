<%@page
	import="net.simpleframework.web.page.component.ui.picshow.PicShowBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page
	import="net.simpleframework.web.page.component.ui.picshow.PicShowUtils"%>
<%@page
	import="net.simpleframework.web.page.component.ComponentParameter"%>
<%@page
	import="net.simpleframework.web.page.component.AbstractComponentBean"%>

<%
	final ComponentParameter compParameter = new ComponentParameter(
			request, response, null);
	compParameter.componentBean = AbstractComponentBean
			.getComponentBeanByRequestId(compParameter,
					PicShowUtils.BEAN_ID);
	PicShowBean bean = (PicShowBean) compParameter.componentBean;
%>
<div id="wrap">
	<%
		String display = "";
		if (!bean.isShowTop()) {
			display = "none";
		}
	%>
	<table cellpadding="" cellspacing=""></table>
	<div id="wrapTop" style="display:<%=display%>;">
		<div class="eTitle">
			<div class="h1title">
				<span id="d_picTit"></span><span id="total">(<span
					class="cC00">0</span>/0)</span>
			</div>
		</div>

		<!-- 2010/12/2 begin 在eControl外面增加了一层DIV -->
		<div class="clearfix">
			<div class="eControl">

				<div class="ecCont">
					<div id="ecbFullScreen" title="点击全屏获得更好观看效果"
						style="visibility: hidden">
						<div class="buttonCont" id="fullScreenFlash"></div>
					</div>
					<div id="ecbSpeed">
						<div id="ecbSpeedInfo" class="buttonCont">
							5秒
						</div>
					</div>
					<div id="ecbPre" title="上一张">
						<div class="buttonCont"></div>
					</div>
					<div id="ecbPlay">
						<div id="ecpPlayStatus" class="play"></div>
					</div>
					<div id="ecbNext" title="下一张">
						<div class="buttonCont"></div>
					</div>
					<div id="ecbLine">
						<div class="buttonCont"></div>
					</div>
					<div id="ecbMode" title="列表模式(tab)">
						<div class="buttonCont"></div>
					</div>
					<div id="ecbModeReturn" title="返回幻灯模式(tab)">
						<div class="buttonCont"></div>
					</div>

					<!-- 速度条 begin -->
					<div id="SpeedBox">
						<div id="SpeedCont">
							<div id="SpeedSlide"></div>
							<div id="SpeedNonius"></div>

						</div>
					</div>
					<!-- 速度条 end -->
				</div>
			</div>
		</div>
		<!-- 2010/12/2 end -->
	</div>

	<div id="eFramePic">
	<%
		 display = "";
		if (!bean.isShowCenter()) {
			display = "none";
		}
	%>
		<div id="efpBigPic" style="display: <%=display%>">
			<div id="efpClew"></div>

			<div id="d_BigPic"></div>
			<div id="efpLeftArea" class="arrLeft" title="上一张"></div>
			<div id="efpRightArea" class="arrRight" title="下一张"></div>
			<!-- endSelect begin -->
			<div id="endSelect">
				<div id="endSelClose"></div>
				<div class="bg"></div>
				<div class="E_Cont">
					<p>
						您已经浏览完所有图片
					</p>
					<p>
						<a href="javascript:void(0)" id="rePlayBut"></a><a
							href="javascript:void(0)" id="nextPicsBut" title=""
							onclick="toOther(this);"></a>
					</p>
				</div>
			</div>
			<!-- endSelect end -->
		</div>

		<div id="efpTxt" style="display: <%=display%>">
			<div id="d_picIntro"></div>
		</div>

		<div id="efpPicList">
			<div id="efpPreGroup">
				<div id="efpPrePic" onmouseover="this.className='selected'"
					onmouseout="this.className=''">
					<table cellspacing="0">
						<tr>
							<td>
								<a onclick="toOther(this);" href="" title=""> <img src=""
										alt="" title="" style="width: 50px; height: 50px;" /> </a>
							</td>
						</tr>
					</table>
				</div>
				<div id="efpPreTxt">
					<a href="" title="" onclick="toOther(this);">&lt;&lt;上一图集</a>
				</div>
			</div>

			<div id="efpListLeftArr" onmouseover="this.className='selected'"
				onmouseout="this.className=''"></div>
			<div id="efpPicListCont"></div>
			<div id="efpListRightArr" onmouseover="this.className='selected'"
				onmouseout="this.className=''"></div>

			<div id="efpNextGroup">
				<div id="efpNextPic" onmouseover="this.className='selected'"
					onmouseout="this.className=''">
					<table cellspacing="0">
						<tr>
							<td>
								<a href="" onclick="toOther(this);" title=""><img src=""
										alt="" title="" style="width: 50px; height: 50px;" /> </a>
							</td>
						</tr>

					</table>
				</div>
				<div id="efpNextTxt">
					<a href="" title="" onclick="toOther(this);">下一图集&gt;&gt;</a>
				</div>
			</div>
		</div>
	</div>
	<div id="ePicList"></div>
</div>
<script language="ja
	
cript" type="text/javascript">
	<%=PicShowUtils.genJavascript(compParameter)%>
	function toOther(t) {
		var href = t.href;
		var idx = href.indexOf('?');
		if (idx == -1) {
			href += '?arg=' + t.title;
		} else {
			var forward = href.substring(0, idx);
			var query = href.substring(idx + 1);
			var arr = query.split('&');
			var nowArr = new Array();
			for ( var i = 0; i < arr.length; i++) {
				var s = arr[i].split('=');
				if (s[0] == 'arg') {
					continue;
				}
				nowArr.push(arr[i]);
			}
			nowArr.push('arg=' + t.title);
			href = forward + '?' + nowArr.join('&');
		}
		t.href = href;
	}
</script>
