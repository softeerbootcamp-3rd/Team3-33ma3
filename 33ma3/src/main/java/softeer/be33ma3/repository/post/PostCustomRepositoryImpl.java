package softeer.be33ma3.repository.post;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import softeer.be33ma3.domain.Post;

import static softeer.be33ma3.domain.QPost.post;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostCustomRepositoryImpl implements PostCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> findAllByConditions(Boolean done, List<String> regions, List<String> repairs, List<String> tuneUps, List<Long> postIds) {
        BooleanExpression predicate = makeCondition(done, regions, repairs, tuneUps, postIds);
        return jpaQueryFactory.selectFrom(post)
                .where(predicate)
                .orderBy(post.createTime.desc(), post.postId.desc())
                .fetch();
    }

    private BooleanExpression makeCondition(Boolean done, List<String> regions, List<String> repairs, List<String> tuneUps, List<Long> postIds) {
        BooleanExpression predicate = Expressions.TRUE;
        if(done != null) {
            predicate = post.done.eq(done);
        }
        if(!regions.isEmpty()) {
            predicate = predicate.and(post.region.regionName.in(regions));
        }
        if(!repairs.isEmpty()) {
            predicate = predicate.and(containsAllService("repair", repairs));
        }
        if(!tuneUps.isEmpty()) {
            predicate = predicate.and(containsAllService("tuneUp", tuneUps));
        }
        if(postIds != null) {
            predicate = predicate.and(post.postId.in(postIds));
        }
        return predicate;
    }

    private BooleanExpression containsAllService(String type, List<String> services) {
        return services.stream()
                .map(service -> type.equals("repair") ? post.repairService.contains(service) : post.tuneUpService.contains(service))
                .reduce(BooleanExpression::and)
                .orElse(Expressions.TRUE);
    }
}
