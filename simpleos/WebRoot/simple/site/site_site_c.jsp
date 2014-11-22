<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div class="simple_custom" id="site_site_form">
	<table width="100%">
		<tr>
			<th class="w-100px" nowrap="nowrap">
				站点URL
			</th>
			<td width="100%">
				<input type="text" name="site_url" id="site_url" value=""
					class="text-3">
			</td>
		</tr>
		<tr>
			<th class="w-100px" nowrap="nowrap">
				#(Site.Site.0)
			</th>
			<td width="100%">
				<input type="text" name="site_name" id="site_name" value=""
					class="text-3">
			</td>
		</tr>
		<tr>
			<th>
				#(Site.Site.1)
			</th>
			<td>
				<input type="text" name="site_copyright" id="site_copyright"
					value="2013" class="text-3">
			</td>
		</tr>
		<tr>
			<th>
				#(Site.Site.2)
			</th>
			<td>
				<input type="text" name="site_slogan" id="site_slogan" value=""
					class="text-1">
			</td>
		</tr>
		<tr>
			<th>
				#(Site.Site.3)
			</th>
			<td>
				<input type="text" name="site_keywords" id="site_keywords" value=""
					class="text-1">
			</td>
		</tr>
		<tr>
			<th valign="top">
				#(Site.Site.4)
			</th>
			<td>
				<textarea name="site_desc" id="site_desc" class="area-1" rows="10" style="width: 98%;"></textarea>
			</td>
		</tr>
		<tr>
			<th>
				#(Site.Site.5)
			</th>
			<td>
				<input type="text" name="site_icp" id="site_icp" value=""
					class="text-1">
			</td>
		</tr>
		<tr>
			<th></th>
			<td>
				<input type="button" class="button2" id="site_site_btn"
					onclick="$IT.A('site_siteAct','type=site');" value="#(Itsite.c.ok)">
			</td>
		</tr>
	</table>
</div>