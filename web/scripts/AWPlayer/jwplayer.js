if (typeof jwplayer == "undefined") {
    var jwplayer = function (a) {
        if (jwplayer.api) {
            return jwplayer.api.selectPlayer(a)
        }
    };
    var $jw = jwplayer;
    jwplayer.version = "5.10.2295";
    jwplayer.vid = document.createElement("video");
    jwplayer.audio = document.createElement("audio");
    jwplayer.source = document.createElement("source");
    (function (b) {
        b.utils = function () {
        };
        b.utils.typeOf = function (d) {
            var c = typeof d;
            if (c === "object") {
                if (d) {
                    if (d instanceof Array) {
                        c = "array"
                    }
                } else {
                    c = "null"
                }
            }
            return c
        };
        b.utils.extend = function () {
            var c = b.utils.extend["arguments"];
            if (c.length > 1) {
                for (var e = 1; e < c.length; e++) {
                    for (var d in c[e]) {
                        c[0][d] = c[e][d]
                    }
                }
                return c[0]
            }
            return null
        };
        b.utils.clone = function (f) {
            var c;
            var d = b.utils.clone["arguments"];
            if (d.length == 1) {
                switch (b.utils.typeOf(d[0])) {
                    case"object":
                        c = {};
                        for (var e in d[0]) {
                            c[e] = b.utils.clone(d[0][e])
                        }
                        break;
                    case"array":
                        c = [];
                        for (var e in d[0]) {
                            c[e] = b.utils.clone(d[0][e])
                        }
                        break;
                    default:
                        return d[0];
                        break
                }
            }
            return c
        };
        b.utils.extension = function (c) {
            if (!c) {
                return""
            }
            c = c.substring(c.lastIndexOf("/") + 1, c.length);
            c = c.split("?")[0];
            if (c.lastIndexOf(".") > -1) {
                return c.substr(c.lastIndexOf(".") + 1, c.length).toLowerCase()
            }
            return
        };
        b.utils.html = function (c, d) {
            c.innerHTML = d
        };
        b.utils.wrap = function (c, d) {
            if (c.parentNode) {
                c.parentNode.replaceChild(d, c)
            }
            d.appendChild(c)
        };
        b.utils.ajax = function (g, f, c) {
            var e;
            if (window.XMLHttpRequest) {
                e = new XMLHttpRequest()
            } else {
                e = new ActiveXObject("Microsoft.XMLHTTP")
            }
            e.onreadystatechange = function () {
                if (e.readyState === 4) {
                    if (e.status === 200) {
                        if (f) {
                            if (!b.utils.exists(e.responseXML)) {
                                try {
                                    if (window.DOMParser) {
                                        var h = (new DOMParser()).parseFromString(e.responseText, "text/xml");
                                        if (h) {
                                            e = b.utils.extend({}, e, {responseXML:h})
                                        }
                                    } else {
                                        h = new ActiveXObject("Microsoft.XMLDOM");
                                        h.async = "false";
                                        h.loadXML(e.responseText);
                                        e = b.utils.extend({}, e, {responseXML:h})
                                    }
                                } catch (j) {
                                    if (c) {
                                        c(g)
                                    }
                                }
                            }
                            f(e)
                        }
                    } else {
                        if (c) {
                            c(g)
                        }
                    }
                }
            };
            try {
                e.open("GET", g, true);
                e.send(null)
            } catch (d) {
                if (c) {
                    c(g)
                }
            }
            return e
        };
        b.utils.load = function (d, e, c) {
            d.onreadystatechange = function () {
                if (d.readyState === 4) {
                    if (d.status === 200) {
                        if (e) {
                            e()
                        }
                    } else {
                        if (c) {
                            c()
                        }
                    }
                }
            }
        };
        b.utils.find = function (d, c) {
            return d.getElementsByTagName(c)
        };
        b.utils.append = function (c, d) {
            c.appendChild(d)
        };
        b.utils.isIE = function () {
            return((!+"\v1") || (typeof window.ActiveXObject != "undefined"))
        };
        b.utils.userAgentMatch = function (d) {
            var c = navigator.userAgent.toLowerCase();
            return(c.match(d) !== null)
        };
        b.utils.isIOS = function () {
            return b.utils.userAgentMatch(/iP(hone|ad|od)/i)
        };
        b.utils.isIPad = function () {
            return b.utils.userAgentMatch(/iPad/i)
        };
        b.utils.isIPod = function () {
            return b.utils.userAgentMatch(/iP(hone|od)/i)
        };
        b.utils.isAndroid = function () {
            return b.utils.userAgentMatch(/android/i)
        };
        b.utils.isLegacyAndroid = function () {
            return b.utils.userAgentMatch(/android 2.[012]/i)
        };
        b.utils.isBlackberry = function () {
            return b.utils.userAgentMatch(/blackberry/i)
        };
        b.utils.isMobile = function () {
            return b.utils.userAgentMatch(/(iP(hone|ad|od))|android/i)
        };
        b.utils.getFirstPlaylistItemFromConfig = function (c) {
            var d = {};
            var e;
            if (c.playlist && c.playlist.length) {
                e = c.playlist[0]
            } else {
                e = c
            }
            d.file = e.file;
            d.levels = e.levels;
            d.streamer = e.streamer;
            d.playlistfile = e.playlistfile;
            d.provider = e.provider;
            if (!d.provider) {
                if (d.file && (d.file.toLowerCase().indexOf("youtube.com") > -1 || d.file.toLowerCase().indexOf("youtu.be") > -1)) {
                    d.provider = "youtube"
                }
                if (d.streamer && d.streamer.toLowerCase().indexOf("rtmp://") == 0) {
                    d.provider = "rtmp"
                }
                if (e.type) {
                    d.provider = e.type.toLowerCase()
                }
            }
            if (d.provider == "audio") {
                d.provider = "sound"
            }
            return d
        };
        b.utils.getOuterHTML = function (c) {
            if (c.outerHTML) {
                return c.outerHTML
            } else {
                try {
                    return new XMLSerializer().serializeToString(c)
                } catch (d) {
                    return""
                }
            }
        };
        b.utils.setOuterHTML = function (f, e) {
            if (f.outerHTML) {
                f.outerHTML = e
            } else {
                var g = document.createElement("div");
                g.innerHTML = e;
                var c = document.createRange();
                c.selectNodeContents(g);
                var d = c.extractContents();
                f.parentNode.insertBefore(d, f);
                f.parentNode.removeChild(f)
            }
        };
        b.utils.hasFlash = function () {
            if (typeof navigator.plugins != "undefined" && typeof navigator.plugins["Shockwave Flash"] != "undefined") {
                return true
            }
            if (typeof window.ActiveXObject != "undefined") {
                try {
                    new ActiveXObject("ShockwaveFlash.ShockwaveFlash");
                    return true
                } catch (c) {
                }
            }
            return false
        };
        b.utils.getPluginName = function (c) {
            if (c.lastIndexOf("/") >= 0) {
                c = c.substring(c.lastIndexOf("/") + 1, c.length)
            }
            if (c.lastIndexOf("-") >= 0) {
                c = c.substring(0, c.lastIndexOf("-"))
            }
            if (c.lastIndexOf(".swf") >= 0) {
                c = c.substring(0, c.lastIndexOf(".swf"))
            }
            if (c.lastIndexOf(".js") >= 0) {
                c = c.substring(0, c.lastIndexOf(".js"))
            }
            return c
        };
        b.utils.getPluginVersion = function (c) {
            if (c.lastIndexOf("-") >= 0) {
                if (c.lastIndexOf(".js") >= 0) {
                    return c.substring(c.lastIndexOf("-") + 1, c.lastIndexOf(".js"))
                } else {
                    if (c.lastIndexOf(".swf") >= 0) {
                        return c.substring(c.lastIndexOf("-") + 1, c.lastIndexOf(".swf"))
                    } else {
                        return c.substring(c.lastIndexOf("-") + 1)
                    }
                }
            }
            return""
        };
        b.utils.getAbsolutePath = function (j, h) {
            if (!b.utils.exists(h)) {
                h = document.location.href
            }
            if (!b.utils.exists(j)) {
                return undefined
            }
            if (a(j)) {
                return j
            }
            var k = h.substring(0, h.indexOf("://") + 3);
            var g = h.substring(k.length, h.indexOf("/", k.length + 1));
            var d;
            if (j.indexOf("/") === 0) {
                d = j.split("/")
            } else {
                var e = h.split("?")[0];
                e = e.substring(k.length + g.length + 1, e.lastIndexOf("/"));
                d = e.split("/").concat(j.split("/"))
            }
            var c = [];
            for (var f = 0; f < d.length; f++) {
                if (!d[f] || !b.utils.exists(d[f]) || d[f] == ".") {
                    continue
                } else {
                    if (d[f] == "..") {
                        c.pop()
                    } else {
                        c.push(d[f])
                    }
                }
            }
            return k + g + "/" + c.join("/")
        };
        function a(d) {
            if (!b.utils.exists(d)) {
                return
            }
            var e = d.indexOf("://");
            var c = d.indexOf("?");
            return(e > 0 && (c < 0 || (c > e)))
        }

        b.utils.pluginPathType = {ABSOLUTE:"ABSOLUTE", RELATIVE:"RELATIVE", CDN:"CDN"};
        b.utils.getPluginPathType = function (d) {
            if (typeof d != "string") {
                return
            }
            d = d.split("?")[0];
            var e = d.indexOf("://");
            if (e > 0) {
                return b.utils.pluginPathType.ABSOLUTE
            }
            var c = d.indexOf("/");
            var f = b.utils.extension(d);
            if (e < 0 && c < 0 && (!f || !isNaN(f))) {
                return b.utils.pluginPathType.CDN
            }
            return b.utils.pluginPathType.RELATIVE
        };
        b.utils.mapEmpty = function (c) {
            for (var d in c) {
                return false
            }
            return true
        };
        b.utils.mapLength = function (d) {
            var c = 0;
            for (var e in d) {
                c++
            }
            return c
        };
        b.utils.log = function (d, c) {
            if (typeof console != "undefined" && typeof console.log != "undefined") {
                if (c) {
                    console.log(d, c)
                } else {
                    console.log(d)
                }
            }
        };
        b.utils.css = function (d, g, c) {
            if (b.utils.exists(d)) {
                for (var e in g) {
                    try {
                        if (typeof g[e] === "undefined") {
                            continue
                        } else {
                            if (typeof g[e] == "number" && !(e == "zIndex" || e == "opacity")) {
                                if (isNaN(g[e])) {
                                    continue
                                }
                                if (e.match(/color/i)) {
                                    g[e] = "#" + b.utils.strings.pad(g[e].toString(16), 6)
                                } else {
                                    g[e] = Math.ceil(g[e]) + "px"
                                }
                            }
                        }
                        d.style[e] = g[e]
                    } catch (f) {
                    }
                }
            }
        };
        b.utils.isYouTube = function (c) {
            return(c.indexOf("youtube.com") > -1 || c.indexOf("youtu.be") > -1)
        };
        b.utils.transform = function (e, d, c, g, h) {
            if (!b.utils.exists(d)) {
                d = 1
            }
            if (!b.utils.exists(c)) {
                c = 1
            }
            if (!b.utils.exists(g)) {
                g = 0
            }
            if (!b.utils.exists(h)) {
                h = 0
            }
            if (d == 1 && c == 1 && g == 0 && h == 0) {
                e.style.webkitTransform = "";
                e.style.MozTransform = "";
                e.style.OTransform = ""
            } else {
                var f = "scale(" + d + "," + c + ") translate(" + g + "px," + h + "px)";
                e.style.webkitTransform = f;
                e.style.MozTransform = f;
                e.style.OTransform = f
            }
        };
        b.utils.stretch = function (k, q, p, g, n, h) {
            if (typeof p == "undefined" || typeof g == "undefined" || typeof n == "undefined" || typeof h == "undefined") {
                return
            }
            var d = p / n;
            var f = g / h;
            var m = 0;
            var l = 0;
            var e = false;
            var c = {};
            if (q.parentElement) {
                q.parentElement.style.overflow = "hidden"
            }
            b.utils.transform(q);
            switch (k.toUpperCase()) {
                case b.utils.stretching.NONE:
                    c.width = n;
                    c.height = h;
                    c.top = (g - c.height) / 2;
                    c.left = (p - c.width) / 2;
                    break;
                case b.utils.stretching.UNIFORM:
                    if (d > f) {
                        c.width = n * f;
                        c.height = h * f;
                        if (c.width / p > 0.95) {
                            e = true;
                            d = Math.ceil(100 * p / c.width) / 100;
                            f = 1;
                            c.width = p
                        }
                    } else {
                        c.width = n * d;
                        c.height = h * d;
                        if (c.height / g > 0.95) {
                            e = true;
                            d = 1;
                            f = Math.ceil(100 * g / c.height) / 100;
                            c.height = g
                        }
                    }
                    c.top = (g - c.height) / 2;
                    c.left = (p - c.width) / 2;
                    break;
                case b.utils.stretching.FILL:
                    if (d > f) {
                        c.width = n * d;
                        c.height = h * d
                    } else {
                        c.width = n * f;
                        c.height = h * f
                    }
                    c.top = (g - c.height) / 2;
                    c.left = (p - c.width) / 2;
                    break;
                case b.utils.stretching.EXACTFIT:
                    c.width = n;
                    c.height = h;
                    var o = Math.round((n / 2) * (1 - 1 / d));
                    var j = Math.round((h / 2) * (1 - 1 / f));
                    e = true;
                    c.top = c.left = 0;
                    break;
                default:
                    break
            }
            if (e) {
                b.utils.transform(q, d, f, o, j)
            }
            b.utils.css(q, c)
        };
        b.utils.stretching = {NONE:"NONE", FILL:"FILL", UNIFORM:"UNIFORM", EXACTFIT:"EXACTFIT"};
        b.utils.deepReplaceKeyName = function (k, e, c) {
            switch (b.utils.typeOf(k)) {
                case"array":
                    for (var g = 0; g < k.length; g++) {
                        k[g] = b.utils.deepReplaceKeyName(k[g], e, c)
                    }
                    break;
                case"object":
                    for (var f in k) {
                        var j, h;
                        if (e instanceof Array && c instanceof Array) {
                            if (e.length != c.length) {
                                continue
                            } else {
                                j = e;
                                h = c
                            }
                        } else {
                            j = [e];
                            h = [c]
                        }
                        var d = f;
                        for (var g = 0; g < j.length; g++) {
                            d = d.replace(new RegExp(e[g], "g"), c[g])
                        }
                        k[d] = b.utils.deepReplaceKeyName(k[f], e, c);
                        if (f != d) {
                            delete k[f]
                        }
                    }
                    break
            }
            return k
        };
        b.utils.isInArray = function (e, d) {
            if (!(e) || !(e instanceof Array)) {
                return false
            }
            for (var c = 0; c < e.length; c++) {
                if (d === e[c]) {
                    return true
                }
            }
            return false
        };
        b.utils.exists = function (c) {
            switch (typeof(c)) {
                case"string":
                    return(c.length > 0);
                    break;
                case"object":
                    return(c !== null);
                case"undefined":
                    return false
            }
            return true
        };
        b.utils.empty = function (c) {
            if (typeof c.hasChildNodes == "function") {
                while (c.hasChildNodes()) {
                    c.removeChild(c.firstChild)
                }
            }
        };
        b.utils.parseDimension = function (c) {
            if (typeof c == "string") {
                if (c === "") {
                    return 0
                } else {
                    if (c.lastIndexOf("%") > -1) {
                        return c
                    } else {
                        return parseInt(c.replace("px", ""), 10)
                    }
                }
            }
            return c
        };
        b.utils.getDimensions = function (c) {
            if (c && c.style) {
                return{x:b.utils.parseDimension(c.style.left), y:b.utils.parseDimension(c.style.top), width:b.utils.parseDimension(c.style.width), height:b.utils.parseDimension(c.style.height)}
            } else {
                return{}
            }
        };
        b.utils.getElementWidth = function (c) {
            if (!c) {
                return null
            } else {
                if (c == document.body) {
                    return b.utils.parentNode(c).clientWidth
                } else {
                    if (c.clientWidth > 0) {
                        return c.clientWidth
                    } else {
                        if (c.style) {
                            return b.utils.parseDimension(c.style.width)
                        } else {
                            return null
                        }
                    }
                }
            }
        };
        b.utils.getElementHeight = function (c) {
            if (!c) {
                return null
            } else {
                if (c == document.body) {
                    return b.utils.parentNode(c).clientHeight
                } else {
                    if (c.clientHeight > 0) {
                        return c.clientHeight
                    } else {
                        if (c.style) {
                            return b.utils.parseDimension(c.style.height)
                        } else {
                            return null
                        }
                    }
                }
            }
        };
        b.utils.timeFormat = function (c) {
            str = "00:00";
            if (c > 0) {
                str = Math.floor(c / 60) < 10 ? "0" + Math.floor(c / 60) + ":" : Math.floor(c / 60) + ":";
                str += Math.floor(c % 60) < 10 ? "0" + Math.floor(c % 60) : Math.floor(c % 60)
            }
            return str
        };
        b.utils.useNativeFullscreen = function () {
            return(navigator && navigator.vendor && navigator.vendor.indexOf("Apple") == 0)
        };
        b.utils.parentNode = function (c) {
            if (!c) {
                return document.body
            } else {
                if (c.parentNode) {
                    return c.parentNode
                } else {
                    if (c.parentElement) {
                        return c.parentElement
                    } else {
                        return c
                    }
                }
            }
        };
        b.utils.getBoundingClientRect = function (c) {
            if (typeof c.getBoundingClientRect == "function") {
                return c.getBoundingClientRect()
            } else {
                return{left:c.offsetLeft + document.body.scrollLeft, top:c.offsetTop + document.body.scrollTop, width:c.offsetWidth, height:c.offsetHeight}
            }
        };
        b.utils.translateEventResponse = function (e, c) {
            var g = b.utils.extend({}, c);
            if (e == b.api.events.JWPLAYER_FULLSCREEN && !g.fullscreen) {
                g.fullscreen = g.message == "true" ? true : false;
                delete g.message
            } else {
                if (typeof g.data == "object") {
                    g = b.utils.extend(g, g.data);
                    delete g.data
                } else {
                    if (typeof g.metadata == "object") {
                        b.utils.deepReplaceKeyName(g.metadata, ["__dot__", "__spc__", "__dsh__"], [".", " ", "-"])
                    }
                }
            }
            var d = ["position", "duration", "offset"];
            for (var f in d) {
                if (g[d[f]]) {
                    g[d[f]] = Math.round(g[d[f]] * 1000) / 1000
                }
            }
            return g
        };
        b.utils.saveCookie = function (c, d) {
            document.cookie = "jwplayer." + c + "=" + d + "; path=/"
        };
        b.utils.getCookies = function () {
            var f = {};
            var e = document.cookie.split("; ");
            for (var d = 0; d < e.length; d++) {
                var c = e[d].split("=");
                if (c[0].indexOf("jwplayer.") == 0) {
                    f[c[0].substring(9, c[0].length)] = c[1]
                }
            }
            return f
        };
        b.utils.readCookie = function (c) {
            return b.utils.getCookies()[c]
        }
    })(jwplayer);
    (function (a) {
        a.events = function () {
        };
        a.events.COMPLETE = "COMPLETE";
        a.events.ERROR = "ERROR"
    })(jwplayer);
    (function (jwplayer) {
        jwplayer.events.eventdispatcher = function (debug) {
            var _debug = debug;
            var _listeners;
            var _globallisteners;
            this.resetEventListeners = function () {
                _listeners = {};
                _globallisteners = []
            };
            this.resetEventListeners();
            this.addEventListener = function (type, listener, count) {
                try {
                    if (!jwplayer.utils.exists(_listeners[type])) {
                        _listeners[type] = []
                    }
                    if (typeof(listener) == "string") {
                        eval("listener = " + listener)
                    }
                    _listeners[type].push({listener:listener, count:count})
                } catch (err) {
                    jwplayer.utils.log("error", err)
                }
                return false
            };
            this.removeEventListener = function (type, listener) {
                if (!_listeners[type]) {
                    return
                }
                try {
                    for (var listenerIndex = 0; listenerIndex < _listeners[type].length; listenerIndex++) {
                        if (_listeners[type][listenerIndex].listener.toString() == listener.toString()) {
                            _listeners[type].splice(listenerIndex, 1);
                            break
                        }
                    }
                } catch (err) {
                    jwplayer.utils.log("error", err)
                }
                return false
            };
            this.addGlobalListener = function (listener, count) {
                try {
                    if (typeof(listener) == "string") {
                        eval("listener = " + listener)
                    }
                    _globallisteners.push({listener:listener, count:count})
                } catch (err) {
                    jwplayer.utils.log("error", err)
                }
                return false
            };
            this.removeGlobalListener = function (listener) {
                if (!listener) {
                    return
                }
                try {
                    for (var globalListenerIndex = 0; globalListenerIndex < _globallisteners.length; globalListenerIndex++) {
                        if (_globallisteners[globalListenerIndex].listener.toString() == listener.toString()) {
                            _globallisteners.splice(globalListenerIndex, 1);
                            break
                        }
                    }
                } catch (err) {
                    jwplayer.utils.log("error", err)
                }
                return false
            };
            this.sendEvent = function (type, data) {
                if (!jwplayer.utils.exists(data)) {
                    data = {}
                }
                if (_debug) {
                    jwplayer.utils.log(type, data)
                }
                if (typeof _listeners[type] != "undefined") {
                    for (var listenerIndex = 0; listenerIndex < _listeners[type].length; listenerIndex++) {
                        try {
                            _listeners[type][listenerIndex].listener(data)
                        } catch (err) {
                            jwplayer.utils.log("There was an error while handling a listener: " + err.toString(), _listeners[type][listenerIndex].listener)
                        }
                        if (_listeners[type][listenerIndex]) {
                            if (_listeners[type][listenerIndex].count === 1) {
                                delete _listeners[type][listenerIndex]
                            } else {
                                if (_listeners[type][listenerIndex].count > 0) {
                                    _listeners[type][listenerIndex].count = _listeners[type][listenerIndex].count - 1
                                }
                            }
                        }
                    }
                }
                for (var globalListenerIndex = 0; globalListenerIndex < _globallisteners.length; globalListenerIndex++) {
                    try {
                        _globallisteners[globalListenerIndex].listener(data)
                    } catch (err) {
                        jwplayer.utils.log("There was an error while handling a listener: " + err.toString(), _globallisteners[globalListenerIndex].listener)
                    }
                    if (_globallisteners[globalListenerIndex]) {
                        if (_globallisteners[globalListenerIndex].count === 1) {
                            delete _globallisteners[globalListenerIndex]
                        } else {
                            if (_globallisteners[globalListenerIndex].count > 0) {
                                _globallisteners[globalListenerIndex].count = _globallisteners[globalListenerIndex].count - 1
                            }
                        }
                    }
                }
            }
        }
    })(jwplayer);
    (function (a) {
        var b = {};
        a.utils.animations = function () {
        };
        a.utils.animations.transform = function (c, d) {
            c.style.webkitTransform = d;
            c.style.MozTransform = d;
            c.style.OTransform = d;
            c.style.msTransform = d
        };
        a.utils.animations.transformOrigin = function (c, d) {
            c.style.webkitTransformOrigin = d;
            c.style.MozTransformOrigin = d;
            c.style.OTransformOrigin = d;
            c.style.msTransformOrigin = d
        };
        a.utils.animations.rotate = function (c, d) {
            a.utils.animations.transform(c, ["rotate(", d, "deg)"].join(""))
        };
        a.utils.cancelAnimation = function (c) {
            delete b[c.id]
        };
        a.utils.fadeTo = function (m, f, e, j, h, d) {
            if (b[m.id] != d && a.utils.exists(d)) {
                return
            }
            if (m.style.opacity == f) {
                return
            }
            var c = new Date().getTime();
            if (d > c) {
                setTimeout(function () {
                    a.utils.fadeTo(m, f, e, j, 0, d)
                }, d - c)
            }
            if (m.style.display == "none") {
                m.style.display = "block"
            }
            if (!a.utils.exists(j)) {
                j = m.style.opacity === "" ? 1 : m.style.opacity
            }
            if (m.style.opacity == f && m.style.opacity !== "" && a.utils.exists(d)) {
                if (f === 0) {
                    m.style.display = "none"
                }
                return
            }
            if (!a.utils.exists(d)) {
                d = c;
                b[m.id] = d
            }
            if (!a.utils.exists(h)) {
                h = 0
            }
            var k = (e > 0) ? ((c - d) / (e * 1000)) : 0;
            k = k > 1 ? 1 : k;
            var l = f - j;
            var g = j + (k * l);
            if (g > 1) {
                g = 1
            } else {
                if (g < 0) {
                    g = 0
                }
            }
            m.style.opacity = g;
            if (h > 0) {
                b[m.id] = d + h * 1000;
                a.utils.fadeTo(m, f, e, j, 0, b[m.id]);
                return
            }
            setTimeout(function () {
                a.utils.fadeTo(m, f, e, j, 0, d)
            }, 10)
        }
    })(jwplayer);
    (function (a) {
        a.utils.arrays = function () {
        };
        a.utils.arrays.indexOf = function (c, d) {
            for (var b = 0; b < c.length; b++) {
                if (c[b] == d) {
                    return b
                }
            }
            return -1
        };
        a.utils.arrays.remove = function (c, d) {
            var b = a.utils.arrays.indexOf(c, d);
            if (b > -1) {
                c.splice(b, 1)
            }
        }
    })(jwplayer);
    (function (a) {
        a.utils.extensionmap = {"3gp":{html5:"video/3gpp", flash:"video"}, "3gpp":{html5:"video/3gpp"}, "3g2":{html5:"video/3gpp2", flash:"video"}, "3gpp2":{html5:"video/3gpp2"}, flv:{flash:"video"}, f4a:{html5:"audio/mp4"}, f4b:{html5:"audio/mp4", flash:"video"}, f4v:{html5:"video/mp4", flash:"video"}, mov:{html5:"video/quicktime", flash:"video"}, m4a:{html5:"audio/mp4", flash:"video"}, m4b:{html5:"audio/mp4"}, m4p:{html5:"audio/mp4"}, m4v:{html5:"video/mp4", flash:"video"}, mp4:{html5:"video/mp4", flash:"video"}, rbs:{flash:"sound"}, aac:{html5:"audio/aac", flash:"video"}, mp3:{html5:"audio/mp3", flash:"sound"}, ogg:{html5:"audio/ogg"}, oga:{html5:"audio/ogg"}, ogv:{html5:"video/ogg"}, webm:{html5:"video/webm"}, m3u8:{html5:"audio/x-mpegurl"}, gif:{flash:"image"}, jpeg:{flash:"image"}, jpg:{flash:"image"}, swf:{flash:"image"}, png:{flash:"image"}, wav:{html5:"audio/x-wav"}}
    })(jwplayer);
    (function (e) {
        e.utils.mediaparser = function () {
        };
        var g = {element:{width:"width", height:"height", id:"id", "class":"className", name:"name"}, media:{src:"file", preload:"preload", autoplay:"autostart", loop:"repeat", controls:"controls"}, source:{src:"file", type:"type", media:"media", "data-jw-width":"width", "data-jw-bitrate":"bitrate"}, video:{poster:"image"}};
        var f = {};
        e.utils.mediaparser.parseMedia = function (j) {
            return d(j)
        };
        function c(k, j) {
            if (!e.utils.exists(j)) {
                j = g[k]
            } else {
                e.utils.extend(j, g[k])
            }
            return j
        }

        function d(n, j) {
            if (f[n.tagName.toLowerCase()] && !e.utils.exists(j)) {
                return f[n.tagName.toLowerCase()](n)
            } else {
                j = c("element", j);
                var o = {};
                for (var k in j) {
                    if (k != "length") {
                        var m = n.getAttribute(k);
                        if (e.utils.exists(m)) {
                            o[j[k]] = m
                        }
                    }
                }
                var l = n.style["#background-color"];
                if (l && !(l == "transparent" || l == "rgba(0, 0, 0, 0)")) {
                    o.screencolor = l
                }
                return o
            }
        }

        function h(n, k) {
            k = c("media", k);
            var l = [];
            var j = e.utils.selectors("source", n);
            for (var m in j) {
                if (!isNaN(m)) {
                    l.push(a(j[m]))
                }
            }
            var o = d(n, k);
            if (e.utils.exists(o.file)) {
                l[0] = {file:o.file}
            }
            o.levels = l;
            return o
        }

        function a(l, k) {
            k = c("source", k);
            var j = d(l, k);
            j.width = j.width ? j.width : 0;
            j.bitrate = j.bitrate ? j.bitrate : 0;
            return j
        }

        function b(l, k) {
            k = c("video", k);
            var j = h(l, k);
            return j
        }

        f.media = h;
        f.audio = h;
        f.source = a;
        f.video = b
    })(jwplayer);
    (function (a) {
        a.utils.loaderstatus = {NEW:"NEW", LOADING:"LOADING", ERROR:"ERROR", COMPLETE:"COMPLETE"};
        a.utils.scriptloader = function (c) {
            var d = a.utils.loaderstatus.NEW;
            var b = new a.events.eventdispatcher();
            a.utils.extend(this, b);
            this.load = function () {
                if (d == a.utils.loaderstatus.NEW) {
                    d = a.utils.loaderstatus.LOADING;
                    var e = document.createElement("script");
                    e.onload = function (f) {
                        d = a.utils.loaderstatus.COMPLETE;
                        b.sendEvent(a.events.COMPLETE)
                    };
                    e.onerror = function (f) {
                        d = a.utils.loaderstatus.ERROR;
                        b.sendEvent(a.events.ERROR)
                    };
                    e.onreadystatechange = function () {
                        if (e.readyState == "loaded" || e.readyState == "complete") {
                            d = a.utils.loaderstatus.COMPLETE;
                            b.sendEvent(a.events.COMPLETE)
                        }
                    };
                    document.getElementsByTagName("head")[0].appendChild(e);
                    e.src = c
                }
            };
            this.getStatus = function () {
                return d
            }
        }
    })(jwplayer);
    (function (a) {
        a.utils.selectors = function (b, e) {
            if (!a.utils.exists(e)) {
                e = document
            }
            b = a.utils.strings.trim(b);
            var c = b.charAt(0);
            if (c == "#") {
                return e.getElementById(b.substr(1))
            } else {
                if (c == ".") {
                    if (e.getElementsByClassName) {
                        return e.getElementsByClassName(b.substr(1))
                    } else {
                        return a.utils.selectors.getElementsByTagAndClass("*", b.substr(1))
                    }
                } else {
                    if (b.indexOf(".") > 0) {
                        var d = b.split(".");
                        return a.utils.selectors.getElementsByTagAndClass(d[0], d[1])
                    } else {
                        return e.getElementsByTagName(b)
                    }
                }
            }
            return null
        };
        a.utils.selectors.getElementsByTagAndClass = function (e, h, g) {
            var j = [];
            if (!a.utils.exists(g)) {
                g = document
            }
            var f = g.getElementsByTagName(e);
            for (var d = 0; d < f.length; d++) {
                if (a.utils.exists(f[d].className)) {
                    var c = f[d].className.split(" ");
                    for (var b = 0; b < c.length; b++) {
                        if (c[b] == h) {
                            j.push(f[d])
                        }
                    }
                }
            }
            return j
        }
    })(jwplayer);
    (function (a) {
        a.utils.strings = function () {
        };
        a.utils.strings.trim = function (b) {
            return b.replace(/^\s*/, "").replace(/\s*$/, "")
        };
        a.utils.strings.pad = function (c, d, b) {
            if (!b) {
                b = "0"
            }
            while (c.length < d) {
                c = b + c
            }
            return c
        };
        a.utils.strings.serialize = function (b) {
            if (b == null) {
                return null
            } else {
                if (b == "true") {
                    return true
                } else {
                    if (b == "false") {
                        return false
                    } else {
                        if (isNaN(Number(b)) || b.length > 5 || b.length == 0) {
                            return b
                        } else {
                            return Number(b)
                        }
                    }
                }
            }
        };
        a.utils.strings.seconds = function (d) {
            d = d.replace(",", ".");
            var b = d.split(":");
            var c = 0;
            if (d.substr(-1) == "s") {
                c = Number(d.substr(0, d.length - 1))
            } else {
                if (d.substr(-1) == "m") {
                    c = Number(d.substr(0, d.length - 1)) * 60
                } else {
                    if (d.substr(-1) == "h") {
                        c = Number(d.substr(0, d.length - 1)) * 3600
                    } else {
                        if (b.length > 1) {
                            c = Number(b[b.length - 1]);
                            c += Number(b[b.length - 2]) * 60;
                            if (b.length == 3) {
                                c += Number(b[b.length - 3]) * 3600
                            }
                        } else {
                            c = Number(d)
                        }
                    }
                }
            }
            return c
        };
        a.utils.strings.xmlAttribute = function (b, c) {
            for (var d = 0; d < b.attributes.length; d++) {
                if (b.attributes[d].name && b.attributes[d].name.toLowerCase() == c.toLowerCase()) {
                    return b.attributes[d].value.toString()
                }
            }
            return""
        };
        a.utils.strings.jsonToString = function (f) {
            var h = h || {};
            if (h && h.stringify) {
                return h.stringify(f)
            }
            var c = typeof(f);
            if (c != "object" || f === null) {
                if (c == "string") {
                    f = '"' + f.replace(/"/g, '\\"') + '"'
                } else {
                    return String(f)
                }
            } else {
                var g = [], b = (f && f.constructor == Array);
                for (var d in f) {
                    var e = f[d];
                    switch (typeof(e)) {
                        case"string":
                            e = '"' + e.replace(/"/g, '\\"') + '"';
                            break;
                        case"object":
                            if (a.utils.exists(e)) {
                                e = a.utils.strings.jsonToString(e)
                            }
                            break
                    }
                    if (b) {
                        if (typeof(e) != "function") {
                            g.push(String(e))
                        }
                    } else {
                        if (typeof(e) != "function") {
                            g.push('"' + d + '":' + String(e))
                        }
                    }
                }
                if (b) {
                    return"[" + String(g) + "]"
                } else {
                    return"{" + String(g) + "}"
                }
            }
        }
    })(jwplayer);
    (function (c) {
        var d = new RegExp(/^(#|0x)[0-9a-fA-F]{3,6}/);
        c.utils.typechecker = function (g, f) {
            f = !c.utils.exists(f) ? b(g) : f;
            return e(g, f)
        };
        function b(f) {
            var g = ["true", "false", "t", "f"];
            if (g.toString().indexOf(f.toLowerCase().replace(" ", "")) >= 0) {
                return"boolean"
            } else {
                if (d.test(f)) {
                    return"color"
                } else {
                    if (!isNaN(parseInt(f, 10)) && parseInt(f, 10).toString().length == f.length) {
                        return"integer"
                    } else {
                        if (!isNaN(parseFloat(f)) && parseFloat(f).toString().length == f.length) {
                            return"float"
                        }
                    }
                }
            }
            return"string"
        }

        function e(g, f) {
            if (!c.utils.exists(f)) {
                return g
            }
            switch (f) {
                case"color":
                    if (g.length > 0) {
                        return a(g)
                    }
                    return null;
                case"integer":
                    return parseInt(g, 10);
                case"float":
                    return parseFloat(g);
                case"boolean":
                    if (g.toLowerCase() == "true") {
                        return true
                    } else {
                        if (g == "1") {
                            return true
                        }
                    }
                    return false
            }
            return g
        }

        function a(f) {
            switch (f.toLowerCase()) {
                case"blue":
                    return parseInt("0000FF", 16);
                case"green":
                    return parseInt("00FF00", 16);
                case"red":
                    return parseInt("FF0000", 16);
                case"cyan":
                    return parseInt("00FFFF", 16);
                case"magenta":
                    return parseInt("FF00FF", 16);
                case"yellow":
                    return parseInt("FFFF00", 16);
                case"black":
                    return parseInt("000000", 16);
                case"white":
                    return parseInt("FFFFFF", 16);
                default:
                    f = f.replace(/(#|0x)?([0-9A-F]{3,6})$/gi, "$2");
                    if (f.length == 3) {
                        f = f.charAt(0) + f.charAt(0) + f.charAt(1) + f.charAt(1) + f.charAt(2) + f.charAt(2)
                    }
                    return parseInt(f, 16)
            }
            return parseInt("000000", 16)
        }
    })(jwplayer);
    (function (a) {
        a.utils.parsers = function () {
        };
        a.utils.parsers.localName = function (b) {
            if (!b) {
                return""
            } else {
                if (b.localName) {
                    return b.localName
                } else {
                    if (b.baseName) {
                        return b.baseName
                    } else {
                        return""
                    }
                }
            }
        };
        a.utils.parsers.textContent = function (b) {
            if (!b) {
                return""
            } else {
                if (b.textContent) {
                    return b.textContent
                } else {
                    if (b.text) {
                        return b.text
                    } else {
                        return""
                    }
                }
            }
        }
    })(jwplayer);
    (function (a) {
        a.utils.parsers.jwparser = function () {
        };
        a.utils.parsers.jwparser.PREFIX = "jwplayer";
        a.utils.parsers.jwparser.parseEntry = function (c, d) {
            for (var b = 0; b < c.childNodes.length; b++) {
                if (c.childNodes[b].prefix == a.utils.parsers.jwparser.PREFIX) {
                    d[a.utils.parsers.localName(c.childNodes[b])] = a.utils.strings.serialize(a.utils.parsers.textContent(c.childNodes[b]));
                    if (a.utils.parsers.localName(c.childNodes[b]) == "file" && d.levels) {
                        delete d.levels
                    }
                }
                if (!d.file && String(d.link).toLowerCase().indexOf("youtube") > -1) {
                    d.file = d.link
                }
            }
            return d
        };
        a.utils.parsers.jwparser.getProvider = function (c) {
            if (c.type) {
                return c.type
            } else {
                if (c.file.indexOf("youtube.com/w") > -1 || c.file.indexOf("youtube.com/v") > -1 || c.file.indexOf("youtu.be/") > -1) {
                    return"youtube"
                } else {
                    if (c.streamer && c.streamer.indexOf("rtmp") == 0) {
                        return"rtmp"
                    } else {
                        if (c.streamer && c.streamer.indexOf("http") == 0) {
                            return"http"
                        } else {
                            var b = a.utils.strings.extension(c.file);
                            if (extensions.hasOwnProperty(b)) {
                                return extensions[b]
                            }
                        }
                    }
                }
            }
            return""
        }
    })(jwplayer);
    (function (a) {
        a.utils.parsers.mediaparser = function () {
        };
        a.utils.parsers.mediaparser.PREFIX = "media";
        a.utils.parsers.mediaparser.parseGroup = function (d, f) {
            var e = false;
            for (var c = 0; c < d.childNodes.length; c++) {
                if (d.childNodes[c].prefix == a.utils.parsers.mediaparser.PREFIX) {
                    if (!a.utils.parsers.localName(d.childNodes[c])) {
                        continue
                    }
                    switch (a.utils.parsers.localName(d.childNodes[c]).toLowerCase()) {
                        case"content":
                            if (!e) {
                                f.file = a.utils.strings.xmlAttribute(d.childNodes[c], "url")
                            }
                            if (a.utils.strings.xmlAttribute(d.childNodes[c], "duration")) {
                                f.duration = a.utils.strings.seconds(a.utils.strings.xmlAttribute(d.childNodes[c], "duration"))
                            }
                            if (a.utils.strings.xmlAttribute(d.childNodes[c], "start")) {
                                f.start = a.utils.strings.seconds(a.utils.strings.xmlAttribute(d.childNodes[c], "start"))
                            }
                            if (d.childNodes[c].childNodes && d.childNodes[c].childNodes.length > 0) {
                                f = a.utils.parsers.mediaparser.parseGroup(d.childNodes[c], f)
                            }
                            if (a.utils.strings.xmlAttribute(d.childNodes[c], "width") || a.utils.strings.xmlAttribute(d.childNodes[c], "bitrate") || a.utils.strings.xmlAttribute(d.childNodes[c], "url")) {
                                if (!f.levels) {
                                    f.levels = []
                                }
                                f.levels.push({width:a.utils.strings.xmlAttribute(d.childNodes[c], "width"), bitrate:a.utils.strings.xmlAttribute(d.childNodes[c], "bitrate"), file:a.utils.strings.xmlAttribute(d.childNodes[c], "url")})
                            }
                            break;
                        case"title":
                            f.title = a.utils.parsers.textContent(d.childNodes[c]);
                            break;
                        case"description":
                            f.description = a.utils.parsers.textContent(d.childNodes[c]);
                            break;
                        case"keywords":
                            f.tags = a.utils.parsers.textContent(d.childNodes[c]);
                            break;
                        case"thumbnail":
                            f.image = a.utils.strings.xmlAttribute(d.childNodes[c], "url");
                            break;
                        case"credit":
                            f.author = a.utils.parsers.textContent(d.childNodes[c]);
                            break;
                        case"player":
                            var b = d.childNodes[c].url;
                            if (b.indexOf("youtube.com") >= 0 || b.indexOf("youtu.be") >= 0) {
                                e = true;
                                f.file = a.utils.strings.xmlAttribute(d.childNodes[c], "url")
                            }
                            break;
                        case"group":
                            a.utils.parsers.mediaparser.parseGroup(d.childNodes[c], f);
                            break
                    }
                }
            }
            return f
        }
    })(jwplayer);
    (function (b) {
        b.utils.parsers.rssparser = function () {
        };
        b.utils.parsers.rssparser.parse = function (f) {
            var c = [];
            for (var e = 0; e < f.childNodes.length; e++) {
                if (b.utils.parsers.localName(f.childNodes[e]).toLowerCase() == "channel") {
                    for (var d = 0; d < f.childNodes[e].childNodes.length; d++) {
                        if (b.utils.parsers.localName(f.childNodes[e].childNodes[d]).toLowerCase() == "item") {
                            c.push(a(f.childNodes[e].childNodes[d]))
                        }
                    }
                }
            }
            return c
        };
        function a(d) {
            var e = {};
            for (var c = 0; c < d.childNodes.length; c++) {
                if (!b.utils.parsers.localName(d.childNodes[c])) {
                    continue
                }
                switch (b.utils.parsers.localName(d.childNodes[c]).toLowerCase()) {
                    case"enclosure":
                        e.file = b.utils.strings.xmlAttribute(d.childNodes[c], "url");
                        break;
                    case"title":
                        e.title = b.utils.parsers.textContent(d.childNodes[c]);
                        break;
                    case"pubdate":
                        e.date = b.utils.parsers.textContent(d.childNodes[c]);
                        break;
                    case"description":
                        e.description = b.utils.parsers.textContent(d.childNodes[c]);
                        break;
                    case"link":
                        e.link = b.utils.parsers.textContent(d.childNodes[c]);
                        break;
                    case"category":
                        if (e.tags) {
                            e.tags += b.utils.parsers.textContent(d.childNodes[c])
                        } else {
                            e.tags = b.utils.parsers.textContent(d.childNodes[c])
                        }
                        break
                }
            }
            e = b.utils.parsers.mediaparser.parseGroup(d, e);
            e = b.utils.parsers.jwparser.parseEntry(d, e);
            return new b.html5.playlistitem(e)
        }
    })(jwplayer);
    (function (a) {
        var c = {};
        var b = {};
        a.plugins = function () {
        };
        a.plugins.loadPlugins = function (e, d) {
            b[e] = new a.plugins.pluginloader(new a.plugins.model(c), d);
            return b[e]
        };
        a.plugins.registerPlugin = function (h, f, e) {
            var d = a.utils.getPluginName(h);
            if (c[d]) {
                c[d].registerPlugin(h, f, e)
            } else {
                a.utils.log("A plugin (" + h + ") was registered with the player that was not loaded. Please check your configuration.");
                for (var g in b) {
                    b[g].pluginFailed()
                }
            }
        }
    })(jwplayer);
    (function (a) {
        a.plugins.model = function (b) {
            this.addPlugin = function (c) {
                var d = a.utils.getPluginName(c);
                if (!b[d]) {
                    b[d] = new a.plugins.plugin(c)
                }
                return b[d]
            }
        }
    })(jwplayer);
    (function (a) {
        a.plugins.pluginmodes = {FLASH:"FLASH", JAVASCRIPT:"JAVASCRIPT", HYBRID:"HYBRID"};
        a.plugins.plugin = function (b) {
            var d = "http://plugins.longtailvideo.com";
            var j = a.utils.loaderstatus.NEW;
            var k;
            var h;
            var l;
            var c = new a.events.eventdispatcher();
            a.utils.extend(this, c);
            function e() {
                switch (a.utils.getPluginPathType(b)) {
                    case a.utils.pluginPathType.ABSOLUTE:
                        return b;
                    case a.utils.pluginPathType.RELATIVE:
                        return a.utils.getAbsolutePath(b, window.location.href);
                    case a.utils.pluginPathType.CDN:
                        var o = a.utils.getPluginName(b);
                        var n = a.utils.getPluginVersion(b);
                        var m = (window.location.href.indexOf("https://") == 0) ? d.replace("http://", "https://secure") : d;
                        return m + "/" + a.version.split(".")[0] + "/" + o + "/" + o + (n !== "" ? ("-" + n) : "") + ".js"
                }
            }

            function g(m) {
                l = setTimeout(function () {
                    j = a.utils.loaderstatus.COMPLETE;
                    c.sendEvent(a.events.COMPLETE)
                }, 1000)
            }

            function f(m) {
                j = a.utils.loaderstatus.ERROR;
                c.sendEvent(a.events.ERROR)
            }

            this.load = function () {
                if (j == a.utils.loaderstatus.NEW) {
                    if (b.lastIndexOf(".swf") > 0) {
                        k = b;
                        j = a.utils.loaderstatus.COMPLETE;
                        c.sendEvent(a.events.COMPLETE);
                        return
                    }
                    j = a.utils.loaderstatus.LOADING;
                    var m = new a.utils.scriptloader(e());
                    m.addEventListener(a.events.COMPLETE, g);
                    m.addEventListener(a.events.ERROR, f);
                    m.load()
                }
            };
            this.registerPlugin = function (o, n, m) {
                if (l) {
                    clearTimeout(l);
                    l = undefined
                }
                if (n && m) {
                    k = m;
                    h = n
                } else {
                    if (typeof n == "string") {
                        k = n
                    } else {
                        if (typeof n == "function") {
                            h = n
                        } else {
                            if (!n && !m) {
                                k = o
                            }
                        }
                    }
                }
                j = a.utils.loaderstatus.COMPLETE;
                c.sendEvent(a.events.COMPLETE)
            };
            this.getStatus = function () {
                return j
            };
            this.getPluginName = function () {
                return a.utils.getPluginName(b)
            };
            this.getFlashPath = function () {
                if (k) {
                    switch (a.utils.getPluginPathType(k)) {
                        case a.utils.pluginPathType.ABSOLUTE:
                            return k;
                        case a.utils.pluginPathType.RELATIVE:
                            if (b.lastIndexOf(".swf") > 0) {
                                return a.utils.getAbsolutePath(k, window.location.href)
                            }
                            return a.utils.getAbsolutePath(k, e());
                        case a.utils.pluginPathType.CDN:
                            if (k.indexOf("-") > -1) {
                                return k + "h"
                            }
                            return k + "-h"
                    }
                }
                return null
            };
            this.getJS = function () {
                return h
            };
            this.getPluginmode = function () {
                if (typeof k != "undefined" && typeof h != "undefined") {
                    return a.plugins.pluginmodes.HYBRID
                } else {
                    if (typeof k != "undefined") {
                        return a.plugins.pluginmodes.FLASH
                    } else {
                        if (typeof h != "undefined") {
                            return a.plugins.pluginmodes.JAVASCRIPT
                        }
                    }
                }
            };
            this.getNewInstance = function (n, m, o) {
                return new h(n, m, o)
            };
            this.getURL = function () {
                return b
            }
        }
    })(jwplayer);
    (function (a) {
        a.plugins.pluginloader = function (h, e) {
            var g = {};
            var k = a.utils.loaderstatus.NEW;
            var d = false;
            var b = false;
            var c = new a.events.eventdispatcher();
            a.utils.extend(this, c);
            function f() {
                if (!b) {
                    b = true;
                    k = a.utils.loaderstatus.COMPLETE;
                    c.sendEvent(a.events.COMPLETE)
                }
            }

            function j() {
                if (!b) {
                    var m = 0;
                    for (plugin in g) {
                        var l = g[plugin].getStatus();
                        if (l == a.utils.loaderstatus.LOADING || l == a.utils.loaderstatus.NEW) {
                            m++
                        }
                    }
                    if (m == 0) {
                        f()
                    }
                }
            }

            this.setupPlugins = function (n, l, s) {
                var m = {length:0, plugins:{}};
                var p = {length:0, plugins:{}};
                for (var o in g) {
                    var q = g[o].getPluginName();
                    if (g[o].getFlashPath()) {
                        m.plugins[g[o].getFlashPath()] = l.plugins[o];
                        m.plugins[g[o].getFlashPath()].pluginmode = g[o].getPluginmode();
                        m.length++
                    }
                    if (g[o].getJS()) {
                        var r = document.createElement("div");
                        r.id = n.id + "_" + q;
                        r.style.position = "absolute";
                        r.style.zIndex = p.length + 10;
                        p.plugins[q] = g[o].getNewInstance(n, l.plugins[o], r);
                        p.length++;
                        if (typeof p.plugins[q].resize != "undefined") {
                            n.onReady(s(p.plugins[q], r, true));
                            n.onResize(s(p.plugins[q], r))
                        }
                    }
                }
                n.plugins = p.plugins;
                return m
            };
            this.load = function () {
                k = a.utils.loaderstatus.LOADING;
                d = true;
                for (var l in e) {
                    if (a.utils.exists(l)) {
                        g[l] = h.addPlugin(l);
                        g[l].addEventListener(a.events.COMPLETE, j);
                        g[l].addEventListener(a.events.ERROR, j)
                    }
                }
                for (l in g) {
                    g[l].load()
                }
                d = false;
                j()
            };
            this.pluginFailed = function () {
                f()
            };
            this.getStatus = function () {
                return k
            }
        }
    })(jwplayer);
    (function (b) {
        var a = [];
        b.api = function (d) {
            this.container = d;
            this.id = d.id;
            var m = {};
            var t = {};
            var p = {};
            var c = [];
            var g = undefined;
            var k = false;
            var h = [];
            var r = undefined;
            var o = b.utils.getOuterHTML(d);
            var s = {};
            var j = {};
            this.getBuffer = function () {
                return this.callInternal("jwGetBuffer")
            };
            this.getContainer = function () {
                return this.container
            };
            function e(v, u) {
                return function (A, w, x, y) {
                    if (v.renderingMode == "flash" || v.renderingMode == "html5") {
                        var z;
                        if (w) {
                            j[A] = w;
                            z = "jwplayer('" + v.id + "').callback('" + A + "')"
                        } else {
                            if (!w && j[A]) {
                                delete j[A]
                            }
                        }
                        g.jwDockSetButton(A, z, x, y)
                    }
                    return u
                }
            }

            this.getPlugin = function (u) {
                var w = this;
                var v = {};
                if (u == "dock") {
                    return b.utils.extend(v, {setButton:e(w, v), show:function () {
                        w.callInternal("jwDockShow");
                        return v
                    }, hide:function () {
                        w.callInternal("jwDockHide");
                        return v
                    }, onShow:function (x) {
                        w.componentListener("dock", b.api.events.JWPLAYER_COMPONENT_SHOW, x);
                        return v
                    }, onHide:function (x) {
                        w.componentListener("dock", b.api.events.JWPLAYER_COMPONENT_HIDE, x);
                        return v
                    }})
                } else {
                    if (u == "controlbar") {
                        return b.utils.extend(v, {show:function () {
                            w.callInternal("jwControlbarShow");
                            return v
                        }, hide:function () {
                            w.callInternal("jwControlbarHide");
                            return v
                        }, onShow:function (x) {
                            w.componentListener("controlbar", b.api.events.JWPLAYER_COMPONENT_SHOW, x);
                            return v
                        }, onHide:function (x) {
                            w.componentListener("controlbar", b.api.events.JWPLAYER_COMPONENT_HIDE, x);
                            return v
                        }})
                    } else {
                        if (u == "display") {
                            return b.utils.extend(v, {show:function () {
                                w.callInternal("jwDisplayShow");
                                return v
                            }, hide:function () {
                                w.callInternal("jwDisplayHide");
                                return v
                            }, onShow:function (x) {
                                w.componentListener("display", b.api.events.JWPLAYER_COMPONENT_SHOW, x);
                                return v
                            }, onHide:function (x) {
                                w.componentListener("display", b.api.events.JWPLAYER_COMPONENT_HIDE, x);
                                return v
                            }})
                        } else {
                            return this.plugins[u]
                        }
                    }
                }
            };
            this.callback = function (u) {
                if (j[u]) {
                    return j[u]()
                }
            };
            this.getDuration = function () {
                return this.callInternal("jwGetDuration")
            };
            this.getFullscreen = function () {
                return this.callInternal("jwGetFullscreen")
            };
            this.getHeight = function () {
                return this.callInternal("jwGetHeight")
            };
            this.getLockState = function () {
                return this.callInternal("jwGetLockState")
            };
            this.getMeta = function () {
                return this.getItemMeta()
            };
            this.getMute = function () {
                return this.callInternal("jwGetMute")
            };
            this.getPlaylist = function () {
                var v = this.callInternal("jwGetPlaylist");
                if (this.renderingMode == "flash") {
                    b.utils.deepReplaceKeyName(v, ["__dot__", "__spc__", "__dsh__"], [".", " ", "-"])
                }
                for (var u = 0; u < v.length; u++) {
                    if (!b.utils.exists(v[u].index)) {
                        v[u].index = u
                    }
                }
                return v
            };
            this.getPlaylistItem = function (u) {
                if (!b.utils.exists(u)) {
                    u = this.getCurrentItem()
                }
                return this.getPlaylist()[u]
            };
            this.getPosition = function () {
                return this.callInternal("jwGetPosition")
            };
            this.getRenderingMode = function () {
                return this.renderingMode
            };
            this.getState = function () {
                return this.callInternal("jwGetState")
            };
            this.getVolume = function () {
                return this.callInternal("jwGetVolume")
            };
            this.getWidth = function () {
                return this.callInternal("jwGetWidth")
            };
            this.setFullscreen = function (u) {
                if (!b.utils.exists(u)) {
                    this.callInternal("jwSetFullscreen", !this.callInternal("jwGetFullscreen"))
                } else {
                    this.callInternal("jwSetFullscreen", u)
                }
                return this
            };
            this.setMute = function (u) {
                if (!b.utils.exists(u)) {
                    this.callInternal("jwSetMute", !this.callInternal("jwGetMute"))
                } else {
                    this.callInternal("jwSetMute", u)
                }
                return this
            };
            this.lock = function () {
                return this
            };
            this.unlock = function () {
                return this
            };
            this.load = function (u) {
                this.callInternal("jwLoad", u);
                return this
            };
            this.playlistItem = function (u) {
                this.callInternal("jwPlaylistItem", u);
                return this
            };
            this.playlistPrev = function () {
                this.callInternal("jwPlaylistPrev");
                return this
            };
            this.playlistNext = function () {
                this.callInternal("jwPlaylistNext");
                return this
            };
            this.resize = function (v, u) {
                if (this.renderingMode == "html5") {
                    g.jwResize(v, u)
                } else {
                    this.container.width = v;
                    this.container.height = u;
                    var w = document.getElementById(this.id + "_wrapper");
                    if (w) {
                        w.style.width = v + "px";
                        w.style.height = u + "px"
                    }
                }
                return this
            };
            this.play = function (u) {
                if (typeof u == "undefined") {
                    u = this.getState();
                    if (u == b.api.events.state.PLAYING || u == b.api.events.state.BUFFERING) {
                        this.callInternal("jwPause")
                    } else {
                        this.callInternal("jwPlay")
                    }
                } else {
                    this.callInternal("jwPlay", u)
                }
                return this
            };
            this.pause = function (u) {
                if (typeof u == "undefined") {
                    u = this.getState();
                    if (u == b.api.events.state.PLAYING || u == b.api.events.state.BUFFERING) {
                        this.callInternal("jwPause")
                    } else {
                        this.callInternal("jwPlay")
                    }
                } else {
                    this.callInternal("jwPause", u)
                }
                return this
            };
            this.stop = function () {
                this.callInternal("jwStop");
                return this
            };
            this.seek = function (u) {
                this.callInternal("jwSeek", u);
                return this
            };
            this.setVolume = function (u) {
                this.callInternal("jwSetVolume", u);
                return this
            };
            this.loadInstream = function (v, u) {
                r = new b.api.instream(this, g, v, u);
                return r
            };
            this.onBufferChange = function (u) {
                return this.eventListener(b.api.events.JWPLAYER_MEDIA_BUFFER, u)
            };
            this.onBufferFull = function (u) {
                return this.eventListener(b.api.events.JWPLAYER_MEDIA_BUFFER_FULL, u)
            };
            this.onError = function (u) {
                return this.eventListener(b.api.events.JWPLAYER_ERROR, u)
            };
            this.onFullscreen = function (u) {
                return this.eventListener(b.api.events.JWPLAYER_FULLSCREEN, u)
            };
            this.onMeta = function (u) {
                return this.eventListener(b.api.events.JWPLAYER_MEDIA_META, u)
            };
            this.onMute = function (u) {
                return this.eventListener(b.api.events.JWPLAYER_MEDIA_MUTE, u)
            };
            this.onPlaylist = function (u) {
                return this.eventListener(b.api.events.JWPLAYER_PLAYLIST_LOADED, u)
            };
            this.onPlaylistItem = function (u) {
                return this.eventListener(b.api.events.JWPLAYER_PLAYLIST_ITEM, u)
            };
            this.onReady = function (u) {
                return this.eventListener(b.api.events.API_READY, u)
            };
            this.onResize = function (u) {
                return this.eventListener(b.api.events.JWPLAYER_RESIZE, u)
            };
            this.onComplete = function (u) {
                return this.eventListener(b.api.events.JWPLAYER_MEDIA_COMPLETE, u)
            };
            this.onSeek = function (u) {
                return this.eventListener(b.api.events.JWPLAYER_MEDIA_SEEK, u)
            };
            this.onTime = function (u) {
                return this.eventListener(b.api.events.JWPLAYER_MEDIA_TIME, u)
            };
            this.onVolume = function (u) {
                return this.eventListener(b.api.events.JWPLAYER_MEDIA_VOLUME, u)
            };
            this.onBeforePlay = function (u) {
                return this.eventListener(b.api.events.JWPLAYER_MEDIA_BEFOREPLAY, u)
            };
            this.onBeforeComplete = function (u) {
                return this.eventListener(b.api.events.JWPLAYER_MEDIA_BEFORECOMPLETE, u)
            };
            this.onBuffer = function (u) {
                return this.stateListener(b.api.events.state.BUFFERING, u)
            };
            this.onPause = function (u) {
                return this.stateListener(b.api.events.state.PAUSED, u)
            };
            this.onPlay = function (u) {
                return this.stateListener(b.api.events.state.PLAYING, u)
            };
            this.onIdle = function (u) {
                return this.stateListener(b.api.events.state.IDLE, u)
            };
            this.remove = function () {
                if (!k) {
                    throw"Cannot call remove() before player is ready";
                    return
                }
                q(this)
            };
            function q(u) {
                h = [];
                if (b.utils.getOuterHTML(u.container) != o) {
                    b.api.destroyPlayer(u.id, o)
                }
            }

            this.setup = function (v) {
                if (b.embed) {
                    var u = this.id;
                    q(this);
                    var w = b(u);
                    w.config = v;
                    return new b.embed(w)
                }
                return this
            };
            this.registerPlugin = function (w, v, u) {
                b.plugins.registerPlugin(w, v, u)
            };
            this.setPlayer = function (u, v) {
                g = u;
                this.renderingMode = v
            };
            this.stateListener = function (u, v) {
                if (!t[u]) {
                    t[u] = [];
                    this.eventListener(b.api.events.JWPLAYER_PLAYER_STATE, f(u))
                }
                t[u].push(v);
                return this
            };
            this.detachMedia = function () {
                if (this.renderingMode == "html5") {
                    return this.callInternal("jwDetachMedia")
                }
            };
            this.attachMedia = function () {
                if (this.renderingMode == "html5") {
                    return this.callInternal("jwAttachMedia")
                }
            };
            function f(u) {
                return function (w) {
                    var v = w.newstate, y = w.oldstate;
                    if (v == u) {
                        var x = t[v];
                        if (x) {
                            for (var z = 0; z < x.length; z++) {
                                if (typeof x[z] == "function") {
                                    x[z].call(this, {oldstate:y, newstate:v})
                                }
                            }
                        }
                    }
                }
            }

            this.componentListener = function (u, v, w) {
                if (!p[u]) {
                    p[u] = {}
                }
                if (!p[u][v]) {
                    p[u][v] = [];
                    this.eventListener(v, l(u, v))
                }
                p[u][v].push(w);
                return this
            };
            function l(u, v) {
                return function (x) {
                    if (u == x.component) {
                        var w = p[u][v];
                        if (w) {
                            for (var y = 0; y < w.length; y++) {
                                if (typeof w[y] == "function") {
                                    w[y].call(this, x)
                                }
                            }
                        }
                    }
                }
            }

            this.addInternalListener = function (u, v) {
                try {
                    u.jwAddEventListener(v, 'function(dat) { jwplayer("' + this.id + '").dispatchEvent("' + v + '", dat); }')
                } catch (w) {
                    b.utils.log("Could not add internal listener")
                }
            };
            this.eventListener = function (u, v) {
                if (!m[u]) {
                    m[u] = [];
                    if (g && k) {
                        this.addInternalListener(g, u)
                    }
                }
                m[u].push(v);
                return this
            };
            this.dispatchEvent = function (w) {
                if (m[w]) {
                    var v = _utils.translateEventResponse(w, arguments[1]);
                    for (var u = 0; u < m[w].length; u++) {
                        if (typeof m[w][u] == "function") {
                            m[w][u].call(this, v)
                        }
                    }
                }
            };
            this.dispatchInstreamEvent = function (u) {
                if (r) {
                    r.dispatchEvent(u, arguments)
                }
            };
            this.callInternal = function () {
                if (k) {
                    var w = arguments[0], u = [];
                    for (var v = 1; v < arguments.length; v++) {
                        u.push(arguments[v])
                    }
                    if (typeof g != "undefined" && typeof g[w] == "function") {
                        if (u.length == 2) {
                            return(g[w])(u[0], u[1])
                        } else {
                            if (u.length == 1) {
                                return(g[w])(u[0])
                            } else {
                                return(g[w])()
                            }
                        }
                    }
                    return null
                } else {
                    h.push(arguments)
                }
            };
            this.playerReady = function (v) {
                k = true;
                if (!g) {
                    this.setPlayer(document.getElementById(v.id))
                }
                this.container = document.getElementById(this.id);
                for (var u in m) {
                    this.addInternalListener(g, u)
                }
                this.eventListener(b.api.events.JWPLAYER_PLAYLIST_ITEM, function (w) {
                    s = {}
                });
                this.eventListener(b.api.events.JWPLAYER_MEDIA_META, function (w) {
                    b.utils.extend(s, w.metadata)
                });
                this.dispatchEvent(b.api.events.API_READY);
                while (h.length > 0) {
                    this.callInternal.apply(this, h.shift())
                }
            };
            this.getItemMeta = function () {
                return s
            };
            this.getCurrentItem = function () {
                return this.callInternal("jwGetPlaylistIndex")
            };
            function n(w, y, x) {
                var u = [];
                if (!y) {
                    y = 0
                }
                if (!x) {
                    x = w.length - 1
                }
                for (var v = y; v <= x; v++) {
                    u.push(w[v])
                }
                return u
            }

            return this
        };
        b.api.selectPlayer = function (d) {
            var c;
            if (!b.utils.exists(d)) {
                d = 0
            }
            if (d.nodeType) {
                c = d
            } else {
                if (typeof d == "string") {
                    c = document.getElementById(d)
                }
            }
            if (c) {
                var e = b.api.playerById(c.id);
                if (e) {
                    return e
                } else {
                    return b.api.addPlayer(new b.api(c))
                }
            } else {
                if (typeof d == "number") {
                    return b.getPlayers()[d]
                }
            }
            return null
        };
        b.api.events = {API_READY:"jwplayerAPIReady", JWPLAYER_READY:"jwplayerReady", JWPLAYER_FULLSCREEN:"jwplayerFullscreen", JWPLAYER_RESIZE:"jwplayerResize", JWPLAYER_ERROR:"jwplayerError", JWPLAYER_MEDIA_BEFOREPLAY:"jwplayerMediaBeforePlay", JWPLAYER_MEDIA_BEFORECOMPLETE:"jwplayerMediaBeforeComplete", JWPLAYER_COMPONENT_SHOW:"jwplayerComponentShow", JWPLAYER_COMPONENT_HIDE:"jwplayerComponentHide", JWPLAYER_MEDIA_BUFFER:"jwplayerMediaBuffer", JWPLAYER_MEDIA_BUFFER_FULL:"jwplayerMediaBufferFull", JWPLAYER_MEDIA_ERROR:"jwplayerMediaError", JWPLAYER_MEDIA_LOADED:"jwplayerMediaLoaded", JWPLAYER_MEDIA_COMPLETE:"jwplayerMediaComplete", JWPLAYER_MEDIA_SEEK:"jwplayerMediaSeek", JWPLAYER_MEDIA_TIME:"jwplayerMediaTime", JWPLAYER_MEDIA_VOLUME:"jwplayerMediaVolume", JWPLAYER_MEDIA_META:"jwplayerMediaMeta", JWPLAYER_MEDIA_MUTE:"jwplayerMediaMute", JWPLAYER_PLAYER_STATE:"jwplayerPlayerState", JWPLAYER_PLAYLIST_LOADED:"jwplayerPlaylistLoaded", JWPLAYER_PLAYLIST_ITEM:"jwplayerPlaylistItem", JWPLAYER_INSTREAM_CLICK:"jwplayerInstreamClicked", JWPLAYER_INSTREAM_DESTROYED:"jwplayerInstreamDestroyed"};
        b.api.events.state = {BUFFERING:"BUFFERING", IDLE:"IDLE", PAUSED:"PAUSED", PLAYING:"PLAYING"};
        b.api.playerById = function (d) {
            for (var c = 0; c < a.length; c++) {
                if (a[c].id == d) {
                    return a[c]
                }
            }
            return null
        };
        b.api.addPlayer = function (c) {
            for (var d = 0; d < a.length; d++) {
                if (a[d] == c) {
                    return c
                }
            }
            a.push(c);
            return c
        };
        b.api.destroyPlayer = function (h, d) {
            var g = -1;
            for (var l = 0; l < a.length; l++) {
                if (a[l].id == h) {
                    g = l;
                    continue
                }
            }
            if (g >= 0) {
                try {
                    a[g].callInternal("jwDestroy")
                } catch (k) {
                }
                var c = document.getElementById(a[g].id);
                if (document.getElementById(a[g].id + "_wrapper")) {
                    c = document.getElementById(a[g].id + "_wrapper")
                }
                if (c) {
                    if (d) {
                        b.utils.setOuterHTML(c, d)
                    } else {
                        var j = document.createElement("div");
                        var f = c.id;
                        if (c.id.indexOf("_wrapper") == c.id.length - 8) {
                            newID = c.id.substring(0, c.id.length - 8)
                        }
                        j.setAttribute("id", f);
                        c.parentNode.replaceChild(j, c)
                    }
                }
                a.splice(g, 1)
            }
            return null
        };
        b.getPlayers = function () {
            return a.slice(0)
        }
    })(jwplayer);
    var _userPlayerReady = (typeof playerReady == "function") ? playerReady : undefined;
    playerReady = function (b) {
        var a = jwplayer.api.playerById(b.id);
        if (a) {
            a.playerReady(b)
        } else {
            jwplayer.api.selectPlayer(b.id).playerReady(b)
        }
        if (_userPlayerReady) {
            _userPlayerReady.call(this, b)
        }
    };
    (function (a) {
        a.api.instream = function (c, j, n, q) {
            var h = c;
            var b = j;
            var g = n;
            var k = q;
            var e = {};
            var p = {};

            function f() {
                h.callInternal("jwLoadInstream", n, q)
            }

            function m(r, s) {
                b.jwInstreamAddEventListener(s, 'function(dat) { jwplayer("' + h.id + '").dispatchInstreamEvent("' + s + '", dat); }')
            }

            function d(r, s) {
                if (!e[r]) {
                    e[r] = [];
                    m(b, r)
                }
                e[r].push(s);
                return this
            }

            function o(r, s) {
                if (!p[r]) {
                    p[r] = [];
                    d(a.api.events.JWPLAYER_PLAYER_STATE, l(r))
                }
                p[r].push(s);
                return this
            }

            function l(r) {
                return function (t) {
                    var s = t.newstate, v = t.oldstate;
                    if (s == r) {
                        var u = p[s];
                        if (u) {
                            for (var w = 0; w < u.length; w++) {
                                if (typeof u[w] == "function") {
                                    u[w].call(this, {oldstate:v, newstate:s, type:t.type})
                                }
                            }
                        }
                    }
                }
            }

            this.dispatchEvent = function (u, t) {
                if (e[u]) {
                    var s = _utils.translateEventResponse(u, t[1]);
                    for (var r = 0; r < e[u].length; r++) {
                        if (typeof e[u][r] == "function") {
                            e[u][r].call(this, s)
                        }
                    }
                }
            };
            this.onError = function (r) {
                return d(a.api.events.JWPLAYER_ERROR, r)
            };
            this.onFullscreen = function (r) {
                return d(a.api.events.JWPLAYER_FULLSCREEN, r)
            };
            this.onMeta = function (r) {
                return d(a.api.events.JWPLAYER_MEDIA_META, r)
            };
            this.onMute = function (r) {
                return d(a.api.events.JWPLAYER_MEDIA_MUTE, r)
            };
            this.onComplete = function (r) {
                return d(a.api.events.JWPLAYER_MEDIA_COMPLETE, r)
            };
            this.onSeek = function (r) {
                return d(a.api.events.JWPLAYER_MEDIA_SEEK, r)
            };
            this.onTime = function (r) {
                return d(a.api.events.JWPLAYER_MEDIA_TIME, r)
            };
            this.onVolume = function (r) {
                return d(a.api.events.JWPLAYER_MEDIA_VOLUME, r)
            };
            this.onBuffer = function (r) {
                return o(a.api.events.state.BUFFERING, r)
            };
            this.onPause = function (r) {
                return o(a.api.events.state.PAUSED, r)
            };
            this.onPlay = function (r) {
                return o(a.api.events.state.PLAYING, r)
            };
            this.onIdle = function (r) {
                return o(a.api.events.state.IDLE, r)
            };
            this.onInstreamClick = function (r) {
                return d(a.api.events.JWPLAYER_INSTREAM_CLICK, r)
            };
            this.onInstreamDestroyed = function (r) {
                return d(a.api.events.JWPLAYER_INSTREAM_DESTROYED, r)
            };
            this.play = function (r) {
                b.jwInstreamPlay(r)
            };
            this.pause = function (r) {
                b.jwInstreamPause(r)
            };
            this.seek = function (r) {
                b.jwInstreamSeek(r)
            };
            this.destroy = function () {
                b.jwInstreamDestroy()
            };
            this.getState = function () {
                return b.jwInstreamGetState()
            };
            this.getDuration = function () {
                return b.jwInstreamGetDuration()
            };
            this.getPosition = function () {
                return b.jwInstreamGetPosition()
            };
            f()
        }
    })(jwplayer);
    (function (a) {
        var c = a.utils;
        a.embed = function (h) {
            var k = {width:400, height:300, components:{controlbar:{position:"over"}}};
            var g = c.mediaparser.parseMedia(h.container);
            var f = new a.embed.config(c.extend(k, g, h.config), this);
            var j = a.plugins.loadPlugins(h.id, f.plugins);

            function d(n, m) {
                for (var l in m) {
                    if (typeof n[l] == "function") {
                        (n[l]).call(n, m[l])
                    }
                }
            }

            function e() {
                if (j.getStatus() == c.loaderstatus.COMPLETE) {
                    for (var n = 0; n < f.modes.length; n++) {
                        if (f.modes[n].type && a.embed[f.modes[n].type]) {
                            var p = f.modes[n].config;
                            var t = f;
                            if (p) {
                                t = c.extend(c.clone(f), p);
                                var s = ["file", "levels", "playlist"];
                                for (var m = 0; m < s.length; m++) {
                                    var q = s[m];
                                    if (c.exists(p[q])) {
                                        for (var l = 0; l < s.length; l++) {
                                            if (l != m) {
                                                var o = s[l];
                                                if (c.exists(t[o]) && !c.exists(p[o])) {
                                                    delete t[o]
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            var r = new a.embed[f.modes[n].type](document.getElementById(h.id), f.modes[n], t, j, h);
                            if (r.supportsConfig()) {
                                r.embed();
                                d(h, f.events);
                                return h
                            }
                        }
                    }
                    c.log("No suitable players found");
                    new a.embed.logo(c.extend({hide:true}, f.components.logo), "none", h.id)
                }
            }

            j.addEventListener(a.events.COMPLETE, e);
            j.addEventListener(a.events.ERROR, e);
            j.load();
            return h
        };
        function b() {
            if (!document.body) {
                return setTimeout(b, 15)
            }
            var d = c.selectors.getElementsByTagAndClass("video", "jwplayer");
            for (var e = 0; e < d.length; e++) {
                var f = d[e];
                if (f.id == "") {
                    f.id = "jwplayer_" + Math.round(Math.random() * 100000)
                }
                a(f.id).setup({})
            }
        }

        b()
    })(jwplayer);
    (function (e) {
        var k = e.utils;

        function h(m) {
            var l = [
                {type:"flash", src:m ? m : "/jwplayer/player.swf"},
                {type:"html5"},
                {type:"download"}
            ];
            if (k.isAndroid()) {
                l[0] = l.splice(1, 1, l[0])[0]
            }
            return l
        }

        var a = {players:"modes", autoplay:"autostart"};

        function b(o) {
            var n = o.toLowerCase();
            var m = ["left", "right", "top", "bottom"];
            for (var l = 0; l < m.length; l++) {
                if (n == m[l]) {
                    return true
                }
            }
            return false
        }

        function c(m) {
            var l = false;
            l = (m instanceof Array) || (typeof m == "object" && !m.position && !m.size);
            return l
        }

        function j(l) {
            if (typeof l == "string") {
                if (parseInt(l).toString() == l || l.toLowerCase().indexOf("px") > -1) {
                    return parseInt(l)
                }
            }
            return l
        }

        var g = ["playlist", "dock", "controlbar", "logo", "display"];

        function f(l) {
            var o = {};
            switch (k.typeOf(l.plugins)) {
                case"object":
                    for (var n in l.plugins) {
                        o[k.getPluginName(n)] = n
                    }
                    break;
                case"string":
                    var p = l.plugins.split(",");
                    for (var m = 0; m < p.length; m++) {
                        o[k.getPluginName(p[m])] = p[m]
                    }
                    break
            }
            return o
        }

        function d(p, o, n, l) {
            if (k.typeOf(p[o]) != "object") {
                p[o] = {}
            }
            var m = p[o][n];
            if (k.typeOf(m) != "object") {
                p[o][n] = m = {}
            }
            if (l) {
                if (o == "plugins") {
                    var q = k.getPluginName(n);
                    m[l] = p[q + "." + l];
                    delete p[q + "." + l]
                } else {
                    m[l] = p[n + "." + l];
                    delete p[n + "." + l]
                }
            }
        }

        e.embed.deserialize = function (m) {
            var n = f(m);
            for (var l in n) {
                d(m, "plugins", n[l])
            }
            for (var q in m) {
                if (q.indexOf(".") > -1) {
                    var p = q.split(".");
                    var o = p[0];
                    var q = p[1];
                    if (k.isInArray(g, o)) {
                        d(m, "components", o, q)
                    } else {
                        if (n[o]) {
                            d(m, "plugins", n[o], q)
                        }
                    }
                }
            }
            return m
        };
        e.embed.config = function (l, v) {
            var u = k.extend({}, l);
            var s;
            if (c(u.playlist)) {
                s = u.playlist;
                delete u.playlist
            }
            u = e.embed.deserialize(u);
            if(typeof limitVideoWidth !=="undefined")
            {
                u.width = jQuery("body").width()*0.9;
                u.height = u.width*0.75;
            }
            u.height = j(u.height);
            u.width = j(u.width);
            if (typeof u.plugins == "string") {
                var m = u.plugins.split(",");
                if (typeof u.plugins != "object") {
                    u.plugins = {}
                }
                for (var q = 0; q < m.length; q++) {
                    var r = k.getPluginName(m[q]);
                    if (typeof u[r] == "object") {
                        u.plugins[m[q]] = u[r];
                        delete u[r]
                    } else {
                        u.plugins[m[q]] = {}
                    }
                }
            }
            for (var t = 0; t < g.length; t++) {
                var p = g[t];
                if (k.exists(u[p])) {
                    if (typeof u[p] != "object") {
                        if (!u.components[p]) {
                            u.components[p] = {}
                        }
                        if (p == "logo") {
                            u.components[p].file = u[p]
                        } else {
                            u.components[p].position = u[p]
                        }
                        delete u[p]
                    } else {
                        if (!u.components[p]) {
                            u.components[p] = {}
                        }
                        k.extend(u.components[p], u[p]);
                        delete u[p]
                    }
                }
                if (typeof u[p + "size"] != "undefined") {
                    if (!u.components[p]) {
                        u.components[p] = {}
                    }
                    u.components[p].size = u[p + "size"];
                    delete u[p + "size"]
                }
            }
            if (typeof u.icons != "undefined") {
                if (!u.components.display) {
                    u.components.display = {}
                }
                u.components.display.icons = u.icons;
                delete u.icons
            }
            for (var o in a) {
                if (u[o]) {
                    if (!u[a[o]]) {
                        u[a[o]] = u[o]
                    }
                    delete u[o]
                }
            }
            var n;
            if (u.flashplayer && !u.modes) {
                n = h(u.flashplayer);
                delete u.flashplayer
            } else {
                if (u.modes) {
                    if (typeof u.modes == "string") {
                        n = h(u.modes)
                    } else {
                        if (u.modes instanceof Array) {
                            n = u.modes
                        } else {
                            if (typeof u.modes == "object" && u.modes.type) {
                                n = [u.modes]
                            }
                        }
                    }
                    delete u.modes
                } else {
                    n = h()
                }
            }
            u.modes = n;
            if (s) {
                u.playlist = s
            }
            return u
        }
    })(jwplayer);
    (function (a) {
        a.embed.download = function (c, g, b, d, f) {
            this.embed = function () {
                var k = a.utils.extend({}, b);
                var q = {};
                var j = b.width ? b.width : 480;
                if (typeof j != "number") {
                    j = parseInt(j, 10)
                }
                var m = b.height ? b.height : 320;
                if (typeof m != "number") {
                    m = parseInt(m, 10)
                }
                var u, o, n;
                var s = {};
                if (b.playlist && b.playlist.length) {
                    s.file = b.playlist[0].file;
                    o = b.playlist[0].image;
                    s.levels = b.playlist[0].levels
                } else {
                    s.file = b.file;
                    o = b.image;
                    s.levels = b.levels
                }
                if (s.file) {
                    u = s.file
                } else {
                    if (s.levels && s.levels.length) {
                        u = s.levels[0].file
                    }
                }
                n = u ? "pointer" : "auto";
                var l = {display:{style:{cursor:n, width:j, height:m, backgroundColor:"#000", position:"relative", textDecoration:"none", border:"none", display:"block"}}, display_icon:{style:{cursor:n, position:"absolute", display:u ? "block" : "none", top:0, left:0, border:0, margin:0, padding:0, zIndex:3, width:50, height:50, backgroundImage:"url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADIAAAAyCAYAAAAeP4ixAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAALdJREFUeNrs18ENgjAYhmFouDOCcQJGcARHgE10BDcgTOIosAGwQOuPwaQeuFRi2p/3Sb6EC5L3QCxZBgAAAOCorLW1zMn65TrlkH4NcV7QNcUQt7Gn7KIhxA+qNIR81spOGkL8oFJDyLJRdosqKDDkK+iX5+d7huzwM40xptMQMkjIOeRGo+VkEVvIPfTGIpKASfYIfT9iCHkHrBEzf4gcUQ56aEzuGK/mw0rHpy4AAACAf3kJMACBxjAQNRckhwAAAABJRU5ErkJggg==)"}}, display_iconBackground:{style:{cursor:n, position:"absolute", display:u ? "block" : "none", top:((m - 50) / 2), left:((j - 50) / 2), border:0, width:50, height:50, margin:0, padding:0, zIndex:2, backgroundImage:"url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADIAAAAyCAYAAAAeP4ixAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAEpJREFUeNrszwENADAIA7DhX8ENoBMZ5KR10EryckCJiIiIiIiIiIiIiIiIiIiIiIh8GmkRERERERERERERERERERERERGRHSPAAPlXH1phYpYaAAAAAElFTkSuQmCC)"}}, display_image:{style:{width:j, height:m, display:o ? "block" : "none", position:"absolute", cursor:n, left:0, top:0, margin:0, padding:0, textDecoration:"none", zIndex:1, border:"none"}}};
                var h = function (v, x, y) {
                    var w = document.createElement(v);
                    if (y) {
                        w.id = y
                    } else {
                        w.id = c.id + "_jwplayer_" + x
                    }
                    a.utils.css(w, l[x].style);
                    return w
                };
                q.display = h("a", "display", c.id);
                if (u) {
                    q.display.setAttribute("href", a.utils.getAbsolutePath(u))
                }
                q.display_image = h("img", "display_image");
                q.display_image.setAttribute("alt", "Click to download...");
                if (o) {
                    q.display_image.setAttribute("src", a.utils.getAbsolutePath(o))
                }
                if (true) {
                    q.display_icon = h("div", "display_icon");
                    q.display_iconBackground = h("div", "display_iconBackground");
                    q.display.appendChild(q.display_image);
                    q.display_iconBackground.appendChild(q.display_icon);
                    q.display.appendChild(q.display_iconBackground)
                }
                _css = a.utils.css;
                _hide = function (v) {
                    _css(v, {display:"none"})
                };
                function r(v) {
                    _imageWidth = q.display_image.naturalWidth;
                    _imageHeight = q.display_image.naturalHeight;
                    t()
                }

                function t() {
                    a.utils.stretch(a.utils.stretching.UNIFORM, q.display_image, j, m, _imageWidth, _imageHeight)
                }

                q.display_image.onerror = function (v) {
                    _hide(q.display_image)
                };
                q.display_image.onload = r;
                c.parentNode.replaceChild(q.display, c);
                var p = (b.plugins && b.plugins.logo) ? b.plugins.logo : {};
                q.display.appendChild(new a.embed.logo(b.components.logo, "download", c.id));
                f.container = document.getElementById(f.id);
                f.setPlayer(q.display, "download")
            };
            this.supportsConfig = function () {
                if (b) {
                    var j = a.utils.getFirstPlaylistItemFromConfig(b);
                    if (typeof j.file == "undefined" && typeof j.levels == "undefined") {
                        return true
                    } else {
                        if (j.file) {
                            return e(j.file, j.provider, j.playlistfile)
                        } else {
                            if (j.levels && j.levels.length) {
                                for (var h = 0; h < j.levels.length; h++) {
                                    if (j.levels[h].file && e(j.levels[h].file, j.provider, j.playlistfile)) {
                                        return true
                                    }
                                }
                            }
                        }
                    }
                } else {
                    return true
                }
            };
            function e(j, l, h) {
                if (h) {
                    return false
                }
                var k = ["image", "sound", "youtube", "http"];
                if (l && (k.toString().indexOf(l) > -1)) {
                    return true
                }
                if (!l || (l && l == "video")) {
                    var m = a.utils.extension(j);
                    if (m && a.utils.extensionmap[m]) {
                        return true
                    }
                }
                return false
            }
        }
    })(jwplayer);
    (function (a) {
        a.embed.flash = function (f, g, l, e, j) {
            function m(o, n, p) {
                var q = document.createElement("param");
                q.setAttribute("name", n);
                q.setAttribute("value", p);
                o.appendChild(q)
            }

            function k(o, p, n) {
                return function (q) {
                    if (n) {
                        document.getElementById(j.id + "_wrapper").appendChild(p)
                    }
                    var s = document.getElementById(j.id).getPluginConfig("display");
                    o.resize(s.width, s.height);
                    var r = {left:s.x, top:s.y};
                    a.utils.css(p, r)
                }
            }

            function d(p) {
                if (!p) {
                    return{}
                }
                var r = {};
                for (var o in p) {
                    var n = p[o];
                    for (var q in n) {
                        r[o + "." + q] = n[q]
                    }
                }
                return r
            }

            function h(q, p) {
                if (q[p]) {
                    var s = q[p];
                    for (var o in s) {
                        var n = s[o];
                        if (typeof n == "string") {
                            if (!q[o]) {
                                q[o] = n
                            }
                        } else {
                            for (var r in n) {
                                if (!q[o + "." + r]) {
                                    q[o + "." + r] = n[r]
                                }
                            }
                        }
                    }
                    delete q[p]
                }
            }

            function b(q) {
                if (!q) {
                    return{}
                }
                var t = {}, s = [];
                for (var n in q) {
                    var p = a.utils.getPluginName(n);
                    var o = q[n];
                    s.push(n);
                    for (var r in o) {
                        t[p + "." + r] = o[r]
                    }
                }
                t.plugins = s.join(",");
                return t
            }

            function c(p) {
                var n = p.netstreambasepath ? "" : "netstreambasepath=" + encodeURIComponent(window.location.href.split("#")[0]) + "&";
                for (var o in p) {
                    if (typeof(p[o]) == "object") {
                        n += o + "=" + encodeURIComponent("[[JSON]]" + a.utils.strings.jsonToString(p[o])) + "&"
                    } else {
                        n += o + "=" + encodeURIComponent(p[o]) + "&"
                    }
                }
                return n.substring(0, n.length - 1)
            }

            this.embed = function () {
                l.id = j.id;
                var A;
                var r = a.utils.extend({}, l);
                var o = r.width;
                var y = r.height;
                if (f.id + "_wrapper" == f.parentNode.id) {
                    A = document.getElementById(f.id + "_wrapper")
                } else {
                    A = document.createElement("div");
                    A.id = f.id + "_wrapper";
                    a.utils.wrap(f, A);
                    a.utils.css(A, {position:"relative", width:o, height:y,margin:"0 auto"})
                }
                var p = e.setupPlugins(j, r, k);
                if (p.length > 0) {
                    a.utils.extend(r, b(p.plugins))
                } else {
                    delete r.plugins
                }
                var s = ["height", "width", "modes", "events"];
                for (var v = 0; v < s.length; v++) {
                    delete r[s[v]]
                }
                var q = "opaque";
                if (r.wmode) {
                    q = r.wmode
                }
                h(r, "components");
                h(r, "providers");
                if (typeof r["dock.position"] != "undefined") {
                    if (r["dock.position"].toString().toLowerCase() == "false") {
                        r.dock = r["dock.position"];
                        delete r["dock.position"]
                    }
                }
                var x = a.utils.getCookies();
                for (var n in x) {
                    if (typeof(r[n]) == "undefined") {
                        r[n] = x[n]
                    }
                }
                var z = "#000000";
                var u;
                if (a.utils.isIE()) {
                    var w = '<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" bgcolor="' + z + '" width="100%" height="100%" id="' + f.id + '" name="' + f.id + '" tabindex=0"">';
                    w += '<param name="movie" value="' + g.src + '">';
                    w += '<param name="allowfullscreen" value="true">';
                    w += '<param name="allowscriptaccess" value="always">';
                    w += '<param name="seamlesstabbing" value="true">';
                    w += '<param name="wmode" value="' + q + '">';
                    w += '<param name="flashvars" value="' + c(r) + '">';
                    w += "</object>";
                    a.utils.setOuterHTML(f, w);
                    u = document.getElementById(f.id)
                } else {
                    var t = document.createElement("object");
                    t.setAttribute("type", "application/x-shockwave-flash");
                    t.setAttribute("data", g.src);
                    t.setAttribute("width", "100%");
                    t.setAttribute("height", "100%");
                    t.setAttribute("bgcolor", "#000000");
                    t.setAttribute("id", f.id);
                    t.setAttribute("name", f.id);
                    t.setAttribute("tabindex", 0);
                    m(t, "allowfullscreen", "true");
                    m(t, "allowscriptaccess", "always");
                    m(t, "seamlesstabbing", "true");
                    m(t, "wmode", q);
                    m(t, "flashvars", c(r));
                    f.parentNode.replaceChild(t, f);
                    u = t
                }
                j.container = u;
                j.setPlayer(u, "flash")
            };
            this.supportsConfig = function () {
                if (a.utils.hasFlash()) {
                    if (l) {
                        var o = a.utils.getFirstPlaylistItemFromConfig(l);
                        if (typeof o.file == "undefined" && typeof o.levels == "undefined") {
                            return true
                        } else {
                            if (o.file) {
                                return flashCanPlay(o.file, o.provider)
                            } else {
                                if (o.levels && o.levels.length) {
                                    for (var n = 0; n < o.levels.length; n++) {
                                        if (o.levels[n].file && flashCanPlay(o.levels[n].file, o.provider)) {
                                            return true
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        return true
                    }
                }
                return false
            };
            flashCanPlay = function (n, p) {
                var o = ["video", "http", "sound", "image"];
                if (p && (o.toString().indexOf(p) < 0)) {
                    return true
                }
                var q = a.utils.extension(n);
                if (!q) {
                    return true
                }
                if (a.utils.exists(a.utils.extensionmap[q]) && !a.utils.exists(a.utils.extensionmap[q].flash)) {
                    return false
                }
                return true
            }
        }
    })(jwplayer);
    (function (a) {
        a.embed.html5 = function (c, g, b, d, f) {
            function e(j, k, h) {
                return function (l) {
                    var m = document.getElementById(c.id + "_displayarea");
                    if (h) {
                        m.appendChild(k)
                    }
                    j.resize(m.clientWidth, m.clientHeight);
                    k.left = m.style.left;
                    k.top = m.style.top
                }
            }

            this.embed = function () {
                if (a.html5) {
                    d.setupPlugins(f, b, e);
                    c.innerHTML = "";
                    var j = a.utils.extend({screencolor:"0x000000"}, b);
                    var h = ["plugins", "modes", "events"];
                    for (var k = 0; k < h.length; k++) {
                        delete j[h[k]]
                    }
                    if (j.levels && !j.sources) {
                        j.sources = b.levels
                    }
                    if (j.skin && j.skin.toLowerCase().indexOf(".zip") > 0) {
                        j.skin = j.skin.replace(/\.zip/i, ".xml")
                    }
                    var l = new (a.html5(c)).setup(j);
                    f.container = document.getElementById(f.id);
                    f.setPlayer(l, "html5")
                } else {
                    return null
                }
            };
            this.supportsConfig = function () {
                if (!!a.vid.canPlayType) {
                    if (b) {
                        var j = a.utils.getFirstPlaylistItemFromConfig(b);
                        if (typeof j.file == "undefined" && typeof j.levels == "undefined") {
                            return true
                        } else {
                            if (j.file) {
                                return html5CanPlay(a.vid, j.file, j.provider, j.playlistfile)
                            } else {
                                if (j.levels && j.levels.length) {
                                    for (var h = 0; h < j.levels.length; h++) {
                                        if (j.levels[h].file && html5CanPlay(a.vid, j.levels[h].file, j.provider, j.playlistfile)) {
                                            return true
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        return true
                    }
                }
                return false
            };
            html5CanPlay = function (k, j, l, h) {
                if (h) {
                    return false
                }
                if (l && l == "youtube") {
                    return true
                }
                if (l && l != "video" && l != "http" && l != "sound") {
                    return false
                }
                if (navigator.userAgent.match(/BlackBerry/i) !== null) {
                    return false
                }
                var m = a.utils.extension(j);
                if (!a.utils.exists(m) || !a.utils.exists(a.utils.extensionmap[m])) {
                    return true
                }
                if (!a.utils.exists(a.utils.extensionmap[m].html5)) {
                    return false
                }
                if (a.utils.isLegacyAndroid() && m.match(/m4v|mp4/)) {
                    return true
                }
                return browserCanPlay(k, a.utils.extensionmap[m].html5)
            };
            browserCanPlay = function (j, h) {
                if (!h) {
                    return true
                }
                if (j.canPlayType(h)) {
                    return true
                } else {
                    if (h == "audio/mp3" && navigator.userAgent.match(/safari/i)) {
                        return j.canPlayType("audio/mpeg")
                    } else {
                        return false
                    }
                }
            }
        }
    })(jwplayer);
    (function (a) {
        a.embed.logo = function (m, l, d) {
            var j = {prefix:"http://l.longtailvideo.com/" + l + "/", file:"logo.png", link:"http://www.longtailvideo.com/players/jw-flv-player/", linktarget:"_top", margin:8, out:0.5, over:1, timeout:5, hide:false, position:"bottom-left"};
            _css = a.utils.css;
            var b;
            var h;
            k();
            function k() {
                o();
                c();
                f()
            }

            function o() {
                if (j.prefix) {
                    var q = a.version.split(/\W/).splice(0, 2).join("/");
                    if (j.prefix.indexOf(q) < 0) {
                        j.prefix += q + "/"
                    }
                }
                h = a.utils.extend({}, j)
            }

            function p() {
                var s = {border:"none", textDecoration:"none", position:"absolute", cursor:"pointer", zIndex:10};
                s.display = h.hide ? "none" : "block";
                var r = h.position.toLowerCase().split("-");
                for (var q in r) {
                    s[r[q]] = h.margin
                }
                return s
            }

            function c() {
                b = document.createElement("img");
                b.id = d + "_jwplayer_logo";
                b.style.display = "none";
                b.onload = function (q) {
                    _css(b, p());
                    e()
                };
                if (!h.file) {
                    return
                }
                if (h.file.indexOf("http://") === 0) {
                    b.src = h.file
                } else {
                    b.src = h.prefix + h.file
                }
            }

            if (!h.file) {
                return
            }
            function f() {
                if (h.link) {
                    b.onmouseover = g;
                    b.onmouseout = e;
                    b.onclick = n
                } else {
                    this.mouseEnabled = false
                }
            }

            function n(q) {
                if (typeof q != "undefined") {
                    q.preventDefault();
                    q.stopPropagation()
                }
                if (h.link) {
                    window.open(h.link, h.linktarget)
                }
                return
            }

            function e(q) {
                if (h.link) {
                    b.style.opacity = h.out
                }
                return
            }

            function g(q) {
                if (h.hide) {
                    b.style.opacity = h.over
                }
                return
            }

            return b
        }
    })(jwplayer);
    (function (a) {
        a.html5 = function (b) {
            var c = b;
            this.setup = function (d) {
                a.utils.extend(this, new a.html5.api(c, d));
                return this
            };
            return this
        }
    })(jwplayer);
    (function (a) {
        var d = a.utils;
        var b = d.css;
        var c = d.isIOS();
        a.html5.view = function (n, H, h) {
            var m = n;
            var y = H;
            var j = h;
            var R;
            var g;
            var t;
            var o;
            var F;
            var P;
            var O;
            var E = false;
            var x = false;
            var A, N;
            var f, S, u;

            function L() {
                R = document.createElement("div");
                R.id = y.id;
                R.className = y.className;
                _videowrapper = document.createElement("div");
                _videowrapper.id = R.id + "_video_wrapper";
                y.id = R.id + "_video";
                b(R, {position:"relative", height:j.height, width:j.width, padding:0, backgroundColor:U(), zIndex:0,margin:"0 auto"});
                function U() {
                    if (m.skin.getComponentSettings("display") && m.skin.getComponentSettings("display").backgroundcolor) {
                        return m.skin.getComponentSettings("display").backgroundcolor
                    }
                    return parseInt("000000", 16)
                }

                b(y, {width:"100%", height:"100%", top:0, left:0, zIndex:1, margin:"auto", display:"block"});
                b(_videowrapper, {overflow:"hidden", position:"absolute", top:0, left:0, bottom:0, right:0});
                d.wrap(y, R);
                d.wrap(y, _videowrapper);
                o = document.createElement("div");
                o.id = R.id + "_displayarea";
                R.appendChild(o);
                _instreamArea = document.createElement("div");
                _instreamArea.id = R.id + "_instreamarea";
                b(_instreamArea, {overflow:"hidden", position:"absolute", top:0, left:0, bottom:0, right:0, zIndex:100, background:"000000", display:"none"});
                R.appendChild(_instreamArea)
            }

            function K() {
                for (var U = 0; U < j.plugins.order.length; U++) {
                    var V = j.plugins.order[U];
                    if (d.exists(j.plugins.object[V].getDisplayElement)) {
                        j.plugins.object[V].height = d.parseDimension(j.plugins.object[V].getDisplayElement().style.height);
                        j.plugins.object[V].width = d.parseDimension(j.plugins.object[V].getDisplayElement().style.width);
                        j.plugins.config[V].currentPosition = j.plugins.config[V].position
                    }
                }
                v()
            }

            function s(U) {
                x = j.fullscreen
            }

            function p(U) {
                if (S) {
                    return
                }
                switch (U.newstate) {
                    case a.api.events.state.PLAYING:
                        if (j.getMedia() && j.getMedia().hasChrome()) {
                            o.style.display = "none"
                        }
                        break;
                    default:
                        o.style.display = "block";
                        break
                }
                l()
            }

            function v(V) {
                var X = j.getMedia() ? j.getMedia().getDisplayElement() : null;
                if (d.exists(X)) {
                    if (O != X) {
                        if (O && O.parentNode) {
                            O.parentNode.replaceChild(X, O)
                        }
                        O = X
                    }
                    for (var U = 0; U < j.plugins.order.length; U++) {
                        var W = j.plugins.order[U];
                        if (d.exists(j.plugins.object[W].getDisplayElement)) {
                            j.plugins.config[W].currentPosition = j.plugins.config[W].position
                        }
                    }
                }
                G(j.width, j.height)
            }

            this.setup = function () {
                if (j && j.getMedia()) {
                    y = j.getMedia().getDisplayElement()
                }
                L();
                K();
                m.jwAddEventListener(a.api.events.JWPLAYER_PLAYER_STATE, p);
                m.jwAddEventListener(a.api.events.JWPLAYER_MEDIA_LOADED, v);
                m.jwAddEventListener(a.api.events.JWPLAYER_MEDIA_BEFOREPLAY, s);
                m.jwAddEventListener(a.api.events.JWPLAYER_MEDIA_META, function (V) {
                    l()
                });
                var U;
                if (d.exists(window.onresize)) {
                    U = window.onresize
                }
                window.onresize = function (V) {
                    if (d.exists(U)) {
                        try {
                            U(V)
                        } catch (X) {
                        }
                    }
                    if (m.jwGetFullscreen()) {
                        if (!B()) {
                            var W = d.getBoundingClientRect(document.body);
                            j.width = Math.abs(W.left) + Math.abs(W.right);
                            j.height = window.innerHeight;
                            G(j.width, j.height)
                        }
                    } else {
                        G(j.width, j.height)
                    }
                }
            };
            function M(U) {
                switch (U.keyCode) {
                    case 27:
                        if (m.jwGetFullscreen()) {
                            m.jwSetFullscreen(false)
                        }
                        break;
                    case 32:
                        if (m.jwGetState() != a.api.events.state.IDLE && m.jwGetState() != a.api.events.state.PAUSED) {
                            m.jwPause()
                        } else {
                            m.jwPlay()
                        }
                        break
                }
            }

            function G(U, ad) {
                if (R.style.display == "none") {
                    return
                }
                var X = [].concat(j.plugins.order);
                X.reverse();
                F = X.length + 2;
                if (x && B()) {
                    try {
                        if (j.fullscreen && !j.getMedia().getDisplayElement().webkitDisplayingFullscreen) {
                            j.fullscreen = false
                        }
                    } catch (aa) {
                    }
                }
                if (!j.fullscreen) {
                    g = U;
                    t = ad;
                    if (typeof U == "string" && U.indexOf("%") > 0) {
                        g = d.getElementWidth(d.parentNode(R)) * parseInt(U.replace("%"), "") / 100
                    } else {
                        g = U
                    }
                    if (typeof ad == "string" && ad.indexOf("%") > 0) {
                        t = d.getElementHeight(d.parentNode(R)) * parseInt(ad.replace("%"), "") / 100
                    } else {
                        t = ad
                    }
                    var Y = {top:0, bottom:0, left:0, right:0, width:g, height:t, position:"absolute"};
                    b(o, Y);
                    var ae = {};
                    var ab;
                    try {
                        ab = j.plugins.object.display.getDisplayElement()
                    } catch (aa) {
                    }
                    if (ab) {
                        ae.width = d.parseDimension(ab.style.width);
                        ae.height = d.parseDimension(ab.style.height)
                    }
                    var ac = d.extend({}, Y, ae, {zIndex:_instreamArea.style.zIndex, display:_instreamArea.style.display});
                    b(_instreamArea, ac);
                    b(R, {height:t, width:g});
                    var Z = w(I, X);
                    if (Z.length > 0) {
                        F += Z.length;
                        var W = Z.indexOf("playlist"), V = Z.indexOf("controlbar");
                        if (W >= 0 && V >= 0) {
                            Z[W] = Z.splice(V, 1, Z[W])[0]
                        }
                        w(q, Z, true)
                    }
                    A = d.getElementWidth(o);
                    N = d.getElementHeight(o)
                } else {
                    if (!B() && !c) {
                        w(e, X, true)
                    }
                }
                l()
            }

            var r;

            function w(ab, X, Y) {
                r = 0;
                var Z = [];
                for (var W = 0; W < X.length; W++) {
                    var aa = X[W];
                    if (d.exists(j.plugins.object[aa].getDisplayElement)) {
                        if (j.plugins.config[aa].currentPosition != a.html5.view.positions.NONE) {
                            var U = ab(aa, F--);
                            if (!U) {
                                Z.push(aa)
                            } else {
                                var V = U.width;
                                var ac = U.height;
                                if (Y) {
                                    delete U.width;
                                    delete U.height
                                }
                                b(j.plugins.object[aa].getDisplayElement(), U);
                                j.plugins.object[aa].resize(V, ac)
                            }
                        } else {
                            b(j.plugins.object[aa].getDisplayElement(), {display:"none"})
                        }
                    }
                }
                return Z
            }

            function I(V, W) {
                if (d.exists(j.plugins.object[V].getDisplayElement)) {
                    if (j.plugins.config[V].position && T(j.plugins.config[V].position)) {
                        if (!d.exists(j.plugins.object[V].getDisplayElement().parentNode)) {
                            R.appendChild(j.plugins.object[V].getDisplayElement())
                        }
                        var U = z(V);
                        U.zIndex = W;
                        return U
                    }
                }
                return false
            }

            function q(U, V) {
                if (!d.exists(j.plugins.object[U].getDisplayElement().parentNode)) {
                    o.appendChild(j.plugins.object[U].getDisplayElement())
                }
                return{position:"absolute", width:(d.getElementWidth(o) - d.parseDimension(o.style.right)), height:(d.getElementHeight(o) - d.parseDimension(o.style.bottom)), zIndex:V}
            }

            function e(U, V) {
                return{position:"fixed", width:j.width, height:j.height, zIndex:V}
            }

            var l = this.resizeMedia = function () {
                o.style.position = "absolute";
                var W = j.getMedia() ? j.getMedia().getDisplayElement() : u;
                if (!W) {
                    return
                }
                if (W && W.tagName.toLowerCase() == "video") {
                    if (!W.videoWidth || !W.videoHeight) {
                        W.style.width = o.style.width;
                        W.style.height = o.style.height;
                        return
                    }
                    W.style.position = "absolute";
                    d.fadeTo(W, 1, 0.25);
                    if (W.parentNode) {
                        W.parentNode.style.left = o.style.left;
                        W.parentNode.style.top = o.style.top
                    }
                    if (j.fullscreen && m.jwGetStretching() == a.utils.stretching.EXACTFIT && !d.isMobile()) {
                        var U = document.createElement("div");
                        d.stretch(a.utils.stretching.UNIFORM, U, d.getElementWidth(o), d.getElementHeight(o), A, N);
                        d.stretch(a.utils.stretching.EXACTFIT, W, d.parseDimension(U.style.width), d.parseDimension(U.style.height), W.videoWidth ? W.videoWidth : 400, W.videoHeight ? W.videoHeight : 300);
                        b(W, {left:U.style.left, top:U.style.top})
                    } else {
                        if (!c) {
                            d.stretch(m.jwGetStretching(), W, d.getElementWidth(o), d.getElementHeight(o), W.videoWidth ? W.videoWidth : 400, W.videoHeight ? W.videoHeight : 300)
                        }
                    }
                } else {
                    var V = j.plugins.object.display.getDisplayElement();
                    if (V) {
                        j.getMedia().resize(d.parseDimension(V.style.width), d.parseDimension(V.style.height))
                    } else {
                        j.getMedia().resize(d.parseDimension(o.style.width), d.parseDimension(o.style.height))
                    }
                }
            };
            var z = this.getComponentPosition = function (V) {
                var W = {position:"absolute", margin:0, padding:0, top:null};
                var U = j.plugins.config[V].currentPosition.toLowerCase();
                switch (U.toUpperCase()) {
                    case a.html5.view.positions.TOP:
                        W.top = d.parseDimension(o.style.top);
                        W.left = d.parseDimension(o.style.left);
                        W.width = d.getElementWidth(o) - d.parseDimension(o.style.left) - d.parseDimension(o.style.right);
                        W.height = j.plugins.object[V].height;
                        o.style[U] = d.parseDimension(o.style[U]) + j.plugins.object[V].height + "px";
                        o.style.height = d.getElementHeight(o) - W.height + "px";
                        break;
                    case a.html5.view.positions.RIGHT:
                        W.top = d.parseDimension(o.style.top);
                        W.right = d.parseDimension(o.style.right);
                        W.width = j.plugins.object[V].width;
                        W.height = d.getElementHeight(o) - d.parseDimension(o.style.top) - d.parseDimension(o.style.bottom);
                        o.style.width = d.getElementWidth(o) - W.width + "px";
                        break;
                    case a.html5.view.positions.BOTTOM:
                        W.left = d.parseDimension(o.style.left);
                        W.width = d.getElementWidth(o) - d.parseDimension(o.style.left) - d.parseDimension(o.style.right);
                        W.height = j.plugins.object[V].height;
                        W.bottom = d.parseDimension(o.style.bottom + r);
                        r += W.height;
                        o.style.height = d.getElementHeight(o) - W.height + "px";
                        break;
                    case a.html5.view.positions.LEFT:
                        W.top = d.parseDimension(o.style.top);
                        W.left = d.parseDimension(o.style.left);
                        W.width = j.plugins.object[V].width;
                        W.height = d.getElementHeight(o) - d.parseDimension(o.style.top) - d.parseDimension(o.style.bottom);
                        o.style[U] = d.parseDimension(o.style[U]) + j.plugins.object[V].width + "px";
                        o.style.width = d.getElementWidth(o) - W.width + "px";
                        break;
                    default:
                        break
                }
                return W
            };
            this.resize = G;
            var J, k, Q;
            var C = this.fullscreen = function (W) {
                if (c) {
                    return
                }
                var Y;
                try {
                    Y = j.getMedia().getDisplayElement()
                } catch (X) {
                }
                if (W) {
                    k = j.width;
                    Q = j.height
                }
                var aa = {position:"fixed", width:"100%", height:"100%", top:0, left:0, zIndex:2147483000}, Z = {position:"relative", height:k, width:Q, zIndex:0};
                if (B() && Y && Y.webkitSupportsFullscreen) {
                    if (W && !Y.webkitDisplayingFullscreen) {
                        try {
                            b(Y, aa);
                            d.transform(Y);
                            J = o.style.display;
                            o.style.display = "none";
                            Y.webkitEnterFullscreen()
                        } catch (V) {
                        }
                    } else {
                        if (!W) {
                            b(Y, Z);
                            l();
                            if (Y.webkitDisplayingFullscreen) {
                                try {
                                    Y.webkitExitFullscreen()
                                } catch (V) {
                                }
                            }
                            o.style.display = J
                        }
                    }
                    E = false
                } else {
                    if (W) {
                        document.onkeydown = M;
                        clearInterval(P);
                        var U = d.getBoundingClientRect(document.body);
                        j.width = Math.abs(U.left) + Math.abs(U.right);
                        j.height = window.innerHeight;
                        b(R, aa);
                        aa.zIndex = 1;
                        if (j.getMedia() && j.getMedia().getDisplayElement()) {
                            b(j.getMedia().getDisplayElement(), aa)
                        }
                        aa.zIndex = 2;
                        b(o, aa);
                        E = true
                    } else {
                        document.onkeydown = "";
                        j.width = g;
                        j.height = t;
                        b(R, Z);
                        E = false
                    }
                    G(j.width, j.height)
                }
            };

            function T(U) {
                return([a.html5.view.positions.TOP, a.html5.view.positions.RIGHT, a.html5.view.positions.BOTTOM, a.html5.view.positions.LEFT].toString().indexOf(U.toUpperCase()) > -1)
            }

            function B() {
                if (m.jwGetState() != a.api.events.state.IDLE && !E && (j.getMedia() && j.getMedia().getDisplayElement() && j.getMedia().getDisplayElement().webkitSupportsFullscreen) && d.useNativeFullscreen()) {
                    return true
                }
                return false
            }

            this.setupInstream = function (U, V) {
                d.css(_instreamArea, {display:"block", position:"absolute"});
                o.style.display = "none";
                _instreamArea.appendChild(U);
                u = V;
                S = true
            };
            var D = this.destroyInstream = function () {
                _instreamArea.style.display = "none";
                _instreamArea.innerHTML = "";
                o.style.display = "block";
                u = null;
                S = false;
                G(j.width, j.height)
            }
        };
        a.html5.view.positions = {TOP:"TOP", RIGHT:"RIGHT", BOTTOM:"BOTTOM", LEFT:"LEFT", OVER:"OVER", NONE:"NONE"}
    })(jwplayer);
    (function (a) {
        var b = {backgroundcolor:"", margin:10, font:"Arial,sans-serif", fontsize:10, fontcolor:parseInt("000000", 16), fontstyle:"normal", fontweight:"bold", buttoncolor:parseInt("ffffff", 16), position:a.html5.view.positions.BOTTOM, idlehide:false, hideplaylistcontrols:false, forcenextprev:false, layout:{left:{position:"left", elements:[
            {name:"play", type:"button"},
            {name:"divider", type:"divider"},
            {name:"prev", type:"button"},
            {name:"divider", type:"divider"},
            {name:"next", type:"button"},
            {name:"divider", type:"divider"},
            {name:"elapsed", type:"text"}
        ]}, center:{position:"center", elements:[
            {name:"time", type:"slider"}
        ]}, right:{position:"right", elements:[
            {name:"duration", type:"text"},
            {name:"blank", type:"button"},
            {name:"divider", type:"divider"},
            {name:"mute", type:"button"},
            {name:"volume", type:"slider"},
            {name:"divider", type:"divider"},
            {name:"fullscreen", type:"button"}
        ]}}};
        _utils = a.utils;
        _css = _utils.css;
        _hide = function (c) {
            _css(c, {display:"none"})
        };
        _show = function (c) {
            _css(c, {display:"block"})
        };
        a.html5.controlbar = function (m, Y) {
            window.controlbar = this;
            var l = m;
            var D = _utils.extend({}, b, l.skin.getComponentSettings("controlbar"), Y);
            if (D.position == a.html5.view.positions.NONE || typeof a.html5.view.positions[D.position] == "undefined") {
                return
            }
            if (_utils.mapLength(l.skin.getComponentLayout("controlbar")) > 0) {
                D.layout = l.skin.getComponentLayout("controlbar")
            }
            var ag;
            var R;
            var af;
            var E;
            var w = "none";
            var h;
            var k;
            var ah;
            var g;
            var f;
            var z;
            var S = {};
            var q = false;
            var c = {};
            var Q = -1;
            var ac;
            var j = false;
            var p;
            var d;
            var V = false;
            var G = false;
            var H;
            var aa = new a.html5.eventdispatcher();
            _utils.extend(this, aa);
            function K() {
                if (!ac) {
                    ac = l.skin.getSkinElement("controlbar", "background");
                    if (!ac) {
                        ac = {width:0, height:0, src:null}
                    }
                }
                return ac
            }

            function O() {
                af = 0;
                E = 0;
                R = 0;
                if (!q) {
                    var ap = {height:K().height, backgroundColor:D.backgroundcolor};
                    ag = document.createElement("div");
                    ag.id = l.id + "_jwplayer_controlbar";
                    _css(ag, ap)
                }
                var ao = (l.skin.getSkinElement("controlbar", "capLeft"));
                var an = (l.skin.getSkinElement("controlbar", "capRight"));
                if (ao) {
                    y("capLeft", "left", false, ag)
                }
                ad("background", ag, {position:"absolute", height:K().height, left:(ao ? ao.width : 0), zIndex:0}, "img");
                if (K().src) {
                    S.background.src = K().src
                }
                ad("elements", ag, {position:"relative", height:K().height, zIndex:1});
                if (an) {
                    y("capRight", "right", false, ag)
                }
            }

            this.getDisplayElement = function () {
                return ag
            };
            this.resize = function (ap, an) {
                T();
                _utils.cancelAnimation(ag);
                f = ap;
                z = an;
                if (G != l.jwGetFullscreen()) {
                    G = l.jwGetFullscreen();
                    if (!G) {
                        Z()
                    }
                    d = undefined
                }
                var ao = x();
                J({id:l.id, duration:ah, position:k});
                v({id:l.id, bufferPercent:g});
                return ao
            };
            this.show = function () {
                if (j) {
                    j = false;
                    _show(ag);
                    W()
                }
            };
            this.hide = function () {
                if (!j) {
                    j = true;
                    _hide(ag);
                    ae()
                }
            };
            function r() {
                var ao = ["timeSlider", "volumeSlider", "timeSliderRail", "volumeSliderRail"];
                for (var ap in ao) {
                    var an = ao[ap];
                    if (typeof S[an] != "undefined") {
                        c[an] = _utils.getBoundingClientRect(S[an])
                    }
                }
            }

            var e;

            function Z(an) {
                if (j) {
                    return
                }
                clearTimeout(p);
                if (D.position == a.html5.view.positions.OVER || l.jwGetFullscreen()) {
                    switch (l.jwGetState()) {
                        case a.api.events.state.PAUSED:
                        case a.api.events.state.IDLE:
                            if (ag && ag.style.opacity < 1 && (!D.idlehide || _utils.exists(an))) {
                                e = false;
                                setTimeout(function () {
                                    if (!e) {
                                        X()
                                    }
                                }, 100)
                            }
                            if (D.idlehide) {
                                p = setTimeout(function () {
                                    A()
                                }, 2000)
                            }
                            break;
                        default:
                            e = true;
                            if (an) {
                                X()
                            }
                            p = setTimeout(function () {
                                A()
                            }, 2000);
                            break
                    }
                } else {
                    X()
                }
            }

            function A() {
                if (!j) {
                    ae();
                    if (ag.style.opacity == 1) {
                        _utils.cancelAnimation(ag);
                        _utils.fadeTo(ag, 0, 0.1, 1, 0)
                    }
                }
            }

            function X() {
                if (!j) {
                    W();
                    if (ag.style.opacity == 0) {
                        _utils.cancelAnimation(ag);
                        _utils.fadeTo(ag, 1, 0.1, 0, 0)
                    }
                }
            }

            function I(an) {
                return function () {
                    if (V && d != an) {
                        d = an;
                        aa.sendEvent(an, {component:"controlbar", boundingRect:P()})
                    }
                }
            }

            var W = I(a.api.events.JWPLAYER_COMPONENT_SHOW);
            var ae = I(a.api.events.JWPLAYER_COMPONENT_HIDE);

            function P() {
                if (D.position == a.html5.view.positions.OVER || l.jwGetFullscreen()) {
                    return _utils.getDimensions(ag)
                } else {
                    return{x:0, y:0, width:0, height:0}
                }
            }

            function ad(ar, aq, ap, an) {
                var ao;
                if (!q) {
                    if (!an) {
                        an = "div"
                    }
                    ao = document.createElement(an);
                    S[ar] = ao;
                    ao.id = ag.id + "_" + ar;
                    aq.appendChild(ao)
                } else {
                    ao = document.getElementById(ag.id + "_" + ar)
                }
                if (_utils.exists(ap)) {
                    _css(ao, ap)
                }
                return ao
            }

            function N() {
                if (l.jwGetHeight() <= 40) {
                    D.layout = _utils.clone(D.layout);
                    for (var an = 0; an < D.layout.left.elements.length; an++) {
                        if (D.layout.left.elements[an].name == "fullscreen") {
                            D.layout.left.elements.splice(an, 1)
                        }
                    }
                    for (an = 0; an < D.layout.right.elements.length; an++) {
                        if (D.layout.right.elements[an].name == "fullscreen") {
                            D.layout.right.elements.splice(an, 1)
                        }
                    }
                    o()
                }
                am(D.layout.left);
                am(D.layout.center);
                am(D.layout.right)
            }

            function am(aq, an) {
                var ar = aq.position == "right" ? "right" : "left";
                var ap = _utils.extend([], aq.elements);
                if (_utils.exists(an)) {
                    ap.reverse()
                }
                var aq = ad(aq.position + "Group", S.elements, {"float":"left", styleFloat:"left", cssFloat:"left", height:"100%"});
                for (var ao = 0; ao < ap.length; ao++) {
                    C(ap[ao], ar, aq)
                }
            }

            function L() {
                return R++
            }

            function C(ar, au, aw) {
                var aq, ao, ap, an, ax;
                if (!aw) {
                    aw = S.elements
                }
                if (ar.type == "divider") {
                    y("divider" + L(), au, true, aw, undefined, ar.width, ar.element);
                    return
                }
                switch (ar.name) {
                    case"play":
                        y("playButton", au, false, aw);
                        y("pauseButton", au, true, aw);
                        U("playButton", "jwPlay");
                        U("pauseButton", "jwPause");
                        break;
                    case"prev":
                        y("prevButton", au, true, aw);
                        U("prevButton", "jwPlaylistPrev");
                        break;
                    case"stop":
                        y("stopButton", au, true, aw);
                        U("stopButton", "jwStop");
                        break;
                    case"next":
                        y("nextButton", au, true, aw);
                        U("nextButton", "jwPlaylistNext");
                        break;
                    case"elapsed":
                        y("elapsedText", au, true, aw, null, null, l.skin.getSkinElement("controlbar", "elapsedBackground"));
                        break;
                    case"time":
                        ao = !_utils.exists(l.skin.getSkinElement("controlbar", "timeSliderCapLeft")) ? 0 : l.skin.getSkinElement("controlbar", "timeSliderCapLeft").width;
                        ap = !_utils.exists(l.skin.getSkinElement("controlbar", "timeSliderCapRight")) ? 0 : l.skin.getSkinElement("controlbar", "timeSliderCapRight").width;
                        aq = au == "left" ? ao : ap;
                        ax = {height:K().height, position:"relative", "float":"left", styleFloat:"left", cssFloat:"left"};
                        var at = ad("timeSlider", aw, ax);
                        y("timeSliderCapLeft", au, true, at, "relative");
                        y("timeSliderRail", au, false, at, "relative");
                        y("timeSliderBuffer", au, false, at, "absolute");
                        y("timeSliderProgress", au, false, at, "absolute");
                        y("timeSliderThumb", au, false, at, "absolute");
                        y("timeSliderCapRight", au, true, at, "relative");
                        ab("time");
                        break;
                    case"fullscreen":
                        y("fullscreenButton", au, false, aw);
                        y("normalscreenButton", au, true, aw);
                        U("fullscreenButton", "jwSetFullscreen", true);
                        U("normalscreenButton", "jwSetFullscreen", false);
                        break;
                    case"volume":
                        ao = !_utils.exists(l.skin.getSkinElement("controlbar", "volumeSliderCapLeft")) ? 0 : l.skin.getSkinElement("controlbar", "volumeSliderCapLeft").width;
                        ap = !_utils.exists(l.skin.getSkinElement("controlbar", "volumeSliderCapRight")) ? 0 : l.skin.getSkinElement("controlbar", "volumeSliderCapRight").width;
                        aq = au == "left" ? ao : ap;
                        an = l.skin.getSkinElement("controlbar", "volumeSliderRail").width + ao + ap;
                        ax = {height:K().height, position:"relative", width:an, "float":"left", styleFloat:"left", cssFloat:"left"};
                        var av = ad("volumeSlider", aw, ax);
                        y("volumeSliderCapLeft", au, false, av, "relative");
                        y("volumeSliderRail", au, false, av, "relative");
                        y("volumeSliderProgress", au, false, av, "absolute");
                        y("volumeSliderThumb", au, false, av, "absolute");
                        y("volumeSliderCapRight", au, false, av, "relative");
                        ab("volume");
                        break;
                    case"mute":
                        y("muteButton", au, false, aw);
                        y("unmuteButton", au, true, aw);
                        U("muteButton", "jwSetMute", true);
                        U("unmuteButton", "jwSetMute", false);
                        break;
                    case"duration":
                        y("durationText", au, true, aw, null, null, l.skin.getSkinElement("controlbar", "durationBackground"));
                        break
                }
            }

            function y(aq, au, ao, ax, ar, an, ap) {
                if (_utils.exists(l.skin.getSkinElement("controlbar", aq)) || aq.indexOf("Text") > 0 || aq.indexOf("divider") === 0) {
                    var at = {height:"100%", position:ar ? ar : "relative", display:"block", "float":"left", styleFloat:"left", cssFloat:"left"};
                    if ((aq.indexOf("next") === 0 || aq.indexOf("prev") === 0) && (l.jwGetPlaylist().length < 2 || D.hideplaylistcontrols.toString() == "true")) {
                        if (D.forcenextprev.toString() != "true") {
                            ao = false;
                            at.display = "none"
                        }
                    }
                    var ay;
                    if (aq.indexOf("Text") > 0) {
                        aq.innerhtml = "00:00";
                        at.font = D.fontsize + "px/" + (K().height + 1) + "px " + D.font;
                        at.color = D.fontcolor;
                        at.textAlign = "center";
                        at.fontWeight = D.fontweight;
                        at.fontStyle = D.fontstyle;
                        at.cursor = "default";
                        if (ap) {
                            at.background = "url(" + ap.src + ") no-repeat center";
                            at.backgroundSize = "100% " + K().height + "px"
                        }
                        at.padding = "0 5px"
                    } else {
                        if (aq.indexOf("divider") === 0) {
                            if (an) {
                                if (!isNaN(parseInt(an))) {
                                    ay = parseInt(an)
                                }
                            } else {
                                if (ap) {
                                    var av = l.skin.getSkinElement("controlbar", ap);
                                    if (av) {
                                        at.background = "url(" + av.src + ") repeat-x center left";
                                        ay = av.width
                                    }
                                } else {
                                    at.background = "url(" + l.skin.getSkinElement("controlbar", "divider").src + ") repeat-x center left";
                                    ay = l.skin.getSkinElement("controlbar", "divider").width
                                }
                            }
                        } else {
                            at.background = "url(" + l.skin.getSkinElement("controlbar", aq).src + ") repeat-x center left";
                            ay = l.skin.getSkinElement("controlbar", aq).width
                        }
                    }
                    if (au == "left") {
                        if (ao) {
                            af += ay
                        }
                    } else {
                        if (au == "right") {
                            if (ao) {
                                E += ay
                            }
                        }
                    }
                    if (_utils.typeOf(ax) == "undefined") {
                        ax = S.elements
                    }
                    at.width = ay;
                    if (q) {
                        _css(S[aq], at)
                    } else {
                        var aw = ad(aq, ax, at);
                        if (_utils.exists(l.skin.getSkinElement("controlbar", aq + "Over"))) {
                            aw.onmouseover = function (az) {
                                aw.style.backgroundImage = ["url(", l.skin.getSkinElement("controlbar", aq + "Over").src, ")"].join("")
                            };
                            aw.onmouseout = function (az) {
                                aw.style.backgroundImage = ["url(", l.skin.getSkinElement("controlbar", aq).src, ")"].join("")
                            }
                        }
                        if (aq.indexOf("divider") == 0) {
                            aw.setAttribute("class", "divider")
                        }
                        aw.innerHTML = "&nbsp;"
                    }
                }
            }

            function F() {
                l.jwAddEventListener(a.api.events.JWPLAYER_PLAYLIST_LOADED, B);
                l.jwAddEventListener(a.api.events.JWPLAYER_PLAYLIST_ITEM, t);
                l.jwAddEventListener(a.api.events.JWPLAYER_MEDIA_BUFFER, v);
                l.jwAddEventListener(a.api.events.JWPLAYER_PLAYER_STATE, s);
                l.jwAddEventListener(a.api.events.JWPLAYER_MEDIA_TIME, J);
                l.jwAddEventListener(a.api.events.JWPLAYER_MEDIA_MUTE, al);
                l.jwAddEventListener(a.api.events.JWPLAYER_MEDIA_VOLUME, n);
                l.jwAddEventListener(a.api.events.JWPLAYER_MEDIA_COMPLETE, M)
            }

            function B() {
                if (!D.hideplaylistcontrols) {
                    if (l.jwGetPlaylist().length > 1 || D.forcenextprev.toString() == "true") {
                        _show(S.nextButton);
                        _show(S.prevButton)
                    } else {
                        _hide(S.nextButton);
                        _hide(S.prevButton)
                    }
                    x();
                    ai()
                }
            }

            function t(an) {
                ah = l.jwGetPlaylist()[an.index].duration;
                Q = -1;
                J({id:l.id, duration:ah, position:0});
                v({id:l.id, bufferProgress:0})
            }

            function ai() {
                J({id:l.id, duration:l.jwGetDuration(), position:0});
                v({id:l.id, bufferProgress:0});
                al({id:l.id, mute:l.jwGetMute()});
                s({id:l.id, newstate:a.api.events.state.IDLE});
                n({id:l.id, volume:l.jwGetVolume()})
            }

            function U(ap, aq, ao) {
                if (q) {
                    return
                }
                if (_utils.exists(l.skin.getSkinElement("controlbar", ap))) {
                    var an = S[ap];
                    if (_utils.exists(an)) {
                        _css(an, {cursor:"pointer"});
                        if (aq == "fullscreen") {
                            an.onmouseup = function (ar) {
                                ar.stopPropagation();
                                l.jwSetFullscreen(!l.jwGetFullscreen())
                            }
                        } else {
                            an.onmouseup = function (ar) {
                                ar.stopPropagation();
                                if (_utils.exists(ao)) {
                                    l[aq](ao)
                                } else {
                                    l[aq]()
                                }
                            }
                        }
                    }
                }
            }

            function ab(an) {
                if (q) {
                    return
                }
                var ao = S[an + "Slider"];
                _css(S.elements, {cursor:"pointer"});
                _css(ao, {cursor:"pointer"});
                ao.onmousedown = function (ap) {
                    w = an
                };
                ao.onmouseup = function (ap) {
                    ap.stopPropagation();
                    ak(ap.pageX)
                };
                ao.onmousemove = function (ap) {
                    if (w == "time") {
                        h = true;
                        var aq = ap.pageX - c[an + "Slider"].left - window.pageXOffset;
                        _css(S[w + "SliderThumb"], {left:aq})
                    }
                }
            }

            function ak(ao) {
                h = false;
                var an;
                if (w == "time") {
                    an = ao - c.timeSliderRail.left + window.pageXOffset;
                    var aq = an / c.timeSliderRail.width * ah;
                    if (aq < 0) {
                        aq = 0
                    } else {
                        if (aq > ah) {
                            aq = ah - 3
                        }
                    }
                    if (l.jwGetState() == a.api.events.state.PAUSED || l.jwGetState() == a.api.events.state.IDLE) {
                        l.jwPlay()
                    }
                    l.jwSeek(aq)
                } else {
                    if (w == "volume") {
                        an = ao - c.volumeSliderRail.left - window.pageXOffset;
                        var ap = Math.round(an / c.volumeSliderRail.width * 100);
                        if (ap < 10) {
                            ap = 0
                        } else {
                            if (ap > 100) {
                                ap = 100
                            }
                        }
                        if (l.jwGetMute()) {
                            l.jwSetMute(false)
                        }
                        l.jwSetVolume(ap)
                    }
                }
                w = "none"
            }

            function v(ao) {
                if (_utils.exists(ao.bufferPercent)) {
                    g = ao.bufferPercent
                }
                if (c.timeSliderRail) {
                    var aq = l.skin.getSkinElement("controlbar", "timeSliderCapLeft");
                    var ap = c.timeSliderRail.width;
                    var an = isNaN(Math.round(ap * g / 100)) ? 0 : Math.round(ap * g / 100);
                    _css(S.timeSliderBuffer, {width:an, left:aq ? aq.width : 0})
                }
            }

            function al(an) {
                if (an.mute) {
                    _hide(S.muteButton);
                    _show(S.unmuteButton);
                    _hide(S.volumeSliderProgress)
                } else {
                    _show(S.muteButton);
                    _hide(S.unmuteButton);
                    _show(S.volumeSliderProgress)
                }
            }

            function s(an) {
                if (an.newstate == a.api.events.state.BUFFERING || an.newstate == a.api.events.state.PLAYING) {
                    _show(S.pauseButton);
                    _hide(S.playButton)
                } else {
                    _hide(S.pauseButton);
                    _show(S.playButton)
                }
                Z();
                if (an.newstate == a.api.events.state.IDLE) {
                    _hide(S.timeSliderBuffer);
                    _hide(S.timeSliderProgress);
                    _hide(S.timeSliderThumb);
                    J({id:l.id, duration:l.jwGetDuration(), position:0})
                } else {
                    _show(S.timeSliderBuffer);
                    if (an.newstate != a.api.events.state.BUFFERING) {
                        _show(S.timeSliderProgress);
                        _show(S.timeSliderThumb)
                    }
                }
            }

            function M(an) {
                v({bufferPercent:0});
                J(_utils.extend(an, {position:0, duration:ah}))
            }

            function J(at) {
                if (_utils.exists(at.position)) {
                    k = at.position
                }
                var ao = false;
                if (_utils.exists(at.duration) && at.duration != ah) {
                    ah = at.duration;
                    ao = true
                }
                var aq = (k === ah === 0) ? 0 : k / ah;
                var av = c.timeSliderRail;
                if (av) {
                    var ap = isNaN(Math.round(av.width * aq)) ? 0 : Math.round(av.width * aq);
                    var au = l.skin.getSkinElement("controlbar", "timeSliderCapLeft");
                    var ar = ap + (au ? au.width : 0);
                    if (S.timeSliderProgress) {
                        _css(S.timeSliderProgress, {width:ap, left:au ? au.width : 0});
                        if (!h) {
                            if (S.timeSliderThumb) {
                                S.timeSliderThumb.style.left = ar + "px"
                            }
                        }
                    }
                }
                if (S.durationText) {
                    S.durationText.innerHTML = _utils.timeFormat(ah)
                }
                if (S.elapsedText) {
                    var an = _utils.timeFormat(k);
                    S.elapsedText.innerHTML = an;
                    if (Q != an.length) {
                        ao = true;
                        Q = an.length
                    }
                }
                if (ao) {
                    x()
                }
            }

            function o() {
                var an = S.elements.childNodes;
                var at, aq;
                for (var ap = 0; ap < an.length; ap++) {
                    var ar = an[ap].childNodes;
                    for (var ao in ar) {
                        if (isNaN(parseInt(ao, 10))) {
                            continue
                        }
                        if (ar[ao].id.indexOf(ag.id + "_divider") === 0 && aq && aq.id.indexOf(ag.id + "_divider") === 0 && ar[ao].style.backgroundImage == aq.style.backgroundImage) {
                            ar[ao].style.display = "none"
                        } else {
                            if (ar[ao].id.indexOf(ag.id + "_divider") === 0 && at && at.style.display != "none") {
                                ar[ao].style.display = "block"
                            }
                        }
                        if (ar[ao].style.display != "none") {
                            aq = ar[ao]
                        }
                        at = ar[ao]
                    }
                }
            }

            function aj() {
                if (l.jwGetFullscreen()) {
                    _show(S.normalscreenButton);
                    _hide(S.fullscreenButton)
                } else {
                    _hide(S.normalscreenButton);
                    _show(S.fullscreenButton)
                }
                if (l.jwGetState() == a.api.events.state.BUFFERING || l.jwGetState() == a.api.events.state.PLAYING) {
                    _show(S.pauseButton);
                    _hide(S.playButton)
                } else {
                    _hide(S.pauseButton);
                    _show(S.playButton)
                }
                if (l.jwGetMute() == true) {
                    _hide(S.muteButton);
                    _show(S.unmuteButton);
                    _hide(S.volumeSliderProgress)
                } else {
                    _show(S.muteButton);
                    _hide(S.unmuteButton);
                    _show(S.volumeSliderProgress)
                }
            }

            function x() {
                o();
                aj();
                var ap = {width:f};
                var ax = {"float":"left", styleFloat:"left", cssFloat:"left"};
                if (D.position == a.html5.view.positions.OVER || l.jwGetFullscreen()) {
                    ap.left = D.margin;
                    ap.width -= 2 * D.margin;
                    ap.top = z - K().height - D.margin;
                    ap.height = K().height
                }
                var ar = l.skin.getSkinElement("controlbar", "capLeft");
                var av = l.skin.getSkinElement("controlbar", "capRight");
                ax.width = ap.width - (ar ? ar.width : 0) - (av ? av.width : 0);
                var aq = _utils.getBoundingClientRect(S.leftGroup).width;
                var au = _utils.getBoundingClientRect(S.rightGroup).width;
                var at = ax.width - aq - au - 1;
                var ao = at;
                var an = l.skin.getSkinElement("controlbar", "timeSliderCapLeft");
                var aw = l.skin.getSkinElement("controlbar", "timeSliderCapRight");
                if (_utils.exists(an)) {
                    ao -= an.width
                }
                if (_utils.exists(aw)) {
                    ao -= aw.width
                }
                S.timeSlider.style.width = at + "px";
                S.timeSliderRail.style.width = ao + "px";
                _css(ag, ap);
                _css(S.elements, ax);
                _css(S.background, ax);
                r();
                return ap
            }

            function n(at) {
                if (_utils.exists(S.volumeSliderRail)) {
                    var ap = isNaN(at.volume / 100) ? 1 : at.volume / 100;
                    var aq = _utils.parseDimension(S.volumeSliderRail.style.width);
                    var an = isNaN(Math.round(aq * ap)) ? 0 : Math.round(aq * ap);
                    var au = _utils.parseDimension(S.volumeSliderRail.style.right);
                    var ao = (!_utils.exists(l.skin.getSkinElement("controlbar", "volumeSliderCapLeft"))) ? 0 : l.skin.getSkinElement("controlbar", "volumeSliderCapLeft").width;
                    _css(S.volumeSliderProgress, {width:an, left:ao});
                    if (S.volumeSliderThumb) {
                        var ar = (an - Math.round(_utils.parseDimension(S.volumeSliderThumb.style.width) / 2));
                        ar = Math.min(Math.max(ar, 0), aq - _utils.parseDimension(S.volumeSliderThumb.style.width));
                        _css(S.volumeSliderThumb, {left:ar})
                    }
                    if (_utils.exists(S.volumeSliderCapLeft)) {
                        _css(S.volumeSliderCapLeft, {left:0})
                    }
                }
            }

            function T() {
                try {
                    var ao = (l.id.indexOf("_instream") > 0 ? l.id.replace("_instream", "") : l.id);
                    H = document.getElementById(ao);
                    H.addEventListener("mousemove", Z)
                } catch (an) {
                    _utils.log("Could not add mouse listeners to controlbar: " + an)
                }
            }

            function u() {
                O();
                N();
                r();
                q = true;
                F();
                D.idlehide = (D.idlehide.toString().toLowerCase() == "true");
                if (D.position == a.html5.view.positions.OVER && D.idlehide) {
                    ag.style.opacity = 0;
                    V = true
                } else {
                    ag.style.opacity = 1;
                    setTimeout((function () {
                        V = true;
                        W()
                    }), 1)
                }
                T();
                ai()
            }

            u();
            return this
        }
    })(jwplayer);
    (function (b) {
        var a = ["width", "height", "state", "playlist", "item", "position", "buffer", "duration", "volume", "mute", "fullscreen"];
        var c = b.utils;
        b.html5.controller = function (o, K, f, h) {
            var n = o, m = f, j = h, y = K, M = true, G = -1, A = false, d = false, P, C = [], q = false;
            var D = (c.exists(m.config.debug) && (m.config.debug.toString().toLowerCase() == "console")), N = new b.html5.eventdispatcher(y.id, D);
            c.extend(this, N);
            function L(T) {
                if (q) {
                    N.sendEvent(T.type, T)
                } else {
                    C.push(T)
                }
            }

            function s(T) {
                if (!q) {
                    q = true;
                    N.sendEvent(b.api.events.JWPLAYER_READY, T);
                    if (b.utils.exists(window.playerReady)) {
                        playerReady(T)
                    }
                    if (b.utils.exists(window[f.config.playerReady])) {
                        window[f.config.playerReady](T)
                    }
                    while (C.length > 0) {
                        var V = C.shift();
                        N.sendEvent(V.type, V)
                    }
                    if (f.config.autostart && !b.utils.isIOS()) {
                        O()
                    }
                    while (x.length > 0) {
                        var U = x.shift();
                        B(U.method, U.arguments)
                    }
                }
            }

            m.addGlobalListener(L);
            m.addEventListener(b.api.events.JWPLAYER_MEDIA_BUFFER_FULL, function () {
                m.getMedia().play()
            });
            m.addEventListener(b.api.events.JWPLAYER_MEDIA_TIME, function (T) {
                if (T.position >= m.playlist[m.item].start && G >= 0) {
                    m.playlist[m.item].start = G;
                    G = -1
                }
            });
            m.addEventListener(b.api.events.JWPLAYER_MEDIA_COMPLETE, function (T) {
                setTimeout(E, 25)
            });
            m.addEventListener(b.api.events.JWPLAYER_PLAYLIST_LOADED, O);
            m.addEventListener(b.api.events.JWPLAYER_FULLSCREEN, p);
            function F() {
                try {
                    P = F;
                    if (!A) {
                        A = true;
                        N.sendEvent(b.api.events.JWPLAYER_MEDIA_BEFOREPLAY);
                        A = false;
                        if (d) {
                            d = false;
                            P = null;
                            return
                        }
                    }
                    v(m.item);
                    if (m.playlist[m.item].levels[0].file.length > 0) {
                        if (M || m.state == b.api.events.state.IDLE) {
                            m.getMedia().load(m.playlist[m.item]);
                            M = false
                        } else {
                            if (m.state == b.api.events.state.PAUSED) {
                                m.getMedia().play()
                            }
                        }
                    }
                    return true
                } catch (T) {
                    N.sendEvent(b.api.events.JWPLAYER_ERROR, T);
                    P = null
                }
                return false
            }

            function e() {
                try {
                    if (m.playlist[m.item].levels[0].file.length > 0) {
                        switch (m.state) {
                            case b.api.events.state.PLAYING:
                            case b.api.events.state.BUFFERING:
                                if (m.getMedia()) {
                                    m.getMedia().pause()
                                }
                                break;
                            default:
                                if (A) {
                                    d = true
                                }
                        }
                    }
                    return true
                } catch (T) {
                    N.sendEvent(b.api.events.JWPLAYER_ERROR, T)
                }
                return false
            }

            function z(T) {
                try {
                    if (m.playlist[m.item].levels[0].file.length > 0) {
                        if (typeof T != "number") {
                            T = parseFloat(T)
                        }
                        switch (m.state) {
                            case b.api.events.state.IDLE:
                                if (G < 0) {
                                    G = m.playlist[m.item].start;
                                    m.playlist[m.item].start = T
                                }
                                if (!A) {
                                    F()
                                }
                                break;
                            case b.api.events.state.PLAYING:
                            case b.api.events.state.PAUSED:
                            case b.api.events.state.BUFFERING:
                                m.seek(T);
                                break
                        }
                    }
                    return true
                } catch (U) {
                    N.sendEvent(b.api.events.JWPLAYER_ERROR, U)
                }
                return false
            }

            function w(T) {
                P = null;
                if (!c.exists(T)) {
                    T = true
                }
                try {
                    if ((m.state != b.api.events.state.IDLE || T) && m.getMedia()) {
                        m.getMedia().stop(T)
                    }
                    if (A) {
                        d = true
                    }
                    return true
                } catch (U) {
                    N.sendEvent(b.api.events.JWPLAYER_ERROR, U)
                }
                return false
            }

            function k() {
                try {
                    if (m.playlist[m.item].levels[0].file.length > 0) {
                        if (m.config.shuffle) {
                            v(S())
                        } else {
                            if (m.item + 1 == m.playlist.length) {
                                v(0)
                            } else {
                                v(m.item + 1)
                            }
                        }
                    }
                    if (m.state != b.api.events.state.IDLE) {
                        var U = m.state;
                        m.state = b.api.events.state.IDLE;
                        N.sendEvent(b.api.events.JWPLAYER_PLAYER_STATE, {oldstate:U, newstate:b.api.events.state.IDLE})
                    }
                    F();
                    return true
                } catch (T) {
                    N.sendEvent(b.api.events.JWPLAYER_ERROR, T)
                }
                return false
            }

            function I() {
                try {
                    if (m.playlist[m.item].levels[0].file.length > 0) {
                        if (m.config.shuffle) {
                            v(S())
                        } else {
                            if (m.item === 0) {
                                v(m.playlist.length - 1)
                            } else {
                                v(m.item - 1)
                            }
                        }
                    }
                    if (m.state != b.api.events.state.IDLE) {
                        var U = m.state;
                        m.state = b.api.events.state.IDLE;
                        N.sendEvent(b.api.events.JWPLAYER_PLAYER_STATE, {oldstate:U, newstate:b.api.events.state.IDLE})
                    }
                    F();
                    return true
                } catch (T) {
                    N.sendEvent(b.api.events.JWPLAYER_ERROR, T)
                }
                return false
            }

            function S() {
                var T = null;
                if (m.playlist.length > 1) {
                    while (!c.exists(T)) {
                        T = Math.floor(Math.random() * m.playlist.length);
                        if (T == m.item) {
                            T = null
                        }
                    }
                } else {
                    T = 0
                }
                return T
            }

            function H(U) {
                if (!m.playlist || !m.playlist[U]) {
                    return false
                }
                try {
                    if (m.playlist[U].levels[0].file.length > 0) {
                        var V = m.state;
                        if (V !== b.api.events.state.IDLE) {
                            if (m.playlist[m.item] && m.playlist[m.item].provider == m.playlist[U].provider) {
                                w(false)
                            } else {
                                w()
                            }
                        }
                        v(U);
                        F()
                    }
                    return true
                } catch (T) {
                    N.sendEvent(b.api.events.JWPLAYER_ERROR, T)
                }
                return false
            }

            function v(T) {
                if (!m.playlist[T]) {
                    return
                }
                m.setActiveMediaProvider(m.playlist[T]);
                if (m.item != T) {
                    m.item = T;
                    M = true;
                    N.sendEvent(b.api.events.JWPLAYER_PLAYLIST_ITEM, {index:T})
                }
            }

            function g(U) {
                try {
                    v(m.item);
                    var V = m.getMedia();
                    switch (typeof(U)) {
                        case"number":
                            V.volume(U);
                            break;
                        case"string":
                            V.volume(parseInt(U, 10));
                            break
                    }
                    m.setVolume(U);
                    return true
                } catch (T) {
                    N.sendEvent(b.api.events.JWPLAYER_ERROR, T)
                }
                return false
            }

            function r(U) {
                try {
                    v(m.item);
                    var V = m.getMedia();
                    if (typeof U == "undefined") {
                        V.mute(!m.mute);
                        m.setMute(!m.mute)
                    } else {
                        if (U.toString().toLowerCase() == "true") {
                            V.mute(true);
                            m.setMute(true)
                        } else {
                            V.mute(false);
                            m.setMute(false)
                        }
                    }
                    return true
                } catch (T) {
                    N.sendEvent(b.api.events.JWPLAYER_ERROR, T)
                }
                return false
            }

            function J(U, T) {
                try {
                    m.width = U;
                    m.height = T;
                    j.resize(U, T);
                    N.sendEvent(b.api.events.JWPLAYER_RESIZE, {width:m.width, height:m.height});
                    return true
                } catch (V) {
                    N.sendEvent(b.api.events.JWPLAYER_ERROR, V)
                }
                return false
            }

            function u(U, V) {
                try {
                    if (typeof U == "undefined") {
                        U = !m.fullscreen
                    }
                    if (typeof V == "undefined") {
                        V = true
                    }
                    if (U != m.fullscreen) {
                        m.fullscreen = (U.toString().toLowerCase() == "true");
                        j.fullscreen(m.fullscreen);
                        if (V) {
                            N.sendEvent(b.api.events.JWPLAYER_FULLSCREEN, {fullscreen:m.fullscreen})
                        }
                        N.sendEvent(b.api.events.JWPLAYER_RESIZE, {width:m.width, height:m.height})
                    }
                    return true
                } catch (T) {
                    N.sendEvent(b.api.events.JWPLAYER_ERROR, T)
                }
                return false
            }

            function R(T) {
                try {
                    w();
                    if (A) {
                        d = false
                    }
                    m.loadPlaylist(T);
                    if (m.playlist[m.item].provider) {
                        v(m.item);
                        if (m.config.autostart.toString().toLowerCase() == "true" && !c.isIOS() && !A) {
                            F()
                        }
                        return true
                    } else {
                        return false
                    }
                } catch (U) {
                    N.sendEvent(b.api.events.JWPLAYER_ERROR, U)
                }
                return false
            }

            function O(T) {
                if (!c.isIOS()) {
                    v(m.item);
                    if (m.config.autostart.toString().toLowerCase() == "true" && !c.isIOS()) {
                        F()
                    }
                }
            }

            function p(T) {
                u(T.fullscreen, false)
            }

            function t() {
                try {
                    return m.getMedia().detachMedia()
                } catch (T) {
                    return null
                }
            }

            function l() {
                try {
                    var T = m.getMedia().attachMedia();
                    if (typeof P == "function") {
                        P()
                    }
                } catch (U) {
                    return null
                }
            }

            b.html5.controller.repeatoptions = {LIST:"LIST", ALWAYS:"ALWAYS", SINGLE:"SINGLE", NONE:"NONE"};
            function E() {
                if (m.state != b.api.events.state.IDLE) {
                    return
                }
                P = E;
                switch (m.config.repeat.toUpperCase()) {
                    case b.html5.controller.repeatoptions.SINGLE:
                        F();
                        break;
                    case b.html5.controller.repeatoptions.ALWAYS:
                        if (m.item == m.playlist.length - 1 && !m.config.shuffle) {
                            H(0)
                        } else {
                            k()
                        }
                        break;
                    case b.html5.controller.repeatoptions.LIST:
                        if (m.item == m.playlist.length - 1 && !m.config.shuffle) {
                            w();
                            v(0)
                        } else {
                            k()
                        }
                        break;
                    default:
                        w();
                        break
                }
            }

            var x = [];

            function Q(T) {
                return function () {
                    if (q) {
                        B(T, arguments)
                    } else {
                        x.push({method:T, arguments:arguments})
                    }
                }
            }

            function B(V, U) {
                var T = [];
                for (i = 0; i < U.length; i++) {
                    T.push(U[i])
                }
                V.apply(this, T)
            }

            this.play = Q(F);
            this.pause = Q(e);
            this.seek = Q(z);
            this.stop = Q(w);
            this.next = Q(k);
            this.prev = Q(I);
            this.item = Q(H);
            this.setVolume = Q(g);
            this.setMute = Q(r);
            this.resize = Q(J);
            this.setFullscreen = Q(u);
            this.load = Q(R);
            this.playerReady = s;
            this.detachMedia = t;
            this.attachMedia = l;
            this.beforePlay = function () {
                return A
            };
            this.destroy = function () {
                if (m.getMedia()) {
                    m.getMedia().destroy()
                }
            }
        }
    })(jwplayer);
    (function (a) {
        a.html5.defaultSkin = function () {
            this.text = '<?xml version="1.0" ?><skin author="LongTail Video" name="Five" version="1.1"><components><component name="controlbar"><settings><setting name="margin" value="20"/><setting name="fontsize" value="11"/><setting name="fontcolor" value="0x000000"/></settings><layout><group position="left"><button name="play"/><divider name="divider"/><button name="prev"/><divider name="divider"/><button name="next"/><divider name="divider"/><text name="elapsed"/></group><group position="center"><slider name="time"/></group><group position="right"><text name="duration"/><divider name="divider"/><button name="blank"/><divider name="divider"/><button name="mute"/><slider name="volume"/><divider name="divider"/><button name="fullscreen"/></group></layout><elements><element name="background" src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAIAAABvFaqvAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAElJREFUOI3t1LERACAMQlFgGvcfxNIhHMK4gsUvUviOmgtNsiAZkBSEKxKEnCYkkQrJn/YwbUNiSDDYRZaQRDaShv+oX9GBZEIuK+8hXVLs+/YAAAAASUVORK5CYII="/><element name="blankButton" src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAUAAAAYCAYAAAAyJzegAAAAFElEQVQYV2P8//8/AzpgHBUc7oIAGZdH0RjKN8EAAAAASUVORK5CYII="/><element name="capLeft" src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAAYCAYAAAA7zJfaAAAAQElEQVQIWz3LsRGAMADDQJ0XB5bMINABZ9GENGrszxhjT2WLSqxEJG2JQrTMdV2q5LpOAvyRaVmsi7WdeZ/7+AAaOTq7BVrfOQAAAABJRU5ErkJggg=="/><element name="capRight" src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAAYCAYAAAA7zJfaAAAAQElEQVQIWz3LsRGAMADDQJ0XB5bMINABZ9GENGrszxhjT2WLSqxEJG2JQrTMdV2q5LpOAvyRaVmsi7WdeZ/7+AAaOTq7BVrfOQAAAABJRU5ErkJggg=="/><element name="divider" src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAAYCAIAAAC0rgCNAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAADhJREFUCB0FwcENgEAAw7Aq+893g8APUILNOQcbFRktVGqUVFRkWNz3xTa2sUaLNUosKlRUvvf5AdbWOTtzmzyWAAAAAElFTkSuQmCC"/><element name="playButton" src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABUAAAAYCAYAAAAVibZIAAAANUlEQVR42u2RsQkAAAjD/NTTPaW6dXLrINJA1kBpGPMAjDWmOgp1HFQXx+b1KOefO4oxY57R73YnVYCQUCQAAAAASUVORK5CYII="/><element name="pauseButton" src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABUAAAAYCAYAAAAVibZIAAAAIUlEQVQ4jWNgGAWjYOiD/0gYG3/U0FFDB4Oho2AUDAYAAEwiL9HrpdMVAAAAAElFTkSuQmCC"/><element name="prevButton" src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABUAAAAYCAYAAAAVibZIAAAAQklEQVQ4y2NgGAWjYOiD/1AMA/JAfB5NjCJD/YH4PRaLyDa0H4lNNUP/DxlD59PCUBCIp3ZEwYA+NZLUKBgFgwEAAN+HLX9sB8u8AAAAAElFTkSuQmCC"/><element name="nextButton" src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABUAAAAYCAYAAAAVibZIAAAAQElEQVQ4y2NgGAWjYOiD/0B8Hojl0cT+U2ooCL8HYn9qGwrD/bQw9P+QMXQ+tSMqnpoRBUpS+tRMUqNgFAwGAADxZy1/mHvFnAAAAABJRU5ErkJggg=="/><element name="timeSliderRail" src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAAAOElEQVRIDe3BwQkAIRADwAhhw/nU/kWwUK+KPITMABFh19Y+F0acY8CJvX9wYpXgRElwolSIiMf9ZWEDhtwurFsAAAAASUVORK5CYII="/><element name="timeSliderBuffer" src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAAAN0lEQVRIDe3BwQkAMQwDMBcc55mRe9zi7RR+FCwBEWG39vcfGHFm4MTuhhMlwYlVBSdKhYh43AW/LQMKm1spzwAAAABJRU5ErkJggg=="/><element name="timeSliderProgress" src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAAAIElEQVRIiWNgGAWjYBTQBfynMR61YCRYMApGwSigMQAAiVWPcbq6UkIAAAAASUVORK5CYII="/><element name="timeSliderThumb" src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAMAAAAYCAYAAAA/OUfnAAAAO0lEQVQYlWP4//8/Awwz0JgDBP/BeN6Cxf/hnI2btiI4u/fsQ3AOHjqK4Jw4eQbBOX/hEoKDYjSd/AMA4cS4mfLsorgAAAAASUVORK5CYII="/><element name="muteButton" src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAYCAYAAADKx8xXAAAAJklEQVQ4y2NgGAUjDcwH4v/kaPxPikZkxcNVI9mBQ5XoGAWDFwAAsKAXKQQmfbUAAAAASUVORK5CYII="/><element name="unmuteButton" src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAYCAYAAADKx8xXAAAAMklEQVQ4y2NgGAWDHPyntub5xBr6Hwv/Pzk2/yfVG/8psRFE25Oq8T+tQnsIaB4FVAcAi2YVysVY52AAAAAASUVORK5CYII="/><element name="volumeSliderRail" src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYAgMAAACdGdVrAAAACVBMVEUAAACmpqampqbBXAu8AAAAAnRSTlMAgJsrThgAAAArSURBVAhbY2AgErBAyA4I2QEhOyBkB4TsYOhAoaCCUCUwDTDtMMNgRuMHAFB5FoGH5T0UAAAAAElFTkSuQmCC"/><element name="volumeSliderProgress" src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYAgMAAACdGdVrAAAACVBMVEUAAAAAAAAAAACDY+nAAAAAAnRSTlMAgJsrThgAAAArSURBVAhbY2AgErBAyA4I2QEhOyBkB4TsYOhAoaCCUCUwDTDtMMNgRuMHAFB5FoGH5T0UAAAAAElFTkSuQmCC"/><element name="volumeSliderCapRight" src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAUAAAAYCAYAAAAyJzegAAAAFElEQVQYV2P8//8/AzpgHBUc7oIAGZdH0RjKN8EAAAAASUVORK5CYII="/><element name="fullscreenButton" src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAAAQklEQVRIiWNgGAWjYMiD/0iYFDmSLbDHImdPLQtgBpEiR7Zl2NijAA5oEkT/0Whi5UiyAJ8BVMsHNMtoo2AUDAIAAGdcIN3IDNXoAAAAAElFTkSuQmCC"/><element name="normalscreenButton" src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAAAP0lEQVRIx2NgGAWjYMiD/1RSQ5QB/wmIUWzJfzx8qhj+n4DYCAY0DyJ7PBbYU8sHMEvwiZFtODXUjIJRMJgBACpWIN2ZxdPTAAAAAElFTkSuQmCC"/></elements></component><component name="display"><elements><element name="background" src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADIAAAAyAQMAAAAk8RryAAAABlBMVEUAAAAAAAClZ7nPAAAAAnRSTlOZpuml+rYAAAASSURBVBhXY2AYJuA/GBwY6jQAyDyoK8QcL4QAAAAASUVORK5CYII="/><element name="playIcon" src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADIAAAAyCAYAAAAeP4ixAAAAiUlEQVR42u3XSw2AMBREURwgAQlIQAISKgUpSEFKJeCg5b0E0kWBTVcD9ySTsL0Jn9IBAAAA+K2UUrBlW/Rr5ZDoIeeuoFkxJD9ss03aIXXQqB9SttoG7ZA6qNcOKdttiwcJh9RB+iFl4SshkRBuLR72+9cvH0SOKI2HRo7x/Fi1/uoCAAAAwLsD8ki99IlO2dQAAAAASUVORK5CYII="/><element name="muteIcon" src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADIAAAAyCAYAAAAeP4ixAAAAVUlEQVR42u3WMQrAIAxAUW/g/SdvGmvpoOBeSHgPsjj5QTANAACARCJilIhYM0tEvJM+Ik3Id9E957kQIb+F3OdCPC0hPkQriqWx9hp/x/QGAABQyAPLB22VGrpLDgAAAABJRU5ErkJggg=="/><element name="errorIcon" src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADIAAAAyCAYAAAAeP4ixAAAA/0lEQVR42u2U0QmEMBAF7cASLMESUoIlpARLSCkpwRJSgiWkhOvAXD4WsgRkyaG5DbyB+Yvg8KITAAAAAAAYk+u61mwk15EjPtlEfihmqIiZR1Qx80ghjgdUuiHXGHSVsoag0x6x8DUoyjD5KovmEJ9NTDMRPIT0mtdIUkjlonuNohO+Ha99DTmkuGgKCTcvebAzx82ZoCWC3/3aIMWSRucaxcjORSFY4xpFdjYJGp1rFGcyCYZ/RVh6AUnfcNZ2zih3/mGj1jVCdiNDwyrq1rA/xMdeEXvDVdnYc1vDc3uPkDObXrlaxbNHSOohQhr/WOeLEWfWTgAAAAAAADzNF9sHJ7PJ57MlAAAAAElFTkSuQmCC"/><element name="bufferIcon" src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADIAAAAyCAYAAAAeP4ixAAACBklEQVR42u3Zv0sCYRzH8USTzOsHHEWGkC1HgaDgkktGDjUYtDQ01RDSljQ1BLU02+rk1NTm2NLq4Nx/0L/h9fnCd3j4cnZe1/U8xiO8h3uurufF0/3COd/3/0UWYiEWYiEWYiGJQ+J8xuPxKhXjEMZANinjIZhkGuVRNioE4wVURo4JkHm0xKWmhRAc1bh1EyCUw5BcBIjHiApKa4CErko6DEJwuRo6IRKzyJD8FJAyI3Zp2zRImiBcRhlfo5RtlxCcE3CcDNpGrhYIT2IhAJKilO0VRmzJ32fAMTpBTS0QMfGwlcuKMRftE0DJ0wCJdcOsCkBdXP3Mh9CEFUBTPS9mDZJBG6io4aqVzMdCokCw9H3kT6j/C/9iDdSeUMNC7DkyyxAs/Rk6Qss8FPWRZgdVtUH4DjxEn1zxh+/zj1wHlf4MQhNGrwqA6sY40U8JonRJwEQh+AO3AvCG6gHv4U7IY4krxkroWoAOkoQMGfCBrgIm+YBGqPENpIJ66CJg3x66Y0gnSUidAEEnNr9jjLiWMn5DiWP0OC/oAsCgkq43xBdGDMQr7YASP/vEkHvdl1+JOCcEV5sC4hGEOzTlPuKgd0b0xD4JkRcOgnRRTjdErkYhAsQVq6IdUuPJtmk7BCL3t/h88cx91pKQkI/pkDx6pmYTIjEoxiHsN1YWYiEWYiEWknhflZ5IErA5nr8AAAAASUVORK5CYII="/></elements></component><component name="dock"><settings><setting name="fontcolor" value="0xffffff"/></settings><elements><element name="button" src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADIAAAAyAQMAAAAk8RryAAAABlBMVEUAAAAAAAClZ7nPAAAAAnRSTlOZpuml+rYAAAASSURBVBhXY2AYJuA/GBwY6jQAyDyoK8QcL4QAAAAASUVORK5CYII="/></elements></component><component name="playlist"><settings><setting name="backgroundcolor" value="0xe8e8e8"/></settings><elements><element name="item" src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADwAAAA8CAIAAAC1nk4lAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAHBJREFUaN7t2MENwCAMBEEe9N8wSKYC/D8YV7CyJoRkVtVImxkZPQInMxoP0XiIxkM0HsGbjjSNBx544IEHHnjggUe/6UQeey0PIh7XTftGxKPj4eXCtLsHHh+ZxkO0Iw8PR55Ni8ZD9Hu/EAoP0dc5RRg9qeRjVF8AAAAASUVORK5CYII="/><element name="sliderCapTop" src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABUAAAAHCAYAAADnCQYGAAAAFUlEQVQokWP8//8/A7UB46ihI9hQAKt6FPPXhVGHAAAAAElFTkSuQmCC"/><element name="sliderRail" src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABUAAAAUCAYAAABiS3YzAAAAKElEQVQ4y2P4//8/Az68bNmy/+iYkB6GUUNHDR01dNTQUUNHDaXcUABUDOKhcxnsSwAAAABJRU5ErkJggg=="/><element name="sliderThumb" src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABUAAAAUCAYAAABiS3YzAAAAJUlEQVQ4T2P4//8/Ay4MBP9xYbz6Rg0dNXTU0FFDRw0dNZRyQwHH4NBa7GJsXAAAAABJRU5ErkJggg=="/><element name="sliderCapBottom" src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABUAAAAHCAYAAADnCQYGAAAAFUlEQVQokWP8//8/A7UB46ihI9hQAKt6FPPXhVGHAAAAAElFTkSuQmCC"/></elements></component></components></skin>';
            this.xml = null;
            if (window.DOMParser) {
                parser = new DOMParser();
                this.xml = parser.parseFromString(this.text, "text/xml")
            } else {
                this.xml = new ActiveXObject("Microsoft.XMLDOM");
                this.xml.async = "false";
                this.xml.loadXML(this.text)
            }
            return this
        }
    })(jwplayer);
    (function (a) {
        _utils = a.utils;
        _css = _utils.css;
        _hide = function (b) {
            _css(b, {display:"none"})
        };
        _show = function (b) {
            _css(b, {display:"block"})
        };
        a.html5.display = function (k, K) {
            var j = {icons:true, showmute:false};
            var X = _utils.extend({}, j, K);
            var h = k;
            var W = {};
            var e;
            var w;
            var z;
            var T;
            var u;
            var M;
            var E;
            var N = !_utils.exists(h.skin.getComponentSettings("display").bufferrotation) ? 15 : parseInt(h.skin.getComponentSettings("display").bufferrotation, 10);
            var s = !_utils.exists(h.skin.getComponentSettings("display").bufferinterval) ? 100 : parseInt(h.skin.getComponentSettings("display").bufferinterval, 10);
            var D = -1;
            var v = a.api.events.state.IDLE;
            var O = true;
            var d;
            var C = false, V = true;
            var p = "";
            var g = false;
            var o = false;
            var m;
            var y, R;
            var L = new a.html5.eventdispatcher();
            _utils.extend(this, L);
            var H = {display:{style:{cursor:"pointer", top:0, left:0, overflow:"hidden"}, click:n}, display_icon:{style:{cursor:"pointer", position:"absolute", top:((h.skin.getSkinElement("display", "background").height - h.skin.getSkinElement("display", "playIcon").height) / 2), left:((h.skin.getSkinElement("display", "background").width - h.skin.getSkinElement("display", "playIcon").width) / 2), border:0, margin:0, padding:0, zIndex:3, display:"none"}}, display_iconBackground:{style:{cursor:"pointer", position:"absolute", top:((w - h.skin.getSkinElement("display", "background").height) / 2), left:((e - h.skin.getSkinElement("display", "background").width) / 2), border:0, backgroundImage:(["url(", h.skin.getSkinElement("display", "background").src, ")"]).join(""), width:h.skin.getSkinElement("display", "background").width, height:h.skin.getSkinElement("display", "background").height, margin:0, padding:0, zIndex:2, display:"none"}}, display_image:{style:{display:"none", width:e, height:w, position:"absolute", cursor:"pointer", left:0, top:0, margin:0, padding:0, textDecoration:"none", zIndex:1}}, display_text:{style:{zIndex:4, position:"relative", opacity:0.8, backgroundColor:parseInt("000000", 16), color:parseInt("ffffff", 16), textAlign:"center", fontFamily:"Arial,sans-serif", padding:"0 5px", fontSize:14}}};
            h.jwAddEventListener(a.api.events.JWPLAYER_PLAYER_STATE, q);
            h.jwAddEventListener(a.api.events.JWPLAYER_MEDIA_MUTE, q);
            h.jwAddEventListener(a.api.events.JWPLAYER_PLAYLIST_LOADED, P);
            h.jwAddEventListener(a.api.events.JWPLAYER_PLAYLIST_ITEM, q);
            h.jwAddEventListener(a.api.events.JWPLAYER_ERROR, r);
            Q();
            function Q() {
                W.display = G("div", "display");
                W.display_text = G("div", "display_text");
                W.display.appendChild(W.display_text);
                W.display_image = G("img", "display_image");
                W.display_image.onerror = function (Y) {
                    _hide(W.display_image)
                };
                W.display_image.onload = B;
                W.display_icon = G("div", "display_icon");
                W.display_iconBackground = G("div", "display_iconBackground");
                W.display.appendChild(W.display_image);
                W.display_iconBackground.appendChild(W.display_icon);
                W.display.appendChild(W.display_iconBackground);
                f();
                setTimeout((function () {
                    o = true;
                    if (X.icons.toString() == "true") {
                        J()
                    }
                }), 1)
            }

            this.getDisplayElement = function () {
                return W.display
            };
            this.resize = function (Z, Y) {
                if (h.jwGetFullscreen() && _utils.isMobile()) {
                    return
                }
                _css(W.display, {width:Z, height:Y});
                _css(W.display_text, {width:(Z - 10), top:((Y - _utils.getBoundingClientRect(W.display_text).height) / 2)});
                _css(W.display_iconBackground, {top:((Y - h.skin.getSkinElement("display", "background").height) / 2), left:((Z - h.skin.getSkinElement("display", "background").width) / 2)});
                if (e != Z || w != Y) {
                    e = Z;
                    w = Y;
                    d = undefined;
                    J()
                }
                if (!h.jwGetFullscreen()) {
                    y = Z;
                    R = Y
                }
                c();
                q({})
            };
            this.show = function () {
                if (g) {
                    g = false;
                    t(h.jwGetState())
                }
            };
            this.hide = function () {
                if (!g) {
                    F();
                    g = true
                }
            };
            function B(Y) {
                z = W.display_image.naturalWidth;
                T = W.display_image.naturalHeight;
                c();
                if (h.jwGetState() == a.api.events.state.IDLE || h.jwGetPlaylist()[h.jwGetPlaylistIndex()].provider == "sound") {
                    _css(W.display_image, {display:"block", opacity:0});
                    _utils.fadeTo(W.display_image, 1, 0.1)
                }
                C = false
            }

            function c() {
                if (h.jwGetFullscreen() && h.jwGetStretching() == a.utils.stretching.EXACTFIT) {
                    var Y = document.createElement("div");
                    _utils.stretch(a.utils.stretching.UNIFORM, Y, e, w, y, R);
                    _utils.stretch(a.utils.stretching.EXACTFIT, W.display_image, _utils.parseDimension(Y.style.width), _utils.parseDimension(Y.style.height), z, T);
                    _css(W.display_image, {left:Y.style.left, top:Y.style.top})
                } else {
                    _utils.stretch(h.jwGetStretching(), W.display_image, e, w, z, T)
                }
            }

            function G(Y, aa) {
                var Z = document.createElement(Y);
                Z.id = h.id + "_jwplayer_" + aa;
                _css(Z, H[aa].style);
                return Z
            }

            function f() {
                for (var Y in W) {
                    if (_utils.exists(H[Y].click)) {
                        W[Y].onclick = H[Y].click
                    }
                }
            }

            function n(Y) {
                if (typeof Y.preventDefault != "undefined") {
                    Y.preventDefault()
                } else {
                    Y.returnValue = false
                }
                if (typeof m == "function") {
                    m(Y);
                    return
                } else {
                    if (h.jwGetState() != a.api.events.state.PLAYING) {
                        h.jwPlay()
                    } else {
                        h.jwPause()
                    }
                }
            }

            function U(Y) {
                if (E) {
                    F();
                    return
                }
                W.display_icon.style.backgroundImage = (["url(", h.skin.getSkinElement("display", Y).src, ")"]).join("");
                _css(W.display_icon, {width:h.skin.getSkinElement("display", Y).width, height:h.skin.getSkinElement("display", Y).height, top:(h.skin.getSkinElement("display", "background").height - h.skin.getSkinElement("display", Y).height) / 2, left:(h.skin.getSkinElement("display", "background").width - h.skin.getSkinElement("display", Y).width) / 2});
                b();
                if (_utils.exists(h.skin.getSkinElement("display", Y + "Over"))) {
                    W.display_icon.onmouseover = function (Z) {
                        W.display_icon.style.backgroundImage = ["url(", h.skin.getSkinElement("display", Y + "Over").src, ")"].join("")
                    };
                    W.display_icon.onmouseout = function (Z) {
                        W.display_icon.style.backgroundImage = ["url(", h.skin.getSkinElement("display", Y).src, ")"].join("")
                    }
                } else {
                    W.display_icon.onmouseover = null;
                    W.display_icon.onmouseout = null
                }
            }

            function F() {
                if (X.icons.toString() == "true") {
                    _hide(W.display_icon);
                    _hide(W.display_iconBackground);
                    S()
                }
            }

            function b() {
                if (!g && X.icons.toString() == "true") {
                    _show(W.display_icon);
                    _show(W.display_iconBackground);
                    J()
                }
            }

            function r(Y) {
                E = true;
                F();
                W.display_text.innerHTML = Y.message;
                _show(W.display_text);
                W.display_text.style.top = ((w - _utils.getBoundingClientRect(W.display_text).height) / 2) + "px"
            }

            function I() {
                V = false;
                W.display_image.style.display = "none"
            }

            function P() {
                v = ""
            }

            function q(Y) {
                if ((Y.type == a.api.events.JWPLAYER_PLAYER_STATE || Y.type == a.api.events.JWPLAYER_PLAYLIST_ITEM) && E) {
                    E = false;
                    _hide(W.display_text)
                }
                var Z = h.jwGetState();
                if (Z == v) {
                    return
                }
                v = Z;
                if (D >= 0) {
                    clearTimeout(D)
                }
                if (O || h.jwGetState() == a.api.events.state.PLAYING || h.jwGetState() == a.api.events.state.PAUSED) {
                    t(h.jwGetState())
                } else {
                    D = setTimeout(l(h.jwGetState()), 500)
                }
            }

            function l(Y) {
                return(function () {
                    t(Y)
                })
            }

            function t(Y) {
                if (_utils.exists(M)) {
                    clearInterval(M);
                    M = null;
                    _utils.animations.rotate(W.display_icon, 0)
                }
                switch (Y) {
                    case a.api.events.state.BUFFERING:
                        if (_utils.isIPod()) {
                            I();
                            F()
                        } else {
                            if (h.jwGetPlaylist()[h.jwGetPlaylistIndex()].provider == "sound") {
                                x()
                            }
                            u = 0;
                            M = setInterval(function () {
                                u += N;
                                _utils.animations.rotate(W.display_icon, u % 360)
                            }, s);
                            U("bufferIcon");
                            O = true
                        }
                        break;
                    case a.api.events.state.PAUSED:
                        if (!_utils.isIPod()) {
                            if (h.jwGetPlaylist()[h.jwGetPlaylistIndex()].provider != "sound") {
                                _css(W.display_image, {background:"transparent no-repeat center center"})
                            }
                            U("playIcon");
                            O = true
                        }
                        break;
                    case a.api.events.state.IDLE:
                        if (h.jwGetPlaylist()[h.jwGetPlaylistIndex()] && h.jwGetPlaylist()[h.jwGetPlaylistIndex()].image) {
                            x()
                        } else {
                            I()
                        }
                        U("playIcon");
                        O = true;
                        break;
                    default:
                        if (h.jwGetPlaylist()[h.jwGetPlaylistIndex()] && h.jwGetPlaylist()[h.jwGetPlaylistIndex()].provider == "sound") {
                            if (_utils.isIPod()) {
                                I();
                                O = false
                            } else {
                                x()
                            }
                        } else {
                            I();
                            O = false
                        }
                        if (h.jwGetMute() && X.showmute) {
                            U("muteIcon")
                        } else {
                            F()
                        }
                        break
                }
                D = -1
            }

            function x() {
                if (h.jwGetPlaylist()[h.jwGetPlaylistIndex()]) {
                    var Y = h.jwGetPlaylist()[h.jwGetPlaylistIndex()].image;
                    if (Y) {
                        if (Y != p) {
                            p = Y;
                            C = true;
                            W.display_image.src = _utils.getAbsolutePath(Y)
                        } else {
                            if (!(C || V)) {
                                V = true;
                                W.display_image.style.opacity = 0;
                                W.display_image.style.display = "block";
                                _utils.fadeTo(W.display_image, 1, 0.1)
                            }
                        }
                    }
                }
            }

            function A(Y) {
                return function () {
                    if (!o) {
                        return
                    }
                    if (!g && d != Y) {
                        d = Y;
                        L.sendEvent(Y, {component:"display", boundingRect:_utils.getDimensions(W.display_iconBackground)})
                    }
                }
            }

            var J = A(a.api.events.JWPLAYER_COMPONENT_SHOW);
            var S = A(a.api.events.JWPLAYER_COMPONENT_HIDE);
            this.setAlternateClickHandler = function (Y) {
                m = Y
            };
            this.revertAlternateClickHandler = function () {
                m = undefined
            };
            return this
        }
    })(jwplayer);
    (function (a) {
        var c = a.utils;
        var b = c.css;
        a.html5.dock = function (w, D) {
            function x() {
                return{align:a.html5.view.positions.RIGHT}
            }

            var n = c.extend({}, x(), D);
            if (n.align == "FALSE") {
                return
            }
            var j = {};
            var A = [];
            var k;
            var F;
            var f = false;
            var C = false;
            var g = {x:0, y:0, width:0, height:0};
            var z;
            var o;
            var y;
            var m = new a.html5.eventdispatcher();
            c.extend(this, m);
            var r = document.createElement("div");
            r.id = w.id + "_jwplayer_dock";
            r.style.opacity = 1;
            p();
            w.jwAddEventListener(a.api.events.JWPLAYER_PLAYER_STATE, q);
            this.getDisplayElement = function () {
                return r
            };
            this.setButton = function (K, H, I, J) {
                if (!H && j[K]) {
                    c.arrays.remove(A, K);
                    r.removeChild(j[K].div);
                    delete j[K]
                } else {
                    if (H) {
                        if (!j[K]) {
                            j[K] = {}
                        }
                        j[K].handler = H;
                        j[K].outGraphic = I;
                        j[K].overGraphic = J;
                        if (!j[K].div) {
                            A.push(K);
                            j[K].div = document.createElement("div");
                            j[K].div.style.position = "absolute";
                            r.appendChild(j[K].div);
                            j[K].div.appendChild(document.createElement("div"));
                            j[K].div.childNodes[0].style.position = "relative";
                            j[K].div.childNodes[0].style.width = "100%";
                            j[K].div.childNodes[0].style.height = "100%";
                            j[K].div.childNodes[0].style.zIndex = 10;
                            j[K].div.childNodes[0].style.cursor = "pointer";
                            j[K].div.appendChild(document.createElement("img"));
                            j[K].div.childNodes[1].style.position = "absolute";
                            j[K].div.childNodes[1].style.left = 0;
                            j[K].div.childNodes[1].style.top = 0;
                            if (w.skin.getSkinElement("dock", "button")) {
                                j[K].div.childNodes[1].src = w.skin.getSkinElement("dock", "button").src
                            }
                            j[K].div.childNodes[1].style.zIndex = 9;
                            j[K].div.childNodes[1].style.cursor = "pointer";
                            j[K].div.onmouseover = function () {
                                if (j[K].overGraphic) {
                                    j[K].div.childNodes[0].style.background = h(j[K].overGraphic)
                                }
                                if (w.skin.getSkinElement("dock", "buttonOver")) {
                                    j[K].div.childNodes[1].src = w.skin.getSkinElement("dock", "buttonOver").src
                                }
                            };
                            j[K].div.onmouseout = function () {
                                if (j[K].outGraphic) {
                                    j[K].div.childNodes[0].style.background = h(j[K].outGraphic)
                                }
                                if (w.skin.getSkinElement("dock", "button")) {
                                    j[K].div.childNodes[1].src = w.skin.getSkinElement("dock", "button").src
                                }
                            };
                            if (w.skin.getSkinElement("dock", "button")) {
                                j[K].div.childNodes[1].src = w.skin.getSkinElement("dock", "button").src
                            }
                        }
                        if (j[K].outGraphic) {
                            j[K].div.childNodes[0].style.background = h(j[K].outGraphic)
                        } else {
                            if (j[K].overGraphic) {
                                j[K].div.childNodes[0].style.background = h(j[K].overGraphic)
                            }
                        }
                        if (H) {
                            j[K].div.onclick = function (L) {
                                L.preventDefault();
                                a(w.id).callback(K);
                                if (j[K].overGraphic) {
                                    j[K].div.childNodes[0].style.background = h(j[K].overGraphic)
                                }
                                if (w.skin.getSkinElement("dock", "button")) {
                                    j[K].div.childNodes[1].src = w.skin.getSkinElement("dock", "button").src
                                }
                            }
                        }
                    }
                }
                l(k, F)
            };
            function h(H) {
                return"url(" + H + ") no-repeat center center"
            }

            function t(H) {
            }

            function l(H, T) {
                p();
                if (A.length > 0) {
                    var I = 10;
                    var S = I;
                    var P = -1;
                    var Q = w.skin.getSkinElement("dock", "button").height;
                    var O = w.skin.getSkinElement("dock", "button").width;
                    var M = H - O - I;
                    var R, L;
                    if (n.align == a.html5.view.positions.LEFT) {
                        P = 1;
                        M = I
                    }
                    for (var J = 0; J < A.length; J++) {
                        var U = Math.floor(S / T);
                        if ((S + Q + I) > ((U + 1) * T)) {
                            S = ((U + 1) * T) + I;
                            U = Math.floor(S / T)
                        }
                        var K = j[A[J]].div;
                        K.style.top = (S % T) + "px";
                        K.style.left = (M + (w.skin.getSkinElement("dock", "button").width + I) * U * P) + "px";
                        var N = {x:c.parseDimension(K.style.left), y:c.parseDimension(K.style.top), width:O, height:Q};
                        if (!R || (N.x <= R.x && N.y <= R.y)) {
                            R = N
                        }
                        if (!L || (N.x >= L.x && N.y >= L.y)) {
                            L = N
                        }
                        K.style.width = O + "px";
                        K.style.height = Q + "px";
                        S += w.skin.getSkinElement("dock", "button").height + I
                    }
                    g = {x:R.x, y:R.y, width:L.x - R.x + L.width, height:R.y - L.y + L.height}
                }
                if (C != w.jwGetFullscreen() || k != H || F != T) {
                    k = H;
                    F = T;
                    C = w.jwGetFullscreen();
                    z = undefined;
                    setTimeout(s, 1)
                }
            }

            function d(H) {
                return function () {
                    if (!f && z != H && A.length > 0) {
                        z = H;
                        m.sendEvent(H, {component:"dock", boundingRect:g})
                    }
                }
            }

            function q(H) {
                if (c.isMobile()) {
                    if (H.newstate == a.api.events.state.IDLE) {
                        v()
                    } else {
                        e()
                    }
                } else {
                    B()
                }
            }

            function B(H) {
                if (f) {
                    return
                }
                clearTimeout(y);
                if (D.position == a.html5.view.positions.OVER || w.jwGetFullscreen()) {
                    switch (w.jwGetState()) {
                        case a.api.events.state.PAUSED:
                        case a.api.events.state.IDLE:
                            if (r && r.style.opacity < 1 && (!D.idlehide || c.exists(H))) {
                                E()
                            }
                            if (D.idlehide) {
                                y = setTimeout(function () {
                                    u()
                                }, 2000)
                            }
                            break;
                        default:
                            if (c.exists(H)) {
                                E()
                            }
                            y = setTimeout(function () {
                                u()
                            }, 2000);
                            break
                    }
                } else {
                    E()
                }
            }

            var s = d(a.api.events.JWPLAYER_COMPONENT_SHOW);
            var G = d(a.api.events.JWPLAYER_COMPONENT_HIDE);
            this.resize = l;
            var v = function () {
                b(r, {display:"block"});
                if (f) {
                    f = false;
                    s()
                }
            };
            var e = function () {
                b(r, {display:"none"});
                if (!f) {
                    G();
                    f = true
                }
            };

            function u() {
                if (!f) {
                    G();
                    if (r.style.opacity == 1) {
                        c.cancelAnimation(r);
                        c.fadeTo(r, 0, 0.1, 1, 0)
                    }
                }
            }

            function E() {
                if (!f) {
                    s();
                    if (r.style.opacity == 0) {
                        c.cancelAnimation(r);
                        c.fadeTo(r, 1, 0.1, 0, 0)
                    }
                }
            }

            function p() {
                try {
                    o = document.getElementById(w.id);
                    o.addEventListener("mousemove", B)
                } catch (H) {
                    c.log("Could not add mouse listeners to dock: " + H)
                }
            }

            this.hide = e;
            this.show = v;
            return this
        }
    })(jwplayer);
    (function (a) {
        a.html5.eventdispatcher = function (d, b) {
            var c = new a.events.eventdispatcher(b);
            a.utils.extend(this, c);
            this.sendEvent = function (e, f) {
                if (!a.utils.exists(f)) {
                    f = {}
                }
                a.utils.extend(f, {id:d, version:a.version, type:e});
                c.sendEvent(e, f)
            }
        }
    })(jwplayer);
    (function (a) {
        var b = a.utils;
        a.html5.instream = function (y, m, x, z) {
            var t = {controlbarseekable:"always", controlbarpausable:true, controlbarstoppable:true, playlistclickable:true};
            var v, A, C = y, E = m, j = x, w = z, r, H, o, G, e, f, g, l, q, h = false, k, d, n = this;
            this.load = function (M, K) {
                c();
                h = true;
                A = b.extend(t, K);
                v = a.html5.playlistitem(M);
                F();
                d = document.createElement("div");
                d.id = n.id + "_instream_container";
                w.detachMedia();
                r = g.getDisplayElement();
                f = E.playlist[E.item];
                e = C.jwGetState();
                if (e == a.api.events.state.BUFFERING || e == a.api.events.state.PLAYING) {
                    r.pause()
                }
                H = r.src ? r.src : r.currentSrc;
                o = r.innerHTML;
                G = r.currentTime;
                q = new a.html5.display(n, b.extend({}, E.plugins.config.display));
                q.setAlternateClickHandler(function (N) {
                    if (_fakemodel.state == a.api.events.state.PAUSED) {
                        n.jwInstreamPlay()
                    } else {
                        D(a.api.events.JWPLAYER_INSTREAM_CLICK, N)
                    }
                });
                d.appendChild(q.getDisplayElement());
                if (!b.isMobile()) {
                    l = new a.html5.controlbar(n, b.extend({}, E.plugins.config.controlbar, {}));
                    if (E.plugins.config.controlbar.position == a.html5.view.positions.OVER) {
                        d.appendChild(l.getDisplayElement())
                    } else {
                        var L = E.plugins.object.controlbar.getDisplayElement().parentNode;
                        L.appendChild(l.getDisplayElement())
                    }
                }
                j.setupInstream(d, r);
                p();
                g.load(v)
            };
            this.jwInstreamDestroy = function (K) {
                if (!h) {
                    return
                }
                h = false;
                if (e != a.api.events.state.IDLE) {
                    g.load(f, false);
                    g.stop(false)
                } else {
                    g.stop(true)
                }
                g.detachMedia();
                j.destroyInstream();
                if (l) {
                    try {
                        l.getDisplayElement().parentNode.removeChild(l.getDisplayElement())
                    } catch (L) {
                    }
                }
                D(a.api.events.JWPLAYER_INSTREAM_DESTROYED, {reason:(K ? "complete" : "destroyed")}, true);
                w.attachMedia();
                if (e == a.api.events.state.BUFFERING || e == a.api.events.state.PLAYING) {
                    r.play();
                    if (E.playlist[E.item] == f) {
                        E.getMedia().seek(G)
                    }
                }
                return
            };
            this.jwInstreamAddEventListener = function (K, L) {
                k.addEventListener(K, L)
            };
            this.jwInstreamRemoveEventListener = function (K, L) {
                k.removeEventListener(K, L)
            };
            this.jwInstreamPlay = function () {
                if (!h) {
                    return
                }
                g.play(true)
            };
            this.jwInstreamPause = function () {
                if (!h) {
                    return
                }
                g.pause(true)
            };
            this.jwInstreamSeek = function (K) {
                if (!h) {
                    return
                }
                g.seek(K)
            };
            this.jwInstreamGetState = function () {
                if (!h) {
                    return undefined
                }
                return _fakemodel.state
            };
            this.jwInstreamGetPosition = function () {
                if (!h) {
                    return undefined
                }
                return _fakemodel.position
            };
            this.jwInstreamGetDuration = function () {
                if (!h) {
                    return undefined
                }
                return _fakemodel.duration
            };
            this.playlistClickable = function () {
                return(!h || A.playlistclickable.toString().toLowerCase() == "true")
            };
            function s() {
                _fakemodel = new a.html5.model(this, E.getMedia() ? E.getMedia().getDisplayElement() : E.container, E);
                k = new a.html5.eventdispatcher();
                C.jwAddEventListener(a.api.events.JWPLAYER_RESIZE, p);
                C.jwAddEventListener(a.api.events.JWPLAYER_FULLSCREEN, p)
            }

            function c() {
                _fakemodel.setMute(E.mute);
                _fakemodel.setVolume(E.volume)
            }

            function F() {
                if (!g) {
                    g = new a.html5.mediavideo(_fakemodel, E.getMedia() ? E.getMedia().getDisplayElement() : E.container);
                    g.addGlobalListener(I);
                    g.addEventListener(a.api.events.JWPLAYER_MEDIA_META, J);
                    g.addEventListener(a.api.events.JWPLAYER_MEDIA_COMPLETE, u);
                    g.addEventListener(a.api.events.JWPLAYER_MEDIA_BUFFER_FULL, B)
                }
                g.attachMedia()
            }

            function I(K) {
                if (h) {
                    D(K.type, K)
                }
            }

            function B(K) {
                if (h) {
                    g.play()
                }
            }

            function u(K) {
                if (h) {
                    setTimeout(function () {
                        n.jwInstreamDestroy(true)
                    }, 10)
                }
            }

            function J(K) {
                if (K.metadata.width && K.metadata.height) {
                    j.resizeMedia()
                }
            }

            function D(K, L, M) {
                if (h || M) {
                    k.sendEvent(K, L)
                }
            }

            function p() {
                var K = E.plugins.object.display.getDisplayElement().style;
                if (l) {
                    var L = E.plugins.object.controlbar.getDisplayElement().style;
                    l.resize(b.parseDimension(K.width), b.parseDimension(K.height));
                    _css(l.getDisplayElement(), b.extend({}, L, {zIndex:1001, opacity:1}))
                }
                if (q) {
                    q.resize(b.parseDimension(K.width), b.parseDimension(K.height));
                    _css(q.getDisplayElement(), b.extend({}, K, {zIndex:1000}))
                }
                if (j) {
                    j.resizeMedia()
                }
            }

            this.jwPlay = function (K) {
                if (A.controlbarpausable.toString().toLowerCase() == "true") {
                    this.jwInstreamPlay()
                }
            };
            this.jwPause = function (K) {
                if (A.controlbarpausable.toString().toLowerCase() == "true") {
                    this.jwInstreamPause()
                }
            };
            this.jwStop = function () {
                if (A.controlbarstoppable.toString().toLowerCase() == "true") {
                    this.jwInstreamDestroy();
                    C.jwStop()
                }
            };
            this.jwSeek = function (K) {
                switch (A.controlbarseekable.toLowerCase()) {
                    case"always":
                        this.jwInstreamSeek(K);
                        break;
                    case"backwards":
                        if (_fakemodel.position > K) {
                            this.jwInstreamSeek(K)
                        }
                        break
                }
            };
            this.jwGetPosition = function () {
            };
            this.jwGetDuration = function () {
            };
            this.jwGetWidth = C.jwGetWidth;
            this.jwGetHeight = C.jwGetHeight;
            this.jwGetFullscreen = C.jwGetFullscreen;
            this.jwSetFullscreen = C.jwSetFullscreen;
            this.jwGetVolume = function () {
                return E.volume
            };
            this.jwSetVolume = function (K) {
                g.volume(K);
                C.jwSetVolume(K)
            };
            this.jwGetMute = function () {
                return E.mute
            };
            this.jwSetMute = function (K) {
                g.mute(K);
                C.jwSetMute(K)
            };
            this.jwGetState = function () {
                return _fakemodel.state
            };
            this.jwGetPlaylist = function () {
                return[v]
            };
            this.jwGetPlaylistIndex = function () {
                return 0
            };
            this.jwGetStretching = function () {
                return E.config.stretching
            };
            this.jwAddEventListener = function (L, K) {
                k.addEventListener(L, K)
            };
            this.jwRemoveEventListener = function (L, K) {
                k.removeEventListener(L, K)
            };
            this.skin = C.skin;
            this.id = C.id + "_instream";
            s();
            return this
        }
    })(jwplayer);
    (function (a) {
        var b = {prefix:"http://l.longtailvideo.com/html5/", file:"logo.png", link:"http://www.longtailvideo.com/players/jw-flv-player/", linktarget:"_top", margin:8, out:0.5, over:1, timeout:5, hide:true, position:"bottom-left"};
        _css = a.utils.css;
        a.html5.logo = function (n, r) {
            var q = n;
            var u;
            var d;
            var t;
            var h = false;
            g();
            function g() {
                o();
                q.jwAddEventListener(a.api.events.JWPLAYER_PLAYER_STATE, j);
                c();
                l()
            }

            function o() {
                if (b.prefix) {
                    var v = n.version.split(/\W/).splice(0, 2).join("/");
                    if (b.prefix.indexOf(v) < 0) {
                        b.prefix += v + "/"
                    }
                }
                if (r.position == a.html5.view.positions.OVER) {
                    r.position = b.position
                }
                try {
                    if (window.location.href.indexOf("https") == 0) {
                        b.prefix = b.prefix.replace("http://l.longtailvideo.com", "https://securel.longtailvideo.com")
                    }
                } catch (w) {
                }
                d = a.utils.extend({}, b)
            }

            function c() {
                t = document.createElement("img");
                t.id = q.id + "_jwplayer_logo";
                t.style.display = "none";
                t.onload = function (v) {
                    _css(t, k());
                    p()
                };
                if (!d.file) {
                    return
                }
                if (d.file.indexOf("/") >= 0) {
                    t.src = d.file
                } else {
                    t.src = d.prefix + d.file
                }
            }

            if (!d.file) {
                return
            }
            this.resize = function (w, v) {
            };
            this.getDisplayElement = function () {
                return t
            };
            function l() {
                if (d.link) {
                    t.onmouseover = f;
                    t.onmouseout = p;
                    t.onclick = s
                } else {
                    this.mouseEnabled = false
                }
            }

            function s(v) {
                if (typeof v != "undefined") {
                    v.stopPropagation()
                }
                if (!h) {
                    return
                }
                q.jwPause();
                q.jwSetFullscreen(false);
                if (d.link) {
                    window.open(d.link, d.linktarget)
                }
                return
            }

            function p(v) {
                if (d.link && h) {
                    t.style.opacity = d.out
                }
                return
            }

            function f(v) {
                if (h) {
                    t.style.opacity = d.over
                }
                return
            }

            function k() {
                var x = {textDecoration:"none", position:"absolute", cursor:"pointer"};
                x.display = (d.hide.toString() == "true" && !h) ? "none" : "block";
                var w = d.position.toLowerCase().split("-");
                for (var v in w) {
                    x[w[v]] = parseInt(d.margin)
                }
                return x
            }

            function m() {
                if (d.hide.toString() == "true") {
                    t.style.display = "block";
                    t.style.opacity = 0;
                    a.utils.fadeTo(t, d.out, 0.1, parseFloat(t.style.opacity));
                    u = setTimeout(function () {
                        e()
                    }, d.timeout * 1000)
                }
                h = true
            }

            function e() {
                h = false;
                if (d.hide.toString() == "true") {
                    a.utils.fadeTo(t, 0, 0.1, parseFloat(t.style.opacity))
                }
            }

            function j(v) {
                if (v.newstate == a.api.events.state.BUFFERING) {
                    clearTimeout(u);
                    m()
                }
            }

            return this
        }
    })(jwplayer);
    (function (b) {
        var d = {ended:b.api.events.state.IDLE, playing:b.api.events.state.PLAYING, pause:b.api.events.state.PAUSED, buffering:b.api.events.state.BUFFERING};
        var f = b.utils;
        var a = f.isMobile();
        var g, e;
        var c = {};
        b.html5.mediavideo = function (k, I) {
            var M = {abort:A, canplay:r, canplaythrough:r, durationchange:w, emptied:A, ended:r, error:q, loadeddata:w, loadedmetadata:w, loadstart:r, pause:r, play:A, playing:r, progress:G, ratechange:A, seeked:r, seeking:r, stalled:r, suspend:r, timeupdate:Q, volumechange:n, waiting:r, canshowcurrentframe:A, dataunavailable:A, empty:A, load:j, loadedfirstframe:A, webkitfullscreenchange:m};
            var E = {};
            var N = new b.html5.eventdispatcher();
            f.extend(this, N);
            var l = k, D = I, o, h, F, W, H, P, O = false, v = false, z = false, L, J, T;
            U();
            this.load = function (Y, Z) {
                if (typeof Z == "undefined") {
                    Z = true
                }
                if (!v) {
                    return
                }
                W = Y;
                z = (W.duration > 0);
                l.duration = W.duration;
                f.empty(o);
                o.style.display = "block";
                o.style.opacity = 1;
                if (g && e) {
                    o.style.width = g;
                    o.style.height = e;
                    g = _previousHieght = 0
                }
                T = 0;
                s(Y.levels);
                if (Y.levels && Y.levels.length > 0) {
                    if (Y.levels.length == 1 || f.isIOS()) {
                        o.src = Y.levels[0].file
                    } else {
                        if (o.src) {
                            o.removeAttribute("src")
                        }
                        for (var X = 0; X < Y.levels.length; X++) {
                            var aa = o.ownerDocument.createElement("source");
                            aa.src = Y.levels[X].file;
                            o.appendChild(aa);
                            T++
                        }
                    }
                } else {
                    o.src = Y.file
                }
                o.volume = l.volume / 100;
                o.muted = l.mute;
                if (a) {
                    S()
                }
                L = J = F = false;
                l.buffer = 0;
                if (!f.exists(Y.start)) {
                    Y.start = 0
                }
                P = (Y.start > 0) ? Y.start : -1;
                u(b.api.events.JWPLAYER_MEDIA_LOADED);
                if ((!a && Y.levels.length == 1) || !O) {
                    o.load()
                }
                O = false;
                if (Z) {
                    y(b.api.events.state.BUFFERING);
                    u(b.api.events.JWPLAYER_MEDIA_BUFFER, {bufferPercent:0});
                    C()
                }
                if (o.videoWidth > 0 && o.videoHeight > 0) {
                    w()
                }
            };
            this.play = function () {
                if (!v) {
                    return
                }
                C();
                if (J) {
                    y(b.api.events.state.PLAYING)
                } else {
                    o.load();
                    y(b.api.events.state.BUFFERING)
                }
                o.play()
            };
            this.pause = function () {
                if (!v) {
                    return
                }
                o.pause();
                y(b.api.events.state.PAUSED)
            };
            this.seek = function (X) {
                if (!v) {
                    return
                }
                if (!F && o.readyState > 0) {
                    if (!(l.duration <= 0 || isNaN(l.duration)) && !(l.position <= 0 || isNaN(l.position))) {
                        o.currentTime = X;
                        o.play()
                    }
                } else {
                    P = X
                }
            };
            var B = this.stop = function (X) {
                if (!v) {
                    return
                }
                if (!f.exists(X)) {
                    X = true
                }
                t();
                if (X) {
                    J = false;
                    var Y = navigator.userAgent;
                    if (o.webkitSupportsFullscreen) {
                        try {
                            o.webkitExitFullscreen()
                        } catch (Z) {
                        }
                    }
                    o.style.opacity = 0;
                    x();
                    if (f.isIE()) {
                        o.src = ""
                    } else {
                        o.removeAttribute("src")
                    }
                    f.empty(o);
                    o.load();
                    O = true
                }
                if (f.isIPod()) {
                    g = o.style.width;
                    e = o.style.height;
                    o.style.width = 0;
                    o.style.height = 0
                } else {
                    if (f.isIPad()) {
                        o.style.display = "none";
                        try {
                            o.webkitExitFullscreen()
                        } catch (aa) {
                        }
                    }
                }
                y(b.api.events.state.IDLE)
            };
            this.fullscreen = function (X) {
                if (X === true) {
                    this.resize("100%", "100%")
                } else {
                    this.resize(l.config.width, l.config.height)
                }
            };
            this.resize = function (Y, X) {
            };
            this.volume = function (X) {
                if (!a) {
                    o.volume = X / 100;
                    u(b.api.events.JWPLAYER_MEDIA_VOLUME, {volume:(X / 100)})
                }
            };
            this.mute = function (X) {
                if (!a) {
                    o.muted = X;
                    u(b.api.events.JWPLAYER_MEDIA_MUTE, {mute:X})
                }
            };
            this.getDisplayElement = function () {
                return o
            };
            this.hasChrome = function () {
                return a && (h == b.api.events.state.PLAYING)
            };
            this.detachMedia = function () {
                v = false;
                return this.getDisplayElement()
            };
            this.attachMedia = function () {
                v = true
            };
            this.destroy = function () {
                if (o && o.parentNode) {
                    t();
                    for (var X in M) {
                        o.removeEventListener(X, K(X, M[X]), true)
                    }
                    f.empty(o);
                    D = o.parentNode;
                    o.parentNode.removeChild(o);
                    delete c[l.id];
                    o = null
                }
            };
            function K(Y, X) {
                if (E[Y]) {
                    return E[Y]
                } else {
                    E[Y] = function (Z) {
                        if (f.exists(Z.target.parentNode)) {
                            X(Z)
                        }
                    };
                    return E[Y]
                }
            }

            function U() {
                h = b.api.events.state.IDLE;
                v = true;
                o = p();
                o.setAttribute("x-webkit-airplay", "allow");
                if (D.parentNode) {
                    o.id = D.id;
                    D.parentNode.replaceChild(o, D)
                }
            }

            function p() {
                var X = c[l.id];
                if (!X) {
                    if (D.tagName.toLowerCase() == "video") {
                        X = D
                    } else {
                        X = document.createElement("video")
                    }
                    c[l.id] = X;
                    if (!X.id) {
                        X.id = D.id
                    }
                }
                for (var Y in M) {
                    X.addEventListener(Y, K(Y, M[Y]), true)
                }
                return X
            }

            function y(X) {
                if (X == b.api.events.state.PAUSED && h == b.api.events.state.IDLE) {
                    return
                }
                if (a) {
                    switch (X) {
                        case b.api.events.state.PLAYING:
                            S();
                            break;
                        case b.api.events.state.BUFFERING:
                        case b.api.events.state.PAUSED:
                            x();
                            break
                    }
                }
                if (h != X) {
                    var Y = h;
                    l.state = h = X;
                    u(b.api.events.JWPLAYER_PLAYER_STATE, {oldstate:Y, newstate:X})
                }
            }

            function A(X) {
            }

            function n(X) {
                var Y = Math.round(o.volume * 100);
                u(b.api.events.JWPLAYER_MEDIA_VOLUME, {volume:Y}, true);
                u(b.api.events.JWPLAYER_MEDIA_MUTE, {mute:o.muted}, true)
            }

            function G(Z) {
                if (!v) {
                    return
                }
                var Y;
                if (f.exists(Z) && Z.lengthComputable && Z.total) {
                    Y = Z.loaded / Z.total * 100
                } else {
                    if (f.exists(o.buffered) && (o.buffered.length > 0)) {
                        var X = o.buffered.length - 1;
                        if (X >= 0) {
                            Y = o.buffered.end(X) / o.duration * 100
                        }
                    }
                }
                if (f.useNativeFullscreen() && f.exists(o.webkitDisplayingFullscreen)) {
                    if (l.fullscreen != o.webkitDisplayingFullscreen) {
                        u(b.api.events.JWPLAYER_FULLSCREEN, {fullscreen:o.webkitDisplayingFullscreen}, true)
                    }
                }
                if (J === false && h == b.api.events.state.BUFFERING) {
                    u(b.api.events.JWPLAYER_MEDIA_BUFFER_FULL);
                    J = true
                }
                if (!L) {
                    if (Y == 100) {
                        L = true
                    }
                    if (f.exists(Y) && (Y > l.buffer)) {
                        l.buffer = Math.round(Y);
                        u(b.api.events.JWPLAYER_MEDIA_BUFFER, {bufferPercent:Math.round(Y)})
                    }
                }
            }

            function Q(Y) {
                if (!v) {
                    return
                }
                if (f.exists(Y) && f.exists(Y.target)) {
                    if (z > 0) {
                        if (!isNaN(Y.target.duration) && (isNaN(l.duration) || l.duration < 1)) {
                            if (Y.target.duration == Infinity) {
                                l.duration = 0
                            } else {
                                l.duration = Math.round(Y.target.duration * 10) / 10
                            }
                        }
                    }
                    if (!F && o.readyState > 0) {
                        y(b.api.events.state.PLAYING)
                    }
                    if (h == b.api.events.state.PLAYING) {
                        if (o.readyState > 0 && (P > -1 || !F)) {
                            F = true;
                            try {
                                if (o.currentTime != P && P > -1) {
                                    o.currentTime = P;
                                    P = -1
                                }
                            } catch (X) {
                            }
                            o.volume = l.volume / 100;
                            o.muted = l.mute
                        }
                        l.position = l.duration > 0 ? (Math.round(Y.target.currentTime * 10) / 10) : 0;
                        u(b.api.events.JWPLAYER_MEDIA_TIME, {position:l.position, duration:l.duration});
                        if (l.position >= l.duration && (l.position > 0 || l.duration > 0)) {
                            R();
                            return
                        }
                    }
                }
                G(Y)
            }

            function j(X) {
            }

            function r(X) {
                if (!v) {
                    return
                }
                if (g && e) {
                    o.style.width = g;
                    o.style.height = e;
                    g = _previousHieght = 0
                }
                if (d[X.type]) {
                    if (X.type == "ended") {
                        R()
                    } else {
                        y(d[X.type])
                    }
                }
            }

            function w(Y) {
                if (!v) {
                    return
                }
                var X = Math.round(o.duration * 10) / 10;
                var Z = {height:o.videoHeight, width:o.videoWidth, duration:X};
                if (!z) {
                    if ((l.duration < X || isNaN(l.duration)) && o.duration != Infinity) {
                        l.duration = X
                    }
                }
                u(b.api.events.JWPLAYER_MEDIA_META, {metadata:Z})
            }

            function q(Z) {
                if (!v) {
                    return
                }
                if (h == b.api.events.state.IDLE) {
                    return
                }
                var Y = "There was an error: ";
                if ((Z.target.error && Z.target.tagName.toLowerCase() == "video") || Z.target.parentNode.error && Z.target.parentNode.tagName.toLowerCase() == "video") {
                    var X = !f.exists(Z.target.error) ? Z.target.parentNode.error : Z.target.error;
                    switch (X.code) {
                        case X.MEDIA_ERR_ABORTED:
                            f.log("User aborted the video playback.");
                            return;
                        case X.MEDIA_ERR_NETWORK:
                            Y = "A network error caused the video download to fail part-way: ";
                            break;
                        case X.MEDIA_ERR_DECODE:
                            Y = "The video playback was aborted due to a corruption problem or because the video used features your browser did not support: ";
                            break;
                        case X.MEDIA_ERR_SRC_NOT_SUPPORTED:
                            Y = "The video could not be loaded, either because the server or network failed or because the format is not supported: ";
                            break;
                        default:
                            Y = "An unknown error occurred: ";
                            break
                    }
                } else {
                    if (Z.target.tagName.toLowerCase() == "source") {
                        T--;
                        if (T > 0) {
                            return
                        }
                        if (f.userAgentMatch(/firefox/i)) {
                            f.log("The video could not be loaded, either because the server or network failed or because the format is not supported.");
                            B(false);
                            return
                        } else {
                            Y = "The video could not be loaded, either because the server or network failed or because the format is not supported: "
                        }
                    } else {
                        f.log("An unknown error occurred.  Continuing...");
                        return
                    }
                }
                B(false);
                Y += V();
                _error = true;
                u(b.api.events.JWPLAYER_ERROR, {message:Y});
                return
            }

            function V() {
                var Z = "";
                for (var Y in W.levels) {
                    var X = W.levels[Y];
                    var aa = D.ownerDocument.createElement("source");
                    Z += b.utils.getAbsolutePath(X.file);
                    if (Y < (W.levels.length - 1)) {
                        Z += ", "
                    }
                }
                return Z
            }

            function C() {
                if (!f.exists(H)) {
                    H = setInterval(function () {
                        G()
                    }, 100)
                }
            }

            function t() {
                clearInterval(H);
                H = null
            }

            function R() {
                if (h == b.api.events.state.PLAYING) {
                    B(false);
                    u(b.api.events.JWPLAYER_MEDIA_BEFORECOMPLETE);
                    u(b.api.events.JWPLAYER_MEDIA_COMPLETE)
                }
            }

            function m(X) {
                if (f.exists(o.webkitDisplayingFullscreen)) {
                    if (l.fullscreen && !o.webkitDisplayingFullscreen) {
                        u(b.api.events.JWPLAYER_FULLSCREEN, {fullscreen:false}, true)
                    }
                }
            }

            function s(Z) {
                if (Z.length > 0 && f.userAgentMatch(/Safari/i) && !f.userAgentMatch(/Chrome/i)) {
                    var X = -1;
                    for (var Y = 0; Y < Z.length; Y++) {
                        switch (f.extension(Z[Y].file)) {
                            case"mp4":
                                if (X < 0) {
                                    X = Y
                                }
                                break;
                            case"webm":
                                Z.splice(Y, 1);
                                break
                        }
                    }
                    if (X > 0) {
                        var aa = Z.splice(X, 1)[0];
                        Z.unshift(aa)
                    }
                }
            }

            function S() {
                setTimeout(function () {
                    o.setAttribute("controls", "controls")
                }, 100)
            }

            function x() {
                setTimeout(function () {
                    o.removeAttribute("controls")
                }, 250)
            }

            function u(X, Z, Y) {
                if (v || Y) {
                    if (Z) {
                        N.sendEvent(X, Z)
                    } else {
                        N.sendEvent(X)
                    }
                }
            }
        }
    })(jwplayer);
    (function (a) {
        var c = {ended:a.api.events.state.IDLE, playing:a.api.events.state.PLAYING, pause:a.api.events.state.PAUSED, buffering:a.api.events.state.BUFFERING};
        var b = a.utils.css;
        a.html5.mediayoutube = function (j, e) {
            var f = new a.html5.eventdispatcher();
            a.utils.extend(this, f);
            var l = j;
            var h = document.getElementById(e.id);
            var g = a.api.events.state.IDLE;
            var n, m;

            function k(p) {
                if (g != p) {
                    var q = g;
                    l.state = p;
                    g = p;
                    f.sendEvent(a.api.events.JWPLAYER_PLAYER_STATE, {oldstate:q, newstate:p})
                }
            }

            this.getDisplayElement = this.detachMedia = function () {
                return h
            };
            this.attachMedia = function () {
            };
            this.play = function () {
                if (g == a.api.events.state.IDLE) {
                    f.sendEvent(a.api.events.JWPLAYER_MEDIA_BUFFER, {bufferPercent:100});
                    f.sendEvent(a.api.events.JWPLAYER_MEDIA_BUFFER_FULL);
                    k(a.api.events.state.PLAYING)
                } else {
                    if (g == a.api.events.state.PAUSED) {
                        k(a.api.events.state.PLAYING)
                    }
                }
            };
            this.pause = function () {
                k(a.api.events.state.PAUSED)
            };
            this.seek = function (p) {
            };
            this.stop = function (p) {
                if (!_utils.exists(p)) {
                    p = true
                }
                l.position = 0;
                k(a.api.events.state.IDLE);
                if (p) {
                    b(h, {display:"none"})
                }
            };
            this.volume = function (p) {
                l.setVolume(p);
                f.sendEvent(a.api.events.JWPLAYER_MEDIA_VOLUME, {volume:Math.round(p)})
            };
            this.mute = function (p) {
                h.muted = p;
                f.sendEvent(a.api.events.JWPLAYER_MEDIA_MUTE, {mute:p})
            };
            this.resize = function (q, p) {
                if (q * p > 0 && n) {
                    n.width = m.width = q;
                    n.height = m.height = p
                }
            };
            this.fullscreen = function (p) {
                if (p === true) {
                    this.resize("100%", "100%")
                } else {
                    this.resize(l.config.width, l.config.height)
                }
            };
            this.load = function (p) {
                o(p);
                b(n, {display:"block"});
                k(a.api.events.state.BUFFERING);
                f.sendEvent(a.api.events.JWPLAYER_MEDIA_BUFFER, {bufferPercent:0});
                f.sendEvent(a.api.events.JWPLAYER_MEDIA_LOADED);
                this.play()
            };
            this.hasChrome = function () {
                return(g != a.api.events.state.IDLE)
            };
            function o(v) {
                var s = v.levels[0].file;
                s = ["http://www.youtube.com/v/", d(s), "&amp;hl=en_US&amp;fs=1&autoplay=1"].join("");
                n = document.createElement("object");
                n.id = h.id;
                n.style.position = "absolute";
                var u = {movie:s, allowfullscreen:"true", allowscriptaccess:"always"};
                for (var p in u) {
                    var t = document.createElement("param");
                    t.name = p;
                    t.value = u[p];
                    n.appendChild(t)
                }
                m = document.createElement("embed");
                n.appendChild(m);
                var q = {src:s, type:"application/x-shockwave-flash", allowfullscreen:"true", allowscriptaccess:"always", width:n.width, height:n.height};
                for (var r in q) {
                    m.setAttribute(r, q[r])
                }
                n.appendChild(m);
                n.style.zIndex = 2147483000;
                if (h != n && h.parentNode) {
                    h.parentNode.replaceChild(n, h)
                }
                h = n
            }

            function d(q) {
                var p = q.split(/\?|\#\!/);
                var s = "";
                for (var r = 0; r < p.length; r++) {
                    if (p[r].substr(0, 2) == "v=") {
                        s = p[r].substr(2)
                    }
                }
                if (s == "") {
                    if (q.indexOf("/v/") >= 0) {
                        s = q.substr(q.indexOf("/v/") + 3)
                    } else {
                        if (q.indexOf("youtu.be") >= 0) {
                            s = q.substr(q.indexOf("youtu.be/") + 9)
                        } else {
                            s = q
                        }
                    }
                }
                if (s.indexOf("?") > -1) {
                    s = s.substr(0, s.indexOf("?"))
                }
                if (s.indexOf("&") > -1) {
                    s = s.substr(0, s.indexOf("&"))
                }
                return s
            }

            this.embed = m;
            return this
        }
    })(jwplayer);
    (function (jwplayer) {
        var _configurableStateVariables = ["width", "height", "start", "duration", "volume", "mute", "fullscreen", "item", "plugins", "stretching"];
        var _utils = jwplayer.utils;
        jwplayer.html5.model = function (api, container, options) {
            var _api = api;
            var _container = container;
            var _cookies = _utils.getCookies();
            var _model = {id:_container.id, playlist:[], state:jwplayer.api.events.state.IDLE, position:0, buffer:0, container:_container, config:{width:480, height:320, item:-1, skin:undefined, file:undefined, image:undefined, start:0, duration:0, bufferlength:5, volume:_cookies.volume ? _cookies.volume : 90, mute:_cookies.mute && _cookies.mute.toString().toLowerCase() == "true" ? true : false, fullscreen:false, repeat:"", stretching:jwplayer.utils.stretching.UNIFORM, autostart:false, debug:undefined, screencolor:undefined}};
            var _media;
            var _eventDispatcher = new jwplayer.html5.eventdispatcher();
            var _components = ["display", "logo", "controlbar", "playlist", "dock"];
            jwplayer.utils.extend(_model, _eventDispatcher);
            for (var option in options) {
                if (typeof options[option] == "string") {
                    var type = /color$/.test(option) ? "color" : null;
                    options[option] = jwplayer.utils.typechecker(options[option], type)
                }
                var config = _model.config;
                var path = option.split(".");
                for (var edge in path) {
                    if (edge == path.length - 1) {
                        config[path[edge]] = options[option]
                    } else {
                        if (!jwplayer.utils.exists(config[path[edge]])) {
                            config[path[edge]] = {}
                        }
                        config = config[path[edge]]
                    }
                }
            }
            for (var index in _configurableStateVariables) {
                var configurableStateVariable = _configurableStateVariables[index];
                _model[configurableStateVariable] = _model.config[configurableStateVariable]
            }
            var pluginorder = _components.concat([]);
            if (jwplayer.utils.exists(_model.plugins)) {
                if (typeof _model.plugins == "string") {
                    var userplugins = _model.plugins.split(",");
                    for (var userplugin in userplugins) {
                        if (typeof userplugins[userplugin] == "string") {
                            pluginorder.push(userplugins[userplugin].replace(/^\s+|\s+$/g, ""))
                        }
                    }
                }
            }
            if (jwplayer.utils.isMobile()) {
                pluginorder = ["display", "logo", "dock", "playlist"];
                if (!jwplayer.utils.exists(_model.config.repeat)) {
                    _model.config.repeat = "list"
                }
            } else {
                if (_model.config.chromeless) {
                    pluginorder = ["logo", "dock", "playlist"];
                    if (!jwplayer.utils.exists(_model.config.repeat)) {
                        _model.config.repeat = "list"
                    }
                }
            }
            _model.plugins = {order:pluginorder, config:{}, object:{}};
            if (typeof _model.config.components != "undefined") {
                for (var component in _model.config.components) {
                    _model.plugins.config[component] = _model.config.components[component]
                }
            }
            var playlistVisible = false;
            for (var pluginIndex in _model.plugins.order) {
                var pluginName = _model.plugins.order[pluginIndex];
                var pluginConfig = !jwplayer.utils.exists(_model.plugins.config[pluginName]) ? {} : _model.plugins.config[pluginName];
                _model.plugins.config[pluginName] = !jwplayer.utils.exists(_model.plugins.config[pluginName]) ? pluginConfig : jwplayer.utils.extend(_model.plugins.config[pluginName], pluginConfig);
                if (!jwplayer.utils.exists(_model.plugins.config[pluginName].position)) {
                    if (pluginName == "playlist") {
                        _model.plugins.config[pluginName].position = jwplayer.html5.view.positions.NONE
                    } else {
                        _model.plugins.config[pluginName].position = jwplayer.html5.view.positions.OVER
                    }
                } else {
                    if (pluginName == "playlist") {
                        playlistVisible = true
                    }
                    _model.plugins.config[pluginName].position = _model.plugins.config[pluginName].position.toString().toUpperCase()
                }
            }
            if (_model.plugins.config.controlbar && playlistVisible) {
                _model.plugins.config.controlbar.hideplaylistcontrols = true
            }
            if (typeof _model.plugins.config.dock != "undefined") {
                if (typeof _model.plugins.config.dock != "object") {
                    var position = _model.plugins.config.dock.toString().toUpperCase();
                    _model.plugins.config.dock = {position:position}
                }
                if (typeof _model.plugins.config.dock.position != "undefined") {
                    _model.plugins.config.dock.align = _model.plugins.config.dock.position;
                    _model.plugins.config.dock.position = jwplayer.html5.view.positions.OVER
                }
                if (typeof _model.plugins.config.dock.idlehide == "undefined") {
                    try {
                        _model.plugins.config.dock.idlehide = _model.plugins.config.controlbar.idlehide
                    } catch (e) {
                    }
                }
            }
            function _loadExternal(playlistfile) {
                var loader = new jwplayer.html5.playlistloader();
                loader.addEventListener(jwplayer.api.events.JWPLAYER_PLAYLIST_LOADED, function (evt) {
                    _model.playlist = new jwplayer.html5.playlist(evt);
                    _loadComplete(true)
                });
                loader.addEventListener(jwplayer.api.events.JWPLAYER_ERROR, function (evt) {
                    _model.playlist = new jwplayer.html5.playlist({playlist:[]});
                    _loadComplete(false)
                });
                loader.load(playlistfile)
            }

            function _loadComplete() {
                if (_model.config.shuffle) {
                    _model.item = _getShuffleItem()
                } else {
                    if (_model.config.item >= _model.playlist.length) {
                        _model.config.item = _model.playlist.length - 1
                    } else {
                        if (_model.config.item < 0) {
                            _model.config.item = 0
                        }
                    }
                    _model.item = _model.config.item
                }
                _model.position = 0;
                _model.duration = _model.playlist.length > 0 ? _model.playlist[_model.item].duration : 0;
                _eventDispatcher.sendEvent(jwplayer.api.events.JWPLAYER_PLAYLIST_LOADED, {playlist:_model.playlist});
                _eventDispatcher.sendEvent(jwplayer.api.events.JWPLAYER_PLAYLIST_ITEM, {index:_model.item})
            }

            _model.loadPlaylist = function (arg) {
                var input;
                if (typeof arg == "string") {
                    if (arg.indexOf("[") == 0 || arg.indexOf("{") == "0") {
                        try {
                            input = eval(arg)
                        } catch (err) {
                            input = arg
                        }
                    } else {
                        input = arg
                    }
                } else {
                    input = arg
                }
                var config;
                switch (jwplayer.utils.typeOf(input)) {
                    case"object":
                        config = input;
                        break;
                    case"array":
                        config = {playlist:input};
                        break;
                    default:
                        config = {file:input};
                        break
                }
                _model.playlist = new jwplayer.html5.playlist(config);
                _model.item = _model.config.item >= 0 ? _model.config.item : 0;
                if (!_model.playlist[0].provider && _model.playlist[0].file) {
                    _loadExternal(_model.playlist[0].file)
                } else {
                    _loadComplete()
                }
            };
            function _getShuffleItem() {
                var result = null;
                if (_model.playlist.length > 1) {
                    while (!jwplayer.utils.exists(result)) {
                        result = Math.floor(Math.random() * _model.playlist.length);
                        if (result == _model.item) {
                            result = null
                        }
                    }
                } else {
                    result = 0
                }
                return result
            }

            function forward(evt) {
                switch (evt.type) {
                    case jwplayer.api.events.JWPLAYER_MEDIA_LOADED:
                        _container = _media.getDisplayElement();
                        break;
                    case jwplayer.api.events.JWPLAYER_MEDIA_MUTE:
                        this.mute = evt.mute;
                        break;
                    case jwplayer.api.events.JWPLAYER_MEDIA_VOLUME:
                        this.volume = evt.volume;
                        break
                }
                _eventDispatcher.sendEvent(evt.type, evt)
            }

            var _mediaProviders = {};
            _model.setActiveMediaProvider = function (playlistItem) {
                if (playlistItem.provider == "audio") {
                    playlistItem.provider = "sound"
                }
                var provider = playlistItem.provider;
                var current = _media ? _media.getDisplayElement() : null;
                if (provider == "sound" || provider == "http" || provider == "") {
                    provider = "video"
                }
                if (!jwplayer.utils.exists(_mediaProviders[provider])) {
                    switch (provider) {
                        case"video":
                            _media = new jwplayer.html5.mediavideo(_model, current ? current : _container);
                            break;
                        case"youtube":
                            _media = new jwplayer.html5.mediayoutube(_model, current ? current : _container);
                            break
                    }
                    if (!jwplayer.utils.exists(_media)) {
                        return false
                    }
                    _media.addGlobalListener(forward);
                    _mediaProviders[provider] = _media
                } else {
                    if (_media != _mediaProviders[provider]) {
                        if (_media) {
                            _media.stop()
                        }
                        _media = _mediaProviders[provider]
                    }
                }
                return true
            };
            _model.getMedia = function () {
                return _media
            };
            _model.seek = function (pos) {
                _eventDispatcher.sendEvent(jwplayer.api.events.JWPLAYER_MEDIA_SEEK, {position:_model.position, offset:pos});
                return _media.seek(pos)
            };
            _model.setVolume = function (newVol) {
                _utils.saveCookie("volume", newVol);
                _model.volume = newVol
            };
            _model.setMute = function (state) {
                _utils.saveCookie("mute", state);
                _model.mute = state
            };
            _model.setupPlugins = function () {
                if (!jwplayer.utils.exists(_model.plugins) || !jwplayer.utils.exists(_model.plugins.order) || _model.plugins.order.length == 0) {
                    jwplayer.utils.log("No plugins to set up");
                    return _model
                }
                for (var i = 0; i < _model.plugins.order.length; i++) {
                    try {
                        var pluginName = _model.plugins.order[i];
                        if (jwplayer.utils.exists(jwplayer.html5[pluginName])) {
                            if (pluginName == "playlist") {
                                _model.plugins.object[pluginName] = new jwplayer.html5.playlistcomponent(_api, _model.plugins.config[pluginName])
                            } else {
                                _model.plugins.object[pluginName] = new jwplayer.html5[pluginName](_api, _model.plugins.config[pluginName])
                            }
                        } else {
                            _model.plugins.order.splice(plugin, plugin + 1)
                        }
                        if (typeof _model.plugins.object[pluginName].addGlobalListener == "function") {
                            _model.plugins.object[pluginName].addGlobalListener(forward)
                        }
                    } catch (err) {
                        jwplayer.utils.log("Could not setup " + pluginName)
                    }
                }
            };
            return _model
        }
    })(jwplayer);
    (function (a) {
        a.html5.playlist = function (b) {
            var d = [];
            if (b.playlist && b.playlist instanceof Array && b.playlist.length > 0) {
                for (var c in b.playlist) {
                    if (!isNaN(parseInt(c))) {
                        d.push(new a.html5.playlistitem(b.playlist[c]))
                    }
                }
            } else {
                d.push(new a.html5.playlistitem(b))
            }
            return d
        }
    })(jwplayer);
    (function (a) {
        var c = {size:180, position:a.html5.view.positions.NONE, itemheight:60, thumbs:true, fontcolor:"#000000", overcolor:"", activecolor:"", backgroundcolor:"#f8f8f8", font:"_sans", fontsize:"", fontstyle:"", fontweight:""};
        var b = {_sans:"Arial, Helvetica, sans-serif", _serif:"Times, Times New Roman, serif", _typewriter:"Courier New, Courier, monospace"};
        _utils = a.utils;
        _css = _utils.css;
        _hide = function (d) {
            _css(d, {display:"none"})
        };
        _show = function (d) {
            _css(d, {display:"block"})
        };
        a.html5.playlistcomponent = function (r, C) {
            var x = r;
            var e = a.utils.extend({}, c, x.skin.getComponentSettings("playlist"), C);
            if (e.position == a.html5.view.positions.NONE || typeof a.html5.view.positions[e.position] == "undefined") {
                return
            }
            var y;
            var l;
            var D;
            var d;
            var g;
            var f;
            var k = -1;
            var h = {background:undefined, item:undefined, itemOver:undefined, itemImage:undefined, itemActive:undefined};
            this.getDisplayElement = function () {
                return y
            };
            this.resize = function (G, E) {
                l = G;
                D = E;
                if (x.jwGetFullscreen()) {
                    _hide(y)
                } else {
                    var F = {display:"block", width:l, height:D};
                    _css(y, F)
                }
            };
            this.show = function () {
                _show(y)
            };
            this.hide = function () {
                _hide(y)
            };
            function j() {
                y = document.createElement("div");
                y.id = x.id + "_jwplayer_playlistcomponent";
                y.style.overflow = "hidden";
                switch (e.position) {
                    case a.html5.view.positions.RIGHT:
                    case a.html5.view.positions.LEFT:
                        y.style.width = e.size + "px";
                        break;
                    case a.html5.view.positions.TOP:
                    case a.html5.view.positions.BOTTOM:
                        y.style.height = e.size + "px";
                        break
                }
                B();
                if (h.item) {
                    e.itemheight = h.item.height
                }
                y.style.backgroundColor = "#C6C6C6";
                x.jwAddEventListener(a.api.events.JWPLAYER_PLAYLIST_LOADED, s);
                x.jwAddEventListener(a.api.events.JWPLAYER_PLAYLIST_ITEM, v);
                x.jwAddEventListener(a.api.events.JWPLAYER_PLAYER_STATE, m)
            }

            function p() {
                var E = document.createElement("ul");
                _css(E, {width:y.style.width, minWidth:y.style.width, height:y.style.height, backgroundColor:e.backgroundcolor, backgroundImage:h.background ? "url(" + h.background.src + ")" : "", color:e.fontcolor, listStyle:"none", margin:0, padding:0, fontFamily:b[e.font] ? b[e.font] : b._sans, fontSize:(e.fontsize ? e.fontsize : 11) + "px", fontStyle:e.fontstyle, fontWeight:e.fontweight, overflowY:"auto"});
                return E
            }

            function z(E) {
                return function () {
                    var F = f.getElementsByClassName("item")[E];
                    var G = e.fontcolor;
                    var H = h.item ? "url(" + h.item.src + ")" : "";
                    if (E == x.jwGetPlaylistIndex()) {
                        if (e.activecolor !== "") {
                            G = e.activecolor
                        }
                        if (h.itemActive) {
                            H = "url(" + h.itemActive.src + ")"
                        }
                    }
                    _css(F, {color:e.overcolor !== "" ? e.overcolor : G, backgroundImage:h.itemOver ? "url(" + h.itemOver.src + ")" : H})
                }
            }

            function o(E) {
                return function () {
                    var F = f.getElementsByClassName("item")[E];
                    var G = e.fontcolor;
                    var H = h.item ? "url(" + h.item.src + ")" : "";
                    if (E == x.jwGetPlaylistIndex()) {
                        if (e.activecolor !== "") {
                            G = e.activecolor
                        }
                        if (h.itemActive) {
                            H = "url(" + h.itemActive.src + ")"
                        }
                    }
                    _css(F, {color:G, backgroundImage:H})
                }
            }

            function q(J) {
                var Q = d[J];
                var P = document.createElement("li");
                P.className = "item";
                _css(P, {height:e.itemheight, display:"block", cursor:"pointer", backgroundImage:h.item ? "url(" + h.item.src + ")" : "", backgroundSize:"100% " + e.itemheight + "px"});
                P.onmouseover = z(J);
                P.onmouseout = o(J);
                var K = document.createElement("div");
                var G = new Image();
                var L = 0;
                var M = 0;
                var N = 0;
                if (w() && (Q.image || Q["playlist.image"] || h.itemImage)) {
                    G.className = "image";
                    if (h.itemImage) {
                        L = (e.itemheight - h.itemImage.height) / 2;
                        M = h.itemImage.width;
                        N = h.itemImage.height
                    } else {
                        M = e.itemheight * 4 / 3;
                        N = e.itemheight
                    }
                    _css(K, {height:N, width:M, "float":"left", styleFloat:"left", cssFloat:"left", margin:"0 5px 0 0", background:"black", overflow:"hidden", margin:L + "px", position:"relative"});
                    _css(G, {position:"relative"});
                    K.appendChild(G);
                    G.onload = function () {
                        a.utils.stretch(a.utils.stretching.FILL, G, M, N, this.naturalWidth, this.naturalHeight)
                    };
                    if (Q["playlist.image"]) {
                        G.src = Q["playlist.image"]
                    } else {
                        if (Q.image) {
                            G.src = Q.image
                        } else {
                            if (h.itemImage) {
                                G.src = h.itemImage.src
                            }
                        }
                    }
                    P.appendChild(K)
                }
                var F = l - M - L * 2;
                if (D < e.itemheight * d.length) {
                    F -= 15
                }
                var E = document.createElement("div");
                _css(E, {position:"relative", height:"100%", overflow:"hidden"});
                var H = document.createElement("span");
                if (Q.duration > 0) {
                    H.className = "duration";
                    _css(H, {fontSize:(e.fontsize ? e.fontsize : 11) + "px", fontWeight:(e.fontweight ? e.fontweight : "bold"), width:"40px", height:e.fontsize ? e.fontsize + 10 : 20, lineHeight:24, "float":"right", styleFloat:"right", cssFloat:"right"});
                    H.innerHTML = _utils.timeFormat(Q.duration);
                    E.appendChild(H)
                }
                var O = document.createElement("span");
                O.className = "title";
                _css(O, {padding:"5px 5px 0 " + (L ? 0 : "5px"), height:e.fontsize ? e.fontsize + 10 : 20, lineHeight:e.fontsize ? e.fontsize + 10 : 20, overflow:"hidden", "float":"left", styleFloat:"left", cssFloat:"left", width:((Q.duration > 0) ? F - 50 : F) - 10 + "px", fontSize:(e.fontsize ? e.fontsize : 13) + "px", fontWeight:(e.fontweight ? e.fontweight : "bold")});
                O.innerHTML = Q ? Q.title : "";
                E.appendChild(O);
                if (Q.description) {
                    var I = document.createElement("span");
                    I.className = "description";
                    _css(I, {display:"block", "float":"left", styleFloat:"left", cssFloat:"left", margin:0, paddingLeft:O.style.paddingLeft, paddingRight:O.style.paddingRight, lineHeight:(e.fontsize ? e.fontsize + 4 : 16) + "px", overflow:"hidden", position:"relative"});
                    I.innerHTML = Q.description;
                    E.appendChild(I)
                }
                P.appendChild(E);
                return P
            }

            function s(F) {
                y.innerHTML = "";
                d = t();
                if (!d) {
                    return
                }
                items = [];
                f = p();
                for (var G = 0; G < d.length; G++) {
                    var E = q(G);
                    E.onclick = A(G);
                    f.appendChild(E);
                    items.push(E)
                }
                k = x.jwGetPlaylistIndex();
                o(k)();
                y.appendChild(f);
                if (_utils.isIOS() && window.iScroll) {
                    f.style.height = e.itemheight * d.length + "px";
                    var H = new iScroll(y.id)
                }
            }

            function t() {
                var F = x.jwGetPlaylist();
                var G = [];
                for (var E = 0; E < F.length; E++) {
                    if (!F[E]["ova.hidden"]) {
                        G.push(F[E])
                    }
                }
                return G
            }

            function A(E) {
                return function () {
                    x.jwPlaylistItem(E);
                    x.jwPlay(true)
                }
            }

            function n() {
                f.scrollTop = x.jwGetPlaylistIndex() * e.itemheight
            }

            function w() {
                return e.thumbs.toString().toLowerCase() == "true"
            }

            function v(E) {
                if (k >= 0) {
                    o(k)();
                    k = E.index
                }
                o(E.index)();
                n()
            }

            function m() {
                if (e.position == a.html5.view.positions.OVER) {
                    switch (x.jwGetState()) {
                        case a.api.events.state.IDLE:
                            _show(y);
                            break;
                        default:
                            _hide(y);
                            break
                    }
                }
            }

            function B() {
                for (var E in h) {
                    h[E] = u(E)
                }
            }

            function u(E) {
                return x.skin.getSkinElement("playlist", E)
            }

            j();
            return this
        }
    })(jwplayer);
    (function (b) {
        b.html5.playlistitem = function (d) {
            var e = {author:"", date:"", description:"", image:"", link:"", mediaid:"", tags:"", title:"", provider:"", file:"", streamer:"", duration:-1, start:0, currentLevel:-1, levels:[]};
            var c = b.utils.extend({}, e, d);
            if (c.type) {
                c.provider = c.type;
                delete c.type
            }
            if (c.levels.length === 0) {
                c.levels[0] = new b.html5.playlistitemlevel(c)
            }
            if (!c.provider) {
                c.provider = a(c.levels[0])
            } else {
                c.provider = c.provider.toLowerCase()
            }
            return c
        };
        function a(e) {
            if (b.utils.isYouTube(e.file)) {
                return"youtube"
            } else {
                var f = b.utils.extension(e.file);
                var c;
                if (f && b.utils.extensionmap[f]) {
                    if (f == "m3u8") {
                        return"video"
                    }
                    c = b.utils.extensionmap[f].html5
                } else {
                    if (e.type) {
                        c = e.type
                    }
                }
                if (c) {
                    var d = c.split("/")[0];
                    if (d == "audio") {
                        return"sound"
                    } else {
                        if (d == "video") {
                            return d
                        }
                    }
                }
            }
            return""
        }
    })(jwplayer);
    (function (a) {
        a.html5.playlistitemlevel = function (b) {
            var d = {file:"", streamer:"", bitrate:0, width:0};
            for (var c in d) {
                if (a.utils.exists(b[c])) {
                    d[c] = b[c]
                }
            }
            return d
        }
    })(jwplayer);
    (function (a) {
        a.html5.playlistloader = function () {
            var c = new a.html5.eventdispatcher();
            a.utils.extend(this, c);
            this.load = function (e) {
                a.utils.ajax(e, d, b)
            };
            function d(g) {
                var f = [];
                try {
                    var f = a.utils.parsers.rssparser.parse(g.responseXML.firstChild);
                    c.sendEvent(a.api.events.JWPLAYER_PLAYLIST_LOADED, {playlist:new a.html5.playlist({playlist:f})})
                } catch (h) {
                    b("Could not parse the playlist")
                }
            }

            function b(e) {
                c.sendEvent(a.api.events.JWPLAYER_ERROR, {message:e ? e : "Could not load playlist an unknown reason."})
            }
        }
    })(jwplayer);
    (function (a) {
        a.html5.skin = function () {
            var b = {};
            var c = false;
            this.load = function (d, e) {
                new a.html5.skinloader(d, function (f) {
                    c = true;
                    b = f;
                    e()
                }, function () {
                    new a.html5.skinloader("", function (f) {
                        c = true;
                        b = f;
                        e()
                    })
                })
            };
            this.getSkinElement = function (d, e) {
                if (c) {
                    try {
                        return b[d].elements[e]
                    } catch (f) {
                        a.utils.log("No such skin component / element: ", [d, e])
                    }
                }
                return null
            };
            this.getComponentSettings = function (d) {
                if (c && b && b[d]) {
                    return b[d].settings
                }
                return null
            };
            this.getComponentLayout = function (d) {
                if (c) {
                    return b[d].layout
                }
                return null
            }
        }
    })(jwplayer);
    (function (a) {
        a.html5.skinloader = function (f, p, k) {
            var o = {};
            var c = p;
            var l = k;
            var e = true;
            var j;
            var n = f;
            var s = false;

            function m() {
                if (typeof n != "string" || n === "") {
                    d(a.html5.defaultSkin().xml)
                } else {
                    a.utils.ajax(a.utils.getAbsolutePath(n), function (t) {
                        try {
                            if (a.utils.exists(t.responseXML)) {
                                d(t.responseXML);
                                return
                            }
                        } catch (u) {
                            h()
                        }
                        d(a.html5.defaultSkin().xml)
                    }, function (t) {
                        d(a.html5.defaultSkin().xml)
                    })
                }
            }

            function d(y) {
                var E = y.getElementsByTagName("component");
                if (E.length === 0) {
                    return
                }
                for (var H = 0; H < E.length; H++) {
                    var C = E[H].getAttribute("name");
                    var B = {settings:{}, elements:{}, layout:{}};
                    o[C] = B;
                    var G = E[H].getElementsByTagName("elements")[0].getElementsByTagName("element");
                    for (var F = 0; F < G.length; F++) {
                        b(G[F], C)
                    }
                    var z = E[H].getElementsByTagName("settings")[0];
                    if (z && z.childNodes.length > 0) {
                        var K = z.getElementsByTagName("setting");
                        for (var P = 0; P < K.length; P++) {
                            var Q = K[P].getAttribute("name");
                            var I = K[P].getAttribute("value");
                            var x = /color$/.test(Q) ? "color" : null;
                            o[C].settings[Q] = a.utils.typechecker(I, x)
                        }
                    }
                    var L = E[H].getElementsByTagName("layout")[0];
                    if (L && L.childNodes.length > 0) {
                        var M = L.getElementsByTagName("group");
                        for (var w = 0; w < M.length; w++) {
                            var A = M[w];
                            o[C].layout[A.getAttribute("position")] = {elements:[]};
                            for (var O = 0; O < A.attributes.length; O++) {
                                var D = A.attributes[O];
                                o[C].layout[A.getAttribute("position")][D.name] = D.value
                            }
                            var N = A.getElementsByTagName("*");
                            for (var v = 0; v < N.length; v++) {
                                var t = N[v];
                                o[C].layout[A.getAttribute("position")].elements.push({type:t.tagName});
                                for (var u = 0; u < t.attributes.length; u++) {
                                    var J = t.attributes[u];
                                    o[C].layout[A.getAttribute("position")].elements[v][J.name] = J.value
                                }
                                if (!a.utils.exists(o[C].layout[A.getAttribute("position")].elements[v].name)) {
                                    o[C].layout[A.getAttribute("position")].elements[v].name = t.tagName
                                }
                            }
                        }
                    }
                    e = false;
                    r()
                }
            }

            function r() {
                clearInterval(j);
                if (!s) {
                    j = setInterval(function () {
                        q()
                    }, 100)
                }
            }

            function b(y, x) {
                var w = new Image();
                var t = y.getAttribute("name");
                var v = y.getAttribute("src");
                var A;
                if (v.indexOf("data:image/png;base64,") === 0) {
                    A = v
                } else {
                    var u = a.utils.getAbsolutePath(n);
                    var z = u.substr(0, u.lastIndexOf("/"));
                    A = [z, x, v].join("/")
                }
                o[x].elements[t] = {height:0, width:0, src:"", ready:false, image:w};
                w.onload = function (B) {
                    g(w, t, x)
                };
                w.onerror = function (B) {
                    s = true;
                    r();
                    l()
                };
                w.src = A
            }

            function h() {
                for (var u in o) {
                    var w = o[u];
                    for (var t in w.elements) {
                        var x = w.elements[t];
                        var v = x.image;
                        v.onload = null;
                        v.onerror = null;
                        delete x.image;
                        delete w.elements[t]
                    }
                    delete o[u]
                }
            }

            function q() {
                for (var t in o) {
                    if (t != "properties") {
                        for (var u in o[t].elements) {
                            if (!o[t].elements[u].ready) {
                                return
                            }
                        }
                    }
                }
                if (e === false) {
                    clearInterval(j);
                    c(o)
                }
            }

            function g(t, v, u) {
                if (o[u] && o[u].elements[v]) {
                    o[u].elements[v].height = t.height;
                    o[u].elements[v].width = t.width;
                    o[u].elements[v].src = t.src;
                    o[u].elements[v].ready = true;
                    r()
                } else {
                    a.utils.log("Loaded an image for a missing element: " + u + "." + v)
                }
            }

            m()
        }
    })(jwplayer);
    (function (a) {
        a.html5.api = function (c, p) {
            var n = {};
            var g = document.createElement("div");
            c.parentNode.replaceChild(g, c);
            g.id = c.id;
            n.version = a.version;
            n.id = g.id;
            var m = new a.html5.model(n, g, p);
            var k = new a.html5.view(n, g, m);
            var l = new a.html5.controller(n, g, m, k);
            n.skin = new a.html5.skin();
            n.jwPlay = function (q) {
                if (typeof q == "undefined") {
                    f()
                } else {
                    if (q.toString().toLowerCase() == "true") {
                        l.play()
                    } else {
                        l.pause()
                    }
                }
            };
            n.jwPause = function (q) {
                if (typeof q == "undefined") {
                    f()
                } else {
                    if (q.toString().toLowerCase() == "true") {
                        l.pause()
                    } else {
                        l.play()
                    }
                }
            };
            function f() {
                if (m.state == a.api.events.state.PLAYING || m.state == a.api.events.state.BUFFERING) {
                    l.pause()
                } else {
                    l.play()
                }
            }

            n.jwStop = l.stop;
            n.jwSeek = l.seek;
            n.jwPlaylistItem = function (q) {
                if (d) {
                    if (d.playlistClickable()) {
                        d.jwInstreamDestroy();
                        return l.item(q)
                    }
                } else {
                    return l.item(q)
                }
            };
            n.jwPlaylistNext = l.next;
            n.jwPlaylistPrev = l.prev;
            n.jwResize = l.resize;
            n.jwLoad = l.load;
            n.jwDetachMedia = l.detachMedia;
            n.jwAttachMedia = l.attachMedia;
            function j(q) {
                return function () {
                    return m[q]
                }
            }

            function e(q, s, r) {
                return function () {
                    var t = m.plugins.object[q];
                    if (t && t[s] && typeof t[s] == "function") {
                        t[s].apply(t, r)
                    }
                }
            }

            n.jwGetPlaylistIndex = j("item");
            n.jwGetPosition = j("position");
            n.jwGetDuration = j("duration");
            n.jwGetBuffer = j("buffer");
            n.jwGetWidth = j("width");
            n.jwGetHeight = j("height");
            n.jwGetFullscreen = j("fullscreen");
            n.jwSetFullscreen = l.setFullscreen;
            n.jwGetVolume = j("volume");
            n.jwSetVolume = l.setVolume;
            n.jwGetMute = j("mute");
            n.jwSetMute = l.setMute;
            n.jwGetStretching = function () {
                return m.stretching.toUpperCase()
            };
            n.jwGetState = j("state");
            n.jwGetVersion = function () {
                return n.version
            };
            n.jwGetPlaylist = function () {
                return m.playlist
            };
            n.jwAddEventListener = l.addEventListener;
            n.jwRemoveEventListener = l.removeEventListener;
            n.jwSendEvent = l.sendEvent;
            n.jwDockSetButton = function (t, q, r, s) {
                if (m.plugins.object.dock && m.plugins.object.dock.setButton) {
                    m.plugins.object.dock.setButton(t, q, r, s)
                }
            };
            n.jwControlbarShow = e("controlbar", "show");
            n.jwControlbarHide = e("controlbar", "hide");
            n.jwDockShow = e("dock", "show");
            n.jwDockHide = e("dock", "hide");
            n.jwDisplayShow = e("display", "show");
            n.jwDisplayHide = e("display", "hide");
            var d;
            n.jwLoadInstream = function (r, q) {
                if (!d) {
                    d = new a.html5.instream(n, m, k, l)
                }
                setTimeout(function () {
                    d.load(r, q)
                }, 10)
            };
            n.jwInstreamDestroy = function () {
                if (d) {
                    d.jwInstreamDestroy()
                }
            };
            n.jwInstreamAddEventListener = o("jwInstreamAddEventListener");
            n.jwInstreamRemoveEventListener = o("jwInstreamRemoveEventListener");
            n.jwInstreamGetState = o("jwInstreamGetState");
            n.jwInstreamGetDuration = o("jwInstreamGetDuration");
            n.jwInstreamGetPosition = o("jwInstreamGetPosition");
            n.jwInstreamPlay = o("jwInstreamPlay");
            n.jwInstreamPause = o("jwInstreamPause");
            n.jwInstreamSeek = o("jwInstreamSeek");
            function o(q) {
                return function () {
                    if (d && typeof d[q] == "function") {
                        return d[q].apply(this, arguments)
                    } else {
                        _utils.log("Could not call instream method - instream API not initialized")
                    }
                }
            }

            n.jwDestroy = function () {
                l.destroy()
            };
            n.jwGetLevel = function () {
            };
            n.jwGetBandwidth = function () {
            };
            n.jwGetLockState = function () {
            };
            n.jwLock = function () {
            };
            n.jwUnlock = function () {
            };
            function b() {
                if (m.config.playlistfile) {
                    m.addEventListener(a.api.events.JWPLAYER_PLAYLIST_LOADED, h);
                    m.loadPlaylist(m.config.playlistfile)
                } else {
                    if (typeof m.config.playlist == "string") {
                        m.addEventListener(a.api.events.JWPLAYER_PLAYLIST_LOADED, h);
                        m.loadPlaylist(m.config.playlist)
                    } else {
                        m.loadPlaylist(m.config);
                        setTimeout(h, 25)
                    }
                }
            }

            function h(q) {
                m.removeEventListener(a.api.events.JWPLAYER_PLAYLIST_LOADED, h);
                m.setupPlugins();
                k.setup();
                var q = {id:n.id, version:n.version};
                l.playerReady(q)
            }

            if (m.config.chromeless && !a.utils.isIOS()) {
                b()
            } else {
                n.skin.load(m.config.skin, b)
            }
            return n
        }
    })(jwplayer)
}
;