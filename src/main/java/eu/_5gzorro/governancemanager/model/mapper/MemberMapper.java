package eu._5gzorro.governancemanager.model.mapper;

import eu._5gzorro.governancemanager.dto.MembershipStatusDto;
import eu._5gzorro.governancemanager.model.entity.Member;

public class MemberMapper {

    public static MembershipStatusDto toMembershipStatusDto(Member member) {
        MembershipStatusDto dto = new MembershipStatusDto();
        dto.setStakeholderId(member.getId());
        dto.setStatus(member.getStatus());
        dto.setStatusUpdated(member.getUpdated() != null ? member.getUpdated() : member.getCreated());

        return dto;
    }
}
