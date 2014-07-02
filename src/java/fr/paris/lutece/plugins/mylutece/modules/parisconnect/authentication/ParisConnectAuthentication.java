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
package fr.paris.lutece.plugins.mylutece.modules.parisconnect.authentication;


import fr.paris.lutece.plugins.mylutece.authentication.PortalAuthentication;
import fr.paris.lutece.plugins.mylutece.modules.parisconnect.service.ParisConnectPlugin;
import fr.paris.lutece.plugins.mylutece.modules.parisconnect.service.ParisConnectService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;


/**
 * ParisConnect Authentication
 */

public class ParisConnectAuthentication extends PortalAuthentication
{
    private static final String AUTH_SERVICE_NAME = "Lutece Paris Connect Authentication Service";
    private static final String URL_ICON = "images/local/skin/plugins/mylutece/modules/parisconnect/parisconnect.png";

    /**
     * Constructor
     */
    public ParisConnectAuthentication(  )
    {
    }

    /**
     * Gets the Authentification service name
     * @return The name of the authentication service
     */
    @Override
    public String getAuthServiceName(  )
    {
        return AUTH_SERVICE_NAME;
    }

    /**
     * Gets the Authentification type
     * @param request The HTTP request
     * @return The type of authentication
     */
    @Override
    public String getAuthType( HttpServletRequest request )
    {
        return HttpServletRequest.BASIC_AUTH;
    }

    /**
     * This methods checks the login info in the base repository
     *
     * @param strUserName The username
     * @param strUserPassword The password
     * @param request The HTTP request
     * @return A LuteceUser object corresponding to the login
     * @throws LoginException The LoginException
     */
    @Override
    public LuteceUser login( String strUserName, String strUserPassword, HttpServletRequest request )
        throws LoginException
    {
        return ParisConnectService.getInstance(  ).doLogin( request, strUserName, strUserPassword, this );
    }

    /**
     * This methods logout the user
     * @param user The user
     */
    @Override
    public void logout( LuteceUser user )
    {
    }

    /**
     * This method returns an anonymous Lutece user
     *
     * @return An anonymous Lutece user
     */
    @Override
    public LuteceUser getAnonymousUser(  )
    {
        /**@todo Impl?menter cette m?thode fr.paris.lutece.portal.service.security.PortalAuthentication*/
        throw new java.lang.UnsupportedOperationException( 
            "La methode getAnonymousUser() n'est pas encore implementee." );
    }

    /**
     * Checks that the current user is associated to a given role
     * @param user The user
     * @param request The HTTP request
     * @param strRole The role name
     * @return Returns true if the user is associated to the role, otherwise false
     */
    @Override
    public boolean isUserInRole( LuteceUser user, HttpServletRequest request, String strRole )
    {
        return request.isUserInRole( strRole );
    }

    /**
     * Indicate that the authentication uses only HttpRequest data to authenticate
     * users  (ex : Web Server authentication).
     * @return true if the authentication service authenticates users only with the Http Request, otherwise false.
     */
    public boolean isBasedOnHttpAuthentication(  )
    {
        return true;
    }

    /**
     * Returns a Lutece user object if the user is already authenticated by Paris Connect
     * @param request The HTTP request
     * @return Returns A Lutece User or null if there no user authenticated
     */
    @Override
    public LuteceUser getHttpAuthenticatedUser( HttpServletRequest request )
    {
        ParisConnectUser user = ParisConnectService.getInstance(  ).getHttpAuthenticatedUser( request, this );

        return user;
    }

    /**
     *
     *{@inheritDoc}
     */
    @Override
    public String getIconUrl(  )
    {
        return URL_ICON;
    }

    /**
     *
     *{@inheritDoc}
     */
    @Override
    public String getName(  )
    {
        return ParisConnectPlugin.PLUGIN_NAME;
    }

    /**
     *
     *{@inheritDoc}
     */
    @Override
    public String getPluginName(  )
    {
        return ParisConnectPlugin.PLUGIN_NAME;
    }
}
