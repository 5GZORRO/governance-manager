package eu._5gzorro.governancemanager.controller.v1.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import eu._5gzorro.governancemanager.controller.v1.request.adminAgentHandler.IssueCredentialRequest;
import eu._5gzorro.governancemanager.controller.v1.request.adminAgentHandler.RegisterStakeholderRequest;
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


@RequestMapping("/api/v1/admin-agent-handler")
@Validated
@Tag(name = "Admin Agent")
public interface AdminAgentHandlerController {

    @Operation(description = "Receive a stakeholder credential request. Request is subject to governance prior to approval & issuance.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Request was accepted",
                    content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "The request failed validation checks",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping("stakeholder/receive")
    ResponseEntity<Void> registerStakeholder(@Valid @RequestBody final RegisterStakeholderRequest request) throws JsonProcessingException;


    @Operation(description = "Receive a credential request. Request is subject to governance prior to approval & issuance.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Request was accepted",
                    content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "The request failed validation checks",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping("receive")
    ResponseEntity<Void> issue(@Valid @RequestBody final IssueCredentialRequest request) throws JsonProcessingException;


    @Operation(description = "Receive a stakeholder credential revokation request. Request is subject to governance prior to approval & issuance.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Request was accepted",
                    content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "The request failed validation checks",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @DeleteMapping("receive/{credentialId}")
    ResponseEntity<Void> revoke(@Valid @RequestParam final String credentialId);
}
