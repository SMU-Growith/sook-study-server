package org.growith.be.growith.global.common;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "헬스 체킹 API")
public class HealthCheckController {

    @Operation(summary = "헬스 체킹 API", description = "서버 상태 확인하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공입니다 ")
    })
    @GetMapping("/health")
    public String healthCheck() {
        return "I'm healthy";
    }


}
