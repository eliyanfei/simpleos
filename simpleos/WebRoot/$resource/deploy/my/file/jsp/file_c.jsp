<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div class="myfiles">
	<div class="simple_toolbar2">
		<table style="width: 100%;">
			<tr>
				<td><input type="button" value="#(My.file_c.0)"
					onclick="$Actions['__my_files'].add();" />
				</td>
				<td id="__my_files_sech" style="text-align: right;"></td>
			</tr>
		</table>
		<div style="display: none;" class="sech_pane_params">
			<input type="hidden" id="ff_f0" name="ff_f0" value="true" />
			<table style="width: 360px;">
				<tr>
					<td class="l">#(My.file_c.2)</td>
					<td class="v"><input id="ff_f1" name="ff_f1" type="text" />
					</td>
				</tr>
				<tr>
					<td class="l">#(My.file_c.3)</td>
					<td class="v"><select id="ff_f2" name="ff_f2">
							<option value="0">#(My.file_c.6)</option>
							<option value="1">#(My.file_c.7)</option>
							<option value="2">#(My.file_c.8)</option>
							<option value="3">#(My.file_c.9)</option>
							<option value="4">#(My.file_c.10)</option>
							<option value="5">#(My.file_c.11)</option>
					</select>
					</td>
				</tr>
				<tr>
					<td class="l">#(My.file_c.4)</td>
					<td class="v"><select id="ff_f3" name="ff_f3">
							<option>#(My.file_c.6)</option>
					</select>
					</td>
				</tr>
				<tr>
					<td class="l">#(My.file_c.5)</td>
					<td class="v"><select id="ff_f4" name="ff_f4">
							<option>#(My.file_c.6)</option>
					</select>
					</td>
				</tr>
				<tr>
					<td class="l"></td>
					<td class="v"><input id="ff_f5" name="ff_f5" type="checkbox"
						value="true" /> <label for="ff_f5">#(Org.user_c.6)</label>
					</td>
				</tr>
				<tr>
					<td></td>
					<td><input type="button" class="button2" value="#(Button.Ok)"
						onclick="$Actions['__my_files']($$Form(this.up('.sech_pane_params')));" />
						<input type="button" value="#(Button.Cancel)"
						onclick="this.up('div').$toggle();" />
					</td>
				</tr>
			</table>
		</div>
	</div>
	<div id="__my_files"></div>
</div>
<script type="text/javascript">
	var sb = $Comp.searchButton(function(sp) {
		$Actions['__my_files']("fv=" + $F(sp.down(".txt")));
	}, function(sp) {
		sp.up("table").next().$toggle();
	}, "#(My.file_c.1)", 180);
	$("__my_files_sech").update(sb);
</script>
