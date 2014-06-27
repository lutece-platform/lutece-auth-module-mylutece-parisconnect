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
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * ParisConnectService
 */
public final class ParisConnectService
{

    private static final String AUTHENTICATION_BEAN_NAME = "mylutece-parisconnect.authentication";
    private static final ParisConnectService _singleton = new ParisConnectService();

    /**
     * Empty constructor
     */
    private ParisConnectService()
    {
        // nothing
    }

    /**
     * Gets the instance
     *
     * @return the instance
     */
    public static ParisConnectService getInstance()
    {
        return _singleton;
    }

    /**
     * Inits plugin. Registers authentication
     */
    public void init()
    {
        ParisConnectAuthentication authentication = (ParisConnectAuthentication) SpringContextService.getPluginBean(ParisConnectPlugin.PLUGIN_NAME,
                AUTHENTICATION_BEAN_NAME);
        if (authentication != null)
        {
            MultiLuteceAuthentication.registerAuthentication(authentication);
        }
        else
        {
            AppLogService.error("ParisConnectAuthentication not found, please check your parisconnect_context.xml configuration");
        }
    }

    

    public ParisConnectUser doLogin(HttpServletRequest request, String strUserName, String strUserPassword)
    {
        return ParisConnectAPIService.doLogin( request , strUserName , strUserPassword );
    }
    
    
}
