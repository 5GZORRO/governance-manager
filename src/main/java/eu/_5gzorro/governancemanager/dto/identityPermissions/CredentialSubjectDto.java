package eu._5gzorro.governancemanager.dto.identityPermissions;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

public class CredentialSubjectDto {

    @NotBlank
    private String id;

    private ClaimDto claims;  //TODO: Change to List<ClaimDTO> when schema known

    public CredentialSubjectDto() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ClaimDto getClaims() {
        return claims;
    }

    public void setClaims(ClaimDto claims) {
        this.claims = claims;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CredentialSubjectDto that = (CredentialSubjectDto) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "CredentialSubject{" +
                "id='" + id + '\'' +
                ", claims=" + claims +
                '}';
    }
}
