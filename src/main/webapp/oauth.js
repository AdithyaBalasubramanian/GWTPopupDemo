/**
 * Opens a new window with the provided target URL
 * @param url       Authorize Url of the Auth Server
 * @param target    target = "_blank"
 */
function openWindow(url, target) {
    let self = this;

    self.parentWindow = self.window;
    console.log("parent window = " + self.parentWindow);
    let params = "menubar=no,location=0,resizable=yes,scrollbars=yes,status=0,dependent=true,width=1200,height=720";

    console.log( "me: opening window with the new URL= " + url );
    self.childWindow = window.open(url, target, params);
    console.log("Child Window = " + self.childWindow.location)
    return self.childWindow;
}

/**
 * To construct the url of the OAuth Server
 * @param hostName      Server hostname of the OAuth server
 * @param tenant        ARIS Server Tenant Id
 * @param locale        Locale (default english)
 * @param clientId      Client Id of the target application
 * @param grantType     Grant Type (Auth Code for OIDC)
 * @param redirectUrl   Redirect URI of the client where Auth code will bre submitted
 */
function constructUrl(hostName, tenant, locale, clientId, grantType, redirectUrl) {
    return hostName + "?tenant=" + tenant + "&locale=" + locale + "&clientId=" + clientId +
        "&grantType=" + grantType + "&redirectUrl=" + redirectUrl;
}

/**
 * To close the child window, and send to be reloaded
 * @param windowToClose window to be closed
 * @param redirectUrl   redirect URL to be loaded after close
 * @param userInfoUrl   User Info API URL
 */
function closeWindowOnRedirect(windowToClose, redirectUrl, userInfoUrl) {

    let self = this;
    let winLocation = '';
    self.interval = window.setInterval(function () {
        winLocation = windowToClose.location.href;
        console.log("About to close the child window...");
        console.log("Window location href = " + winLocation);
        if (winLocation.includes(redirectUrl)) {
            console.log("Closing the child Window...");
            clearInterval(self.interval);
            windowToClose.close();
            refreshOnClose(windowToClose, userInfoUrl);
        }
    }, 1000);
}

/**
 * To reload the user Info page on close
 * @param closedWindow  The Window object that is to reloaded/closed
 * @param userInfoUrl   User Info API URL
 */
function refreshOnClose(closedWindow, userInfoUrl) {
    // Use userInfoUrl instead
    // let newURL = 'http://localhost:8080/umc/userinfo1';

    let self = this;
    self.interval = window.setInterval(function () {
        console.log("child window close interval wait listener...")
        if (closedWindow.closed) {
            console.log("child window closed...")
            console.log("self interval = " + self.interval)
            clearInterval(self.interval);
            console.log("trying to assign location to parent window = " + userInfoUrl)
            window.open(userInfoUrl, "");
        }
    }, 1000);
}