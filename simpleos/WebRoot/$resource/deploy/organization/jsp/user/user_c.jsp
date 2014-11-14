<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="org_user">
<div class="pb">
<table style="width: 100%;" cellpadding="0" cellspacing="0">
	<tr>
		<td><input type="button" value="#(Org.user_c.0)"
			onclick="$Actions['__user_list'].add();" /></td>
		<td id="__user_list_sech" style="text-align: right;"></td>
	</tr>
</table>
<div style="display: none;" class="sech_pane_params">
<input type="hidden" id="du_f0" name="du_f0" value="true" />
<table cellpadding="2" cellspacing="0" style="width: 360px;">
	<tr>
		<td class="l">#(Org.user_c.2)</td>
		<td class="v"><input id="du_f1" name="du_f1" type="text" /></td>
	</tr>
	<tr>
		<td class="l">#(Org.user_c.3)</td>
		<td class="v"><input id="du_f2" name="du_f2" type="text" /></td>
	</tr>
	<tr>
		<td class="l">#(Org.user_c.4)</td>
		<td class="v"><input id="du_f3" name="du_f3" type="text" /></td>
	</tr>
	<tr>
		<td class="l">#(Org.user_c.5)</td>
		<td class="v"><input id="du_f4" name="du_f4" type="text" /></td>
	</tr>
	<tr>
		<td class="l"></td>
		<td class="v"><input id="du_f5" name="du_f5" type="checkbox" value="true"/>
			<label for="du_f5">#(Org.user_c.6)</label></td>
	</tr>
	<tr>
		<td></td>
		<td><input type="button" class="button2" value="#(Button.Ok)" 
				onclick="$Actions['__user_list']($$Form(this.up('.sech_pane_params')));" />
			<input type="button" value="#(Button.Cancel)" 
				onclick="this.up('div').$toggle();" />
		</td>
	</tr>
</table>
</div>
</div>
<div id="__user_list"></div>
</div>
<script type="text/javascript">
	var sb = $Comp.searchButton(function(sp) {
		$Actions["__user_list"]("sv=" + $F(sp.down(".txt")));
	}, function(sp) {
		sp.up("table").next().$toggle();
	}, "#(Org.user_c.1)", 180);
	$("__user_list_sech").update(sb);
</script>
