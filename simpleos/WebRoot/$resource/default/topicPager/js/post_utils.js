var POST_UTILS = {

	alluser : function(o) {
		if (this.viewUrl.length > 0) {
			$Actions.loc(this.viewUrl);
		} else {
			$Actions["__pager_postsId"].refresh(this._user_params());
		}
	},

	onlyuser : function(o) {
		if (this.viewUrl.length > 0) {
			$Actions.loc(this.viewUrl.addParameter(this._user_params(o)));
		} else {
			$Actions["__pager_postsId"].refresh(this._user_params(o));
		}
	},
	
	user_stat : function(o) {
		$Actions["userAccountStatWindow"](this._user_params(o));
	},

	move : function(o, up) {
		o = $target(o).up("table");
		var id = o.id.substring(4);
		o = o.up(".postbody");
		o = up ? o.previous(".postbody") : o.next(".postbody");
		if (o) {
			var id2 = o.down("table").id.substring(4);
			$Actions["ajaxPostMove"]
					("up=" + up + "&postId=" + id + "&postId2=" + id2);
		}
	},

	move2 : function(o, up) {
		o = $target(o).up("table");
		var id = o.id.substring(4);
		o = o.up(".cl");
		var id2 = $F(up ? o.down("#firstPost") : o.down("#lastPost"));
		if (id2 != "") {
			$Actions["ajaxPostMove"]
					("up=" + up + "&postId=" + id + "&postId2=" + id2);
		}
	},
	
	del : function(o) {
		$Actions['ajaxPostDelete'](this._params(o));
	},
	
	listMy : function(o) {
		$Actions.loc(this.topicUrl.addParameter(this._user_params(o)));
	},
	
	listMy2 : function(o) {
		$Actions.loc(this.topicUrl.addParameter(this._user_params(o) + "&r=true"));
	},
	
	blog : function(o) {
		$Actions.loc($F($target(o).next()));
	},
	
	addFriend : function(o) {
		$Actions["addMyFriendWindow"](this._user_params(o));
	},

	_params : function(o) {
		return "postId=" + $target(o).up("table").id.substring(4);
	},
	
	_user_params : function(o) {
		var r = this.userIdParameterName + "=";
		if (o)
			r += $target(o).getAttribute("userId");
		return r;
	}
};