var picShow={ps:function(objName){if(document.getElementById){return eval('document.getElementById("'+objName+'")')}else{return eval("document.all."+objName)}},isIE:navigator.appVersion.indexOf("MSIE")!=-1?true:false,addEvent:function(f,e,d){if(f.attachEvent){f.attachEvent("on"+e,d)
}else{f.addEventListener(e,d,false)}},delEvent:function(f,e,d){if(f.detachEvent){f.detachEvent("on"+e,d)}else{f.removeEventListener(e,d,false)}},readCookie:function(f){var i="",j=f+"=";if(document.cookie.length>0){var h=document.cookie.indexOf(j);
if(h!=-1){h+=j.length;var g=document.cookie.indexOf(";",h);if(g==-1){g=document.cookie.length}i=document.cookie.substring(h,g)}}return i},writeCookie:function(k,j,h,c){var l="",i="";if(h!=null){l=new Date((new Date).getTime()+h*3600000);
l="; expires="+l.toGMTString()}if(c!=null){i=";domain="+c}document.cookie=k+"="+escape(j)+l+i},readStyle:function(f,d){if(f.style[d]){return f.style[d]}else{if(f.currentStyle){return f.currentStyle[d]}else{if(document.defaultView&&document.defaultView.getComputedStyle){var e=document.defaultView.getComputedStyle(f,null);
return e.getPropertyValue(d)}else{return null}}}},absPosition:function(i,e){var j=0;var k=0;var h=i;try{do{j+=h.offsetLeft;k+=h.offsetTop;h=h.offsetParent}while(h.id!=document.body&&h.id!=document.documentElement&&h!=e&&h!=null)
}catch(l){}return{left:j,top:k}},style:{setOpacity:function(c,d){if(typeof(c.style.opacity)!="undefined"){c.style.opacity=d}else{c.style.filter="Alpha(Opacity="+(d*100)+")"}}},extend:{show:function(h,g){if(picShow.readStyle(h,"display")==="none"){h.style.display="block"
}picShow.style.setOpacity(h,0);if(!g){g=200}var f=0,e=g/20;clearTimeout(h._extend_show_timeOut);h._extend_show_timeOut=setTimeout(function(){if(f>=1){return}f+=1/e;picShow.style.setOpacity(h,f);h._extend_show_timeOut=setTimeout(arguments.callee,20)
},20)},hide:function(h,g){if(!g){g=200}picShow.style.setOpacity(h,1);var f=1,e=g/20;clearTimeout(h._extend_show_timeOut);h._extend_show_timeOut=setTimeout(function(){if(f<=0){h.style.display="none";picShow.style.setOpacity(h,1);
return}f-=1/e;picShow.style.setOpacity(h,f);h._extend_show_timeOut=setTimeout(arguments.callee,20)},20)},actPX:function(l,k,r,m,n,q,j){if(typeof(j)=="undefined"){j="px"}clearTimeout(l["_extend_actPX"+k.replace(/\-\.\=/,"_")+"_timeOut"]);
if(r>m){n=-Math.abs(n)}else{n=Math.abs(n)}var p=r;var o=m-r;l["_extend_actPX"+k.replace(/\-\.\=/,"_")+"_timeOut"]=setTimeout(function(){p+=n;var a=m-p;if(r<m){if(a<o/3){n=Math.ceil(a/3)}if(a<=0){l[k]=m+j;
if(q){q()}return}}else{if(a>o/3){n=Math.floor(a/3)}if(a>=0){l[k]=m+j;if(q){q()}return}}l[k]=p+j;l["_extend_actPX"+k.replace(/\-\.\=/,"_")+"_timeOut"]=setTimeout(arguments.callee,20)},20)}}};picShow.Step=function(){this.stepIndex=0;
this.classBase="step_";this.limit=3;this.stepTime=20;this.element=null;this._timeObj=null;this._type="+"};picShow.Step.prototype.action=function(c){if(!this.element){return}var d=this;if(c=="+"){this._type="+"
}else{this._type="-"}clearInterval(this._timeObj);this._timeObj=setInterval(function(){d.nextStep()},this.stepTime)};picShow.Step.prototype.nextStep=function(){if(this._type=="+"){this.stepIndex++}else{this.stepIndex--
}if(this.stepIndex<=0){clearInterval(this._timeObj);this.stepIndex=0;if(this._type=="-"){if(this.onfirst){this.onfirst()}}}if(this.stepIndex>=this.limit){clearInterval(this._timeObj);this.stepIndex=this.limit;
if(this._type=="+"){if(this.onlast){this.onlast()}}}this.element.className=this.classBase+this.stepIndex;if(this.onstep){this.onstep()}};var getData={initF:false,nextUrl:"",preUrl:"",curUrl:"",fillData:function(z){epidiascope.clearData();
var H=slide_data.images;var K=slide_data.slide;var G="";if(K.path){G=K.path}var D="",E="";for(var y=0;y<H.length;y++){var t,F,J,w,B,I,u,i;F=G+"/"+H[y].image_url;J=H[y].createtime;t=H[y].title;w=H[y].intro;
if(G!=""){B=G+"/"+H[y].thumb_160}else{B=H[y].thumb_160}if(G!=""){I=G+"/"+H[y].thumb_50}else{I=H[y].thumb_50}u=H[y].id;i=H[y].callback;epidiascope.add({src:F,date:J,title:t,text:w,lowsrc_b:B,lowsrc_s:I,id:u,callback:i});
if(D!=""){D+="|"}D+=F;if(E!=""){E+="|"}if(!getData.initF){E+=encodeURIComponent(t)+"#����"+encodeURIComponent(w.replace(/<.*?>/g,""))}else{E+=t+"#����"+w.replace(/<.*?>/g,"")}}var A=picShow.ps("efpPrePic");
var C=picShow.ps("efpPreTxt");var v=picShow.ps("efpNextPic");var x=picShow.ps("efpNextTxt");A.getElementsByTagName("a")[0].title=slide_data.prev_album.arg;A.getElementsByTagName("a")[0].href="";A.getElementsByTagName("img")[0].src=G+slide_data.prev_album.thumb_50;
A.getElementsByTagName("img")[0].alt=slide_data.prev_album.title;A.getElementsByTagName("img")[0].title=slide_data.prev_album.title;C.getElementsByTagName("a")[0].href="";C.getElementsByTagName("a")[0].title=slide_data.prev_album.arg;
v.getElementsByTagName("a")[0].title=slide_data.next_album.arg;v.getElementsByTagName("a")[0].href="";v.getElementsByTagName("img")[0].src=G+slide_data.next_album.thumb_50;v.getElementsByTagName("img")[0].alt=slide_data.next_album.title;
v.getElementsByTagName("img")[0].title=slide_data.next_album.title;x.getElementsByTagName("a")[0].title=slide_data.next_album.arg;x.getElementsByTagName("a")[0].href="";if(!getData.initF){epidiascope.init();
getData.initF=true}else{epidiascope.initNot();if(z){epidiascope.select(H.length-1)}else{epidiascope.select(0)}}}};var epidiascope={picTitleId:"d_picTit",picMemoId:"d_picIntro",picListId:"efpPicListCont",BigPicId:"d_BigPic",picArrLeftId:"efpLeftArea",picArrRightId:"efpRightArea",playButtonId:"ecbPlay",statusId:"ecpPlayStatus",mainBoxId:"efpBigPic",repetition:false,prefetch:false,autoPlay:false,mode:"player",autoPlayTimeObj:null,timeSpeed:5,maxWidth:948,filmstrips:[],prefetchImg:[],selectedIndex:-1,previousPicList:{},nextPicList:{},loadTime:0,add:function(d){this.filmstrips.push(d);
if(this.prefetch){var c=new Image();c.src=d.src;this.prefetchImg.push(c)}},clearData:function(){this.selectedIndex=-1;this.filmstrips=[]},jsClickCallBack:function(a){this.select(a);if(this.filmstrips[a].callback){this.filmstrips[a].callback(this.filmstrips[a])
}},init:function(){var n=this;var m=0;if(this.filmstrips.length*110<picShow.ps(this.picListId).offsetWidth){m=Math.round(picShow.ps(this.picListId).offsetWidth/2-this.filmstrips.length*110/2)}var o='<div style="width:32760px;padding-left:'+m+'px;">',l;
for(l=0;l<this.filmstrips.length;l++){if(this.filmstrips[l].title!=""){o+='<div class="pic" style="padding:1px 0px;" id="slide_'+l+'"><table cellspacing="0" width="100%"><tr><td class="picCont"><table cellspacing="0"><tr><td class="pb_01"></td><td class="pb_02"></td><td class="pb_03"></td></tr><tr><td class="pb_04"></td><td><a href="javascript:epidiascope.jsClickCallBack('+l+');" onclick="this.blur();"><img style="width: 70px;height: 70px;" src="'+this.filmstrips[l].lowsrc_s+'" title="'+this.filmstrips[l].title+'" alt="'+this.filmstrips[l].title+'" oncontextmenu="event.returnValue=false;return false;" /></a></td><td class="pb_05"></td></tr><tr><td class="pb_06"></td><td class="pb_07"></td><td class="pb_08"></td></tr></table></td></tr><tr><td>'+(this.filmstrips[l].title.length>20?this.filmstrips[l].title.substring(0,20)+"...":this.filmstrips[l].title)+"</td></tr></table></div>"
}else{o+='<div class="pic" id="slide_'+l+'"><table cellspacing="0" width="98%"><tr><td class="picCont"><table cellspacing="0"><tr><td class="pb_01"></td><td class="pb_02"></td><td class="pb_03"></td></tr><tr><td class="pb_04"></td><td><a href="javascript:epidiascope.select('+l+');" onclick="this.blur();"><img style="width: 80px;height: 80px;" src="'+this.filmstrips[l].lowsrc_s+'" title="'+this.filmstrips[l].title+'" alt="'+this.filmstrips[l].title+'" oncontextmenu="event.returnValue=false;return false;" /></a></td><td class="pb_05"></td></tr><tr><td class="pb_06"></td><td class="pb_07"></td><td class="pb_08"></td></tr></table></td></tr><tr><td></td></tr></table></div>'
}}picShow.ps(this.picListId).innerHTML=o+"</div>";picShow.ps(this.picArrLeftId).onclick=function(){epidiascope.previous();epidiascope.stop()};picShow.ps(this.picArrRightId).onclick=function(){epidiascope.next();
epidiascope.stop()};if(window.location.href.indexOf("2010.")!=-1){this.autoPlay=true;picShow.ps(this.picArrRightId).onclick=function(){epidiascope.next();epidiascope.play()}}this.buttonNext=new epidiascope.Button("ecbNext");
this.buttonPre=new epidiascope.Button("ecbPre");this.buttonPlay=new epidiascope.Button("ecbPlay");this.buttonMode=new epidiascope.Button("ecbMode");this.buttonSpeed=new epidiascope.Button("ecbSpeed");this.buttonModeReturn=new epidiascope.Button("ecbModeReturn");
this.buttonPre.element.onclick=function(){epidiascope.previous();epidiascope.stop()};this.buttonNext.element.onclick=function(){epidiascope.next();epidiascope.stop()};this.buttonMode.element.onclick=function(){epidiascope.setMode("list")
};this.buttonModeReturn.element.onclick=function(){epidiascope.setMode("player")};this.BigImgBox=picShow.ps(this.BigPicId);this.BigImgBox.oncontextmenu=function(a){a=a?a:event;a.returnValue=false;return false
};this._imgLoad=function(){if(epidiascope.maxWidth==0){return}if(this.width>epidiascope.maxWidth){this.width=epidiascope.maxWidth}if(this.width<948){picShow.ps("d_BigPic").style.paddingTop="15px";this.style.border="1px solid #000"
}else{picShow.ps("d_BigPic").style.paddingTop="0px";this.style.border="none";this.style.borderBottom="1px solid #e5e6e6"}clearTimeout(n._hideBgTimeObj);picShow.ps("d_BigPic").className=""};this._preLoad=function(){var a=new Date().getTime();
var c=a-epidiascope.loadTime;if(c>5000){var b=new Image().src="http://roll.book.picShow.com.cn/interface/slow_log.php?time="+c+"&url="+encodeURIComponent(this.src)+"&t=1"}};this.createImg(this.filmstrips[0].src);
var k;var p=window.location.search.match(/img=(\d+)/i);if(p){p=p[1];k=0;for(var l=0,j=this.filmstrips.length;l<j;l++){if(parseInt(this.filmstrips[l]["id"])==parseInt(p)){k=l;break}}}else{k=window.location.hash.match(/p=(\d+)/i);
if(k){k=k[1]-1;if(k<0||k>=this.filmstrips.length){k=0}}else{k=0}}if(slide_data.slide.showCenter){this.select(k)}if(!picShow.isIE){this.BigImgBox.style.position="relative";this.BigImgBox.style.overflow="hidden"
}else{clearInterval(this._ieButHeiTimeObj);this._ieButHeiTimeObj=setInterval(function(){n.setPicButtonHeight()},300)}var i=picShow.ps("efpNextGroup").getElementsByTagName("a");picShow.ps("nextPicsBut").href=i[0].href;
picShow.ps("nextPicsBut").title=i[0].title;if(this.autoPlay){this.play()}else{this.stop()}if(this.onstart){this.onstart()}},initNot:function(){var n=this;var m=0;if(this.filmstrips.length*110<picShow.ps(this.picListId).offsetWidth){m=Math.round(picShow.ps(this.picListId).offsetWidth/2-this.filmstrips.length*110/2)
}var o='<div style="width:32760px;padding-left:'+m+'px;">',l;for(l=0;l<this.filmstrips.length;l++){o+='<div class="pic" id="slide_'+l+'"><table cellspacing="0"><tr><td class="picCont"><table cellspacing="0"><tr><td class="pb_01"></td><td class="pb_02"></td><td class="pb_03"></td></tr><tr><td class="pb_04"></td><td><a href="javascript:epidiascope.select('+l+');" onclick="this.blur();"><img src="'+this.filmstrips[l].lowsrc_s+'" alt="'+this.filmstrips[l].title+'"  onload="DrawImage(this);" oncontextmenu="event.returnValue=false;return false;" /></a></td><td class="pb_05"></td></tr><tr><td class="pb_06"></td><td class="pb_07"></td><td class="pb_08"></td></tr></table></td></tr></table></div>'
}picShow.ps(this.picListId).innerHTML=o+"</div>";this.createImg(this.filmstrips[0].src);var k;var p=window.location.search.match(/img=(\d+)/i);if(p){p=p[1];k=0;for(var l=0,j=this.filmstrips.length;l<j;
l++){if(parseInt(this.filmstrips[l]["id"])==parseInt(p)){k=l;break}}}else{k=window.location.hash.match(/p=(\d+)/i);if(k){k=k[1]-1;if(k<0||k>=this.filmstrips.length){k=0}}else{k=0}}this.select(k);setTimeout(function(){n.picList.foucsTo(k+1)
},500);if(!picShow.isIE){this.BigImgBox.style.position="relative";this.BigImgBox.style.overflow="hidden"}else{clearInterval(this._ieButHeiTimeObj);this._ieButHeiTimeObj=setInterval(function(){n.setPicButtonHeight()
},300)}this.listInitStatus=false;var i=picShow.ps("efpNextGroup").getElementsByTagName("a");picShow.ps("nextPicsBut").href=i[0].href;if(this.autoPlay){this.play()}else{this.stop()}if(this.onstart){this.onstart()
}},readTry:0,createImg:function(b){if(this.ImgObj1){this.BigImgBox.removeChild(this.ImgObj1)}this.ImgObj1=document.createElement("img");this.ImgObj1.onmousedown=function(){return false};this.ImgObj1.galleryImg=false;
this.ImgObj1.onload=this._imgLoad;if(b){this.ImgObj1.src=b}this.BigImgBox.appendChild(this.ImgObj1)},select:function(e,g){var f=this;if(this.endSelect.status==1){this.endSelect.close()}if(e==this.selectedIndex){return
}var h;if(e>=this.filmstrips.length||e<0){return}picShow.ps(this.picTitleId).innerHTML=this.filmstrips[e].title;picShow.ps(this.picMemoId).innerHTML=this.filmstrips[e].text;picShow.ps("d_BigPic").className="";
clearTimeout(this._hideBgTimeObj);this._hideBgTimeObj=setTimeout("picShow.ps('d_BigPic').className='loading'",500);this.createImg();if(this._timeOut){for(h=0;h<this._timeOut.length;h++){clearTimeout(this._timeOut[h])
}}this._timeOut=[];if(picShow.isIE){this.ImgObj1.src="http://i0.sinaimg.cn/dy/deco/2008/0331/yocc080331img/news_mj_005.gif";this.ImgObj1.filters[0].Apply();this.ImgObj1.src=this.filmstrips[e].src;this.ImgObj1.filters[0].Play()
}else{this.ImgObj1.style.opacity=0;this.ImgObj1.src=this.filmstrips[e].src;for(h=0;h<=3;h++){this._timeOut[h]=setTimeout("epidiascope.ImgObj1.style.opacity = "+h*0.3,h*100)}this._timeOut[h]=setTimeout("epidiascope.ImgObj1.style.opacity = 1;",4*100)
}if(picShow.ps("slide_"+this.selectedIndex)){picShow.ps("slide_"+this.selectedIndex).className="pic"}picShow.ps("slide_"+e).className="picOn";this.selectedIndex=e;this.picList.foucsTo(e+1);picShow.ps("total").innerHTML='(<span class="cC00">'+(e+1)+"</span>/"+this.filmstrips.length+")";
if(this.autoPlay){this.play()}if(!this.prefetch&&e<this.filmstrips.length-1){this.reLoad=new Image();this.reLoad.src=this.filmstrips[e+1].src;this.loadTime=new Date().getTime();this.reLoad.onload=this._preLoad
}},setPicButtonHeight:function(){picShow.ps(this.picArrLeftId).style.height=picShow.ps(this.picArrRightId).style.height=picShow.ps(this.picArrLeftId).parentNode.offsetHeight+"px"},setPageInfo:function(b){window.location.hash="p="+Math.round(b+1)
},next:function(e){var d=this.selectedIndex+1;if(d>=this.filmstrips.length){if(this.repetition){d=0}else{this.endSelect.open();return}}if(e=="auto"){var f=new Image();f.src=this.filmstrips[d].src;if(!f.complete){return
}}this.select(d,e)},previous:function(){var b=this.selectedIndex-1;if(b<0){if(this.repetition){b=this.filmstrips.length-1}else{return}}this.select(b)},play:function(){clearInterval(this.autoPlayTimeObj);
this.autoPlayTimeObj=setInterval("epidiascope.next('auto')",this.timeSpeed*1000);picShow.ps(this.playButtonId).onclick=function(){epidiascope.stop()};picShow.ps(this.statusId).className="stop";picShow.ps(this.statusId).title="��ͣ";
this.autoPlay=true},stop:function(){clearInterval(this.autoPlayTimeObj);picShow.ps(this.playButtonId).onclick=function(){epidiascope.play();epidiascope.next("auto")};picShow.ps(this.statusId).className="play";
picShow.ps(this.statusId).title="����";this.autoPlay=false},rePlay:function(){if(this.endSelect.status==1){this.endSelect.close()}this.autoPlay=true;this.select(0)},downloadPic:function(){var b=this.filmstrips[this.selectedIndex]
},setMode:function(b){this.speedBar.close();if(this.endSelect.status==1){this.endSelect.close()}if(b=="list"){if(!this.listInitStatus){this.listInit()}this.buttonSpeed.hide();this.buttonPlay.hide();this.buttonNext.hide();
this.buttonPre.hide();picShow.ps("ecbLine").style.visibility="hidden";this.buttonMode.element.style.display="none";this.buttonModeReturn.element.style.display="block";this.buttonModeReturn.rePosi();this.stop();
this.mode="list";this.listSelect(this.selectedIndex);picShow.ps("eFramePic").style.display="none";picShow.ps("ePicList").style.display="block";this.listView()}else{window.scroll(0,0);this.buttonSpeed.show();
this.buttonPlay.show();this.buttonNext.show();this.buttonPre.show();picShow.ps("ecbLine").style.visibility="visible";this.buttonMode.element.className="";this.buttonMode.element.style.display="block";this.buttonModeReturn.element.style.display="none";
this.mode="player";picShow.ps("eFramePic").style.display="block";picShow.ps("ePicList").style.display="none"}},switchMode:function(){if(this.mode=="list"){this.setMode("player")}else{this.setMode("list")
}},listData:null,listFrameId:"ePicList",listSelectedIndex:null,listSelect:function(b){if(b<0||b>=this.filmstrips.length){return}if(this.listSelectedIndex!==null){if(picShow.ps("picList_"+this.listSelectedIndex)){picShow.ps("picList_"+this.listSelectedIndex).className="picBox"
}}this.listSelectedIndex=b;if(picShow.ps("picList_"+this.listSelectedIndex)){picShow.ps("picList_"+this.listSelectedIndex).className="picBox selected"}},listInit:function(){var e=this;var f="";for(var d=0;
d<this.filmstrips.length;d++){f+='<div class="picBox" id="picList_'+d+'" onmousemove="epidiascope.listSelect('+d+')" onclick="epidiascope.select(epidiascope.listSelectedIndex);epidiascope.setMode(\'player\');"><table cellspacing="0"><tr><td><img style="width: 160px;height: 160px;" src="'+this.filmstrips[d].lowsrc_b+'" alt="" /></td></tr></table><h3>'+this.filmstrips[d].title+'</h3><p class="time">'+this.filmstrips[d].date+"</p></div>"
}picShow.ps(this.listFrameId).innerHTML=f;this.listInitStatus=true},listRowSize:4,listView:function(){var e=picShow.ps("picList_"+this.listSelectedIndex);var g=document.documentElement.clientHeight==0?document.body.clientHeight:document.documentElement.clientHeight;
var h=document.documentElement.scrollTop==0?document.body.scrollTop:document.documentElement.scrollTop;var f=picShow.absPosition(e,document.documentElement);if((f.top+(e.offsetHeight*0.3))<h||(f.top+(e.offsetHeight*0.7))>h+g){window.scroll(0,f.top-Math.round((g-e.offsetHeight)/2))
}},listMoveUp:function(){var b=this.listSelectedIndex-this.listRowSize;if(b<0){return}this.listSelect(b);this.listView()},listMoveDown:function(){var b=this.listSelectedIndex+this.listRowSize;if(b>=this.filmstrips.length){nweNum=this.filmstrips.length-1
}this.listSelect(b);this.listView()},listMoveLeft:function(){var b=this.listSelectedIndex-1;if(b<0){return}this.listSelect(b);this.listView()},listMoveRight:function(){var b=this.listSelectedIndex+1;if(b>=this.filmstrips.length){return
}this.listSelect(b);this.listView()}};epidiascope.speedBar={boxId:"SpeedBox",contId:"SpeedCont",slideId:"SpeedSlide",slideButtonId:"SpeedNonius",infoId:"ecbSpeedInfo",grades:10,grade:5,_slideHeight:112,_slideButtonHeight:9,_baseTop:4,_marginTop:0,_mouseDisparity:0,_showStep:0,_showType:"close",_showTimeObj:null,init:function(){var b=this;
this._marginTop=Math.round(this._slideHeight/this.grades*(this.grade-1));picShow.ps(this.slideButtonId).style.top=this._marginTop+this._baseTop+"px";picShow.ps(this.infoId).innerHTML=this.grade+"��";this.step=new picShow.Step();
this.step.element=picShow.ps(this.contId);this.step.limit=6;this.step.stepTime=20;this.step.classBase="speedStep_";this.step.onfirst=function(){epidiascope.buttonSpeed.setStatus("ok");picShow.ps(epidiascope.speedBar.boxId).style.display="none"
};picShow.ps(this.slideId).onselectstart=function(){return false};picShow.ps(this.slideButtonId).onmousedown=function(a){b.mouseDown(a);return false};picShow.ps(this.slideId).onmousedown=function(a){b.slideClick(a);
return false};epidiascope.buttonSpeed.element.onmousedown=function(){b.show();return false};epidiascope.buttonSpeed.element.onselectstart=function(){return false}},show:function(){if(this._showType=="close"){this.open()
}else{this.close()}},open:function(){var e=this;this._showType="open";var f=document.onmousedown;var d=function(a){a=window.event?event:a;if(a.stopPropagation){a.stopPropagation()}else{window.event.cancelBubble=true
}var b=a.target?a.target:a.srcElement;while(b!=picShow.ps(e.boxId)&&b!=epidiascope.buttonSpeed.element){if(b.parentNode){b=b.parentNode}else{break}}if(b==picShow.ps(e.boxId)||b==epidiascope.buttonSpeed.element){return
}else{e.close()}picShow.delEvent(document,"mousedown",d)};picShow.addEvent(document,"mousedown",d);epidiascope.buttonSpeed.setStatus("down");picShow.ps(this.boxId).style.display="block";this.step.action("+")
},close:function(){var b=this;this._showType="close";epidiascope.buttonSpeed.setStatus("ok");this.step.action("-")},slideClick:function(d){d=window.event?event:d;var c=d.layerY?d.layerY:d.offsetY;if(!c){return
}this._marginTop=c-Math.round(this._slideButtonHeight/2);if(this._marginTop<0){this._marginTop=0}this.grade=Math.round(this._marginTop/(this._slideHeight/this.grades)+1);picShow.ps(this.slideButtonId).style.top=this._marginTop+this._baseTop+"px";
picShow.ps(this.infoId).innerHTML=this.grade+"��";if(this.onend){this.onend()}},setGrade:function(b){this.grade=b;epidiascope.timeSpeed=this.grade;this._marginTop=Math.round(this._slideHeight/this.grades*(this.grade-1));
picShow.ps(this.slideButtonId).style.top=this._marginTop+this._baseTop+"px";picShow.ps(this.infoId).innerHTML=this.grade+"��";picShow.writeCookie("eSp",this.grade,720)},mouseDown:function(c){var d=this;
c=window.event?window.event:c;this._mouseDisparity=(c.pageY?c.pageY:c.clientY)-this._marginTop;document.onmousemove=function(a){d.mouseOver(a)};document.onmouseup=function(){d.mouseEnd()}},mouseOver:function(b){b=window.event?window.event:b;
this._marginTop=(b.pageY?b.pageY:b.clientY)-this._mouseDisparity;if(this._marginTop>(this._slideHeight-this._slideButtonHeight)){this._marginTop=this._slideHeight-this._slideButtonHeight}if(this._marginTop<0){this._marginTop=0
}picShow.ps(this.slideButtonId).style.top=this._marginTop+this._baseTop+"px";this.grade=Math.round(this._marginTop/(this._slideHeight/this.grades)+1);if(this.onmover){this.onmover()}},mouseEnd:function(){if(this.onend){this.onend()
}document.onmousemove=null;document.onmouseup=null},onmover:function(){picShow.ps(this.infoId).innerHTML=this.grade+"��"},onend:function(){picShow.writeCookie("eSp",this.grade,720);epidiascope.timeSpeed=this.grade;
if(epidiascope.autoPlay){epidiascope.play()}}};epidiascope.picList={leftArrId:"efpListLeftArr",rightArrId:"efpListRightArr",picListId:"efpPicListCont",timeoutObj:null,pageWidth:110,totalWidth:0,offsetWidth:0,lock:false,init:function(){picShow.ps(this.rightArrId).onmousedown=function(){epidiascope.picList.leftMouseDown()
};picShow.ps(this.rightArrId).onmouseout=function(){epidiascope.picList.leftEnd("out");this.className=""};picShow.ps(this.rightArrId).onmouseup=function(){epidiascope.picList.leftEnd("up")};picShow.ps(this.leftArrId).onmousedown=function(){epidiascope.picList.rightMouseDown()
};picShow.ps(this.leftArrId).onmouseout=function(){epidiascope.picList.rightEnd("out");this.className=""};picShow.ps(this.leftArrId).onmouseup=function(){epidiascope.picList.rightEnd("up")};this.totalWidth=epidiascope.filmstrips.length*this.pageWidth;
this.offsetWidth=picShow.ps(this.picListId).offsetWidth},leftMouseDown:function(){if(this.lock){return}this.lock=true;this.timeoutObj=setInterval("epidiascope.picList.moveLeft()",10)},rightMouseDown:function(){if(this.lock){return
}this.lock=true;this.timeoutObj=setInterval("epidiascope.picList.moveRight()",10)},moveLeft:function(){if(picShow.ps(this.picListId).scrollLeft+10>this.totalWidth-this.offsetWidth){picShow.ps(this.picListId).scrollLeft=this.totalWidth-this.offsetWidth;
this.leftEnd()}else{picShow.ps(this.picListId).scrollLeft+=10}},moveRight:function(){picShow.ps(this.picListId).scrollLeft-=10;if(picShow.ps(this.picListId).scrollLeft==0){this.rightEnd()}},leftEnd:function(b){if(b=="out"){if(!this.lock){return
}}clearInterval(this.timeoutObj);this.lock=false;this.move(30)},rightEnd:function(b){if(b=="out"){if(!this.lock){return}}clearInterval(this.timeoutObj);this.lock=false;this.move(-30)},foucsTo:function(d){if(this.lock){return
}this.lock=true;var c=Math.round(d*this.pageWidth-this.offsetWidth/2)-33;c-=picShow.ps(this.picListId).scrollLeft;if(picShow.ps(this.picListId).scrollLeft+c<0){c=-picShow.ps(this.picListId).scrollLeft}if(picShow.ps(this.picListId).scrollLeft+c>=this.totalWidth-this.offsetWidth){c=this.totalWidth-this.offsetWidth-picShow.ps(this.picListId).scrollLeft
}this.move(c)},move:function(d){var f=d/4;if(Math.abs(f)<1&&f!=0){f=(f>=0?1:-1)*1}else{f=Math.round(f)}var e=picShow.ps(this.picListId).scrollLeft+f;if(e<=0){picShow.ps(this.picListId).scrollLeft=0;this.lock=false;
return}if(e>=this.totalWidth-this.offsetWidth){picShow.ps(this.picListId).scrollLeft=this.totalWidth-this.offsetWidth;this.lock=false;return}picShow.ps(this.picListId).scrollLeft+=f;d-=f;if(Math.abs(d)<=1){this.lock=false;
return}else{setTimeout("epidiascope.picList.move("+d+")",10)}}};epidiascope.keyboard={_timeObj:null,status:"up",init:function(){var b=this;picShow.addEvent(document,"keydown",function(a){b.keyDown(a)});
picShow.addEvent(document,"keyup",function(a){b.keyUp(a)});this.step=new picShow.Step();this.step.element=picShow.ps("efpClew");this.step.limit=5;this.step.stepTime=30;this.step.classBase="efpClewStep_";
if(!this.closeObj){this.closeObj=document.createElement("span");this.closeObj.style.display="block";this.closeObj.id="efpClewClose";picShow.ps("efpClew").appendChild(this.closeObj);this.closeObj.onclick=function(){b.clewClose()
}}this.clewNum=parseInt(picShow.readCookie("eCn"));if(isNaN(this.clewNum)){this.clewNum=0}if(this.clewNum<5){this.clewOpen()}},clewClose:function(){this.step.action("-");picShow.writeCookie("eCn",6,24*7)
},clewOpen:function(){this.step.action("+")},keyDown:function(f){if(this.status=="down"){return}this.status="down";f=window.event?event:f;var d=f.target?f.target:f.srcElement;if(d.tagName=="INPUT"||d.tagName=="SELECT"||d.tagName=="TEXTAREA"){if(f.stopPropagation){f.stopPropagation()
}else{window.event.cancelBubble=true}return}var e=false;if(epidiascope.mode=="list"){if(f.keyCode==40){epidiascope.listMoveDown();e=true}if(f.keyCode==37){epidiascope.listMoveLeft();e=true}if(f.keyCode==38){epidiascope.listMoveUp();
e=true}if(f.keyCode==39){epidiascope.listMoveRight();e=true}if(f.keyCode==13){epidiascope.setMode("player");epidiascope.select(epidiascope.listSelectedIndex);e=true}}else{if(f.keyCode==39){epidiascope.next();
e=true;this.clewClose()}if(f.keyCode==37){epidiascope.previous();e=true;this.clewClose()}}if(f.keyCode==9){epidiascope.switchMode();e=true}if(e===true){if(f.preventDefault){f.preventDefault()}else{f.returnValue=false
}}},keyUp:function(){this.status="up"}};epidiascope.endSelect={endSelectId:"endSelect",closeId:"endSelClose",rePlayButId:"rePlayBut",status:0,open:function(){this.status=1;picShow.ps(this.endSelectId).style.display="block";
picShow.ps(this.endSelectId).style.left=Math.round((picShow.ps(epidiascope.mainBoxId).offsetWidth-picShow.ps(this.endSelectId).offsetWidth)/2)+"px";picShow.ps(this.endSelectId).style.top=Math.round((picShow.ps(epidiascope.mainBoxId).offsetHeight-picShow.ps(this.endSelectId).offsetHeight)/2)+"px";
epidiascope.stop();picShow.ps(epidiascope.playButtonId).onclick=function(){epidiascope.rePlay()};picShow.ps(this.closeId).onclick=function(){epidiascope.endSelect.close()};picShow.ps(this.rePlayButId).onclick=function(){epidiascope.rePlay()
}},close:function(){this.status=0;picShow.ps(this.endSelectId).style.display="none"}};epidiascope.onstart=function(){try{document.execCommand("BackgroundImageCache",false,true)}catch(b){}epidiascope.speedBar.grade=parseInt(picShow.readCookie("eSp"));
if(isNaN(epidiascope.speedBar.grade)){epidiascope.speedBar.grade=5}epidiascope.speedBar.init();epidiascope.speedBar.onend();epidiascope.picList.init();epidiascope.keyboard.init()};epidiascope.Button=function(c,d){this.status="ok";
this.id=c;this.classNameNum=d;this.init()};epidiascope.Button.prototype.init=function(){if(!picShow.ps(this.id)){return}var b=this;this.element=picShow.ps(this.id);if(this.element.offsetWidth==43){this.classNameNum="1"
}if(!this.classNameNum){this.classNameNum=""}this.mouseStatus="out";this.bgDiv=document.createElement("div");this.bgDiv.className="buttonBg"+this.classNameNum;this.element.parentNode.style.position="relative";
this.element.style.position="relative";this.element.style.zIndex="5";this.element.parentNode.appendChild(this.bgDiv);this.bgDiv.style.top=this.element.offsetTop-6+"px";this.bgDiv.style.left=this.element.offsetLeft-6+"px";
this.step=new picShow.Step();this.step.element=this.bgDiv;this.step.limit=3;this.step.stepTime=30;this.step.classBase="buttonBg"+this.classNameNum+" bBg"+this.classNameNum+"S_";picShow.addEvent(this.element,"mouseover",function(){b.mouseover()
});picShow.addEvent(this.element,"mouseout",function(){b.mouseout()});picShow.addEvent(this.element,"mousedown",function(){b.mousedown()});picShow.addEvent(this.element,"mouseup",function(){b.mouseup()
})};epidiascope.Button.prototype.rePosi=function(){this.bgDiv.style.top=this.element.offsetTop-6+"px";this.bgDiv.style.left=this.element.offsetLeft-6+"px"};epidiascope.Button.prototype.mouseover=function(){this.mouseStatus="in";
if(this.status!="down"){this.element.className="hover";this.step.action("+")}};epidiascope.Button.prototype.mouseout=function(){this.mouseStatus="out";if(this.status!="down"){this.element.className="";
this.step.action("-")}};epidiascope.Button.prototype.mouseup=function(){if(this.status=="down"){return}this.element.className="hover"};epidiascope.Button.prototype.mousedown=function(){if(this.status=="down"){return
}this.element.className="active"};epidiascope.Button.prototype.setStatus=function(b){switch(b){case"ok":this.status="ok";this.element.className="";if(this.mouseStatus=="in"){this.step.action("+")}else{this.step.action("-")
}break;case"down":this.status="down";this.step.action("-");this.element.className="active";break}};epidiascope.Button.prototype.hide=function(){this.element.style.visibility="hidden";this.bgDiv.style.visibility="hidden"
};epidiascope.Button.prototype.show=function(){this.element.style.visibility="visible";this.bgDiv.style.visibility="visible"};function DrawImage(h,f,i){var j=new Image();if(!f){f=90}if(!i){i=90}j.src=h.src;
if(j.width>0&&j.height>0){var g=true;if(j.width/j.height>=f/i){if(j.width>f){h.width=f;h.height=(j.height*f)/j.width}else{h.width=j.width;h.height=j.height}}else{if(j.height>i){h.height=i;h.width=(j.width*i)/j.height
}else{h.width=j.width;h.height=j.height}}}}function DivSelect(m,i,c){var k=this;k.id=m;k.divId=i;k.divClassName=c;k.selectObj=picShow.ps(k.id);if(!k.selectObj){return}var l=k;k.status="close";k.parentObj=k.selectObj.parentNode;
while(picShow.readStyle(k.parentObj,"display")!="block"){if(k.parentObj.parentNode){k.parentObj=k.parentObj.parentNode}else{break}}k.parentObj.style.position="relative";k.selectObjWidth=k.selectObj.offsetWidth;
k.selectObjHeight=k.selectObj.offsetHeight;k.selectPosition=picShow.absPosition(k.selectObj,k.parentObj);k.selectObj.style.visibility="hidden";k.divObj=document.createElement("div");k.divObj.id=k.divId;
if(k.divClassName){k.divObj.className=k.divClassName}k.parentObj.appendChild(k.divObj);k.divObj.style.width=k.selectObjWidth+"px";k.divObj.style.position="absolute";k.divObj.style.left=k.selectPosition.left+"px";
k.divObj.style.top=k.selectPosition.top+"px";k.divObj.onclick=function(){l.click()};k.divObj_count=document.createElement("div");k.divObj.appendChild(k.divObj_count);k.divObj_count.className="ds_cont";
k.divObj_title=document.createElement("div");k.divObj_count.appendChild(k.divObj_title);k.divObj_title.className="ds_title";k.divObj_button=document.createElement("div");k.divObj_count.appendChild(k.divObj_button);
k.divObj_button.className="ds_button";k.divObj_list=document.createElement("div");k.divObj.appendChild(k.divObj_list);k.divObj_list.className="ds_list";k.divObj_list.style.display="none";k.divObj_listCont=document.createElement("div");
k.divObj_list.appendChild(k.divObj_listCont);k.divObj_listCont.className="dsl_cont";k.list=[];var n;for(var j=0;j<k.selectObj.options.length;j++){n=document.createElement("p");k.list.push(n);k.divObj_listCont.appendChild(n);
n.innerHTML=k.selectObj.options[j].innerHTML;if(k.selectObj.selectedIndex==j){k.divObj_title.innerHTML=n.innerHTML}n.onmouseover=function(){this.className="selected"};n.onmouseout=function(){this.className=""
};n.onclick=function(){l.select(this.innerHTML)}}k.select=function(a){var d=this;for(var b=0;b<d.selectObj.options.length;b++){if(d.selectObj.options[b].innerHTML==a){d.selectObj.selectedIndex=b;if(d.selectObj.onchange){d.selectObj.onchange()
}d.divObj_title.innerHTML=a;break}}};k.clickClose=function(b){var a=b.target?b.target:event.srcElement;do{if(a==l.divObj){return}if(a.tagName=="BODY"){break}a=a.parentNode}while(a.parentNode);l.close()
};k.open=function(){var a=this;a.divObj_list.style.display="block";a.status="open";picShow.addEvent(document,"click",a.clickClose)};k.close=function(){var a=this;a.divObj_list.style.display="none";a.status="close";
picShow.delEvent(document,"click",a.clickClose)};k.click=function(){var a=this;if(a.status=="open"){a.close()}else{a.open()}}}function _initData(){getData.curUrl="";getData.fillData();window.scrollTo(0,40)
}function _checkShowOther(){var d=$("efpNextGroup");d.style.display="none";var e=$("efpPreGroup");e.style.display="none";var f=$("efpPicListCont");f.style.width=($("wrap").getWidth()-90)+"px"};