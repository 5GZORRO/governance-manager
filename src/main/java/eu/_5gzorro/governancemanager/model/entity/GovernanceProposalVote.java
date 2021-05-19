package eu._5gzorro.governancemanager.model.entity;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "governance_proposal_vote", indexes = {
        @Index(name = "ix_did", unique = true, columnList = "did")
})
public class GovernanceProposalVote {

    @Id
    private UUID id;

    @NaturalId(mutable = true) // mutable to allow null -> did
    @Column(name="did", nullable = true)
    private String did;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "governance_proposal_id", nullable = false)
    private GovernanceProposal governanceProposal;

    @Column(name = "voter_did", nullable = false)
    private String voterDid;

    @Column(name = "approved", nullable = false)
    private boolean approved;

    @Column(name = "created", nullable = false)
    private LocalDateTime created = LocalDateTime.now();

    private LocalDateTime updated;

    public GovernanceProposalVote() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public GovernanceProposal getGovernanceProposal() {
        return governanceProposal;
    }

    public void setGovernanceProposal(GovernanceProposal governanceProposal) {
        this.governanceProposal = governanceProposal;
    }

    public String getVoterDid() {
        return voterDid;
    }

    public void setVoterDid(String voterDid) {
        this.voterDid = voterDid;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GovernanceProposalVote that = (GovernanceProposalVote) o;
        return approved == that.approved && id.equals(that.id) && Objects.equals(did, that.did) && governanceProposal.equals(that.governanceProposal) && voterDid.equals(that.voterDid) && created.equals(that.created) && Objects.equals(updated, that.updated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, did, governanceProposal, voterDid, approved, created, updated);
    }
}
