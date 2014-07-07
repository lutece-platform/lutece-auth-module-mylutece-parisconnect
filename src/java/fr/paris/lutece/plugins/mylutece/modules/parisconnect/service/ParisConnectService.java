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
package fr.paris.lutece.plugins.mylutece.modules.parisconnect.service;

import fr.paris.lutece.plugins.mylutece.authentication.MultiLuteceAuthentication;
import fr.paris.lutece.plugins.mylutece.modules.parisconnect.authentication.ParisConnectAuthentication;
import fr.paris.lutece.plugins.mylutece.modules.parisconnect.authentication.ParisConnectUser;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.commons.lang.StringUtils;

import org.apache.log4j.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;


/**
 *
 * ParisConnectService
 */
public final class ParisConnectService
{
    private static final String AUTHENTICATION_BEAN_NAME = "mylutece-parisconnect.authentication";
    private static final ParisConnectService _singleton = new ParisConnectService(  );
    private static final String PROPERTY_COOKIE_PARIS_CONNECT_NAME = "parisconnect.cookieName";
    private static final String RESPONSE_STATUS = "status";
    private static final String RESPONSE_DATA = "data";
    private static final String CHECK_CONNEXION_FALSE = "false";
    private static final String RESPONSE_STATUS_SUCCESS = "success";
    private static final String PCUSER_LASTNAME = "name";
    private static final String PCUSER_FIRSTNAME = "firstname";
    private static final String PCUSER_EMAIL = "email";
    private static final String PCUSER_NICKNAME = "pseudo";
    private static final String PCUSER_GENDER = "gender";
    private static final String PCUSER_CITY = "city";
    private static final String PCUSER_ZIPCODE = "zip_code";
    private static final String PCUSER_VERIFIED = "is_verified";
    
    private static final String COOKIE_PARIS_CONNECT_NAME = AppPropertiesService.getProperty( PROPERTY_COOKIE_PARIS_CONNECT_NAME );
    private static Logger _logger = Logger.getLogger( Constants.LOGGER_PARISCONNECT );

    /**
     * Empty constructor
     */
    private ParisConnectService(  )
    {
        // nothing
    }

    /**
     * Gets the instance
     *
     * @return the instance
     */
    public static ParisConnectService getInstance(  )
    {
        return _singleton;
    }

    /**
     * Inits plugin. Registers authentication
     */
    public void init(  )
    {
        ParisConnectAuthentication authentication = (ParisConnectAuthentication) SpringContextService.getPluginBean( ParisConnectPlugin.PLUGIN_NAME,
                AUTHENTICATION_BEAN_NAME );

        if ( authentication != null )
        {
            MultiLuteceAuthentication.registerAuthentication( authentication );
        }
        else
        {
            _logger.error( 
                "ParisConnectAuthentication not found, please check your parisconnect_context.xml configuration" );
        }
    }

    /**
     * Process login
     * @param request The HTTP request
     * @param strUserName The user's name
     * @param strUserPassword The user's password
     * @param parisConnectAuthentication The authentication
     * @return The LuteceUser
     */
    public ParisConnectUser doLogin( HttpServletRequest request, String strUserName, String strUserPassword,
        ParisConnectAuthentication parisConnectAuthentication )
    {
        String strResponse = ParisConnectAPIService.doLogin( strUserName, strUserPassword );

        ParisConnectUser user = null;

        if ( strResponse != null )
        {
            JSONObject joObject = (JSONObject) JSONSerializer.toJSON( strResponse );
            String strAuthenticationStatus = joObject.getString( RESPONSE_STATUS );

            if ( ( strAuthenticationStatus != null ) && strAuthenticationStatus.equals( RESPONSE_STATUS_SUCCESS ) )
            {
                JSONObject joObjectUser = joObject.getJSONObject( RESPONSE_DATA );

                if ( ( joObjectUser != null ) && joObjectUser.containsKey( ParisConnectAPIService.USER_UID ) &&
                        !StringUtils.isEmpty( joObjectUser.getString( ParisConnectAPIService.USER_UID ) ) )
                {
                    String strUID = joObjectUser.getString( ParisConnectAPIService.USER_UID );
                    _logger.debug( "doLogin : Login OK - UID=" + strUID );
                    user = new ParisConnectUser( strUID, parisConnectAuthentication );

                    String strPCUID = joObjectUser.getString( ParisConnectAPIService.PCUID );
                    _logger.debug( "doLogin : get PCUID=" + strPCUID );
                    fillUserData( user, ParisConnectAPIService.getUser( strPCUID ) );
                    // Set a connexion cookie to let the user access other PC Services without sign in
                    ParisConnectAPIService.setConnectionCookie( strUID );
                }
            }
        }

        return user;
    }

    /**
     * Gets the authenticated user
     * @param request The HTTP request
     * @param parisConnectAuthentication The Authentication
     * @return The LuteceUser
     */
    public ParisConnectUser getHttpAuthenticatedUser( HttpServletRequest request,
        ParisConnectAuthentication parisConnectAuthentication )
    {
        ParisConnectUser user = null;

        String strParisConnectCookie = getConnectionCookie( request );

        if ( strParisConnectCookie != null )
        {
            String strResponse = ParisConnectAPIService.checkConnectionCookie( strParisConnectCookie );

            if ( !StringUtils.isEmpty( strResponse ) && ( !strResponse.equals( CHECK_CONNEXION_FALSE ) ) )
            {
                String strUID = strResponse;
                user = new ParisConnectUser( strUID, parisConnectAuthentication );
                fillUserData( user, ParisConnectAPIService.getUser( strUID ) );
            }
        }

        return user;
    }

    /**
     * Extract the value of the connection cookie
     * @param request The HTTP request
     * @return The cookie's value
     */
    private String getConnectionCookie( HttpServletRequest request )
    {
        Cookie[] cookies = request.getCookies(  );
        String strParisConnectCookie = null;

        if ( cookies != null )
        {
            for ( Cookie cookie : cookies )
            {
                if ( cookie.getName(  ).equals( COOKIE_PARIS_CONNECT_NAME ) )
                {
                    strParisConnectCookie = cookie.getValue(  );
                    _logger.debug( "getHttpAuthenticatedUser : cookie '" + COOKIE_PARIS_CONNECT_NAME +
                        "' found - value=" + strParisConnectCookie );
                }
            }
        }

        return strParisConnectCookie;
    }

    /**
     * Fill user's data
     * @param user The User
     * @param strUserData Data in JSON format
     */
    private void fillUserData( ParisConnectUser user, String strUserData )
    {
        JSONObject joObject = (JSONObject) JSONSerializer.toJSON( strUserData );

        user.setUserInfo( LuteceUser.NAME_FAMILY, joObject.getString( PCUSER_LASTNAME ) );
        user.setUserInfo( LuteceUser.NAME_GIVEN, joObject.getString( PCUSER_FIRSTNAME ) );
        user.setUserInfo( LuteceUser.GENDER, joObject.getString( PCUSER_GENDER ) );
        user.setUserInfo( LuteceUser.NAME_NICKNAME, joObject.getString( PCUSER_NICKNAME ) );
        user.setUserInfo( LuteceUser.HOME_INFO_POSTAL_CITY, joObject.getString( PCUSER_CITY ) );
        user.setUserInfo( LuteceUser.HOME_INFO_POSTAL_POSTALCODE, joObject.getString( PCUSER_ZIPCODE ) );
        user.setEmail( joObject.getString( PCUSER_EMAIL ) );
        String strVerified = joObject.getString( PCUSER_VERIFIED );
        boolean bVerified = "1".equals( strVerified );
        user.setVerified( bVerified );
 
    }
}
