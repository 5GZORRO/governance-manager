package eu._5gzorro.governancemanager.model.entity;

import eu._5gzorro.governancemanager.model.enumeration.GovernanceActionType;
import eu._5gzorro.governancemanager.model.enumeration.GovernanceProposalStatus;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "governance_proposal", indexes = {
        @Index(name = "ix_handle", unique = true, columnList = "handle"),
        @Index(name = "ix_actionType_status", unique = false, columnList = "action_type,status")
})
public class GovernanceProposal {

    @Id
    private String id;

    @Column(name="handle", nullable = false)
    private UUID handle;

    @Column(name = "proposer_id", nullable = false)
    private String proposerId;

    @Column(name = "subject_id", nullable = false)
    private String subjectId;

    @Column(name = "status", nullable = false)
    private GovernanceProposalStatus status = GovernanceProposalStatus.CREATING;

    @Column(name = "action_type", nullable = false)
    private GovernanceActionType actionType;

    @Lob
    @Column(name = "evidence")
    private String evidence;

    @Column(name = "created", nullable = false)
    private LocalDateTime created = LocalDateTime.now();

    private LocalDateTime updated;

    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    @Column(name="issue_credential_request")
    private byte[] issueCredentialRequest;

    public GovernanceProposal() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UUID getHandle() {
        return handle;
    }

    public void setHandle(UUID handle) {
        this.handle = handle;
    }

    public String getProposerId() {
        return proposerId;
    }

    public void setProposerId(String proposerId) {
        this.proposerId = proposerId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public GovernanceProposalStatus getStatus() {
        return status;
    }

    public void setStatus(GovernanceProposalStatus status) {
        this.status = status;
    }

    public GovernanceActionType getActionType() {
        return actionType;
    }

    public void setActionType(GovernanceActionType actionType) {
        this.actionType = actionType;
    }

    public String getEvidence() {
        return evidence;
    }

    public void setEvidence(String evidence) {
        this.evidence = evidence;
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

    public byte[] getIssueCredentialRequest() {
        return issueCredentialRequest;
    }

    public void setIssueCredentialRequest(byte[] issueCredentialRequest) {
        this.issueCredentialRequest = issueCredentialRequest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GovernanceProposal proposal = (GovernanceProposal) o;
        return id.equals(proposal.id) && handle.equals(proposal.handle) && proposerId.equals(proposal.proposerId) && subjectId.equals(proposal.subjectId) && status == proposal.status && actionType == proposal.actionType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, handle, proposerId, subjectId, status, actionType);
    }

    @Override
    public String toString() {
        return "GovernanceProposal{" +
                "id='" + id + '\'' +
                ", handle=" + handle +
                ", proposerId='" + proposerId + '\'' +
                ", subjectId='" + subjectId + '\'' +
                ", status=" + status +
                ", actionType=" + actionType +
                ", evidence='" + evidence + '\'' +
                ", created=" + created +
                ", updated=" + updated +
                '}';
    }
}
