
function setupLinks() {
    const links = document.getElementsByTagName("a");
    for (let link of links) {
        console.log(link.href);
        link.addEventListener("click", function(event) {
            event.preventDefault();
            navigate(link.href);
        });
    }
}
setupLinks();

function navigate(url) {
    if (url !== window.location.href) {
        history.pushState({}, '', url);
        console.log("navi : " + window.location.pathname.substring(4));
        getComponent(window.location.pathname.substring(4))
    }
}

function getComponent(path) {
    const outlets = document.getElementsByTagName("outlet");
    for (let outlet of outlets) {
        if (path.length > 0) {
            downloadComponent(outlet, path);
        } else {
            outlet.innerHTML = ""
        }
    }
}

function downloadComponent(component, path) {
    const myRequest = new Request("http://" + window.location.host + "/content" + path, {method: 'GET'});
    console.log("request " + myRequest.url);
    return fetch(myRequest)
        .then(response => response.text())
        .then(response => { component.innerHTML = response; })
}

window.addEventListener("popstate", function() {
    console.log('popstate');
});

window.addEventListener("pushstate", function() {
    console.log('pushstate');
    debugger;
});
