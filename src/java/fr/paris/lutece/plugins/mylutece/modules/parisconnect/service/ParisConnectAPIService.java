/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.paris.lutece.plugins.mylutece.modules.parisconnect.service;

import fr.paris.lutece.plugins.mylutece.modules.parisconnect.authentication.ParisConnectUser;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.httpaccess.HttpAccess;
import fr.paris.lutece.util.httpaccess.HttpAccessException;
import fr.paris.lutece.util.url.UrlItem;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;

/**
 *
 */
public class ParisConnectAPIService
{
    public static final String METHOD_DO_LOGIN = "do_login";

    private static final String PROPERTY_API_URL = "parisconnect.api.account.url";
    private static final String PROPERTY_SECRET_KEY = "parisconnect.api.account.secretKey";
    private static final String API_URL = AppPropertiesService.getProperty(PROPERTY_API_URL);
    private static final String SECRET_KEY = AppPropertiesService.getProperty(PROPERTY_SECRET_KEY);
    private static final String PARAMETER_API_ID = "api_id";
    private static final String PARAMETER_SECRET_KEY = "secret_key";
    private static final String PARAMETER_EMAIL = "email";
    private static final String PARAMETER_PWD = "pwd";
    private static final String API_ID = "accounts";

    
    
    
    
    public static String callMethod( String strMethod , Map mapParameters )
    {
        HttpAccess httpAccess = new HttpAccess();
        UrlItem url = new UrlItem( API_URL + strMethod );
        mapParameters.put( PARAMETER_API_ID , API_ID );
        mapParameters.put( PARAMETER_SECRET_KEY , SECRET_KEY );
        String strResponse = "";
        try
        {
            strResponse = httpAccess.doPost( url.getUrl() , null );
        }
        catch (HttpAccessException ex)
        {
            AppLogService.error( "ParisConnectAPIService : Error calling method '" + strMethod + "' :" + ex.getMessage() , ex );
        }
        return strResponse;
    }
    
    static ParisConnectUser doLogin(HttpServletRequest request, String strUserName, String strUserPassword)
    {
        Map<String, String> mapParameters = new HashMap<String, String>();
        
        mapParameters.put( PARAMETER_EMAIL , strUserName );
        mapParameters.put( PARAMETER_PWD , strUserPassword );
        
        String response = callMethod( METHOD_DO_LOGIN , mapParameters );
        
    }
    
    
}
