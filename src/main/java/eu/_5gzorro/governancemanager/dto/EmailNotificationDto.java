package eu._5gzorro.governancemanager.dto;

import eu._5gzorro.governancemanager.model.NotificationMethodBase;
import eu._5gzorro.governancemanager.model.enumeration.NotificationType;
import eu._5gzorro.governancemanager.validator.DistributionListConstraint;

import java.util.Collection;

public class EmailNotificationDto extends NotificationMethodBase {

    @DistributionListConstraint
    private String distributionList;

    public EmailNotificationDto() {
        super(NotificationType.EMAIL);
    }

    public String getDistributionList() {
        return distributionList;
    }

    public EmailNotificationDto setDistributionList(String distributionList) {
        this.distributionList = distributionList;
        return this;
    }
}
