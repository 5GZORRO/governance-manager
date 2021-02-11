package eu._5gzorro.governancemanager.controller.v1.response;

import eu._5gzorro.governancemanager.dto.MemberDto;
import org.springframework.data.domain.Page;

import java.util.Objects;

public class PagedMembersResponse {
    private Page<MemberDto> pagedMembers;

    public PagedMembersResponse() {
    }

    public Page<MemberDto> getPagedMembers() {
        return pagedMembers;
    }

    public void setPagedMembers(Page<MemberDto> pagedMembers) {
        this.pagedMembers = pagedMembers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PagedMembersResponse that = (PagedMembersResponse) o;
        return Objects.equals(pagedMembers, that.pagedMembers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pagedMembers);
    }

    @Override
    public String toString() {
        return "PagedMemberResponse{" +
                "pagedMembers=" + pagedMembers +
                '}';
    }
}
