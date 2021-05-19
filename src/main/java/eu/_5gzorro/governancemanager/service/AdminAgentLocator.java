package eu._5gzorro.governancemanager.service;

public interface AdminAgentLocator {

    /**
     * Get the base URL for my agent
     * @return base url of my admin agent
     */
    String getMyAdminAgentBaseUrl();

    /**
     * Get an admin agent base url for an arbitrary admin
     * @return base url of the admin stakeholder's admin agent
     */
    String getAdminAgentBaseUrl();

    /**
     * Get an admin agent base url belonging to the prescribed stakeholder
     * @return base url of the admin stakeholder's admin agent
     */
    String getAdminAgentBaseUrl(String adminStakeholderDid);
}
