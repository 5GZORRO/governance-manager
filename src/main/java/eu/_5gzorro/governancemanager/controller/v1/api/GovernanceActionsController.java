package eu._5gzorro.governancemanager.controller.v1.api;

import eu._5gzorro.governancemanager.controller.v1.request.governanceActions.ProposeGovernanceDecisionRequest;
import eu._5gzorro.governancemanager.controller.v1.response.PagedGovernanceProposalsResponse;
import eu._5gzorro.governancemanager.dto.ApiErrorResponse;
import eu._5gzorro.governancemanager.dto.identityPermissions.DIDStateDto;
import eu._5gzorro.governancemanager.dto.GovernanceProposalDto;
import eu._5gzorro.governancemanager.model.PageableOperation;
import eu._5gzorro.governancemanager.model.enumeration.GovernanceActionType;
import eu._5gzorro.governancemanager.model.enumeration.GovernanceProposalStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RequestMapping("/api/v1/governance-actions")
@Validated
public interface GovernanceActionsController {

    @Operation(description = "Propose a governance decision. Request is subject to governance prior to approval.  The resulting identifier can be used to track the progress/status of the proposal", tags= { "Governance - All Stakeholders" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Request was submitted successfully.  The proposal Id is returned in the response",
                    content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "The request failed validation checks",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "The subject of the proposal (entity with provided id) could not be found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping
    ResponseEntity<String> proposeGovernanceDecision(@Valid @RequestBody final ProposeGovernanceDecisionRequest request);

    @Operation(description = "Retrieve a paged collection of 5GZORRO governance proposals according to paging and filter parameters", tags= { "Governance - All Stakeholders" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A Paged List of Proposals",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = PagedGovernanceProposalsResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid page or filter parameters provided",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping
    @PageableOperation
    ResponseEntity<PagedGovernanceProposalsResponse> getProposals(
            final @Parameter(hidden = true) Pageable pageable,
            @RequestParam(required = false) @Parameter(description = "Optional comma separated list of proposalStatus' to filter the response by") final List<GovernanceProposalStatus> statusFilter,
            @RequestParam(required = false) @Parameter(description = "Optional comma separated list of actionTypes to filter the response by") final List<GovernanceActionType> actionTypeFilter);


    @Operation(description = "Retrieve a 5GZORRO governance proposal including current status information by id or DID", tags= { "Governance - All Stakeholders" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns status information pertaining to the request",
                    content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "Invalid identifier provided",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "A proposal with the specified identifier was not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("{identifier}")
    ResponseEntity<GovernanceProposalDto> getGovernanceProposal(@Valid @PathVariable final String identifier);

    @Operation(description = "Vote on a 5GZORRO governance proposal", tags= { "Governance - Admin Only" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vote was submitted successfully",
                    content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "Invalid identifier provided",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "A proposal with the specified DID was not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("{proposalDid}/vote/{accept}")
    ResponseEntity voteGovernanceDecision(@Valid @PathVariable final String proposalDid, @Valid @PathVariable final boolean accept);


    @Operation(description = "Callback endpoint to handle process async DID identifier generation", tags= { "Governance - Admin Only" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proposal DID assigned successfully",
                    content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "Invalid identifier provided",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "A proposal with the specified Id was not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping("{id}/identity")
    ResponseEntity updateProposalIdentity(@Valid @PathVariable final UUID id, @Valid @RequestBody final DIDStateDto state) throws IOException;
}
