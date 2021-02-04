package eu._5gzorro.governancemanager.dto;

import eu._5gzorro.governancemanager.model.NotificationMethodBase;
import eu._5gzorro.governancemanager.model.enumeration.NotificationType;
import eu._5gzorro.governancemanager.validators.DistributionListConstraint;

import javax.validation.constraints.NotNull;
import java.util.Collection;

public class EmailNotificationDto extends NotificationMethodBase {

    @DistributionListConstraint
    private Collection<String> distributionList;

    public EmailNotificationDto() {
        super(NotificationType.EMAIL);
    }

    public Collection<String> getDistributionList() {
        return distributionList;
    }

    public EmailNotificationDto setDistributionList(Collection<String> distributionList) {
        this.distributionList = distributionList;
        return this;
    }
}
