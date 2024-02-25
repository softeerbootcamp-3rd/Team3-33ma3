package softeer.be33ma3.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import softeer.be33ma3.dto.response.CenterListDto;
import softeer.be33ma3.response.DataResponse;
import softeer.be33ma3.service.LocationService;

import java.util.List;

@Tag(name = "Location", description = "위치, 센터 관련 api")
@RestController
@RequiredArgsConstructor
public class LocationController {
    private final LocationService locationService;

    @ApiResponse(responseCode = "200", description = "모든 센터 정보 전송 완료", content = @Content(schema = @Schema(implementation = DataResponse.class)))
    @Operation(summary = "모든 센터 정보", description = "모든 센터 정보를 내려주는 메소드입니다.")
    @GetMapping("/center/all")
    public ResponseEntity<?> getAllCenters(){
        List<CenterListDto> centers = locationService.getAllCenters();

        return ResponseEntity.ok().body(DataResponse.success("모든 센터 정보 전송 완료", centers));
    }

    @ApiResponse(responseCode = "200", description = "반경 내 위치한 센터 정보 전송 완료", content = @Content(schema = @Schema(implementation = DataResponse.class)))
    @Operation(summary = "반경 안 센터 정보", description = "반경안에 있는 센터 정보를 내려주는 메소드입니다.")
    @Parameter(name = "latitude", description = "위도", example = "37.12345", in = ParameterIn.QUERY)
    @Parameter(name = "longitude", description = "경도", example = "127.12345", in = ParameterIn.QUERY)
    @Parameter(name = "radius", description = "반경", example = "1.0", in = ParameterIn.QUERY)
    @GetMapping("/location")
    public ResponseEntity<?> getCentersInRadius(@RequestParam(value = "latitude") double latitude, @RequestParam(value = "longitude") double longitude,
                                        @RequestParam(value = "radius") double radius){
        List<CenterListDto> centers = locationService.getCentersInRadius(latitude, longitude, radius);

        return ResponseEntity.ok().body(DataResponse.success("반경 내 위치한 센터 정보 전송 완료", centers));
    }
}
