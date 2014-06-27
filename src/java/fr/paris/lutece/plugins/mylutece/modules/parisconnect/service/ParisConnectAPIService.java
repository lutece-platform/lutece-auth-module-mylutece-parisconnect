/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.paris.lutece.plugins.mylutece.modules.parisconnect.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.httpaccess.HttpAccess;
import fr.paris.lutece.util.httpaccess.HttpAccessException;
import fr.paris.lutece.util.url.UrlItem;


/**
 *
 */
public class ParisConnectAPIService
{
    public static final String METHOD_DO_LOGIN = "do_login";
    public static final String METHOD_CHECK_CONNECTION_COOKIE = "check_connection_cookie";
    public static final String RESPONSE_STATUS = "status";
    public static final String RESPONSE_DATA = "data";
    public static final String CHECK_CONNEXION_FALSE = "false";
    public static final String RESPONSE_SATUS_SUCCESS = "success";
    public static final String USER_UID = "uid";
    public static final String PCUID = "pcuid";
    public static final String MESSAGE = "message";
    private static final String PROPERTY_API_URL = "parisconnect.api.account.url";
    private static final String PROPERTY_SECRET_KEY = "parisconnect.api.account.secretKey";
    private static final String API_URL = AppPropertiesService.getProperty( PROPERTY_API_URL );
    private static final String SECRET_KEY = AppPropertiesService.getProperty( PROPERTY_SECRET_KEY );
    private static final String PARAMETER_API_ID = "api_id";
    private static final String PARAMETER_SECRET_KEY = "secret_key";
    private static final String PARAMETER_EMAIL = "email";
    private static final String PARAMETER_PWD = "pwd";
    private static final String PARAMETER_CONNECTION_COOKIE = "connection_cookie";
    private static final String API_ID = "accounts";

    public static String callMethod( String strMethod, Map mapParameters )
    {
        HttpAccess httpAccess = new HttpAccess(  );
        UrlItem url = new UrlItem( API_URL + strMethod );
        mapParameters.put( PARAMETER_API_ID, API_ID );
        mapParameters.put( PARAMETER_SECRET_KEY, SECRET_KEY );

        String strResponse = "";

        try
        {
            strResponse = httpAccess.doPost( url.getUrl(  ), mapParameters );
        }
        catch ( HttpAccessException ex )
        {
            AppLogService.error( "ParisConnectAPIService : Error calling method '" + strMethod + "' :" +
                ex.getMessage(  ), ex );
        }

        return strResponse;
    }

    static String doLogin( HttpServletRequest request, String strUserName, String strUserPassword )
    {
        Map<String, String> mapParameters = new HashMap<String, String>(  );

        mapParameters.put( PARAMETER_EMAIL, strUserName );
        mapParameters.put( PARAMETER_PWD, strUserPassword );

        return callMethod( METHOD_DO_LOGIN, mapParameters );
    }

    static String checkConnectionCookie( HttpServletRequest request, String strConnectionCookie )
    {
        Map<String, String> mapParameters = new HashMap<String, String>(  );
        mapParameters.put( PARAMETER_CONNECTION_COOKIE, strConnectionCookie );

        return callMethod( METHOD_CHECK_CONNECTION_COOKIE, mapParameters );
    }
}
