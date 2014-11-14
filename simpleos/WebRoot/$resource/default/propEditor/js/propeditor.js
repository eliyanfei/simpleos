$Comp.FormEditor=Class.create({inputs:["text","hidden","checkbox","password","button"],initialize:function(a,f,j){this.options={title:null,onLoaded:Prototype.emptyFunction};Object.extend(this.options,j||{});
a=$(a);a.setDimensions(this.options);var b=new Element("DIV",{className:"formeditor"}).insert($Comp.createTable());if(this.options.title){var h=new Element("div",{className:"formeditor_title t1"});a.insert(h.insert(this.options.title));
if(this.options.titleToggle){var e=new Element("img",{src:HOME_PATH+"/css/"+SKIN+"/images/toggle.gif"});h.insert(new Element("div",{className:"fe_expand"}).update(e));$Comp.imageToggle(e,b)}}a.insert(b);
var d=a.down("tbody");f=f||[];var c=0;f.each(function(m){var l=new Element("tr");d.insert(l);this.createTip(l,m.desc);var n=(c++==0)?"fe_firstrow ":"fe_row ";if(c==f.length){n+="fe_lastrow "}var k=new Element("td",{className:n+"fe_label"}).update(m.label?m.label:"");
if(m.labelStyle){k.setStyle(m.labelStyle)}var i=new Element("td",{className:n+"fe_value"});l.insert(k).insert(i);(m.components||[]).each(function(p){if(p.type=="textButton"){var o=$Comp.textButton();this._setId(o.textObject,p);
this._setValue(o.textObject,p);o.addClassName("text_comp");this._setAttributes(o.textObject,p);this._setEvent(o.buttonObject,p);i.insert(o)}else{if(p.type=="file"){var o=$Comp.textFileButton();this._setId(o.file,p);
o.textButton.addClassName("text_comp");this._setAttributes(o,p);i.insert(o)}else{var q;if(this.inputs.include(p.type)){q=new Element("input",{type:p.type});if(p.type=="text"||p.type=="password"){q.addClassName("text_comp")
}if(p.type=="checkbox"){q.setStyle("margin-left: 6px;")}}else{if(p.type=="textarea"){q=new Element("textarea",{rows:3});q.addClassName("text_comp")}else{if(p.type=="select"){q=new Element("select",{style:"margin-left: 2px;"})
}else{if(p.type=="div"){q=new Element("div",{style:"margin: 2px;"})}}}}if(q){this._setId(q,p);this._setAttributes(q,p);this._setValue(q,p);this._setEvent(q,p);i.insert(q)}}}}.bind(this))}.bind(this));var g=this.options.onLoaded;
if(g){g()}},_setId:function(b,a){if(a.name){b.writeAttribute("id",a.name).writeAttribute("name",a.name)}},_setAttributes:function(b,a){if(a.style){b.setStyle(a.style)}if(a.attributes){Object.keys(a.attributes).each(function(c){b.writeAttribute(c,a.attributes[c])
})}},_setValue:function(e,b){if(b.defaultValue){if(b.type=="select"){var a=b.defaultValue;$A(a.include("\n")?a.split("\n"):a.split(";")).each(function(d){var c=d.strip().split("=");if(c.length==2){e.insert(new Element("option",{value:c[0]}).update(c[1]))
}else{e.insert(new Element("option").update(c[0]))}})}else{if(b.type=="div"){e.update(b.defaultValue)}else{e.value=b.defaultValue}}}},_setEvent:function(c,d){if(!d.events){return}Object.keys(d.events).each(function(key){c.observe(key,function(e){eval(d.events[key])
})})},createTip:function(b,a){if(a){new Tip(b,a,{stem:"topLeft",hook:{target:"bottomLeft"},offset:{x:4,y:1}})}}});