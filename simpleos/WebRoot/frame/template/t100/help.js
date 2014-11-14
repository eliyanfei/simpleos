var helpObj = function() {
	this.id = null;
	this.left = 0;
	this.top = 0;
	this.idx = 0;
	this.action = null;
};
var helpCache = new Array();
var h_h = 100;
var h_w = 300;
var peObj;
function init_help() {
	var idx = 0;
	helpCache[idx] = new helpObj();
	helpCache[idx].id = null;
	helpCache[idx].idx = 0;
	helpCache[idx].top = sh / 2 - h_h / 2;
	helpCache[idx++].left = sw / 2 - h_w / 2;

	helpCache[idx] = new helpObj();
	helpCache[idx].id = null;
	helpCache[idx].idx = 1;
	helpCache[idx].top = sh / 2 - h_h / 2;
	helpCache[idx++].left = sw / 2 - h_w / 2;

	helpCache[idx] = new helpObj();
	helpCache[idx].id = "navBar";
	helpCache[idx].idx = 2;
	helpCache[idx].top = 50;
	helpCache[idx++].left = sw / 2 - h_w / 2;

	helpCache[idx] = new helpObj();
	helpCache[idx].id = "ui-tab1";
	helpCache[idx].action = "$('ui-tab1').click()";
	helpCache[idx].idx = 16;
	helpCache[idx].top = 50;
	helpCache[idx++].left = sw / 2 - h_w / 2 + 10;

	helpCache[idx] = new helpObj();
	helpCache[idx].id = "ui-tab2";
	helpCache[idx].action = "$('ui-tab2').click()";
	helpCache[idx].idx = 17;
	helpCache[idx].top = 50;
	helpCache[idx++].left = sw / 2 - h_w / 2 + 20;

	helpCache[idx] = new helpObj();
	helpCache[idx].id = "ui-tab3";
	helpCache[idx].action = "$('ui-tab3').click()";
	helpCache[idx].idx = 18;
	helpCache[idx].top = 50;
	helpCache[idx++].left = sw / 2 - h_w / 2 + 30;

	helpCache[idx] = new helpObj();
	helpCache[idx].id = "ui-tab4";
	helpCache[idx].action = "$('ui-tab4').click()";
	helpCache[idx].idx = 19;
	helpCache[idx].top = 50;
	helpCache[idx++].left = sw / 2 - h_w / 2 + 40;

	helpCache[idx] = new helpObj();
	helpCache[idx].id = "allgrid";
	helpCache[idx].action = "$('allgrid').click()";
	helpCache[idx].idx = 20;
	helpCache[idx].top = 50;
	helpCache[idx++].left = sw / 2 - h_w / 2 + 50;

	helpCache[idx] = new helpObj();
	helpCache[idx].idx = 21;
	helpCache[idx].top = sh / 2 - h_h / 2;
	helpCache[idx++].left = sw / 2 - h_w / 2;

	helpCache[idx] = new helpObj();
	helpCache[idx].id = "all_grid_content_id";
	helpCache[idx].action = "$('all_grid_content_id').click();$('ui-tab1').click();";
	helpCache[idx].idx = 22;
	helpCache[idx].top = 40;
	helpCache[idx++].left = sw - h_w - 50;

	helpCache[idx] = new helpObj();
	helpCache[idx].id = "taskBarWrap";
	helpCache[idx].idx = 3;
	helpCache[idx].top = sh - h_h - 40;
	helpCache[idx++].left = sw / 2 - h_w / 2;

	helpCache[idx] = new helpObj();
	helpCache[idx].id = "task_timer";
	helpCache[idx].idx = 5;
	helpCache[idx].left = sw - h_w - 50;
	helpCache[idx++].top = sh - h_h - 20;

	helpCache[idx] = new helpObj();
	helpCache[idx].id = "show_desktop";
	helpCache[idx].action = "$('show_desktop').click();";
	helpCache[idx].idx = 6;
	helpCache[idx].left = 0;
	helpCache[idx++].top = sh - h_h - 20;

	helpCache[idx] = new helpObj();
	helpCache[idx].id = "task_lb";
	helpCache[idx].action = "$('show_desktop').click();";
	helpCache[idx].idx = 7;
	helpCache[idx].left = 50;
	helpCache[idx++].top = sh - h_h - 20;

	helpCache[idx] = new helpObj();
	helpCache[idx].id = "lr_bar";
	helpCache[idx].idx = 4;
	helpCache[idx].left = 80;
	helpCache[idx++].top = 63;

	helpCache[idx] = new helpObj();
	helpCache[idx].id = "start_block";
	helpCache[idx].idx = 8;
	helpCache[idx].left = 80;
	helpCache[idx++].top = 63;

	helpCache[idx] = new helpObj();
	helpCache[idx].id = "monitorWin_home";
	helpCache[idx].idx = 9;
	helpCache[idx].left = 80;
	helpCache[idx++].top = 140;

	helpCache[idx] = new helpObj();
	helpCache[idx].id = "configMgrWin_home";
	helpCache[idx].idx = 15;
	helpCache[idx].left = 80;
	helpCache[idx++].top = 200;

	helpCache[idx] = new helpObj();
	helpCache[idx].id = "ajaxLogout_tools";
	helpCache[idx].idx = 10;
	helpCache[idx].left = 80;
	helpCache[idx++].top = 275;

	helpCache[idx] = new helpObj();
	helpCache[idx].id = "themeWin_tools";
	helpCache[idx].idx = 11;
	helpCache[idx].left = 80;
	helpCache[idx++].top = 275;

	helpCache[idx] = new helpObj();
	helpCache[idx].id = "desktop_sound_id";
	helpCache[idx].idx = 12;
	helpCache[idx].left = 80;
	helpCache[idx++].top = 275;

	helpCache[idx] = new helpObj();
	helpCache[idx].id = "settingWin_tools";
	helpCache[idx].idx = 13;
	helpCache[idx].left = 80;
	helpCache[idx++].top = 275;

	helpCache[idx] = new helpObj();
	helpCache[idx].id = null;
	helpCache[idx].idx = 14;
	helpCache[idx].top = sh / 2 - h_h / 2;
	helpCache[idx++].left = sw / 2 - h_w / 2;
}
function show_help() {
	init_help();
	$('help_id').style.display = '';
	$('help_id_over').style.display = '';
	$('help_id_over').style.height = sh + 'px';
	$('help_id_over').style.width = sw + 'px';
	runStep(helpCache[0]);
	var pe = new PeriodicalExecuter(function(executer) {
		if (peObj.pause != true) {
			var helpO1 = helpCache[executer.step];
			if (runStep(helpO1, helpCache[executer.step - 1])) {
				$('help_id').style.display = 'none';
				$('help_id_over').style.display = 'none';
				executer.stop();
			}
			if (executer.step == helpCache.length) {
				$('help_id').style.display = 'none';
				$('help_id_over').style.display = 'none';
				executer.stop();
			}
			executer.step = executer.step + 1;
		}
	}, 2);
	pe.step = 1;
	peObj = pe;
	return pe;
}

function runStep(helpO, preHelpO) {
	if (preHelpO && $('step_' + preHelpO.idx)) {
		if ($(preHelpO.id)) {
			$(preHelpO.id).style.border = '0px';
		}
	}
	if (helpO) {
		if (peObj && peObj.stopp) {
			return true;
		}
		if ($('step_' + helpO.idx)) {
			if ($(helpO.id)) {
				$(helpO.id).style.border = '3px solid red';
			}
			$('help_id_c').innerHTML = $('step_' + helpO.idx).innerHTML;
		}
		$desktop.move("help_id", {
			left : helpO.left,
			top : helpO.top
		});
		new PeriodicalExecuter(function(executer) {
			if (helpO.action) {
				eval(helpO.action);
			}
			executer.stop();
		}, 0.7);

	}
	return false;
}
function help_stop() {
	peObj.stopp = true;
}
function help_pause(t) {
	var o = t.getAttribute('s');
	if (o == 'run') {
		t.setAttribute('s', 'pause');
		peObj.pause = false;
		t.title = '暂停';
		t.src = contextPath + '/frame/template/t3/images/help_pause.png';
	} else {
		t.setAttribute('s', 'run');
		t.title = '开始ʼ';
		peObj.pause = true;
		t.src = contextPath + '/frame/template/t3/images/help_play.png';
	}
}