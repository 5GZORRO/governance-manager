package eu._5gzorro.governancemanager.controller.v1.response;

import eu._5gzorro.governancemanager.dto.GovernanceProposalDto;
import org.springframework.data.domain.Page;

import java.util.Objects;

public class PagedGovernanceProposalsResponse {
    private final Page<GovernanceProposalDto> pagedGovernanceProposals;

    public PagedGovernanceProposalsResponse(Page<GovernanceProposalDto> pagedGovernanceProposals) {
        this.pagedGovernanceProposals = pagedGovernanceProposals;
    }

    public Page<GovernanceProposalDto> getPagedGovernanceProposals() {
        return pagedGovernanceProposals;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PagedGovernanceProposalsResponse that = (PagedGovernanceProposalsResponse) o;
        return Objects.equals(pagedGovernanceProposals, that.pagedGovernanceProposals);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pagedGovernanceProposals);
    }

    @Override
    public String toString() {
        return "PagedMemberResponse{" +
                "pagedMembers=" + pagedGovernanceProposals +
                '}';
    }
}
