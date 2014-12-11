/*
 * Copyright (c) 2002-2014, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.mylutece.modules.parisconnect.web;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.mylutece.modules.parisconnect.authentication.ParisConnectAuthentication;
import fr.paris.lutece.plugins.mylutece.modules.parisconnect.service.ParisConnectService;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.util.mvc.xpage.MVCApplication;
import fr.paris.lutece.portal.util.mvc.xpage.annotations.Controller;
import fr.paris.lutece.util.json.AbstractJsonResponse;
import fr.paris.lutece.util.json.JsonResponse;
import fr.paris.lutece.util.json.JsonUtil;


/**
 * MyLuteceParisConnectXPage
 *
 */
@Controller( xpageName = MyLuteceParisConnectXPage.PAGE_MYLUTECE_PARIS_CONNECT, pageTitleI18nKey = "module.mylutece.parisconnect.xpage.myluteceParisConnect.pageTitle", pagePathI18nKey = "module.mylutece.parisconnect.xpage.myluteceParisConnect.pagePathLabel" )
public class MyLuteceParisConnectXPage extends MVCApplication
{
    /**
     * Name of this application
     */
    public static final String PAGE_MYLUTECE_PARIS_CONNECT = "myluteceParisConnect";
    private static final long serialVersionUID = -4316691400124512414L;

    //TOKEN
    public static final String  TOKEN_DO_SEND_AVIS ="doSendAvis";
	public static final String  TOKEN_DO_SUBSCRIBE_ALERT ="doSubscribeAlert";
	private static final String TOKEN_DO_LOGIN = "dologin";
    //Parameters
    private static final String PARAMETER_USERNAME = "username";
    private static final String PARAMETER_PASSWORD = "password";
    private static final String PARAMETER_MAIL = "mail";
    private static final String PARAMETER_MESSAGE = "message";
    // Views
    private static final String VIEW_IS_USER_AUTHENTICATED = "isUserAuthenticatedJson";

    // Actions
    private static final String ACTION_DO_LOGIN_JSON = "doLoginJson";
    

    // Json ERROR CODE
    private static final String JSON_ERROR_AUTHENTICATION_NOT_ENABLE = "AUTHENTICATION_NOT_ENABLE";
    private static final String JSON_ERROR_LOGIN_ERROR = "LOGIN_ERROR";
    private static final String JSON_ERROR_DURING_SUBSCRIBED = "ERROR_DURING_SUBSCRIBED";
    private static final String JSON_ERROR_DURING_SEND_AVIS = "ERROR_DURING_SEND_AVIS";
    private static final String JSON_ERROR_USER_ALREADY_SUBSCRIBED = "ERROR_USER_ALREADY_SUBSCRIBED";
   // key
    private static final String KEY_SUSBCIBE_ID_ALERTE="module.mylutece.parisconnect.site_property.subscribe_id_alerte";
    private static final String KEY_CREATE_ACCOUNT_ID_MAIL="module.mylutece.parisconnect.site_property.create_account_id_mail";
    private static final String KEY_SEND_AVIS_BACK_URL="module.mylutece.parisconnect.site_property.send_avis_back_url";
 
   
    private ParisConnectAuthentication _parisConnectAuthentication = (ParisConnectAuthentication) SpringContextService.getBean( 
            "mylutece-parisconnect.authentication" );

    /**
     * Check if the current user is authenticated
     * @param request The request
     * @return A JSON string  containing  true in the field result if the user is authenticated
     */
    public String isUserAuthenticated( HttpServletRequest request )
    {
        AbstractJsonResponse jsonResponse = null;

        LuteceUser user = null;

        if ( SecurityService.isAuthenticationEnable(  ) )
        {
            user = SecurityService.getInstance(  ).getRegisteredUser( request );

            if ( user != null )
            {
                jsonResponse = new JsonResponse( Boolean.TRUE );
            }
            else
            {
                jsonResponse = new JsonResponse( Boolean.FALSE );
            }
        }
        else
        {
            jsonResponse = new ParisConnectErrorJsonResponse( JSON_ERROR_AUTHENTICATION_NOT_ENABLE );
        }

        return JsonUtil.buildJsonResponse( jsonResponse );
    }

    /**
     * doLoginAuthentication
     * @param request The request
     * @return A JSON string  containing  true in the user is authenticated
     */
    public String doLogin( HttpServletRequest request )
    {
        String strUsername = request.getParameter( PARAMETER_USERNAME );
        String strPassword = request.getParameter( PARAMETER_PASSWORD );

        AbstractJsonResponse jsonResponse = null;

        LuteceUser user = null;

        if ( SecurityService.isAuthenticationEnable(  ) )
        {
            try
            {
                user = _parisConnectAuthentication.login( strUsername, strPassword, request );

                if ( user != null )
                {
                    SecurityService.getInstance(  ).registerUser( request, user );
                    jsonResponse = new JsonResponse( Boolean.TRUE );
                }
            }
            catch ( LoginException e )
            {
                jsonResponse = new ParisConnectErrorJsonResponse( JSON_ERROR_LOGIN_ERROR,
                        SecurityTokenService.getInstance(  ).getToken( request, TOKEN_DO_LOGIN ) );
            }
        }
        else
        {
            jsonResponse = new ParisConnectErrorJsonResponse( JSON_ERROR_AUTHENTICATION_NOT_ENABLE );
        }

        return JsonUtil.buildJsonResponse( jsonResponse );
    }
    
    
    /**
     * Do subscribe a user by mail
     * using the AJAX mode
     * @param request The request
     */
    public String doSubscribeAlert( HttpServletRequest request )
    {
    	 String strMail = request.getParameter( PARAMETER_MAIL );
    	 AbstractJsonResponse jsonResponse=null;
         
    	 
//    	 if( !SecurityTokenService.getInstance().validate(request, TOKEN_DO_SUBSCRIBE_ALERT) )
//    		 
//    	 {
//    		 
//           AppLogService.error( "doSubscribeAlert: Token not validated" );
//           
//    	 }
//    	 
//    	 else 
    	 if( StringUtils.isNotEmpty(strMail) )
    	 {
	    	  String strPcuid= ParisConnectService.getInstance().getPcuidByEmail(strMail);
	    	  //the user doesn't exist
	    	  if( StringUtils.isEmpty(strPcuid))
	    	  {
	    		  String stridMailToSend=DatastoreService.getDataValue(KEY_CREATE_ACCOUNT_ID_MAIL,"0");
	    		  strPcuid= ParisConnectService.getInstance().setAccountShadow(strMail, stridMailToSend);
	    	  }
	    	  
	    	  
	    	  if( StringUtils.isNotEmpty(strPcuid))
	    	  {	  
	    		  String strSubscribeIdAlerte=DatastoreService.getDataValue(KEY_SUSBCIBE_ID_ALERTE,"91");
	    		  String strErrorSubscribe=ParisConnectService.getInstance().subscribeUser(strPcuid, strSubscribeIdAlerte);
	    		   
	    		  if(strErrorSubscribe == null)
	    		  {
	    			  
	    			  String strUserUid=ParisConnectService.getInstance().getUidByPcuid(strPcuid);
	    			 
	    			  
	    			  jsonResponse=new JsonResponse(new ParisConnectResponse(Boolean.TRUE,SecurityTokenService.getInstance().getToken(request, TOKEN_DO_SUBSCRIBE_ALERT)));
	    			  
	    		  }
	    		  else if(ParisConnectService.ERROR_ALREADY_SUBSCRIBE.equals(strErrorSubscribe))
	    		  {
	    			  jsonResponse=new ParisConnectErrorJsonResponse(JSON_ERROR_USER_ALREADY_SUBSCRIBED,SecurityTokenService.getInstance().getToken(request, TOKEN_DO_SUBSCRIBE_ALERT));
	    		  }
	    		  else
	    		  {
	    			  jsonResponse=new ParisConnectErrorJsonResponse(JSON_ERROR_DURING_SUBSCRIBED,SecurityTokenService.getInstance().getToken(request, TOKEN_DO_SUBSCRIBE_ALERT));
	    			  
	    		  }
	    			 
	    		  
	    	  
	    	  }
    	 }    	  
	    	  
    	  if(jsonResponse == null)
    	  {
    		  jsonResponse=new ParisConnectErrorJsonResponse(JSON_ERROR_DURING_SUBSCRIBED,SecurityTokenService.getInstance().getToken(request, TOKEN_DO_SUBSCRIBE_ALERT));
         }
	 
	                 
          return JsonUtil.buildJsonResponse(jsonResponse) ; 
    }
    
    
    /**
     * DoSend Avis
     * using the AJAX mode
     * @param request The request
     */
    public String doSendAvis(HttpServletRequest request )
    {
    	 String strMail = request.getParameter( PARAMETER_MAIL );
    	 String strMessage = request.getParameter( PARAMETER_MESSAGE );
    	 String strBackUrl=DatastoreService.getDataValue(KEY_SEND_AVIS_BACK_URL,"");
    	 AbstractJsonResponse jsonResponse=null;
    	 
    	 
//    	 if(SecurityTokenService.getInstance().validate(request, TOKEN_DO_SEND_AVIS) )    	    	 
//        		 
//        	 {
//        		 
//               AppLogService.error( "doSubscribeAlert: Token not validated" );
//               
//        	jsonResponse=new BudgetErrorJsonResponse(JSON_ERROR_DURING_SEND_AVIS,SecurityTokenService.getInstance().getToken(request, TOKEN_DO_SEND_AVIS));
    	 
//    }else
//        	 
//        	 
    	 
    	 if( StringUtils.isNotEmpty(strMessage) && ParisConnectService.getInstance().sendAvisMessage(strMail, strMessage,strBackUrl) )
    	 {
	    	 jsonResponse=new JsonResponse(new ParisConnectResponse(Boolean.TRUE,SecurityTokenService.getInstance().getToken(request, TOKEN_DO_SEND_AVIS)));
	    	
    	 }    	  
    	 else
    	 {
    	   jsonResponse=new ParisConnectErrorJsonResponse(JSON_ERROR_DURING_SEND_AVIS,SecurityTokenService.getInstance().getToken(request, TOKEN_DO_SEND_AVIS));
         }
	 
	                 
          return JsonUtil.buildJsonResponse(jsonResponse) ; 
    }
    
}
