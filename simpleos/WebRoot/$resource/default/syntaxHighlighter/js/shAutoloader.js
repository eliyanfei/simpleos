(function(){var a=SyntaxHighlighter;a.autoloader=function(){var m=arguments,c=a.findElements(),l={},h={},o=SyntaxHighlighter.all,d=false,n=null,j;SyntaxHighlighter.all=function(i){n=i;d=true};function q(r,s){for(var t=0;
t<r.length;t++){l[r[t]]=s}}function f(i){return i.pop?i:i.split(/\s+/)}for(j=0;j<m.length;j++){var g=f(m[j]),e=g.pop();q(g,e)}for(j=0;j<c.length;j++){var e=l[c[j].params.brush];if(!e){continue}h[e]=false;
b(e);if(c[j].params["html-script"]=="true"){var k=e.replace(e.substring(e.lastIndexOf("/")+1),"shBrushXml.js");if(!h[k]){b(k)}}}function b(s){var r=document.createElement("script"),i=false;r.src=s;r.type="text/javascript";
r.language="javascript";r.onload=r.onreadystatechange=function(){if(!i&&(!this.readyState||this.readyState=="loaded"||this.readyState=="complete")){i=true;h[s]=true;p();r.onload=r.onreadystatechange=null;
r.parentNode.removeChild(r)}};document.body.appendChild(r)}function p(){for(var i in h){if(h[i]==false){return}}if(d){SyntaxHighlighter.highlight(n)}}}})();