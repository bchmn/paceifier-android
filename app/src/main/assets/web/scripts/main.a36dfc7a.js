(function (root, factory) {
  if (typeof define === 'function' && define.amd) {
    define(factory);
  } else if (typeof exports === 'object') {
    module.exports = factory(require, exports, module);
  } else {
    (root || window).CountUp = factory();
  }
}(this, function (require, exports, module) {

  /*

   countUp.js
   by @inorganik

   */

// target = id of html element or var of previously selected html element where counting occurs
// startVal = the value you want to begin at
// endVal = the value you want to arrive at
// decimals = number of decimal places, default 0
// duration = duration of animation in seconds, default 2
// options = optional object of options (see below)

  var CountUp = function (target, startVal, endVal, decimals, duration, options) {

    // make sure requestAnimationFrame and cancelAnimationFrame are defined
    // polyfill for browsers without native support
    // by Opera engineer Erik Möller
    var lastTime = 0;
    var vendors = ['webkit', 'moz', 'ms', 'o'];
    for (var x = 0; x < vendors.length && !window.requestAnimationFrame; ++x) {
      window.requestAnimationFrame = window[vendors[x] + 'RequestAnimationFrame'];
      window.cancelAnimationFrame =
        window[vendors[x] + 'CancelAnimationFrame'] || window[vendors[x] + 'CancelRequestAnimationFrame'];
    }
    if (!window.requestAnimationFrame) {
      window.requestAnimationFrame = function (callback, element) {
        var currTime = new Date().getTime();
        var timeToCall = Math.max(0, 16 - (currTime - lastTime));
        var id = window.setTimeout(function () {
            callback(currTime + timeToCall);
          },
          timeToCall);
        lastTime = currTime + timeToCall;
        return id;
      };
    }
    if (!window.cancelAnimationFrame) {
      window.cancelAnimationFrame = function (id) {
        clearTimeout(id);
      };
    }

    // default options
    this.options = {
      useEasing: true, // toggle easing
      useGrouping: true, // 1,000,000 vs 1000000
      separator: ',', // character to use as a separator
      decimal: '.' // character to use as a decimal
    };
    // extend default options with passed options object
    for (var key in options) {
      if (options.hasOwnProperty(key)) {
        this.options[key] = options[key];
      }
    }
    if (this.options.separator === '') this.options.useGrouping = false;
    if (!this.options.prefix) this.options.prefix = '';
    if (!this.options.suffix) this.options.suffix = '';

    this.d = (typeof target === 'string') ? document.getElementById(target) : target;
    this.startVal = Number(startVal);
    this.endVal = Number(endVal);
    this.countDown = (this.startVal > this.endVal);
    this.frameVal = this.startVal;
    this.decimals = Math.max(0, decimals || 0);
    this.dec = Math.pow(10, this.decimals);
    this.duration = Number(duration) * 1000 || 2000;
    var self = this;

    this.version = function () {
      return '1.6.0';
    };

    // Print value to target
    this.printValue = function (value) {
      var result = (!isNaN(value)) ? self.formatNumber(value) : '--';
      if (self.d.tagName == 'INPUT') {
        this.d.value = result;
      }
      else if (self.d.tagName == 'text') {
        this.d.textContent = result;
      }
      else {
        this.d.innerHTML = result;
      }
    };

    // Robert Penner's easeOutExpo
    this.easeOutExpo = function (t, b, c, d) {
      return c * (-Math.pow(2, -10 * t / d) + 1) * 1024 / 1023 + b;
    };
    this.count = function (timestamp) {

      if (!self.startTime) self.startTime = timestamp;

      self.timestamp = timestamp;

      var progress = timestamp - self.startTime;
      self.remaining = self.duration - progress;

      // to ease or not to ease
      if (self.options.useEasing) {
        if (self.countDown) {
          self.frameVal = self.startVal - self.easeOutExpo(progress, 0, self.startVal - self.endVal, self.duration);
        } else {
          self.frameVal = self.easeOutExpo(progress, self.startVal, self.endVal - self.startVal, self.duration);
        }
      } else {
        if (self.countDown) {
          self.frameVal = self.startVal - ((self.startVal - self.endVal) * (progress / self.duration));
        } else {
          self.frameVal = self.startVal + (self.endVal - self.startVal) * (progress / self.duration);
        }
      }

      // don't go past endVal since progress can exceed duration in the last frame
      if (self.countDown) {
        self.frameVal = (self.frameVal < self.endVal) ? self.endVal : self.frameVal;
      } else {
        self.frameVal = (self.frameVal > self.endVal) ? self.endVal : self.frameVal;
      }

      // decimal
      self.frameVal = Math.round(self.frameVal * self.dec) / self.dec;

      // format and print value
      self.printValue(self.frameVal);

      // whether to continue
      if (progress < self.duration) {
        self.rAF = requestAnimationFrame(self.count);
      } else {
        if (self.callback) self.callback();
      }
    };
    // start your animation
    this.start = function (callback) {
      self.callback = callback;
      self.rAF = requestAnimationFrame(self.count);
      return false;
    };
    // toggles pause/resume animation
    this.pauseResume = function () {
      if (!self.paused) {
        self.paused = true;
        cancelAnimationFrame(self.rAF);
      } else {
        self.paused = false;
        delete self.startTime;
        self.duration = self.remaining;
        self.startVal = self.frameVal;
        requestAnimationFrame(self.count);
      }
    };
    // reset to startVal so animation can be run again
    this.reset = function () {
      self.paused = false;
      delete self.startTime;
      self.startVal = startVal;
      cancelAnimationFrame(self.rAF);
      self.printValue(self.startVal);
    };
    // pass a new endVal and start animation
    this.update = function (newEndVal) {
      cancelAnimationFrame(self.rAF);
      self.paused = false;
      delete self.startTime;
      self.startVal = self.frameVal;
      self.endVal = Number(newEndVal);
      self.countDown = (self.startVal > self.endVal);
      self.rAF = requestAnimationFrame(self.count);
    };
    this.formatNumber = function (nStr) {
      nStr = nStr.toFixed(self.decimals);
      nStr += '';
      var x, x1, x2, rgx;
      x = nStr.split('.');
      x1 = x[0];
      x2 = x.length > 1 ? self.options.decimal + x[1] : '';
      rgx = /(\d+)(\d{3})/;
      if (self.options.useGrouping) {
        while (rgx.test(x1)) {
          x1 = x1.replace(rgx, '$1' + self.options.separator + '$2');
        }
      }
      return self.options.prefix + x1 + x2 + self.options.suffix;
    };

    // format startVal on initialization
    self.printValue(self.startVal);
  };

// Example:
// var numAnim = new countUp("SomeElementYouWantToAnimate", 0, 99.99, 2, 2.5);
// numAnim.start();
// numAnim.update(135);
// with optional callback:
// numAnim.start(someMethodToCallOnComplete);

  return CountUp;

}));

var counter;
var speedometer;
var acceleration;

var points = 0;
var last_acc = 0;
var combo = 1;
var comboPoints = 0;
var comboStartTime = new Date();
var comboPercent = 0;
var lastSplash = (new Date()).getTime();

var inAnimation = 'zoomInDown';
var outAnimation = 'fadeOut';

window.speed = 0;
window.acc = 0;
window.speedLimit = 70;
window.pointsPerKm = 20;

window.onload = (function () {
  setTimeout(function () {
    countUpInit();
    speedometerInit();

    setInterval(function () {
      var rndSpeed = parseInt(Math.random() * 100);
      var rndAcc = parseInt(Math.random() * 10);

      checkSpeed(window.speed);
      checkAccelaration(rndAcc);

      speedLimitInit();

    }, Math.max((700 - combo * 100), 100));

  }, 500);

});

function speedLimitInit() {
  $('#speedLimit').html(window.speedLimit);
}

function countUpInit() {
  var options = {
    useEasing: false,
    useGrouping: true,
    separator: ',',
    decimal: '.'
  };
  counter = new CountUp("counter", 0, 0, 2, 1, options);
  counter.start();
}

function speedometerInit() {
  speedometer = $('.radial-progress');
}

function setSpeedometer(speed) {
  speed = Math.min(140, speed);
  var speedPercentage = Number(speed / 140) * 100;
  speedometer.attr('data-progress', speedPercentage);
  speedometer.find('.numbers span').html(speed);
}

function setPoints(points) {

  if (points > window.points) {
    //getting more points = good driver
    counter.update(points);
    $('#counter').removeClass('bad');
    $('#counter').addClass('good');
    pulse();
  } else if (points <= window.points && speed > 0) {
    //getting lerss points = bad driver

    $('#counter').removeClass('good');
    $('#counter').addClass('bad');

    combo = 0;
    $('#combo').html('');

    counter.update(points);
    drop();

  }

  window.points = points;
}

function checkSpeed() {
  var speed = window.speed;
  setSpeedometer(speed);

  //check speed limit and give points
  if (speed <= window.speedLimit) {
    //good job
    var kmPerSec = window.speed / 60 / 60;
    comboPoints += kmPerSec * pointsPerKm * combo / 5;
  } else {
    showSplash('bad', 1);
    comboPoints -= (speed - window.speedLimit);
  }
  comboPoints = Math.max(0, (comboPoints));
  setPoints(comboPoints);

  //check if combo if there
  if ((speed > 0) && (speed <= window.speedLimit)) {
    var comboTime = ((new Date()).getTime() - comboStartTime.getTime()) / 1000;
    comboPercent = (comboTime / (combo * 4));
    $('#combo-bar').css('width', parseInt(comboPercent * 100) + '%');
    if (comboTime > (combo * 4)) {
      //next stage!
      comboStartTime = new Date();
      if (combo <= 5) combo++;
      showSplash('good', combo);
      $('#combo').html("x" + String(combo));
      $('#next-combo').html("x" + String(combo + 1));
      $('#counter').removeClass('combo-' + (combo - 1));
      $('#counter').addClass('combo-' + combo);
    }
  }
}

function showSplash(status, points) {

  var now = (new Date()).getTime();

  if (now - lastSplash < 3000) return;

  lastSplash = now;

  var splash = $('.splash');
  var splash_text = $('.splash .text');
  var splash_score = $('.splash .score');
  var txt = '';
  var clss = '';
  switch (status) {
    case 'bad':
      var arr = ["Slow Down!", "What's The Rush?!", "No! No! No!"];
      var rnd = parseInt(Math.random() * arr.length);
      txt = arr[rnd];
      splash_score.html("-" + String(points));
      clss = 'red';
      points = Math.max(0, (comboPoints - points));
      counter.update(points);

      break;
    case 'boom':
      var arr = ["Boom!", "Are You Alive??", "Careful!"];
      var rnd = parseInt(Math.random() * arr.length);
      txt = arr[rnd];
      splash_score.html("-" + String(points));
      clss = 'red';
      points = Math.max(0, (comboPoints - points));
      counter.update(points);

      break;
    case 'good':
      var arr = ["Doing Great!", "Nice Job!", "Great!"];
      var rnd = parseInt(Math.random() * arr.length);
      txt = arr[rnd];
      splash_score.html("x" + String(points));
      clss = 'yellow';
      //comboPoints += points;
      //counter.update(comboPoints + points);
      break;
    default:
      clss = 'yellow';
      splash_text.html(status);
  }

  splash_text.html(txt);
  splash.removeClass('red');
  splash.removeClass('green');
  splash.removeClass('yellow');
  splash.addClass(clss);

  splash.removeClass(outAnimation);
  splash.removeClass(inAnimation);
  splash.addClass(inAnimation);
  setTimeout(function () {
    splash.removeClass(inAnimation);
    splash.addClass(outAnimation);
  }, 1700);
}

function checkAccelaration() {
  if (window.acc > 1.5) {
    var cont = $('.container');
    cont.removeClass('pulse');
    showSplash('boom', 20);
    setTimeout(function () {
      cont.addClass('pulse');
    }, 100);
  }
  //console.log('acceleration', acceleration);
}

function pulse() {

  clearAnimation();
  setTimeout(function () {
    $('#counter').addClass('pulse')
  }, 100);
}

function drop() {

  clearAnimation();
  setTimeout(function () {

    $('#counter').addClass('tada')

  }, 100);

}

function clearAnimation() {
  $('#counter').removeClass('pulse');
  $('#counter').removeClass('tada');
}
