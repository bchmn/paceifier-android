function countUpInit(){var a={useEasing:!0,useGrouping:!0,separator:",",decimal:"."};counter=new CountUp("counter",0,0,3,1,a),counter.start()}function speedometerInit(){speedometer=$(".radial-progress")}function setSpeed(a){counter.update(a),speedometer.attr("data-progress",a),speedometer.find(".numbers span").html(a)}function setAccelaration(){console.log("acceleration",acceleration)}var counter,speedometer,acceleration;window.onload=function(){setTimeout(function(){countUpInit(),speedometerInit(),setInterval(function(){var a=(parseInt(100*Math.random()),parseInt(10*Math.random()));setSpeed(window.speed),setAccelaration(a)},1e3)},500)},function(a,b){"function"==typeof define&&define.amd?define(b):"object"==typeof exports?module.exports=b(require,exports,module):(a||window).CountUp=b()}(this,function(){var a=function(a,b,c,d,e,f){for(var g=0,h=["webkit","moz","ms","o"],i=0;i<h.length&&!window.requestAnimationFrame;++i)window.requestAnimationFrame=window[h[i]+"RequestAnimationFrame"],window.cancelAnimationFrame=window[h[i]+"CancelAnimationFrame"]||window[h[i]+"CancelRequestAnimationFrame"];window.requestAnimationFrame||(window.requestAnimationFrame=function(a){var b=(new Date).getTime(),c=Math.max(0,16-(b-g)),d=window.setTimeout(function(){a(b+c)},c);return g=b+c,d}),window.cancelAnimationFrame||(window.cancelAnimationFrame=function(a){clearTimeout(a)}),this.options={useEasing:!0,useGrouping:!0,separator:",",decimal:"."};for(var j in f)f.hasOwnProperty(j)&&(this.options[j]=f[j]);""===this.options.separator&&(this.options.useGrouping=!1),this.options.prefix||(this.options.prefix=""),this.options.suffix||(this.options.suffix=""),this.d="string"==typeof a?document.getElementById(a):a,this.startVal=Number(b),this.endVal=Number(c),this.countDown=this.startVal>this.endVal,this.frameVal=this.startVal,this.decimals=Math.max(0,d||0),this.dec=Math.pow(10,this.decimals),this.duration=1e3*Number(e)||2e3;var k=this;this.version=function(){return"1.6.0"},this.printValue=function(a){var b=isNaN(a)?"--":k.formatNumber(a);"INPUT"==k.d.tagName?this.d.value=b:"text"==k.d.tagName?this.d.textContent=b:this.d.innerHTML=b},this.easeOutExpo=function(a,b,c,d){return c*(-Math.pow(2,-10*a/d)+1)*1024/1023+b},this.count=function(a){k.startTime||(k.startTime=a),k.timestamp=a;var b=a-k.startTime;k.remaining=k.duration-b,k.frameVal=k.options.useEasing?k.countDown?k.startVal-k.easeOutExpo(b,0,k.startVal-k.endVal,k.duration):k.easeOutExpo(b,k.startVal,k.endVal-k.startVal,k.duration):k.countDown?k.startVal-(k.startVal-k.endVal)*(b/k.duration):k.startVal+(k.endVal-k.startVal)*(b/k.duration),k.frameVal=k.countDown?k.frameVal<k.endVal?k.endVal:k.frameVal:k.frameVal>k.endVal?k.endVal:k.frameVal,k.frameVal=Math.round(k.frameVal*k.dec)/k.dec,k.printValue(k.frameVal),b<k.duration?k.rAF=requestAnimationFrame(k.count):k.callback&&k.callback()},this.start=function(a){return k.callback=a,k.rAF=requestAnimationFrame(k.count),!1},this.pauseResume=function(){k.paused?(k.paused=!1,delete k.startTime,k.duration=k.remaining,k.startVal=k.frameVal,requestAnimationFrame(k.count)):(k.paused=!0,cancelAnimationFrame(k.rAF))},this.reset=function(){k.paused=!1,delete k.startTime,k.startVal=b,cancelAnimationFrame(k.rAF),k.printValue(k.startVal)},this.update=function(a){cancelAnimationFrame(k.rAF),k.paused=!1,delete k.startTime,k.startVal=k.frameVal,k.endVal=Number(a),k.countDown=k.startVal>k.endVal,k.rAF=requestAnimationFrame(k.count)},this.formatNumber=function(a){a=a.toFixed(k.decimals),a+="";var b,c,d,e;if(b=a.split("."),c=b[0],d=b.length>1?k.options.decimal+b[1]:"",e=/(\d+)(\d{3})/,k.options.useGrouping)for(;e.test(c);)c=c.replace(e,"$1"+k.options.separator+"$2");return k.options.prefix+c+d+k.options.suffix},k.printValue(k.startVal)};return a});