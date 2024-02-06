package softeer.be33ma3.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import softeer.be33ma3.domain.Region;
import softeer.be33ma3.repository.RegionRepository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RegionService {
    private final RegionRepository regionRepository;

    @PostConstruct
    public void setUpRegionData(){
        if(regionRepository.findAll().isEmpty()){//데이터가 없는 경우
            List<String> regionList = Arrays.asList(
                    "강남구", "강동구", "강북구", "강서구",
                    "관악구", "광진구", "구로구", "금천구",
                    "노원구", "도봉구", "동대문구", "동작구",
                    "마포구", "서대문구", "서초구", "성동구",
                    "성북구", "송파구", "양천구", "영등포구",
                    "용산구", "은평구", "종로구", "중구", "중랑구"
            );
            List<Region> regions = regionList.stream()
                    .map(r -> Region.builder()
                            .regionName(r)
                            .build())
                    .collect(Collectors.toList());
            regionRepository.saveAll(regions);
        }
    }
}
