package eu._5gzorro.governancemanager.controller.v1.api;

import eu._5gzorro.governancemanager.controller.v1.request.adminAgentHandler.IssueRequest;
import eu._5gzorro.governancemanager.controller.v1.request.adminAgentHandler.RegisterRequest;
import eu._5gzorro.governancemanager.dto.ApiErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RequestMapping("/api/v1/memberships")
@Validated
@Tag(name = "Stakeholder Membership")
public interface AdminAgentHandlerController {

    @Operation(description = "Request 5GZORRO stakeholder membership.  Request is subject to governance prior to approval.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request was submitted successfully",
                    content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "The request failed validation checks",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping
    ResponseEntity registerStakeholder(@Valid @RequestBody final RegisterRequest request);


    @PostMapping
    ResponseEntity issue(@Valid @RequestBody final IssueRequest request);


    @DeleteMapping("{credentialId}")
    ResponseEntity revoke(@Valid @RequestParam final String credentialId);
}
