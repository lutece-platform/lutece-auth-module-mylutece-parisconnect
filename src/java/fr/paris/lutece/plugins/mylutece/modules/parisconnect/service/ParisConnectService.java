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
import javax.servlet.http.HttpServletResponse;


/**
 *
 * ParisConnectService
 */
public final class ParisConnectService
{
    public static final String ERROR_ALREADY_SUBSCRIBE = "ALREADY_SUBSCRIBE";
    public static final String ERROR_DURING_SUBSCRIBE = "ERROR_DURING_SUBSCRIBE";
    private static final String AUTHENTICATION_BEAN_NAME = "mylutece-parisconnect.authentication";
    private static ParisConnectService _singleton;
    private static final String PROPERTY_COOKIE_PARIS_CONNECT_NAME = "parisconnect.cookieName";
    private static final String PROPERTY_COOKIE_PARIS_CONNECT_DOMAIN = "parisconnect.cookieDomain";
    private static final String PROPERTY_COOKIE_PARIS_CONNECT_PATH = "parisconnect.cookiePath";
    private static final String PROPERTY_COOKIE_PARIS_CONNECT_MAX_AGE = "parisconnect.cookieMaxAge";
    private static final String PROPERTY_COOKIE_PARIS_CONNECT_MAX_SECURE = "parisconnect.cookieSecure";
    private static final String RESPONSE_STATUS = "status";
    private static final String RESPONSE_DATA = "data";
    private static final String RESPONSE_OK = "\"ok\"";
    private static final String RESPONSE_ALREADY = "\"ALREADY\"";
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
    private static final String PCUSER_BIRTHDATE = "birthday";
    private static final String PCUSER_ADDRESS = "location";
    private static String COOKIE_PARIS_CONNECT_NAME;
    private static String COOKIE_PARIS_CONNECT_DOMAIN;
    private static String COOKIE_PARIS_CONNECT_PATH;
    private static int COOKIE_PARIS_CONNECT_MAX_AGE;
    private static boolean COOKIE_PARIS_CONNECT_SECURE;
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
        if ( _singleton == null )
        {
            _singleton = new ParisConnectService(  );
            COOKIE_PARIS_CONNECT_NAME = AppPropertiesService.getProperty( PROPERTY_COOKIE_PARIS_CONNECT_NAME );
            COOKIE_PARIS_CONNECT_DOMAIN = AppPropertiesService.getProperty( PROPERTY_COOKIE_PARIS_CONNECT_DOMAIN );
            COOKIE_PARIS_CONNECT_PATH = AppPropertiesService.getProperty( PROPERTY_COOKIE_PARIS_CONNECT_PATH );
            COOKIE_PARIS_CONNECT_MAX_AGE = AppPropertiesService.getPropertyInt( PROPERTY_COOKIE_PARIS_CONNECT_MAX_AGE,
                    60 * 30 );
            COOKIE_PARIS_CONNECT_SECURE = AppPropertiesService.getPropertyBoolean( PROPERTY_COOKIE_PARIS_CONNECT_MAX_SECURE,
                    true );
        }

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
        String strResponse;
        ParisConnectUser user = null;

        try
        {
            strResponse = ParisConnectAPIService.doLogin( strUserName, strUserPassword );

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
                        //save paris connect cookie value
                        user.setPCUID( strPCUID );
                        fillUserData( user, ParisConnectAPIService.getUser( strPCUID ) );
                    }
                }
            }
        }
        catch ( ParisConnectAPIException ex )
        {
            _logger.warn( ex.getMessage(  ) );
        }

        return user;
    }

    /**
     * Logout to paris connect
     * @param user the ParisConnectUser
     */
    public void doLogout( ParisConnectUser user )
    {
        try
        {
            ParisConnectAPIService.doDisconnect( user.getPCUID(  ) );
        }
        catch ( ParisConnectAPIException ex )
        {
            _logger.warn( ex.getMessage(  ) );
        }
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

        String strPCUID = getConnectionCookie( request );

        if ( strPCUID != null )
        {
            try
            {
                String strResponse = ParisConnectAPIService.checkConnectionCookie( strPCUID );

                if ( !StringUtils.isEmpty( strResponse ) && ( !strResponse.equals( CHECK_CONNEXION_FALSE ) ) )
                {
                    String strUID = strResponse.replace( "\"", "" );
                    user = new ParisConnectUser( strUID, parisConnectAuthentication );
                    //save paris connect cookie value
                    user.setPCUID( strPCUID );
                    fillUserData( user, ParisConnectAPIService.getUser( strPCUID ) );
                }
            }
            catch ( ParisConnectAPIException ex )
            {
                _logger.warn( ex.getMessage(  ) );
            }
        }

        return user;
    }

    /**
     * Extract the value of the connection cookie
     * @param request The HTTP request
     * @return The cookie's value
     */
    public String getConnectionCookie( HttpServletRequest request )
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
     * set a paris connect cokkie in the HttpServletResponse
     * @param strPCUID the user PCUID
     * @param response The HTTP response
     */
    public void setConnectionCookie( String strPCUID, HttpServletResponse response )
    {
        // set a connexion cookie to let the user access other PC Services without sign in
        Cookie parisConnectCookie = new Cookie( COOKIE_PARIS_CONNECT_NAME, strPCUID );
        parisConnectCookie.setDomain( COOKIE_PARIS_CONNECT_DOMAIN );
        parisConnectCookie.setSecure( COOKIE_PARIS_CONNECT_SECURE );
        parisConnectCookie.setMaxAge( COOKIE_PARIS_CONNECT_MAX_AGE );
        parisConnectCookie.setPath( COOKIE_PARIS_CONNECT_PATH );

        response.addCookie( parisConnectCookie );
    }

    /**
     * Set the user birthday
     * @param strPCUID The PCUID
     * @param strBirthday the Birthday date
     * @return  the Json response
     */
    public boolean setBirthday( String strPCUID, String strBirthday )
    {
        String strResponse = ParisConnectAPIService.setBirthday( strPCUID, strBirthday );

        return strResponse != null;
    }

    /**
     * Set the first name and the last name of the user
     * @param strPCUID The PCUID
     * @param strFirstName the user first name
     * @param strLastName the user last name
     *
     */
    public boolean setPatronyme( String strPCUID, String strFirstName, String strLastName )
    {
        String strResponse = ParisConnectAPIService.setPatronyme( strPCUID, strFirstName, strLastName );

        return strResponse != null;
    }

    /**
     * Set the User adress
     * @param strPCUID The PCUID
     * @param strLocation the location adress
     * @param strZipCode the user Zip Code
     * @param strCity the city of the user
     *
     *
     */
    public boolean setAdresse( String strPCUID, String strLocation, String strZipCode, String strCity )
    {
        String strJsonResponse = ParisConnectAPIService.setAdresse( strPCUID, strLocation, strZipCode, strCity );

        return strJsonResponse != null;
    }

    /**
     * Subscribe user to a alert
     * @param strPCUID the user pcuid
     * @param strIdAlertes the alert id
     * @return the JSON response
      */
    public String subscribeUser( String strPCUID, String strIdAlertes )
    {
        String strError = null;
        String strResponse = ParisConnectAPIService.subscribeUser( strPCUID, strIdAlertes );

        if ( strResponse != null )
        {
            if ( !RESPONSE_OK.equals( strResponse ) )
            {
                if ( RESPONSE_ALREADY.equals( strResponse ) )
                {
                    strError = ERROR_ALREADY_SUBSCRIBE;
                }
                else
                {
                    strError = ERROR_DURING_SUBSCRIBE;
                    _logger.error( "error during subscribeUser : " + strResponse );
                }
            }
        }
        else
        {
            strError = ERROR_DURING_SUBSCRIBE;
        }

        return strError;
    }

    /**
     * Unsubscribe user to a alert
     * @param strPCUID the user pcuid
     * @param strIdAlertes the alert id
     * @return the JSON response
      */
    public boolean unSubscribeUser( String strPCUID, String strIdAlertes )
    {
        boolean unscribe = false;
        String strResponse = ParisConnectAPIService.unSubscribeUser( strPCUID, strIdAlertes );

        if ( strResponse != null )
        {
            if ( RESPONSE_OK.equals( strResponse ) )
            {
                unscribe = true;
            }
            else
            {
                _logger.error( "error during unSubscribeUser : " + strResponse );
            }
        }

        return unscribe;
    }

    /**
     * get user pcuid by email
     * @param strMail the mail
     * @return the pcuid of the user if a account exist
     */
    public String getPcuidByEmail( String strMail )
    {
        return ParisConnectAPIService.getPcuidByEmail( strMail );
    }

    /**
     * create a Shadow account
     * @param strMail the mail
     * @param strIdEmail the mail id
     * @return the user PCUID
     */
    public String setAccountShadow( String strMail, String strIdEmail )
    {
        return ParisConnectAPIService.setAccountShadow( strMail, strIdEmail );
    }

    /**
     * get User uid by Pcuid
     * @param strPcuid the user Pcuid
     * @return the user Pcuid
     */
    public String getUidByPcuid( String strPcuid )
    {
        String strUID = null;

        try
        {
            String strResponse = ParisConnectAPIService.checkConnectionCookie( strPcuid );

            if ( !StringUtils.isEmpty( strResponse ) && ( !strResponse.equals( CHECK_CONNEXION_FALSE ) ) )
            {
                strUID = strResponse.replace( "\"", "" );
            }
        }
        catch ( ParisConnectAPIException ex )
        {
            _logger.warn( ex.getMessage(  ) );
        }

        return strUID;
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
        user.setUserInfo( LuteceUser.HOME_INFO_POSTAL_STREET, joObject.getString( PCUSER_ADDRESS ) );

        String strBirthDate = ParisConnectAPIService.getMetadataValue( user.getName(  ), "birthday" );
        user.setUserInfo( LuteceUser.BDATE, strBirthDate );
        user.setEmail( joObject.getString( PCUSER_EMAIL ) );

        String strVerified = joObject.getString( PCUSER_VERIFIED );
        boolean bVerified = "1".equals( strVerified );
        user.setVerified( bVerified );
    }
}
