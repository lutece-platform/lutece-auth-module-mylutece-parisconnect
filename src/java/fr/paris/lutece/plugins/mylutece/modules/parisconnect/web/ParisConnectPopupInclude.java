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
package fr.paris.lutece.plugins.mylutece.modules.parisconnect.web;

import fr.paris.lutece.portal.service.content.PageData;
import fr.paris.lutece.portal.service.includes.PageInclude;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * Page include to add the
 */
public class ParisConnectPopupInclude implements PageInclude
{
    // Template
    private static final String TEMPLATE_INCLUDE = "skin/plugins/mylutece/module/parisconnect/popup_include.html";

    // Properties
    private static final String PROPERTY_PARIS_CONNECT_POPUP_LOGIN_URL = "mylutece-parisconnect.url.popupLoginUrl";

    // Mark
    private static final String MARK_PARIS_CONNECT_POPUP_LOGIN_URL = "paris_connect_login_url";
    private static final String MARK_PARIS_CONNECT_POPUP_INCLUDE = "paris_connect_popup_include";

    /**
     * {@inheritDoc}
     */
    @Override
    public void fillTemplate( Map<String, Object> rootModel, PageData data, int nMode, HttpServletRequest request )
    {
        rootModel.put( MARK_PARIS_CONNECT_POPUP_INCLUDE, getTemplate( request ) );
    }

    /**
     *
     * @param request
     * @return template
     */
    private String getTemplate( HttpServletRequest request )
    {
        Map<String, Object> model = new HashMap<String, Object>(  );
        Locale locale = ( request == null ) ? null : request.getLocale(  );
        String strParisConnectPopupLoginUrl = AppPropertiesService.getProperty( PROPERTY_PARIS_CONNECT_POPUP_LOGIN_URL );
        model.put( MARK_PARIS_CONNECT_POPUP_LOGIN_URL, strParisConnectPopupLoginUrl );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_INCLUDE, locale, model );

        return template.getHtml(  );
    }
}
