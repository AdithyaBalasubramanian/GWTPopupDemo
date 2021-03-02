/**
 * Opens a new window with the provided target URL
 * @param url           Authorize Url of the Auth Server
 * @param target        target = "_blank"
 * @param paramProps    Properties of the window to be opened
 */
function openWindow(url, target, paramProps) {
    let self = this;

    self.parentWindow = self.window;
    console.log("parent window = " + self.parentWindow);
    let params = constructParams(paramProps);

    console.log( "me: opening window with the new URL= " + url );
    self.childWindow = window.open(url, target, params);
    console.log("Child Window = " + self.childWindow.location)
    return self.childWindow;
}

/**
 * To close the child window, and send to be reloaded
 * @param windowToClose window to be closed
 * @param landingPage   URL to be checked in the child window
 */
function closeWindowOnRedirect(windowToClose, landingPage) {
    let self = this;
    let winLocation = '';
    self.interval = window.setInterval(function () {
        winLocation = windowToClose.location.href;
        console.log("About to close the child window...");
        console.log("Window location href = " + winLocation);

        if (winLocation.includes(landingPage)) {
            clearInterval(self.interval);
            console.log("Closing the child Window...");
            windowToClose.close();
        }
        if (windowToClose.closed) {
            console.log("returning to the location = " + winLocation);
            console.log("Parent window location = " + window.location);
            console.log("Trying to redirect the parent window location...");
            window.location.replace(winLocation);
        }
    }, 1000);
}

/**
 * To construct the window parameters as properties
 * @param paramsProps   properties of the window as an object
 * Sample JSON
 * {
  "menubar": "no",
  "location": 0,
  "resizable": "yes",
  "scrollbars": "yes",
  "status": 0,
  "dependent": true,
  "width": 1200,
  "height": 720
    }
 * @returns {string}    constructed parameter string
 */
function constructParams(paramsProps) {
    let params;
    params = "menubar=" + paramsProps.menubar + ",location=" + paramsProps.location + ",resizable="
                + paramsProps.resizable + ",scrollbars=" + paramsProps.scrollbars + ",status=" + paramsProps.status
                + ",dependent=" + paramsProps.dependent + ",width=" + paramsProps.width + ",height=" + paramsProps.height;
    return params;
}

/**
 * To construct the url of the OAuth Server
 * @param hostName      Server hostname of the OAuth server
 * @param tenant        ARIS Server Tenant Id
 * @param locale        Locale (default english)
 * @param clientId      Client Id of the target application
 * @param grantType     Grant Type (Auth Code for OIDC)
 * @param redirectUrl   Redirect URI of the client where Auth code will bre submitted
 * @returns {string}    constructed URL
 */
function constructUrl(hostName, tenant, locale, clientId, grantType, redirectUrl) {
    return hostName + "?tenant=" + tenant + "&locale=" + locale + "&clientId=" + clientId +
        "&grantType=" + grantType + "&redirectUrl=" + redirectUrl;
}