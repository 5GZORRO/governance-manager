package eu._5gzorro.governancemanager.dto;

import java.util.Objects;

public class MemberDto {

    private String stakeholderId;
    private String legalName;
    private String address;

    public MemberDto() {
    }

    public String getStakeholderId() {
        return stakeholderId;
    }

    public void setStakeholderId(String stakeholderId) {
        this.stakeholderId = stakeholderId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberDto memberDto = (MemberDto) o;
        return stakeholderId.equals(memberDto.stakeholderId) && legalName.equals(memberDto.legalName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stakeholderId, legalName);
    }

    @Override
    public String toString() {
        return "MemberDto{" +
                "stakeholderId='" + stakeholderId + '\'' +
                ", legalName='" + legalName + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
