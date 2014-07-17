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

import fr.paris.lutece.portal.service.security.LuteceAuthentication;
import fr.paris.lutece.portal.service.security.LuteceUser;

import java.util.ArrayList;


/**
 * ParisConnect User
 */
public class ParisConnectUser extends LuteceUser
{
    public String ROLE_VERIFIED = "verified";
    private String _strEmail;
    private boolean _bVerified;

    /**
    * Constructor
    * @param strUserName The user's name
    * @param authenticationService The authentication service that authenticates the user
    */
    public ParisConnectUser( String strUserName, LuteceAuthentication authenticationService )
    {
        super( strUserName, authenticationService );
    }

    /**
     * Returns the Email
     *
     * @return The Email
     */
    @Override
    public String getEmail(  )
    {
        return _strEmail;
    }

    /**
     * Sets the Email
     *
     * @param strEmail The Email
     */
    public void setEmail( String strEmail )
    {
        _strEmail = strEmail;
    }

    /**
     * Returns the Verified status
     *
     * @return True if is verified
     */
    public boolean isVerified(  )
    {
        return _bVerified;
    }

    /**
     * Sets the Verified status
     *
     * @param bVerified The Verified status
     */
    public void setVerified( boolean bVerified )
    {
        if ( bVerified )
        {
            ArrayList<String> listRoles = new ArrayList<String>(  );
            listRoles.add( ROLE_VERIFIED );
            addRoles( listRoles );
        }

        _bVerified = bVerified;
    }
}
