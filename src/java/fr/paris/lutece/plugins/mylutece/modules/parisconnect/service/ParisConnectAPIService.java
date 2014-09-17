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

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;


/**
 * ParisConnect API Service
 */
public final class ParisConnectAPIService
{
    public static final String USER_UID = "uid";
    public static final String PCUID = "pcuid";
    public static final String MESSAGE = "message";
    private static final String METHOD_DO_LOGIN = "do_login";
    private static final String METHOD_DISCONNECT = "disconnect";
    private static final String METHOD_CHECK_CONNECTION_COOKIE = "check_connection_cookie";
    private static final String METHOD_GET_USER = "get_user";
    private static final String METHOD_SET_COOKIE = "set_cookie";
    private static final String METHOD_GET_METADATA2 = "get_metadata2";
    private static final String METHOD_SET_BIRTHDAY = "set_birthday";
    private static final String METHOD_SET_PATRONYME = "set_patronyme";
    private static final String METHOD_SET_ADRESSE = "set_adresse";
    private static final String METHOD_SUBSCIBE_USER = "subscribe_user";
    private static final String METHOD_UNSUBSCIBE_USER = "unsubscribe_user";
    private static final String METHOD_GET_PCUID_BY_EMAIL = "get_pcuid_by_email";
    private static final String METHOD_IS_VERIFIED = "is_verified";
    private static final String METHOD_VERIFICATION = "verification";
    private static final String METHOD_SET_ACCOUNT_SHADOW = "set_account_shadow";
    private static final String METHOD_AVIS= "avisajax.php";
    public static final String PARAMETER_PCUID = "pcuid";
    public static final String PARAMETER_ID_ALERTES = "idalertes";
    public static final String PARAMETER_ID_EMAIL = "id_mail";
    private static final String PARAMETER_EMAIL = "email";
    private static final String PARAMETER_PWD = "pwd";
    private static final String PARAMETER_LABEL = "label";
    private static final String PARAMETER_TABLENAME = "tableName";
    private static final String PARAMTER_TABLEID = "tableId";
    private static final String PARAMETER_CONNECTION_COOKIE = "connection_cookie";
    private static final String PARAMETER_BIRTHDAY = "birthday";
    private static final String PARAMETER_FIRSTNAME = "firstname";
    private static final String PARAMETER_NAME = "name";
    private static final String PARAMETER_LOCATION = "location";
    private static final String PARAMETER_ZIP_CODE = "zip_code";
    private static final String PARAMETER_CITY = "city";
    private static final String PARAMETER_MESSAGE = "message";
    private static final String PARAMETER_CATEGORY = "cat";
    private static final String PARAMETER_RETURN_URL = "returnUrl";
    private static final String KEY_VALUE = "value";
    private static final String KEY_DATA = "data";
    private static final String KEY_PCUID = "pcuid";
    private static final String KEY_SUCCESS = "success";
    private static final String KEY_SUCCES = "succes";
    private static final String KEY_STATUS = "status";
    
    private static final String KEY_IS_VERIFIED = "is_verified";
    private static final String KEY_MESSAGE = "message";
    
    
    
    private static final ParisConnectAPI _accountAPI = SpringContextService.getBean( "mylutece-parisconnect.apiAccount" );
    private static final ParisConnectAPI _usersAPI = SpringContextService.getBean( "mylutece-parisconnect.apiUsers" );
    private static final ParisConnectAPI _metadataAPI = SpringContextService.getBean( 
            "mylutece-parisconnect.apiMetadata" );
    
    private static final ParisConnectAPI _mibAPI = SpringContextService.getBean( 
            "mylutece-parisconnect.apiMib" );
    
    private static final ParisConnectAPI _psupAPI = SpringContextService.getBean( "mylutece-parisconnect.apiPsup" );
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

        return _accountAPI.callMethod( METHOD_CHECK_CONNECTION_COOKIE, mapParameters, false );
    }

    /**
     * Process user logout
     * @param strPCUID the user PCUID
     * @return The response provided by the API in JSON format
     */
    static String doDisconnect( String strPCUID ) throws ParisConnectAPIException
    {
        Map<String, String> mapParameters = new HashMap<String, String>(  );
        mapParameters.put( PARAMETER_CONNECTION_COOKIE, strPCUID );

        return _accountAPI.callMethod( METHOD_DISCONNECT, mapParameters, false );
    }

    /**
     * Set a connection cookie for the domain
     * @param strUID The User ID
     * @return the Pcuid
     */
    static String setConnectionCookie( String strUID )
    {
        Map<String, String> mapParameters = new HashMap<String, String>(  );
        mapParameters.put( USER_UID, strUID );

        String strPcuid = null;

        try
        {
            strPcuid = _accountAPI.callMethod( METHOD_SET_COOKIE, mapParameters, false );
        }
        catch ( ParisConnectAPIException ex )
        {
            _logger.error( ex.getMessage(  ) );
        }

        return strPcuid;
    }

    /**
     * Get user infos
     * @param strPCUID The UserID
     * @return The response provided by the API in JSON format
     */
    static String getUser( String strPCUID ) throws ParisConnectAPIException
    {
        Map<String, String> mapParameters = new HashMap<String, String>(  );
        mapParameters.put( PARAMETER_PCUID, strPCUID );

        return _usersAPI.callMethod( METHOD_GET_USER, mapParameters );
    }

    static String getMetadataValue( String strUID, String strMetadataName )
    {
        String strTableName = (String) _metadataAPI.getMap(  ).get( strMetadataName );
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
        catch ( ParisConnectAPIException ex )
        {
            _logger.warn( "Metadata API call : metadata=" + strMetadataName + " - " + ex.getMessage(  ) );

            return "";
        }
        catch ( JSONException ex )
        {
            _logger.warn( "Metadata API call : metadata=" + strMetadataName + " - " + ex.getMessage(  ) );

            return "";
        }
    }

    /**
     * Set the user birthday
     * @param strPCUID The PCUID
     * @param strBirthday the Birthday date
     * @return  the Json response
     */
    static String setBirthday( String strPCUID, String strBirthday )
    {
        Map<String, String> mapParameters = new HashMap<String, String>(  );
        mapParameters.put( PARAMETER_PCUID, strPCUID );
        mapParameters.put( PARAMETER_BIRTHDAY, strBirthday );

        String strResponse = null;

        try
        {
            strResponse = _usersAPI.callMethod( METHOD_SET_BIRTHDAY, mapParameters, false );
        }
        catch ( ParisConnectAPIException ex )
        {
            _logger.error( ex.getMessage(  ) );
        }

        return strResponse;
    }

    /**
     * Set the first name and the last name of the user
     * @param strPCUID The PCUID
     * @param strFirstName the user first name
     * @param strLastName the user last name
     * @return  the Json response
     */
    static String setPatronyme( String strPCUID, String strFirstName, String strLastName )
    {
        Map<String, String> mapParameters = new HashMap<String, String>(  );
        mapParameters.put( PARAMETER_PCUID, strPCUID );
        mapParameters.put( PARAMETER_FIRSTNAME, strFirstName );
        mapParameters.put( PARAMETER_NAME, strLastName );

        String strResponse = null;

        try
        {
            strResponse = _usersAPI.callMethod( METHOD_SET_PATRONYME, mapParameters, false );
        }
        catch ( ParisConnectAPIException ex )
        {
            _logger.error( ex.getMessage(  ) );
        }

        return strResponse;
    }

    /**
     * Set the User adress
     * @param strPCUID The PCUID
     * @param strLocation the location adress
     * @param strZipCode the user Zip Code
     * @param strCity the city of the user
     *
     * @return  the Json response
     */
    static String setAdresse( String strPCUID, String strLocation, String strZipCode, String strCity )
    {
        Map<String, String> mapParameters = new HashMap<String, String>(  );
        mapParameters.put( PARAMETER_PCUID, strPCUID );
        mapParameters.put( PARAMETER_LOCATION, strLocation );
        mapParameters.put( PARAMETER_ZIP_CODE, strZipCode );
        mapParameters.put( PARAMETER_CITY, strCity );

        String strResponse = null;

        try
        {
            strResponse = _usersAPI.callMethod( METHOD_SET_ADRESSE, mapParameters, false );
        }
        catch ( ParisConnectAPIException ex )
        {
            _logger.error( ex.getMessage(  ) );
        }

        return strResponse;
    }

    /**
     * Subscribe user to a alert
     * @param strPCUID the user pcuid
     * @param strIdAlertes the alert id
     * @return the JSON response
      */
    static String subscribeUser( String strPCUID, String strIdAlertes )
    {
        Map<String, String> mapParameters = new HashMap<String, String>(  );
        mapParameters.put( PARAMETER_PCUID, strPCUID );
        mapParameters.put( PARAMETER_ID_ALERTES, strIdAlertes );

        String strResponse = null;

        try
        {
            strResponse = _psupAPI.callMethod( METHOD_SUBSCIBE_USER, mapParameters, false );
        }
        catch ( ParisConnectAPIException ex )
        {
            _logger.error( ex.getMessage(  ) );
        }

        return strResponse;
    }

    /**
     * Unsubscribe user to a alert
     * @param strPCUID the user pcuid
     * @param strIdAlertes the alert id
     * @return the JSON response
      */
    static String unSubscribeUser( String strPCUID, String strIdAlertes )
    {
        Map<String, String> mapParameters = new HashMap<String, String>(  );
        mapParameters.put( PARAMETER_PCUID, strPCUID );
        mapParameters.put( PARAMETER_ID_ALERTES, strIdAlertes );

        String strResponse = null;

        try
        {
            strResponse = _psupAPI.callMethod( METHOD_UNSUBSCIBE_USER, mapParameters, false );
        }
        catch ( ParisConnectAPIException ex )
        {
            _logger.error( ex.getMessage(  ) );
        }

        return strResponse;
    }

    /**
     * get user PCUID by email
     * @param strMail the mail
     * @return the PCUID of the user if a account exist
     */
    static String getPcuidByEmail( String strMail )
    {
        Map<String, String> mapParameters = new HashMap<String, String>(  );
        mapParameters.put( PARAMETER_EMAIL, strMail );

        String strPcuid = null;
        String strResponse = null;

        try
        {
            strResponse = _usersAPI.callMethod( METHOD_GET_PCUID_BY_EMAIL, mapParameters, true );

            JSONObject jo = (JSONObject) JSONSerializer.toJSON( strResponse );
            strPcuid = jo.getString( KEY_PCUID );
        }
        catch ( ParisConnectAPIException ex )
        {
            _logger.warn( "Account Shadow  API call : mail=" + strMail + " - " + ex.getMessage(  ) );
        }
        catch ( JSONException ex )
        {
            _logger.error( "Account Shadow  API call : mail=" + strMail + " - " + ex.getMessage(  ) );
        }

        return strPcuid;
    }

    /**
     * create a Shadow account
     * @param strMail the mail
     * @param strIdEmail the mail id
     * @return the user PCUID
     * */
    static String setAccountShadow( String strMail, String strIdEmail )
    {
        Map<String, String> mapParameters = new HashMap<String, String>(  );
        mapParameters.put( PARAMETER_EMAIL, strMail );
        mapParameters.put( PARAMETER_ID_EMAIL, strIdEmail );

        String strPcuid = null;
        String strResponse = null;

        try
        {
            strResponse = _usersAPI.callMethod( METHOD_SET_ACCOUNT_SHADOW, mapParameters, false );
            strResponse = strResponse.trim(  );

            JSONObject jo = (JSONObject) JSONSerializer.toJSON( strResponse );
            strPcuid = jo.getString( KEY_PCUID );
        }
        catch ( ParisConnectAPIException ex )
        {
            _logger.warn( "Account Shadow  API call : mail=" + strMail + " - " + ex.getMessage(  ) );
        }
        catch ( JSONException ex )
        {
            _logger.error( "Account Shadow  API call : mail=" + strMail + " - " + ex.getMessage(  ) );
        }

        return strPcuid;
    }
    
    /**
     * send Avis Message
     * @param strMail the mail
     * @param strMessage the messgae
     * @return strCategory the category
     * */
    static boolean sendAvisMessage( String strMail, String strMessage,String strCategory,String strBackUrl )
    {
        Map<String, String> mapParameters = new HashMap<String, String>(  );
        mapParameters.put( PARAMETER_EMAIL, strMail );
        mapParameters.put( PARAMETER_MESSAGE, strMessage );
        mapParameters.put( PARAMETER_CATEGORY, strCategory );

        String strResponse = null;

        try
        {
            strResponse = _mibAPI.callMethod( METHOD_AVIS+"?url="+strBackUrl, mapParameters, false );
            JSONObject jo = (JSONObject) JSONSerializer.toJSON( strResponse );
            if(jo.containsKey(KEY_SUCCESS))
            {
            	
            	return true;
            	
            }
            
        }
        catch ( ParisConnectAPIException ex )
        {
            _logger.warn( "Account MIB  API call : mail=" + strMail + " - " + ex.getMessage(  ) );
        }
        catch ( JSONException ex )
        {
            _logger.error( "Account MIB  API call : mail=" + strMail + " - " + ex.getMessage(  ) );
        }

        return false;
    }
    
    
    /**
     * return true if the user has verified his account
     * @param strPcuid the pcuid
     * @return true if the user has verified his account
     */
    static boolean isVerified( String strPcuid )
    {
        Map<String, String> mapParameters = new HashMap<String, String>(  );
        mapParameters.put( PARAMETER_PCUID, strPcuid );
       
        String strResponse = null;

        try
        {
            strResponse = _usersAPI.callMethod( METHOD_IS_VERIFIED, mapParameters, true );

            JSONObject jo = (JSONObject) JSONSerializer.toJSON( strResponse );
            if(jo.containsKey(KEY_STATUS) && jo.getString(KEY_STATUS).equals(KEY_SUCCES) )
            {
            	 
            	
            	if(((JSONObject)jo.get(KEY_DATA)).getString(KEY_IS_VERIFIED).equals("1"))
            	{
            		return true;
            	}
            }
            else
            {
         
            	 _logger.error( "Is Verified  API call : strPcuid=" + strPcuid + " - " + jo.getString(KEY_MESSAGE) );
            	
            }
            
         }
        catch ( ParisConnectAPIException ex )
        {
            _logger.warn( "Is Verified  API call : strPcuid=" + strPcuid + " - " + ex.getMessage(  ) );
        }
        catch ( JSONException ex )
        {
            _logger.error( "Is Verified  API call : strPcuid=" + strPcuid + " - " + ex.getMessage(  ) );
        }

        return false;
    }
    
    
    /**
     * return true if a mail of confirmation have been send to the user
     * @param strMail the user email
     * @param strReturnUrl the user back url
     * @return true if a mail of confirmation have been send to the user
     */
    static boolean verification( String strMail,String strReturnUrl )
    {
        Map<String, String> mapParameters = new HashMap<String, String>(  );
        mapParameters.put( PARAMETER_EMAIL, strMail );
        mapParameters.put( PARAMETER_RETURN_URL, strReturnUrl );
        String strResponse = null;

        try
        {
            strResponse = _usersAPI.callMethod( METHOD_VERIFICATION, mapParameters, true );
          
        	
            JSONObject jo = (JSONObject) JSONSerializer.toJSON( strResponse );
            if(jo.containsKey(KEY_STATUS) && jo.getString(KEY_STATUS).equals(KEY_SUCCESS) )
            {
            	if(jo.getString(KEY_DATA).equals("true"))
            	{
            		return true;
            	}
            }
            else
            {
            	 _logger.error( "Verification  API call : strMail=" + strMail + " - " + jo.getString(KEY_MESSAGE) );
            	
            }
            
         }
        catch ( ParisConnectAPIException ex )
        {
            _logger.warn( "Verification  API call : strMail=" + strMail + " - " + ex.getMessage(  ) );
        }
        catch ( JSONException ex )
        {
            _logger.error( "Verification  API call : strMail=" + strMail + " - " + ex.getMessage(  ) );
        }

        return false;
    }
    
    
}
