package softeer.be33ma3.repository;

import softeer.be33ma3.dto.response.ShowAllReviewDto;

import java.util.List;

public interface ReviewCustomRepository {
    List<ShowAllReviewDto> findReviewGroupByCenter();
}
