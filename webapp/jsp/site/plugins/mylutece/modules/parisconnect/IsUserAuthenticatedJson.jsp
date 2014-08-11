<%@page import="fr.paris.lutece.portal.web.LocalVariables" trimDirectiveWhitespaces="true"%>
<jsp:useBean id="myLuteceParisConnectXPage" scope="session" class="fr.paris.lutece.plugins.mylutece.modules.parisconnect.web.MyLuteceParisConnectXPage" />
<%
	LocalVariables.setLocal( config, request, response );
%>
<%= myLuteceParisConnectXPage.isUserAuthenticated(request) %>