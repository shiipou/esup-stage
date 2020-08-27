/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package fr.esupportail.utils.mail;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.LoggerFactory;

/**
 * A class that provides facilities with HTTP requests for report formatting.
 */
public class ReportUtils {

    /**
     * A logger.
     */
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ReportUtils.class);

    private static final long MB = 1024 * 1024;

    private Set<String> cookies;
    private Set<String> headers;
    private Set<String> parameters;
    private Set<String> sessionsAttributes;
    private String client;
    private String server;    
    private long freeMemory;
    private long totalMemory;
    private long maxMemory;
    private String userAgent;
    private String queryString;
    private String remoteUser;

    /**
     * Private constructor.
     * @param request
     */
    public ReportUtils(HttpServletRequest request) {
        this.cookies = getCookiesString(request);
        this.headers = getRequestHeadersStrings(request);
        this.parameters = getRequestParametersStrings(request);
        this.client = getClientString(request);
        this.server = getServerString(request);
        this.sessionsAttributes = getSessionAttributesStrings(request);
        this.queryString = request.getQueryString();
        this.userAgent = request.getHeader("user-agent");
        this.remoteUser = request.getRemoteUser();
        this.freeMemory = Runtime.getRuntime().freeMemory() / MB;
        this.totalMemory = Runtime.getRuntime().totalMemory() / MB;
        this.maxMemory = Runtime.getRuntime().maxMemory() / MB;
    }    

    public Set<String> getCookies() {
        return cookies;
    }

    public void setCookies(Set<String> cookies) {
        this.cookies = cookies;
    }

    public Set<String> getHeaders() {
        return headers;
    }

    public void setHeaders(Set<String> headers) {
        this.headers = headers;
    }

    public Set<String> getParameters() {
        return parameters;
    }

    public void setParameters(Set<String> parameters) {
        this.parameters = parameters;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public Set<String> getSessionsAttributes() {
        return sessionsAttributes;
    }

    public void setSessionsAttributes(Set<String> sessionsAttributes) {
        this.sessionsAttributes = sessionsAttributes;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public String getRemoteUser() {
        return remoteUser;
    }

    public void setRemoteUser(String remoteUser) {
        this.remoteUser = remoteUser;
    }

    public long getFreeMemory() {
        return freeMemory;
    }

    public void setFreeMemory(long freeMemory) {
        this.freeMemory = freeMemory;
    }

    public long getTotalMemory() {
        return totalMemory;
    }

    public void setTotalMemory(long totalMemory) {
        this.totalMemory = totalMemory;
    }

    public long getMaxMemory() {
        return maxMemory;
    }

    public void setMaxMemory(long maxMemory) {
        this.maxMemory = maxMemory;
    }
    
    

    /**
     * @return the cookies as a set of readable strings.
     */
    private Set<String> getCookiesString(HttpServletRequest request) {
        Set<String> cookiesStrings = new TreeSet<String>();
        Cookie[] cookiess = request.getCookies();
        if (cookiess != null) {
            for (Cookie cookie : cookiess) {
                cookiesStrings.add(cookie.getName() + " = [" + cookie.getValue() + "]");
            }
        }
        return cookiesStrings;
    }

    /**
     * @return The headers of the request, as a set of strings.
     */
    @SuppressWarnings("unchecked")
    private Set<String> getRequestHeadersStrings(HttpServletRequest request) {
        Set<String> sortedStrings = new TreeSet<String>();
        Enumeration<String> names = request.getHeaderNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            Enumeration<String> headers = request.getHeaders(name);
            while (headers.hasMoreElements()) {
                String header = headers.nextElement();
                sortedStrings.add(name + ": " + header);
            }
        }
        return sortedStrings;
    }

    /**
     * @return The parameters of the request, as a set of strings.
     */
    @SuppressWarnings("unchecked")
    private Set<String> getRequestParametersStrings(HttpServletRequest request) {
        Set<String> sortedStrings = new TreeSet<String>();
        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            String[] values = request.getParameterValues(name);
            for (String value : values) {
                sortedStrings.add(name + " = [" + value + "]");
            }
        }
        return sortedStrings;
    }

    /**
     * @return The client as a printable string.
     */
    private String getClientString(HttpServletRequest request) {
        String rawClientString = request.getRemoteAddr();
        if (rawClientString == null) {
            return null;
        }
        try {
            InetAddress client = getRealInetAddress(rawClientString);
            return new StringBuffer(client.getHostAddress())
                    .append(" (").append(client.getHostName()).append(")").toString();
        } catch (UnknownHostException e) {
            logger.error("UnknownHostException caught: " + e);
            return rawClientString;
        }
    }

    /**
     * @param name
     * @return the real IP address, not localhost.
     * @throws UnknownHostException
     */
    @SuppressWarnings("unchecked")
    private InetAddress getRealInetAddress(final String name)
            throws UnknownHostException {
        if (!name.equals("localhost") && !name.equals("127.0.0.1") && !name.equals("0:0:0:0:0:0:0:1")) {
            InetAddress host = InetAddress.getByName(name);
            host.getAddress();
            return host;
        }
        try {
            Enumeration interfaces = NetworkInterface.getNetworkInterfaces();
            if (interfaces == null) {
                InetAddress host = InetAddress.getByName(name);
                return host;
            }
            while (interfaces.hasMoreElements()) {
                NetworkInterface card = (NetworkInterface) interfaces.nextElement();
                Enumeration addresses = card.getInetAddresses();
                if (addresses == null) {
                    continue;
                }
                while (addresses.hasMoreElements()) {
                    InetAddress address = (InetAddress) addresses.nextElement();
                    address.getAddress();
                    String addressName = address.getHostName();
                    if (!addressName.equals("localhost")
                            && !addressName.equals("127.0.0.1")
                            && !addressName.equals("0:0:0:0:0:0:0:1")) {
                        return address;
                    }
                }
            }
        } catch (SocketException e) {
            // fall back to default
        }
        return InetAddress.getByName(name);
    }

    /**
     * @param request
     * @return All the attributes of the current session.
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> getSessionAttributes(final HttpServletRequest request) {
        Map<String, Object> attributes = new Hashtable<String, Object>();
        HttpSession session = request.getSession(true);
        if (session != null) {
            Enumeration<String> names = session.getAttributeNames();
            while (names.hasMoreElements()) {
                String name = names.nextElement();
                Object obj = session.getAttribute(name);
                if (obj != null) {
                    attributes.put(name, obj);
                }
            }
        } else {
            logger.warn("no session, can not get session attributes");
        }
        return attributes;
    }

    /**
     * @return The session attributes, as a set of strings.
     */
    private Set<String> getSessionAttributesStrings(HttpServletRequest request) {
        return getAttributesStrings(getSessionAttributes(request));
    }

    /**
     * @param attributes
     * @return The request attributes, as a set of strings.
     */
    private Set<String> getAttributesStrings(
            final Map<String, Object> attributes) {
        Set<String> sortedStrings = new TreeSet<String>();
        Set<String> keys = attributes.keySet();
        for (String key : keys) {
            Object obj = attributes.get(key);
            String objToString = null;
            if (obj != null) {
                try {
                    objToString = obj.toString();
                } catch (Throwable t) {
                    logger.error(t.getMessage());
                }
            }
            if (objToString != null) {
                sortedStrings.add(key + " = [" + objToString + "]");
            } else {
                sortedStrings.add(key + " = null");
            }
        }
        return sortedStrings;
    }

    /**
     * @return The server as a printable string.
     */
    private String getServerString(HttpServletRequest request) {
        String rawServerString = request.getServerName();
        if (rawServerString == null) {
            return null;
        }
        try {
            InetAddress server = getRealInetAddress(rawServerString);
            return new StringBuffer(server.getHostAddress())
                    .append(" (").append(server.getHostName()).append(")").toString();
        } catch (UnknownHostException e) {
            logger.debug("UnknownHostException caught!");
            return rawServerString;
        }
    }
}
