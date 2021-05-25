package eu._5gzorro.governancemanager.dto;

import java.util.Objects;

public class MemberDto {

    private String id;
    private String legalName;
    private String address;
    private String ledgerIdentity;

    public MemberDto() {
    }

    public String getStakeholderId() {
        return id;
    }

    public void setStakeholderId(String stakeholderId) {
        this.id = stakeholderId;
    }

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLedgerIdentity() {
        return ledgerIdentity;
    }

    public void setLedgerIdentity(String ledgerIdentity) {
        this.ledgerIdentity = ledgerIdentity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberDto memberDto = (MemberDto) o;
        return id.equals(memberDto.id) && legalName.equals(memberDto.legalName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, legalName);
    }

    @Override
    public String toString() {
        return "MemberDto{" +
                "id='" + id + '\'' +
                ", legalName='" + legalName + '\'' +
                ", address='" + address + '\'' +
                ", ledgerIdentity='" + ledgerIdentity + '\'' +
                '}';
    }
}
