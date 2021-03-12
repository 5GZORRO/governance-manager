package eu._5gzorro.governancemanager.dto.identityPermissions;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Objects;

public class CredentialSubjectDto {

    @NotBlank
    private String id;

    private List<String> claims;  //TODO: Change to ClaimDTO when schema known

    public CredentialSubjectDto() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getClaims() {
        return claims;
    }

    public void setClaims(List<String> claims) {
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
