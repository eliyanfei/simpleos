var BBS_UTILS={search_init:function(){$$(".sech_pane_params .v").each(function(a){var c;if(a.hasClassName("b1")){c=$Comp.textButton("_s_startDate",function(){$Actions.calStartDate.show()})}else{if(a.hasClassName("b2")){c=$Comp.textButton("_s_endDate",function(){$Actions.calEndDate.show()
})}}if(c){c.textObject.setAttribute("readonly","readonly");a.update(c)}});$Actions.observeSubmit(".sech_pane_params input[type=text]",function(a){this.search_submit(a)}.bind(this))},search_submit:function(f){var d=f.up(".sech_pane_params");
var e=$F(d.down("#_s_startDate"));var b=$F(d.down("#_s_endDate"));if(e!=""&&b!=""){if(Date.parseString(e,"yyyy-MM-dd").isAfter(Date.parseString(b,"yyyy-MM-dd"))){alert("#(bbs_search_pane.5)");return}}var a="_s_catalog=";
var c=0;$$("#_s_catalog div[id]").each(function(h){if(c++>0){a+=";"}a+=h.id});var g=$$Form(d).addParameter(a+"&c=");if($Actions.bbsManagerToolsWindow){$Actions.loc(this.topicUrl.addParameter(g))}else{$Actions.bbsTopicPager(g)
}},insert_forum:function(a){if(!a.any(function(c){return c.selected})){return}var b=$("_s_catalog");a.each(function(c){if(!c.selected||b.down("#"+c.id)){return}b.insert(new Element("div",{id:c.id}).insert(new Element("a",{className:"delete_image",style:"float: right",onclick:"this.up().$remove();"})).insert(new Element("span").update(c.text)))
});return true},create_searchbar:function(){return $Comp.searchButton(function(a){$Actions.loc(this.topicUrl.addParameter("c="+$F(a.down(".txt"))))}.bind(this),function(a){a.up(".tbar").next().$toggle()
},this.searchbar_msg,210)}};