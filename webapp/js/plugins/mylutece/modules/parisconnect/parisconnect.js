// displayLoginPagePopup
function displayLoginPagePopup() {

    canonicalUrl = $('link[rel="canonical"]').attr('href');
    returnUrl = jQuery.isEmptyObject(canonicalUrl) ? baseUrl : canonicalUrl;
    return displayLoginPagePopupWithBackUrl(returnUrl);
}

//displayLoginPagePopup
function displayLoginPagePopupWithBackUrl(returnUrl) {
	window.open(popupConnectLoginPageUrl + returnUrl, '_blank', 'width=720,height=812,scrollbars=no,status=yes,resizable=no,screenx=0,screeny=0');
    return false;
}
