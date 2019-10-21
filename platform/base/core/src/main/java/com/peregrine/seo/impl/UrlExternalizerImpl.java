package com.peregrine.seo.impl;

import com.peregrine.seo.UrlExternalizer;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.settings.SlingSettingsService;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(
    configurationPolicy = ConfigurationPolicy.OPTIONAL,
    service = UrlExternalizer.class,
    immediate = true
)
@Designate(ocd = UrlExternalizerImpl.Configuration.class)
public class UrlExternalizerImpl implements UrlExternalizer {

  @ObjectClassDefinition(
      name = "Link Externalizer",
      description = "Creates absolute URLs.")
  @interface Configuration {

    @AttributeDefinition(
        name = "domains",
        description = "List of domain mappings.",
        required = true)
    String[] domains() default {
        "local http://localhost:8080",
        "author http://localhost:8080",
        "publish http://localhost:8180"
    };
  }

  private static final Logger log = LoggerFactory.getLogger(UrlExternalizerImpl.class);

  private static final Pattern EXTERNALIZED_PATTERN = Pattern.compile("^([^/]+:|//|#).*$");
  private static final String MANGLED_NAMESPACE_PREFIX = "/_";
  private static final String MANGLED_NAMESPACE_SUFFIX = "_";
  private static final Pattern NAMESPACE_PATTERN = Pattern.compile("/([^:/]+):");

  private static final String DEFAULT_LOCAL_DOMAIN = "local http://localhost:8080";
  private static final String DEFAULT_AUTHOR_DOMAIN = "author http://localhost:8080";
  private static final String DEFAULT_PUBLISH_DOMAIN = "publish http://localhost:8180";

  private static final String[] DEFAULT_DOMAINS = new String[]{
      DEFAULT_LOCAL_DOMAIN,
      DEFAULT_AUTHOR_DOMAIN,
      DEFAULT_PUBLISH_DOMAIN
  };

  private Map<String, URI> domains = new HashMap<>();

  @Reference
  private SlingSettingsService slingSettingsService;

  @Override
  public String authorLink(ResourceResolver resolver, String path) {
    return this.externalLink(resolver, "author", (String) null, path);
  }

  @Override
  public String authorLink(ResourceResolver resolver, String scheme, String path) {
    return this.externalLink(resolver, "author", scheme, path);
  }

  @Override
  public String externalizeUrl(
      SlingHttpServletRequest request, ResourceResolver resolver, String path) {
    String actualPath = path;

    if (Objects.nonNull(path) && Objects.nonNull(resolver)) {
      String urlRemainder = null;
      int urlRemainderPos = StringUtils.indexOfAny(actualPath, '?', '#');
      if (urlRemainderPos >= 0) {
        urlRemainder = actualPath.substring(urlRemainderPos);
        actualPath = actualPath.substring(0, urlRemainderPos);
      }

      actualPath = Objects.nonNull(request)
          ? resolver.map(request, actualPath)
          : resolver.map(actualPath);
      try {
        actualPath = new URI(actualPath).getPath();
        actualPath = StringUtils.replace(actualPath, "%2F", "/");
      } catch (URISyntaxException ex) {
        throw new RuntimeException("Sling map method returned invalid URI: " + actualPath, ex);
      }
      actualPath = actualPath + (Objects.nonNull(urlRemainder) ? urlRemainder : "");
    }

    return actualPath;
  }

  @Override
  public String externalizeUrlWithoutMapping(
      SlingHttpServletRequest request, String path) {
    String actualPath = path;

    if (Objects.nonNull(path)) {
      String urlRemainder = null;
      int urlRemainderPos = StringUtils.indexOfAny(actualPath, '?', '#');
      if (urlRemainderPos >= 0) {
        urlRemainder = actualPath.substring(urlRemainderPos);
        actualPath = actualPath.substring(0, urlRemainderPos);
      }

      actualPath = mangleNamespaces(actualPath);
      actualPath = Objects.nonNull(request)
          ? StringUtils.defaultString(request.getContextPath()) + actualPath
          : actualPath;

      try {
        actualPath = URLEncoder.encode(actualPath, "UTF-8");
      } catch (UnsupportedEncodingException var2) {
        throw new RuntimeException(var2);
      }
      actualPath = StringUtils.replace(actualPath, "+", "%20");
      actualPath = StringUtils.replace(actualPath, "%2F", "/");
      actualPath = actualPath + (Objects.nonNull(urlRemainder) ? urlRemainder : "");
    }

    return actualPath;
  }

  @Override
  public String externalLink(ResourceResolver resolver, String domain, String path) {
    return this.externalLink(resolver, domain, null, path);
  }

  @Override
  public String externalLink(ResourceResolver resolver, String domain, String scheme, String path) {
    if (Objects.isNull(domain)) {
      throw new IllegalArgumentException("Argument 'domain' can not be null!");
    } else {
      URI domainURI = this.domains.get(domain);
      if (Objects.isNull(domainURI)) {
        throw new IllegalArgumentException("No configuration for domain '" + domain + "' exists.");
      } else {
        try (Formatter url = new Formatter()) {
          scheme = Objects.nonNull(scheme) ? scheme
              : (Objects.isNull(domainURI.getScheme()) ? "http" : domainURI.getScheme());

          url.format(scheme + "://");
          URI mapped = URI.create(this.externalizeUrl(null, resolver, path));
          if (Objects.nonNull(mapped.getRawAuthority())) {
            url.format(mapped.getRawAuthority());
          } else {
            String host = domainURI.getHost();
            int port = domainURI.getPort();
            if (port > 0 && (!"http".equals(scheme) || port != 80)
                && (!"https".equals(scheme) || port != 443)) {
              url.format(host + ":" + port);
            } else {
              url.format(host);
            }
          }

          url.format(Objects.nonNull(domainURI.getRawPath()) ? domainURI.getRawPath() : "");
          url.format(mapped.getRawPath());
          url.format(Objects.nonNull(mapped.getRawQuery()) ? "?" + mapped.getRawQuery() : "");
          url.format(Objects.nonNull(mapped.getRawFragment()) ? "#" + mapped.getRawFragment() : "");

          log.debug("Externalizing link for '{}': '{}' -> '{}'.", domain, path, url);
          return url.toString();
        }
      }
    }
  }

  @Override
  public boolean isExternalized(String url) {
    if (url == null) {
      log.error("Given url cannot be null!");
      return false;
    }
    return EXTERNALIZED_PATTERN.matcher(url).matches();
  }

  @Override
  public String mangleNamespaces(String path) {
    if (path == null) {
      log.error("Given path cannot be null!");
      return null;
    } else {
      Matcher matcher = NAMESPACE_PATTERN.matcher(path);
      StringBuffer sb = new StringBuffer();
      while (matcher.find()) {
        String replacement = MANGLED_NAMESPACE_PREFIX + matcher.group(1) + MANGLED_NAMESPACE_SUFFIX;
        matcher.appendReplacement(sb, replacement);
      }
      matcher.appendTail(sb);
      return sb.toString();
    }
  }

  @Override
  public String publishLink(ResourceResolver resolver, String path) {
    return this.externalLink(resolver, "publish", null, path);
  }

  @Override
  public String publishLink(ResourceResolver resolver, String scheme, String path) {
    return this.externalLink(resolver, "publish", scheme, path);
  }

  @Activate
  @SuppressWarnings("unused")
  void activate(Configuration configuration) {
    setup(configuration);
  }

  @Modified
  @SuppressWarnings("unused")
  void modified(Configuration configuration) {
    setup(configuration);
  }

  private void setup(Configuration config) {
    String[] domainCfgs = config.domains() != null ? config.domains() : DEFAULT_DOMAINS;
    for (int i = 0; i < domainCfgs.length; ++i) {
      String cfg = domainCfgs[i];
      cfg = cfg.trim();
      int splitIndex = cfg.indexOf(32);
      if (splitIndex > 0) {
        String name = cfg.substring(0, splitIndex);
        try {
          String domain = cfg.substring(splitIndex + 1);
          domain = !domain.contains("://") ? ("http://" + domain) : domain;
          domains.put(name, URI.create(domain));
        } catch (Exception e) {
          log.error("Invalid URI: '{}' configuration.", cfg);
        }
      }
    }
  }
}
