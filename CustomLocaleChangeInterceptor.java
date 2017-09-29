import java.util.Collection;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;



public class LocaleConfigurerFilter extends SessionLocaleResolver{ 
    public static final String UNDEFINED_KEY = "???"; //$NON-NLS-1$ 
    public static final String PARAM_NAME = "lang"; 
    public static final Locale DEFAULT = new Locale("en"); 
    public static final String DEFAULT_LOCALE_KEY = "defaultLocale"; 
    public static final String DEFAULT_LOCALE_EXPIRY_KEY = "locale_expiry"; 
    public static final String CURRENT_LOCALE_KEY = "defaultLocale"; 
    private String paramValue; 
     
    
    @Override 
    public Locale resolveLocale(HttpServletRequest request) { 
        Locale locale = (Locale) request.getAttribute(CURRENT_LOCALE_KEY); 
         try{
        if (locale == null) { 
            if (request != null && request.getParameter(PARAM_NAME) != null && !request.getParameter(PARAM_NAME).equals(paramValue)) { 
                locale = null; 
                paramValue = request.getParameter(PARAM_NAME);
                if("en".equalsIgnoreCase(paramValue) || "zh_CN".equalsIgnoreCase(paramValue)){
                	String[] temp = paramValue.split("_"); 
                	 
                    if (temp.length == 1 && !temp[0].isEmpty()) { 
                        locale = new Locale(temp[0]); 
                    } else if (temp.length == 2) { 
                        locale = new Locale(temp[0], temp[1]); 
                    } else if (temp.length == 3) { 
                        locale = new Locale(temp[0], temp[1], temp[2]); 
                    } 
                    if (locale != null) { 
                    	setLocaleProhrame(request, null, locale); 
                     } else { 
                    	 setLocaleProhrame(request, null, locale); 
                    } 
                }else{
                	super.setLocale(request, null, DEFAULT);
                }
                
            } else{
            	 String s = SessionLocaleResolver.class.getName() + ".LOCALE";
            	 Locale locales = (Locale)request.getSession().getAttribute(s);
            	 setLocaleProhrame(request, null, locales); 
            }
            String s = SessionLocaleResolver.class.getName() + ".LOCALE";
            Locale locales = (Locale)request.getSession().getAttribute(s);
            System.out.println(s);
            locale = super.resolveLocale(request); 
            request.setAttribute(CURRENT_LOCALE_KEY, locale); 
        } 
         }catch(Exception e){
        	 e.printStackTrace();
         }
        return locale; 
    } 
     
    @Override 
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale){ 
        super.setLocale(request, response, locale); 
    } 
     
    public String getResource(String resourceKey, String defaultValue, Tag tag, PageContext pageContext) { 
        return MessageProvider.getMessage(resourceKey); 
    } 
  
    public void  setLocaleProhrame(HttpServletRequest request, HttpServletResponse response, Locale locale){
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth!=null && !(auth instanceof AnonymousAuthenticationToken)) {
			Collection<GrantedAuthority>  authorityList = (Collection<GrantedAuthority>) auth.getAuthorities();
			for(GrantedAuthority ga:authorityList){
				Role userRole = (Role)ga;
				if(INVALID_FIELDS.equalsIgnoreCase("INVALID_FIELDS") && userRole.getVendorId()!=null){
					setLocale(request, null, locale); 
				}else{
					Locale loc = Locale.ENGLISH;
					setLocale(request, null, loc); 
				}
			}	
		}else{
			setLocale(request, null, DEFAULT);
		}
    }
}
