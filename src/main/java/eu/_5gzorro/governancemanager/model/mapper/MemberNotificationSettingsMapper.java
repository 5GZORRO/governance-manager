package eu._5gzorro.governancemanager.model.mapper;

import eu._5gzorro.governancemanager.dto.EmailNotificationDto;
import eu._5gzorro.governancemanager.model.NotificationMethodBase;
import eu._5gzorro.governancemanager.model.entity.MemberNotificationSetting;
import eu._5gzorro.governancemanager.model.enumeration.NotificationSetting;
import eu._5gzorro.governancemanager.model.enumeration.NotificationType;

import java.util.Set;
import java.util.stream.Collectors;

public class MemberNotificationSettingsMapper {

    public static Set<MemberNotificationSetting> toMemberNotificationSettings(NotificationMethodBase notificationMethod) {

        switch (notificationMethod.getNotificationType()) {
            case EMAIL:
                return mapEmailNotificationSettings((EmailNotificationDto)notificationMethod);
            default:
                throw new UnsupportedOperationException("Notification Method not supported");

        }
    }

    private static Set<MemberNotificationSetting> mapEmailNotificationSettings(EmailNotificationDto notificationMethod) {

        MemberNotificationSetting setting = new MemberNotificationSetting()
                .notificationType(NotificationType.EMAIL)
                .setting(NotificationSetting.EMAIL_DISTRIBUTION_LIST)
                .value(notificationMethod.getDistributionList());

        return Set.of(setting);
    }
}
