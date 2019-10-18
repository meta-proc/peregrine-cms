package com.peregrine.seo;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;

/**
 * Service interface to externalize URLs.
 */
public interface UrlExternalizer {

  /**
   * Creates an absolute URL for the authoring system. Uses the configured default scheme of that
   * domain, or "http".
   *
   * @param resolver a resource resolver for handling the sling mappings and namespace mangling; can
   *                 be null
   * @param path     a resource path; might contain extension, query or fragment, but plain paths
   *                 are recommended; has to be without context path
   * @return an absolute URL string
   */
  String authorLink(ResourceResolver resolver, String path);

  /**
   * Creates an absolute URL for the authoring system. Uses the scheme passed as argument.
   *
   * @param resolver a resource resolver for handling the sling mappings and namespace mangling; can
   *                 be null
   * @param scheme   a protocol scheme such as "http", that will be part of the URL
   * @param path     a resource path; might contain extension, query or fragment, but plain paths
   *                 are recommended; has to be without context path
   * @return an absolute URL string
   */
  String authorLink(ResourceResolver resolver, String scheme, String path);

  /**
   * Externalizes the given resource path relative to the URL request by applying Sling Mapping.
   * Hostname and scheme are not added because they are added by the link handler depending on site
   * URL configuration and secure/non-secure mode. URLs that are already externalized remain
   * untouched.
   *
   * @param request  a sling http request object (required for context path)
   * @param resolver a resource resolver for handling the sling mappings and namespace mangling; can
   *                 be null
   * @param path     a resource path; might contain extension, query or fragment, but plain paths
   *                 are recommended; has to be without context path
   * @return an fully qualified URL string that is relative to the given request; it might be an
   * absolute URL if the resource path is mapped to a different host than the current request
   */
  String externalizeUrl(SlingHttpServletRequest request, ResourceResolver resolver, String path);

  /**
   * Externalizes the given resource path relative to the URL request without applying Sling
   * Mapping. Instead the servlet context path is added and sling namespace mangling is applied
   * manually. Hostname and scheme are not added because they are added by the link handler
   * depending on site URL configuration and secure/non-secure mode. URLs that are already
   * externalized remain untouched.
   *
   * @param request a sling http request object (required for context path)
   * @param path    a resource path; might contain extension, query or fragment, but plain paths are
   *                recommended; has to be without context path
   * @return an fully qualified URL string that is relative to the given request; it might be an
   * absolute URL if the resource path is mapped to a different host than the current request
   */
  String externalizeUrlWithoutMapping(SlingHttpServletRequest request, String path);

  /**
   * Creates an absolute URL for a named domain. Uses the scheme passed as argument. Use the
   * standard LOCAL, PUBLISH or AUTHOR domains. Custom ones are also possible.
   *
   * @param resolver a resource resolver for handling the sling mappings and namespace mangling; can
   *                 be null
   * @param domain   name of the domain configuration to use
   * @param path     a resource path; might contain extension, query or fragment, but plain paths
   *                 are recommended; has to be without context path
   * @return an absolute URL string
   */
  String externalLink(ResourceResolver resolver, String domain, String path);

  /**
   * Creates an absolute URL for a named domain. Uses the scheme passed as argument. Use the
   * standard LOCAL, PUBLISH or AUTHOR domains. Custom ones are also possible.
   *
   * @param resolver a resource resolver for handling the sling mappings and namespace mangling; can
   *                 be null
   * @param domain   name of the domain configuration to use
   * @param scheme   a protocol scheme such as "http", that will be part of the URL
   * @param path     a resource path; might contain extension, query or fragment, but plain paths
   *                 are recommended; has to be without context path
   * @return an absolute URL string
   */
  String externalLink(ResourceResolver resolver, String domain, String scheme, String path);

  /**
   * Checks if the given URL is already externalized. For this check some heuristics are applied.
   *
   * @param url URL to check
   * @return true if path is already externalized; otherwise false.
   */
  boolean isExternalized(String url);

  /**
   * Mangle the namespaces in the given path for usage in sling-based URLs.
   * <p>
   * E.g. /path/jcr:content -> /path/_jcr_content
   *
   * @param path Path to mangle
   * @return Mangled path.
   */
  String mangleNamespaces(String path);

  /**
   * Creates an absolute URL for the public website. Uses the configured default scheme of that
   * domain, or "http".
   *
   * @param resolver a resource resolver for handling the sling mappings and namespace mangling; can
   *                 be null
   * @param path     a resource path; might contain extension, query or fragment, but plain paths
   *                 are recommended; has to be without context path
   * @return an absolute URL string
   */
  String publishLink(ResourceResolver resolver, String path);

  /**
   * Creates an absolute URL for the authoring system. Uses the scheme passed as argument.
   *
   * @param resolver a resource resolver for handling the sling mappings and namespace mangling; can
   *                 be null
   * @param scheme   a protocol scheme such as "http", that will be part of the URL
   * @param path     a resource path; might contain extension, query or fragment, but plain paths
   *                 are recommended; has to be without context path
   * @return an absolute URL string
   */
  String publishLink(ResourceResolver resolver, String scheme, String path);
}