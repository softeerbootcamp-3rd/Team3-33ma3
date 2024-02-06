package softeer.be33ma3.controller;

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

@Tag(name = "Location", description = "위치 관련 api")
@RestController
@RequiredArgsConstructor
public class LocationController {
    private final LocationService locationService;

    @GetMapping("/location")
    public ResponseEntity<?> getCenters(@RequestParam(value = "latitude") double latitude, @RequestParam(value = "longitude") double longitude,
                                        @RequestParam(value = "radius") double radius){
        List<CenterListDto> centers = locationService.getCenters(latitude, longitude, radius);

        return ResponseEntity.ok().body(DataResponse.success("반경 내 위치한 센터 정보 전송 완료", centers));
    }
}
