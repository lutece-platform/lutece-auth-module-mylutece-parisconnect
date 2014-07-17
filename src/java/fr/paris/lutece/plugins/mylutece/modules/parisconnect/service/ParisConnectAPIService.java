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

import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.HashMap;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.apache.log4j.Logger;


/**
 * ParisConnect API Service
 */
public final class ParisConnectAPIService
{
    public static final String USER_UID = "uid";
    public static final String PCUID = "pcuid";
    public static final String MESSAGE = "message";
    private static final String METHOD_DO_LOGIN = "do_login";
    private static final String METHOD_CHECK_CONNECTION_COOKIE = "check_connection_cookie";
    private static final String METHOD_GET_USER = "get_user";
    private static final String METHOD_SET_COOKIE = "set_cookie";
    private static final String METHOD_GET_METADATA2 = "get_metadata2";
    private static final String PARAMETER_EMAIL = "email";
    private static final String PARAMETER_PWD = "pwd";
    private static final String PARAMETER_LABEL = "label";
    private static final String PARAMETER_TABLENAME = "tableName";
    private static final String PARAMTER_TABLEID = "tableId";
    private static final String PARAMETER_CONNECTION_COOKIE = "connection_cookie";
    private static final String KEY_VALUE = "value";
    
    private static final ParisConnectAPI _accountAPI = SpringContextService.getBean( "mylutece-parisconnect.apiAccount" );
    private static final ParisConnectAPI _usersAPI = SpringContextService.getBean( "mylutece-parisconnect.apiUsers" );
    private static final ParisConnectAPI _metadataAPI = SpringContextService.getBean( 
            "mylutece-parisconnect.apiMetadata" );
    private static Logger _logger = org.apache.log4j.Logger.getLogger( Constants.LOGGER_PARISCONNECT );

    /** Private constructor */
    private ParisConnectAPIService(  )
    {
    }

    /**
     * Process login
     * @param strUserName The User's name
     * @param strUserPassword The User's password
     * @return The response provided by the API in JSON format
     */
    static String doLogin( String strUserName, String strUserPassword )
        throws ParisConnectAPIException
    {
        Map<String, String> mapParameters = new HashMap<String, String>(  );

        mapParameters.put( PARAMETER_EMAIL, strUserName );
        mapParameters.put( PARAMETER_PWD, strUserPassword );

        return _accountAPI.callMethod( METHOD_DO_LOGIN, mapParameters );
    }

    /**
     * Checks the connection cookie
     * @param strConnectionCookie The 'account' cookie value
     * @return The UID if the cookie is valid otherwyse false
     */
    static String checkConnectionCookie( String strConnectionCookie )
        throws ParisConnectAPIException
    {
        Map<String, String> mapParameters = new HashMap<String, String>(  );
        mapParameters.put( PARAMETER_CONNECTION_COOKIE, strConnectionCookie );

        return _accountAPI.callMethod( METHOD_CHECK_CONNECTION_COOKIE, mapParameters );
    }

    /**
     * Set a connection cookie for the domain
     * @param strUID The User ID
     */
    static void setConnectionCookie( String strUID )
    {
        Map<String, String> mapParameters = new HashMap<String, String>(  );
        mapParameters.put( USER_UID, strUID );

        try
        {
            _accountAPI.callMethod( METHOD_SET_COOKIE, mapParameters , false );
        }
        catch ( ParisConnectAPIException ex )
        {
            _logger.error( ex.getMessage(  ) );
        }
    }

    /**
     * Get user infos
     * @param strPCUID The UserID
     * @return The response provided by the API in JSON format
     */
    static String getUser( String strPCUID ) throws ParisConnectAPIException
    {
        Map<String, String> mapParameters = new HashMap<String, String>(  );
        mapParameters.put( PCUID, strPCUID );

        return _usersAPI.callMethod( METHOD_GET_USER, mapParameters );
    }

    static String getMetadataValue( String strUID, String strMetadataName )
       
    {
        String strTableName = (String) _metadataAPI.getMap().get( strMetadataName );
        Map<String, String> mapParameters = new HashMap<String, String>(  );
        mapParameters.put( PARAMETER_LABEL, strMetadataName );
        mapParameters.put( PARAMETER_TABLENAME, strTableName );
        mapParameters.put( PARAMTER_TABLEID, strUID );
        
        try
        {
            String strResponse = _metadataAPI.callMethod( METHOD_GET_METADATA2, mapParameters );
            JSONArray jaMetadatas = (JSONArray) JSONSerializer.toJSON( strResponse );
            JSONObject joMetadata = jaMetadatas.getJSONObject( 0 );
            return joMetadata.getString( KEY_VALUE );
        }
        catch (ParisConnectAPIException ex)
        {
            _logger.warn( "Metadata API call : metadata=" + strMetadataName + " - " + ex.getMessage());
            return "";
        }
        catch (JSONException ex)
        {
            _logger.warn( "Metadata API call : metadata=" + strMetadataName + " - " + ex.getMessage());
            return "";
        }
    }
}
