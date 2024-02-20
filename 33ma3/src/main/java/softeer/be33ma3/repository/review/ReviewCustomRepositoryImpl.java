package softeer.be33ma3.repository.review;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import softeer.be33ma3.dto.response.ShowReviewDto;

import java.util.List;
import static softeer.be33ma3.domain.QReview.review;

@Repository
@RequiredArgsConstructor
public class ReviewCustomRepositoryImpl implements ReviewCustomRepository{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<ShowReviewDto> findReviewGroupByCenter() {
        List<ShowReviewDto> result = jpaQueryFactory
                .select(Projections.constructor(ShowReviewDto.class,
                        review.center.memberId,
                        review.score.avg().doubleValue(),
                        review.count(),
                        review.center.loginId,
                        review.center.image.link))
                .from(review)
                .groupBy(review.center.memberId)
                .orderBy(review.score.avg().desc())
                .fetch();

        return result;
    }
}
