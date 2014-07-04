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

import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.httpaccess.HttpAccess;
import fr.paris.lutece.util.httpaccess.HttpAccessException;
import fr.paris.lutece.util.url.UrlItem;

import org.apache.log4j.Logger;

import java.util.Map;


/**
 * ParisConnectAPI
 */
public class ParisConnectAPI
{
    private static final String PARAMETER_API_ID = "api_id";
    private static final String PARAMETER_SECRET_KEY = "secret_key";
    private static final String PROPERTY_API_CALL_DEBUG = "mylutece-parisconnect.api.debug";
    private static Logger _logger = Logger.getLogger( Constants.LOGGER_PARISCONNECT );
    private static boolean _bDebug = AppPropertiesService.getPropertyBoolean( PROPERTY_API_CALL_DEBUG, false );

    // Variables declarations 
    private String _strName;
    private String _strUrl;
    private String _strApiId;
    private String _strSecretKey;

    /**
     * Returns the Name
     *
     * @return The Name
     */
    public String getName(  )
    {
        return _strName;
    }

    /**
     * Sets the Name
     *
     * @param strName The Name
     */
    public void setName( String strName )
    {
        _strName = strName;
    }

    /**
     * Returns the Url
     *
     * @return The Url
     */
    public String getUrl(  )
    {
        return _strUrl;
    }

    /**
     * Sets the Url
     *
     * @param strUrl The Url
     */
    public void setUrl( String strUrl )
    {
        _strUrl = strUrl;
    }

    /**
     * Returns the ApiId
     *
     * @return The ApiId
     */
    public String getApiId(  )
    {
        return _strApiId;
    }

    /**
     * Sets the ApiId
     *
     * @param strApiId The ApiId
     */
    public void setApiId( String strApiId )
    {
        _strApiId = strApiId;
    }

    /**
     * Returns the SecretKey
     *
     * @return The SecretKey
     */
    public String getSecretKey(  )
    {
        return _strSecretKey;
    }

    /**
     * Sets the SecretKey
     *
     * @param strSecretKey The SecretKey
     */
    public void setSecretKey( String strSecretKey )
    {
        _strSecretKey = strSecretKey;
    }

    /**
     * Call a Method of the API
     * @param strMethod The method name
     * @param mapParameters Parameters
     * @return The string returned by the API
     */
    public String callMethod( String strMethod, Map mapParameters )
    {
        HttpAccess httpAccess = new HttpAccess(  );
        UrlItem url = new UrlItem( _strUrl + strMethod );
        mapParameters.put( PARAMETER_API_ID, _strApiId );
        mapParameters.put( PARAMETER_SECRET_KEY, _strSecretKey );

        String strResponse = "";

        try
        {
            strResponse = httpAccess.doPost( url.getUrl(  ), mapParameters );

            if ( _bDebug )
            {
                _logger.debug( "API call : " + getCallUrl( url.getUrl(  ), mapParameters ) );
            }
        }
        catch ( HttpAccessException ex )
        {
            _logger.error( "Error calling method '" + strMethod + " - " + ex.getMessage(  ), ex );
        }

        return strResponse;
    }

    /**
     * Build the URL
     * @param strUrl The base URL
     * @param mapParameters Parameters
     * @return The full URL
     */
    private String getCallUrl( String strUrl, Map<String, String> mapParameters )
    {
        UrlItem url = new UrlItem( strUrl );

        for ( String strKey : mapParameters.keySet(  ) )
        {
            url.addParameter( strKey, mapParameters.get( strKey ) );
        }

        return url.getUrl(  );
    }
}