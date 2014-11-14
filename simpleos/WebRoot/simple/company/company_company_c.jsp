<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div class="simple_custom" id="company_company_form">
	<table>
		<tr>
			<th class="w-100px">
				#(Componey.0)
			</th>
			<td>
				<input type="text" name="company_name" id="company_name" value=""
					class="text-3">
			</td>
		</tr>
		<tr>
			<th>
				#(Componey.1)
			</th>
			<td>
				<textarea name="company_desc" id="company_desc" style="width: 99%"
					rows="3" class="area-1"></textarea>
			</td>
		</tr>
		<tr>
			<th valign="top">
				#(Componey.2)
			</th>
			<td>
				<textarea name="company_content" id="company_content" class="area-1"
					rows="10" style="display: none;"></textarea>
			</td>
		</tr>
		<tr>
			<th></th>
			<td>
				<input type="button" class="button2" id="company_company_btn"
					onclick="$IT.A('company_companyAct');" value="#(Itsite.c.ok)">
			</td>
		</tr>
	</table>
</div>