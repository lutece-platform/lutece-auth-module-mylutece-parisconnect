// displayLoginPagePopupfunction displayLoginPagePopup() {    currentUrl = window.location.href;    returnUrl = jQuery.isEmptyObject(currentUrl) ? baseUrl : currentUrl;    return displayLoginPagePopupWithBackUrl(returnUrl);}//displayLoginPagePopupfunction displayLoginPagePopupWithBackUrl(returnUrl) {	window.open( popupConnectLoginPageUrl + returnUrl, '_blank', 'width=720,height=812,scrollbars=no,status=yes,resizable=no,screenx=0,screeny=0');    return false;}//displayLostPasswordPagePopupfunction displayLostPasswordPagePopup() {	urlPrefix = "https://accounts.paris.fr/account/send_password?pop=1&returnUrl=";	currentUrl = window.location.href;	returnUrl = jQuery.isEmptyObject(currentUrl) ? baseUrl : currentUrl;	window.open( urlPrefix + returnUrl , '_blank', 'width=720,height=812,scrollbars=no,status=yes,resizable=no,screenx=0,screeny=0');    return false;}$(document).ready( function(){	    //Ajax Login authentication    $(".btn-connexion").click(    function () {        var userInfos = {            username: $("#username").val(),            password: $("#password").val(),        };        var urlDoLogin = baseUrl + "jsp/site/plugins/mylutece/modules/parisconnect/DoLoginJson.jsp";        $.ajax({            url: urlDoLogin,            type: "POST",            dataType: "json",            data: userInfos,            async: false,        	cache:false,            success: function (data) {                if (data.status == 'OK') {                    if (data.result) {                        $("#login_error").hide();                        location.reload();                    } else {                        $("#login_error").show();                    }                }            },            error: function (jqXHR, textStatus, errorThrown) {            }        });        return false;    });            $("#username").click(function(){		$("#lost_password").hide();		$("#login_error").hide();	});       $("#password").click(function(){		$("#lost_password").toggle();		$("#login_error").hide();	});		$( "#password").blur(function() {		$("#lost_password").hide();			});			$( "#password").focus(function() {		$("#lost_password").hide();			});		$("#lost_password a").click( function(){		displayLostPasswordPagePopup();		return false;	});});//checked if the user is authenticatedfunction checkIfUserIsAuthenticated() {    var result = false;    $.ajax({        url: baseUrl + 'jsp/site/plugins/mylutece/modules/parisconnect/IsUserAuthenticatedJson.jsp',        type: 'GET',        dataType: "json",        data: {},        async: false,    	cache:false,        success: function (data) {            if (data.status == 'OK') {                result = data.result;            }        },        error: function (jqXHR, textStatus, errorThrown) {        }    });    return result;}