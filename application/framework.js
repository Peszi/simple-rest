
function navigateTo(event) {
    event.preventDefault();
    const link = event.target;
    if (link.href !== window.location.href) {
        history.pushState({}, '', link.href);
        loadComponents();
    }
}

var varw = (function (context) {
    return function (varName, varValue) {
        var value = varValue;

        Object.defineProperty(context, varName, {
            get: function () { return value; },
            set: function (v) {
                value = v;
                console.log('Value changed! New value: ' + value);
            }
        });
    };
})(window);

function onButton(event) {
    alert('root');
}

function loadComponents() {
    const outlets = document.getElementsByTagName("outlet");
    for (let outlet of outlets) {
        const url = "/" + outlet.getAttribute("url");
        const current = outlet.getAttribute("current");
        const component = getComponentPathname(url);
        /*console.log("outlet [" + url + "] " + component + " to " + window.location.href);*/
        if (component.length > 0 && component !== url) {
            /*console.log("current [" + component + "] '" + current + "' url '" + url + "'");*/
            if (component !== current) {
                downloadComponent(outlet, component);
            }
        } else {
            outlet.innerHTML = "";
            outlet.setAttribute("current", "")
        }
    }
}
loadComponents();
function downloadComponent(component, path) {
    const myRequest = new Request(getUrl(path), {method: 'POST'});
    /*console.log("Component request '" + path + "'");*/
    return fetch(myRequest)
        .then(response => response.text())
        .then(response => {
            appendData(component, response);
            component.setAttribute("current", path);
            loadComponents();
        })
}

function appendData(component, response) {
    component.innerHTML = response;
    for (let script of component.getElementsByTagName('script')) {
        script.parentNode.removeChild(script);
        const scriptNode = document.createElement('script');
        scriptNode.innerHTML = script.innerHTML;
        component.appendChild(scriptNode);
    }
}

function printComponents() {
    for (let outlet of document.getElementsByTagName("outlet")) {
        const url = "/" + outlet.getAttribute("url");
        console.log("print o [" + url + "]");
    }
}

function getComponentPathname(url) {
    if (window.location.pathname.indexOf(url) >= 0) {
        let trimVal = window.location.pathname.indexOf("/", url.length);
        if (trimVal < 0) { trimVal = window.location.pathname.length; }
        return window.location.pathname.substring(0, trimVal);
    }
    console.log("closing path");
    return "";
}

function getUrl(pathname) {
    return window.location.href.substring(0, window.location.href.length - window.location.pathname.length) + pathname;
}

window.addEventListener("popstate", function() {
    console.log('popstate');
    loadComponents(window.location.pathname);
});

window.addEventListener("pushstate", function() {
    console.log('pushstate');
    debugger;
});
