package com.peregrine.cms.distribution;

import com.peregrine.commons.util.AbstractUserAndPermissionsService;
import java.security.Principal;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.security.Privilege;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.jackrabbit.commons.jackrabbit.authorization.AccessControlUtils;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.jcr.api.SlingRepository;


/**
 * Creates the System User for the Sling Distribution and sets the necessary permissions if it could
 * log in as default admin otherwise it just tests the permissions.
 */
@Component(immediate = true)
public class CreateDistributionUsersAndPermissions
    extends AbstractUserAndPermissionsService {

  public static final String DISTRIBUTION_AGENT_USER = "distribution-agent-user";
  public static final String DISTRIBUTION_TEST_SUB_SERVICE = "test-distribution";

  public static final String VAR_SLING_DISTRIBUTION_PACKAGES = "/var/sling/distribution/packages";
  public static final String CONTENT = "/content";
  public static final String LIBS_SLING_DISTRIBUTION = "/libs/sling/distribution";
  public static final String ETC_DISTRIBUTION = "/etc/distribution";

  public static final String JCR_ALL = "jcr:all";

  @Reference
  SlingRepository slingRepository;

  @Reference
  ResourceResolverFactory resourceResolverFactory;

  @Override
  public SlingRepository getSlingRepository() {
    return slingRepository;
  }

  @Override
  public ResourceResolverFactory getResourceResolverFactory() {
    return resourceResolverFactory;
  }

  @Activate
  public void activate() throws Exception {
    handleUserAndPermissions(DISTRIBUTION_AGENT_USER, DISTRIBUTION_TEST_SUB_SERVICE);
  }

  @Override
  protected void createPermissions(Session session, Principal principal)
      throws RepositoryException {
    AccessControlUtils.addAccessControlEntry(session, VAR_SLING_DISTRIBUTION_PACKAGES, principal, new String[]{Privilege.JCR_ALL}, true);
    AccessControlUtils.addAccessControlEntry(session, CONTENT, principal, new String[]{Privilege.JCR_ALL}, true);
    AccessControlUtils.addAccessControlEntry(session, LIBS_SLING_DISTRIBUTION, principal, new String[]{Privilege.JCR_ALL}, true);
    AccessControlUtils.addAccessControlEntry(session, ETC_DISTRIBUTION, principal, new String[]{Privilege.JCR_ALL}, true);
    AccessControlUtils.addAccessControlEntry(session, null, principal, new String[]{Privilege.JCR_ALL}, true);
  }

  @Override
  protected void testPermissions(Session session, String userName) {
    checkUserPermission(session, LIBS_SLING_DISTRIBUTION, userName, JCR_ALL);
    checkUserPermission(session, ETC_DISTRIBUTION, userName, JCR_ALL);
    checkUserPermission(session, "/libs", userName, JCR_ALL);
    checkUserPermission(session, "/apps", userName, JCR_ALL);
    checkUserPermission(session, CONTENT, userName, JCR_ALL);
  }
}
