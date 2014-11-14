var SyntaxHighlighter=function(){if(typeof(require)!="undefined"&&typeof(XRegExp)=="undefined"){XRegExp=require("XRegExp").XRegExp}var l={defaults:{"class-name":"","first-line":1,"pad-line-numbers":false,highlight:null,title:null,"smart-tabs":true,"tab-size":4,gutter:true,toolbar:true,"quick-code":true,collapse:false,"auto-links":true,light:false,"html-script":false},config:{space:"&nbsp;",useScriptTags:true,bloggerMode:false,stripBrs:false,tagName:"pre",strings:{expandSource:"expand source",help:"?",alert:"SyntaxHighlighter\n\n",noBrush:"Can't find brush for: ",brushNotHtmlScript:"Brush wasn't configured for html-script option: ",aboutDialog:'<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml"><head><meta http-equiv="Content-Type" content="text/html; charset=utf-8" /><title>About SyntaxHighlighter</title></head><body style="font-family:Geneva,Arial,Helvetica,sans-serif;background-color:#fff;color:#000;font-size:1em;text-align:center;"><div style="text-align:center;margin-top:1.5em;"><div style="font-size:xx-large;">SyntaxHighlighter</div><div style="font-size:.75em;margin-bottom:3em;"><div>version 3.0.83 (July 02 2010)</div><div><a href="http://alexgorbatchev.com/SyntaxHighlighter" target="_blank" style="color:#005896">http://alexgorbatchev.com/SyntaxHighlighter</a></div><div>JavaScript code syntax highlighter.</div><div>Copyright 2004-2010 Alex Gorbatchev.</div></div><div>If you like this script, please <a href="https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=2930402" style="color:#005896">donate</a> to <br/>keep development active!</div></div></body></html>'}},vars:{discoveredBrushes:null,highlighters:{}},brushes:{},regexLib:{multiLineCComments:/\/\*[\s\S]*?\*\//gm,singleLineCComments:/\/\/.*$/gm,singleLinePerlComments:/#.*$/gm,doubleQuotedString:/"([^\\"\n]|\\.)*"/g,singleQuotedString:/'([^\\'\n]|\\.)*'/g,multiLineDoubleQuotedString:new XRegExp('"([^\\\\"]|\\\\.)*"',"gs"),multiLineSingleQuotedString:new XRegExp("'([^\\\\']|\\\\.)*'","gs"),xmlComments:/(&lt;|<)!--[\s\S]*?--(&gt;|>)/gm,url:/\w+:\/\/[\w-.\/?%&=:@;]*/g,phpScriptTags:{left:/(&lt;|<)\?=?/g,right:/\?(&gt;|>)/g},aspScriptTags:{left:/(&lt;|<)%=?/g,right:/%(&gt;|>)/g},scriptScriptTags:{left:/(&lt;|<)\s*script.*?(&gt;|>)/gi,right:/(&lt;|<)\/\s*script\s*(&gt;|>)/gi}},toolbar:{getHtml:function(L){var N='<div class="toolbar">',K=l.toolbar.items,P=K.list;
function O(R,Q){return l.toolbar.getButtonHtml(R,Q,l.config.strings[Q])}for(var M=0;M<P.length;M++){N+=(K[P[M]].getHtml||O)(L,P[M])}N+="</div>";return N},getButtonHtml:function(L,M,K){return'<span><a href="#" class="toolbar_item command_'+M+" "+M+'">'+K+"</a></span>"
},handler:function(P){var O=P.target,N=O.className||"";function K(R){var S=new RegExp(R+"_(\\w+)"),Q=S.exec(N);return Q?Q[1]:null}var L=s(B(O,".syntaxhighlighter").id),M=K("command");if(L&&M){l.toolbar.items[M].execute(L)
}P.preventDefault()},items:{list:["expandSource","help"],expandSource:{getHtml:function(K){if(K.getParam("collapse")!=true){return""}var L=K.getParam("title");return l.toolbar.getButtonHtml(K,"expandSource",L?L:l.config.strings.expandSource)
},execute:function(K){var L=I(K.id);j(L,"collapsed")}},help:{execute:function(K){var L=w("","_blank",500,250,"scrollbars=0"),M=L.document;M.write(l.config.strings.aboutDialog);M.close();L.focus()}}}},findElements:function(O,N){var Q=N?[N]:r(document.getElementsByTagName(l.config.tagName)),L=l.config,K=[];
if(L.useScriptTags){Q=Q.concat(J())}if(Q.length===0){return K}for(var M=0;M<Q.length;M++){var P={target:Q[M],params:A(O,o(Q[M].className))};if(P.params.brush==null){continue}K.push(P)}return K},highlight:function(P,N){var K=this.findElements(P,N),Q="innerHTML",U=null,S=l.config;
if(K.length===0){return}for(var O=0;O<K.length;O++){var N=K[O],R=N.target,M=N.params,V=M.brush,L;if(V==null){continue}if(M["html-script"]=="true"||l.defaults["html-script"]==true){U=new l.HtmlScript(V);
V="htmlscript"}else{var T=g(V);if(T){U=new T()}else{continue}}L=R[Q];if(S.useScriptTags){L=C(L)}if((R.title||"")!=""){M.title=R.title}M.brush=V;U.init(M);N=U.getDiv(L);if((R.id||"")!=""){N.id=R.id}R.parentNode.replaceChild(N,R)
}},all:function(K){f(window,"load",function(){l.highlight(K)})}};l.all=l.all;l.highlight=l.highlight;function D(L,K){return L.className.indexOf(K)!=-1}function t(L,K){if(!D(L,K)){L.className+=" "+K}}function j(L,K){L.className=L.className.replace(K,"")
}function r(M){var K=[];for(var L=0;L<M.length;L++){K.push(M[L])}return K}function v(K){return K.split("\n")}function E(L){var K="highlighter_";return L.indexOf(K)==0?L:K+L}function s(K){return l.vars.highlighters[E(K)]
}function I(K){return document.getElementById(E(K))}function n(K){l.vars.highlighters[E(K.id)]=K}function h(R,O,M){if(R==null){return null}var L=M!=true?R.childNodes:[R.parentNode],P={"#":"id",".":"className"}[O.substr(0,1)]||"nodeName",K,Q;
K=P!="nodeName"?O.substr(1):O.toUpperCase();if((R[P]||"").indexOf(K)!=-1){return R}for(var N=0;L&&N<L.length&&Q==null;N++){Q=h(L[N],O,M)}return Q}function B(L,K){return h(L,K,true)}function k(N,K,M){M=Math.max(M||0,0);
for(var L=M;L<N.length;L++){if(N[L]==K){return L}}return -1}function p(K){return(K||"")+Math.round(Math.random()*1000000).toString()}function A(N,M){var K={},L;for(L in N){K[L]=N[L]}for(L in M){K[L]=M[L]
}return K}function d(L){var K={"true":true,"false":false}[L];return K==null?L:K}function w(O,N,P,L,M){var K=(screen.width-P)/2,R=(screen.height-L)/2;M+=", left="+K+", top="+R+", width="+P+", height="+L;
M=M.replace(/^,/,"");var Q=window.open(O,N,M);Q.focus();return Q}function f(O,M,N,L){function K(P){P=P||window.event;if(!P.target){P.target=P.srcElement;P.preventDefault=function(){this.returnValue=false
}}N.call(L||window,P)}if(O.attachEvent){O.attachEvent("on"+M,K)}else{O.addEventListener(M,K,false)}}function y(K){window.alert(l.config.strings.alert+K)}function g(O,Q){var P=l.vars.discoveredBrushes,K=null;
if(P==null){P={};for(var M in l.brushes){var R=l.brushes[M],L=R.aliases;if(L==null){continue}R.brushName=M.toLowerCase();for(var N=0;N<L.length;N++){P[L[N]]=M}}l.vars.discoveredBrushes=P}K=l.brushes[P[O]];
if(K==null&&Q!=false){y(l.config.strings.noBrush+O)}return K}function H(M,N){var K=v(M);for(var L=0;L<K.length;L++){K[L]=N(K[L],L)}return K.join("\n")}function G(K){return K.replace(/^[ ]*[\n]+|[\n]*[ ]*$/g,"")
}function o(Q){var M,L={},N=new XRegExp("^\\[(?<values>(.*?))\\]$"),O=new XRegExp("(?<name>[\\w-]+)\\s*:\\s*(?<value>[\\w-%#]+|\\[.*?\\]|\".*?\"|'.*?')\\s*;?","g");while((M=O.exec(Q))!=null){var P=M.value.replace(/^['"]|['"]$/g,"");
if(P!=null&&N.test(P)){var K=N.exec(P);P=K.values.length>0?K.values.split(/\s*,\s*/):[]}L[M.name]=P}return L}function x(L,K){if(L==null||L.length==0||L=="\n"){return L}L=L.replace(/</g,"&lt;");L=L.replace(/ {2,}/g,function(M){var N="";
for(var O=0;O<M.length-1;O++){N+=l.config.space}return N+" "});if(K!=null){L=H(L,function(M){if(M.length==0){return""}var N="";M=M.replace(/^(&nbsp;| )+/,function(O){N=O;return""});if(M.length==0){return N
}return N+'<code class="'+K+'">'+M+"</code>"})}return L}function c(M,L){var K=M.toString();while(K.length<L){K="0"+K}return K}function F(M,N){var L="";for(var K=0;K<N;K++){L+=" "}return M.replace(/\t/g,L)
}function u(O,P){var K=v(O),N="\t",L="";for(var M=0;M<50;M++){L+="                    "}function Q(R,T,S){return R.substr(0,T)+L.substr(0,S)+R.substr(T+1,R.length)}O=H(O,function(R){if(R.indexOf(N)==-1){return R
}var T=0;while((T=R.indexOf(N))!=-1){var S=P-T%P;R=Q(R,T,S)}return R});return O}function i(L){var K=/<br\s*\/?>|&lt;br\s*\/?&gt;/gi;if(l.config.bloggerMode==true){L=L.replace(K,"\n")}if(l.config.stripBrs==true){L=L.replace(K,"")
}return L}function a(K){return K.replace(/^\s+|\s+$/g,"")}function z(R){var L=v(i(R)),Q=new Array(),O=/^\s*/,N=1000;for(var M=0;M<L.length&&N>0;M++){var K=L[M];if(a(K).length==0){continue}var P=O.exec(K);
if(P==null){return R}N=Math.min(P[0].length,N)}if(N>0){for(var M=0;M<L.length;M++){L[M]=L[M].substr(N)}}return L.join("\n")}function m(L,K){if(L.index<K.index){return -1}else{if(L.index>K.index){return 1
}else{if(L.length<K.length){return -1}else{if(L.length>K.length){return 1}}}}return 0}function q(O,Q){function R(S,T){return S[0]}var M=0,L=null,P=[],N=Q.func?Q.func:R;while((L=Q.regex.exec(O))!=null){var K=N(L,Q);
if(typeof(K)=="string"){K=[new l.Match(K,L.index,Q.css)]}P=P.concat(K)}return P}function b(L){var K=/(.*)((&gt;|&lt;).*)/;return L.replace(l.regexLib.url,function(M){var O="",N=null;if(N=K.exec(M)){M=N[1];
O=N[2]}return'<a href="'+M+'">'+M+"</a>"+O})}function J(){var L=document.getElementsByTagName("script"),K=[];for(var M=0;M<L.length;M++){if(L[M].type=="syntaxhighlighter"){K.push(L[M])}}return K}function C(N){var P="<![CDATA[",M="]]>",R=a(N),Q=false,L=P.length,O=M.length;
if(R.indexOf(P)==0){R=R.substring(L);Q=true}var K=R.length;if(R.indexOf(M)==K-O){R=R.substring(0,K-O);Q=true}return Q?R:N}function e(O){var P=O.target,N=B(P,".syntaxhighlighter"),K=B(P,".container"),R=document.createElement("textarea"),Q;
if(!K||!N||h(K,"textarea")){return}Q=s(N.id);t(N,"source");var S=K.childNodes,L=[];for(var M=0;M<S.length;M++){L.push(S[M].innerText||S[M].textContent)}L=L.join("\r");R.appendChild(document.createTextNode(L));
K.appendChild(R);R.focus();R.select();f(R,"blur",function(T){R.parentNode.removeChild(R);j(N,"source")})}l.Match=function(M,K,L){this.value=M;this.index=K;this.length=M.length;this.css=L;this.brushName=null
};l.Match.prototype.toString=function(){return this.value};l.HtmlScript=function(O){var S=g(O),R,K=new l.brushes.Xml(),Q=null,M=this,N="getDiv getHtml init".split(" ");if(S==null){return}R=new S();for(var P=0;
P<N.length;P++){(function(){var U=N[P];M[U]=function(){return K[U].apply(K,arguments)}})()}if(R.htmlScript==null){y(l.config.strings.brushNotHtmlScript+O);return}K.regexList.push({regex:R.htmlScript.code,func:L});
function T(V,W){for(var U=0;U<V.length;U++){V[U].index+=W}}function L(ab,V){var U=ab.code,aa=[],Z=R.regexList,X=ab.index+ab.left.length,ac=R.htmlScript,ad;for(var Y=0;Y<Z.length;Y++){ad=q(U,Z[Y]);T(ad,X);
aa=aa.concat(ad)}if(ac.left!=null&&ab.left!=null){ad=q(ab.left,ac.left);T(ad,ab.index);aa=aa.concat(ad)}if(ac.right!=null&&ab.right!=null){ad=q(ab.right,ac.right);T(ad,ab.index+ab[0].lastIndexOf(ab.right));
aa=aa.concat(ad)}for(var W=0;W<aa.length;W++){aa[W].brushName=S.brushName}return aa}};l.Highlighter=function(){};l.Highlighter.prototype={getParam:function(M,L){var K=this.params[M];return d(K==null?L:K)
},create:function(K){return document.createElement(K)},findMatches:function(N,M){var K=[];if(N!=null){for(var L=0;L<N.length;L++){if(typeof(N[L])=="object"){K=K.concat(q(M,N[L]))}}}return this.removeNestedMatches(K.sort(m))
},removeNestedMatches:function(O){for(var N=0;N<O.length;N++){if(O[N]===null){continue}var K=O[N],M=K.index+K.length;for(var L=N+1;L<O.length&&O[N]!==null;L++){var P=O[L];if(P===null){continue}else{if(P.index>M){break
}else{if(P.index==K.index&&P.length>K.length){O[N]=null}else{if(P.index>=K.index&&P.index<M){O[L]=null}}}}}}return O},figureOutLineNumbers:function(M){var K=[],L=parseInt(this.getParam("first-line"));H(M,function(N,O){K.push(O+L)
});return K},isLineHighlighted:function(K){var L=this.getParam("highlight",[]);if(typeof(L)!="object"&&L.push==null){L=[L]}return k(L,K.toString())!=-1},getLineHtml:function(N,K,M){var L=["line","number"+K,"index"+N,"alt"+(K%2==0?1:2).toString()];
if(this.isLineHighlighted(K)){L.push("highlighted")}if(K==0){L.push("break")}return'<div class="'+L.join(" ")+'">'+M+"</div>"},getLineNumbersHtml:function(Q,L){var O="",P=v(Q).length,M=parseInt(this.getParam("first-line")),R=this.getParam("pad-line-numbers");
if(R==true){R=(M+P-1).toString().length}else{if(isNaN(R)==true){R=0}}for(var N=0;N<P;N++){var K=L?L[N]:M+N,Q=K==0?l.config.space:c(K,R);O+=this.getLineHtml(N,K,Q)}return O},getCodeLinesHtml:function(N,R){N=a(N);
var T=v(N),O=this.getParam("pad-line-numbers"),Q=parseInt(this.getParam("first-line")),N="",S=this.getParam("brush");for(var M=0;M<T.length;M++){var U=T[M],K=/^(&nbsp;|\s)+/.exec(U),P=null,L=R?R[M]:Q+M;
if(K!=null){P=K[0].toString();U=U.substr(P.length);P=P.replace(" ",l.config.space)}U=a(U);if(U.length==0){U=l.config.space}N+=this.getLineHtml(M,L,(P!=null?'<code class="'+S+' spaces">'+P+"</code>":"")+U)
}return N},getTitleHtml:function(K){return K?"<caption>"+K+"</caption>":""},getMatchesHtml:function(K,O){var Q=0,S="",R=this.getParam("brush","");function M(U){var T=U?(U.brushName||R):R;return T?T+" ":""
}for(var N=0;N<O.length;N++){var P=O[N],L;if(P===null||P.length===0){continue}L=M(P);S+=x(K.substr(Q,P.index-Q),L+"plain")+x(P.value,L+P.css);Q=P.index+P.length+(P.offset||0)}S+=x(K.substr(Q),M()+"plain");
return S},getHtml:function(N){var M="",L=["syntaxhighlighter"],P,O,K;if(this.getParam("light")==true){this.params.toolbar=this.params.gutter=false}className="syntaxhighlighter";if(this.getParam("collapse")==true){L.push("collapsed")
}if((gutter=this.getParam("gutter"))==false){L.push("nogutter")}L.push(this.getParam("class-name"));L.push(this.getParam("brush"));N=G(N).replace(/\r/g," ");P=this.getParam("tab-size");N=this.getParam("smart-tabs")==true?u(N,P):F(N,P);
N=z(N);if(gutter){K=this.figureOutLineNumbers(N)}O=this.findMatches(this.regexList,N);M=this.getMatchesHtml(N,O);M=this.getCodeLinesHtml(M,K);if(this.getParam("auto-links")){M=b(M)}if(typeof(navigator)!="undefined"&&navigator.userAgent&&navigator.userAgent.match(/MSIE/)){L.push("ie")
}M='<div id="'+E(this.id)+'" class="'+L.join(" ")+'">'+(this.getParam("toolbar")?l.toolbar.getHtml(this):"")+'<table border="0" cellpadding="0" cellspacing="0">'+this.getTitleHtml(this.getParam("title"))+"<tbody><tr>"+(gutter?'<td class="gutter">'+this.getLineNumbersHtml(N)+"</td>":"")+'<td class="code"><div class="container">'+M+"</div></td></tr></tbody></table></div>";
return M},getDiv:function(K){if(K===null){K=""}this.code=K;var L=this.create("div");L.innerHTML=this.getHtml(K);if(this.getParam("toolbar")){f(h(L,".toolbar"),"click",l.toolbar.handler)}if(this.getParam("quick-code")){f(h(L,".code"),"dblclick",e)
}return L},init:function(K){this.id=p();n(this);this.params=A(l.defaults,K||{});if(this.getParam("light")==true){this.params.toolbar=this.params.gutter=false}},getKeywords:function(K){K=K.replace(/^\s+|\s+$/g,"").replace(/\s+/g,"|");
return"\\b(?:"+K+")\\b"},forHtmlScript:function(K){this.htmlScript={left:{regex:K.left,css:"script"},right:{regex:K.right,css:"script"},code:new XRegExp("(?<left>"+K.left.source+")(?<code>.*?)(?<right>"+K.right.source+")","sgi")}
}};return l}();typeof(exports)!="undefined"?exports.SyntaxHighlighter=SyntaxHighlighter:null;