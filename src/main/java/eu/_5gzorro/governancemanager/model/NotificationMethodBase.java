package eu._5gzorro.governancemanager.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import eu._5gzorro.governancemanager.dto.EmailNotificationDto;
import eu._5gzorro.governancemanager.model.enumeration.NotificationType;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "notificationType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = EmailNotificationDto.class, name = "EMAIL"),
})
public abstract class NotificationMethodBase {
    private final NotificationType notificationType;

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public NotificationMethodBase(NotificationType notificationType) {
        this.notificationType = notificationType;
    }
}
