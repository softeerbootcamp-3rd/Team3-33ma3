package softeer.be33ma3.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import softeer.be33ma3.domain.Center;
import softeer.be33ma3.dto.response.CenterListDto;
import softeer.be33ma3.repository.CenterRepository;
import softeer.be33ma3.domain.calcuator.DistanceCalculator;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {
    private final CenterRepository centerRepository;
    private final DistanceCalculator distanceCalculator;

    public List<CenterListDto> getAllCenters() {
        return centerRepository.findAll()
                .stream()
                .map(CenterListDto::from)
                .toList();
    }

    public List<CenterListDto> getCentersInRadius(double memberLatitude, double memberLongitude, double radius) {
        List<CenterListDto> centerListDtos = new ArrayList<>();
        List<Center> allCenter = centerRepository.findAll();

        for (Center center : allCenter) {
            if(center.isWithinRadius(distanceCalculator, memberLatitude, memberLongitude, radius)){
                centerListDtos.add(CenterListDto.from(center));
            }
        }

        return centerListDtos;
    }
}
