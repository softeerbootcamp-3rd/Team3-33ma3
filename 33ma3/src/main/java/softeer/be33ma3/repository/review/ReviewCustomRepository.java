package softeer.be33ma3.repository.review;

import softeer.be33ma3.dto.response.ShowReviewDto;

import java.util.List;

public interface ReviewCustomRepository {
    List<ShowReviewDto> findReviewGroupByCenter();
}
