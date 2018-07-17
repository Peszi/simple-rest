
var onInitEvent = new Event('onInit');
var onDestroyEvent = new Event('onDestroy');

function defineVal(name, value) {
    Object.defineProperty(window, name, {
        configurable: true,
        value: value
    });
}

function registerVal(varName, varComponent, prefix, suffix) {
    console.log("Setting up VAR " + varName);
    let value = window[varName];
    let component = varComponent;
    component.innerText = prefix + value + suffix;
    Object.defineProperty(window, varName, {
        get: function () { return value; },
        set: function (v) { value = v;
            component.innerText = prefix + value + suffix;
        }
    });
}

function checkVariables(component) {
    $(component).find('*').each(function () {
        if (this.nodeName === 'OUTLET') { return; }
        const innerText = this.childNodes[0].nodeValue;
        if (!innerText) { return; }
        const startIdx = innerText.indexOf("{{");
        if (startIdx >= 0) {
            const endIdx = innerText.indexOf("}}", startIdx);
            if (endIdx > 0 && (endIdx - startIdx) < 20) {
                const variableName = innerText.substring(startIdx + 2, endIdx).trim();
                const textSuffix = innerText.substring(endIdx + 2);
                const textPrefix = innerText.substring(0, startIdx);
                registerVal(variableName, this, textPrefix, textSuffix);
            }
        }
    });
}

function navigateTo(event) {
    event.preventDefault();
    const link = event.target;
    if (link.href !== window.location.href) {
        history.pushState({}, '', link.href);
        loadComponents();
    }
}

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
                outlet.dispatchEvent(onDestroyEvent);
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
            console.log('component ' + path);
            appendData(component, response);
            component.setAttribute("current", path);
            loadComponents();
            component.dispatchEvent(onInitEvent);
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
    checkVariables(component);
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
