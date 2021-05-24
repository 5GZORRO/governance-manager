package eu._5gzorro.governancemanager.controller.v1.api;

import eu._5gzorro.governancemanager.controller.v1.response.PagedMembersResponse;
import eu._5gzorro.governancemanager.dto.ApiErrorResponse;
import eu._5gzorro.governancemanager.dto.MembershipStatusDto;
import eu._5gzorro.governancemanager.dto.identityPermissions.DIDStateDto;
import eu._5gzorro.governancemanager.model.PageableOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;


@RequestMapping("/api/v1/memberships")
@Validated
@Tag(name = "Stakeholder Membership")
public interface MembershipsController {

    @Operation(description = "Check the status of a 5GZORRO stakeholder membership request ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns status information pertaining to the request",
                    content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "Invalid identifier provided",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "A member with the specified Stakeholder Id was not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("{stakeholderId}/status")
    ResponseEntity<MembershipStatusDto> checkMembershipStatus(@Valid @PathVariable final String stakeholderId);

    @Operation(description = "Get a 5GZORRO stakeholder's DLT identity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns a string containing the stakeholder's DLT identity for the provided DID",
                    content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "Invalid DID provided",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "A member with the specified Stakeholder DID was not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("{stakeholderDid}/ledger-identity")
    ResponseEntity<String> getLedgerIdentity(@Valid @PathVariable final String stakeholderDid);

    @Operation(description = "Retrieve a paged collection of 5GZORRO member stakeholders according to paging and filter parameters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A Paged List of Member records",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = PagedMembersResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid page or filter parameters provided",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping
    @PageableOperation
    ResponseEntity<PagedMembersResponse> getMembers(
            final @Parameter(hidden = true) Pageable pageable,
            @RequestParam(required = false, defaultValue = "") @Parameter(description = "String to filter members by name containing this text") final String filterText);


    @Operation(description = "Request to revoke 5GZORRO stakeholder membership with a given stakeholder Id.  Decision to uphold the request is subject to governance if the request is not for the requesting stakeholder")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request was submitted successfully. If requesting revocation for another stakeholder the Id of the associated governance proposal will be returned",
                    content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "The request failed validation checks",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "A member with the specified Stakeholder Id was not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @DeleteMapping("{stakeholderId}/revoke-membership")
    ResponseEntity<Optional<UUID>> revokeMembership(@Valid @PathVariable final String stakeholderId);
}
