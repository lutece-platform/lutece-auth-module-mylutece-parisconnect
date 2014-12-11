package fr.paris.lutece.plugins.mylutece.modules.parisconnect.service;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.mylutece.modules.parisconnect.authentication.ParisConnectUser;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.html.HtmlTemplate;


/**
 * 
 * ParisConnectIncludeService
 *
 */
public class ParisConnectIncludeService {


	public static final String  TOKEN_DO_SEND_AVIS ="doSendAvis";
	public static final String  TOKEN_DO_SUBSCRIBE_ALERT ="doSubscribeAlert";
	
	   // Template
    private static final String TEMPLATE_ALERTE_INCLUDE = "skin/plugins/mylutece/module/parisconnect/alerte_include.html";
    private static final String TEMPLATE_AVIS_INCLUDE = "skin/plugins/mylutece/module/parisconnect/avis_include.html";
    private static final String TEMPLATE_MIB_INCLUDE = "skin/plugins/mylutece/module/parisconnect/mib_include.html";
    private static final String TEMPLATE_PC_POPUP_INCLUDE = "skin/plugins/mylutece/module/parisconnect/popup_include.html";
    
    // Mark
    private static final String MARK_USER_EMAIL = "user_email";
    private static final String MARK_MIB_URL = "mib_url";
    private static final String MARK_PARIS_CONNECT_POPUP_LOGIN_URL = "paris_connect_login_url";

    
   //Key
    private static final String KEY_PARIS_CONNECT_MIB_URL="budgetparticipatif.site_property.paris_connect.mib_url";
  
    private static final String PROPERTY_PARIS_CONNECT_POPUP_LOGIN_URL = "mylutece-parisconnect.url.popupLoginUrl";

   
	 
    /**
     * 
     * getAlerteTemplate
     * @return getAlerTemplate
     */
    public static String getAlerteTemplate ( HttpServletRequest request )
    {
        Map<String, Object> model = new HashMap<String, Object>(  );
        Locale locale = ( request == null ) ? null : request.getLocale(  );
        
        
        ParisConnectUser user = (ParisConnectUser) SecurityService.getInstance(  ).getRegisteredUser( request );

        if ( user != null )
        {      
            model.put( MARK_USER_EMAIL, user.getEmail() );            
        }                         
        model.put(SecurityTokenService.MARK_TOKEN,SecurityTokenService.getInstance().getToken(request, TOKEN_DO_SUBSCRIBE_ALERT));
        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_ALERTE_INCLUDE, locale, model );

        return template.getHtml(  );
    }
    
  
    /**
     * 
     * @param request
     * @return
     */
    public static String getAvisTemplate(HttpServletRequest request )
    {
            
        ParisConnectUser user = (ParisConnectUser) SecurityService.getInstance(  ).getRegisteredUser( request );
        String strMibUrl= DatastoreService.getDataValue(KEY_PARIS_CONNECT_MIB_URL,"https://connect.paris.fr/ogc/avis3b.php?url=https://budgetparticipatif.paris.fr/");
        Locale locale = ( request == null ) ? null : request.getLocale(  );
        Map<String, Object> model = new HashMap<String, Object>(  );
      
        if ( user != null )
        {      
            model.put( MARK_USER_EMAIL, user.getEmail() );            
        }
        model.put(MARK_MIB_URL, strMibUrl);
        model.put(SecurityTokenService.MARK_TOKEN,SecurityTokenService.getInstance().getToken(request, TOKEN_DO_SEND_AVIS));
        
        
        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_AVIS_INCLUDE,locale,model );  
        return template.getHtml(  );
    }   
	
    
    
  
 
    /**
     * 
     * @param request
     * @return
     */
    public static String getMIBTemplate ( HttpServletRequest request )
    {
    	  Map<String, Object> model = new HashMap<String, Object>(  );
          Locale locale = ( request == null ) ? null : request.getLocale(  );
          String strMibUrl= DatastoreService.getDataValue(KEY_PARIS_CONNECT_MIB_URL,"https://connect.paris.fr/ogc/avis3b.php?url=https://budgetparticipatif.paris.fr/");
      	
          
          ParisConnectUser user = (ParisConnectUser) SecurityService.getInstance(  ).getRegisteredUser( request );

          if ( user != null )
          {      
              model.put( MARK_USER_EMAIL, user.getEmail() );            
          }   
          model.put(MARK_MIB_URL, strMibUrl);
          model.put(SecurityTokenService.MARK_TOKEN,SecurityTokenService.getInstance().getToken(request, TOKEN_DO_SEND_AVIS));


          HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MIB_INCLUDE, locale, model );

          return template.getHtml(  );
    }
    
    
    
   

    /**
     *
     * @param request
     * @return template
     */
    public static String getPopupTemplate( HttpServletRequest request )
    {
        Map<String, Object> model = new HashMap<String, Object>(  );
        Locale locale = ( request == null ) ? null : request.getLocale(  );
        String strParisConnectPopupLoginUrl = AppPropertiesService.getProperty( PROPERTY_PARIS_CONNECT_POPUP_LOGIN_URL );
        model.put( MARK_PARIS_CONNECT_POPUP_LOGIN_URL, strParisConnectPopupLoginUrl );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_PC_POPUP_INCLUDE, locale, model );

        return template.getHtml(  );
    }
    

  
    
    
    
	
}
