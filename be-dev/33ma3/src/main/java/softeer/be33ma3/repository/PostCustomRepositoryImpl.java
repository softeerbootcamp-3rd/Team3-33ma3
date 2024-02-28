package softeer.be33ma3.repository;

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
    public List<Post> findAllByConditions(Long writerId, Boolean done, List<String> regions, List<String> repairs, List<String> tuneUps, List<Long> postIds) {
        BooleanExpression predicate = makeCondition(writerId, done, regions, repairs, tuneUps, postIds);
        return jpaQueryFactory.selectFrom(post)
                .where(predicate)
                .orderBy(post.createTime.desc(), post.postId.desc())
                .fetch();
    }

    private BooleanExpression makeCondition(Long writerId, Boolean done, List<String> regions, List<String> repairs, List<String> tuneUps, List<Long> postIds) {
        BooleanExpression predicate = Expressions.TRUE;

        if(writerId != null) {      // 서비스 센터가 아닌 유저만 선택 가능
            predicate = post.member.memberId.eq(writerId);
        }
        if(postIds != null) {       // 서비스 센터만 선택 가능
            predicate = post.postId.in(postIds);
        }
        if(done != null) {
            predicate = predicate.and(post.done.eq(done));
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
        return predicate;
    }

    private BooleanExpression containsAllService(String type, List<String> services) {
        return services.stream()
                .map(service -> type.equals("repair") ? post.repairService.contains(service) : post.tuneUpService.contains(service))
                .reduce(BooleanExpression::and)
                .orElse(Expressions.TRUE);
    }
}
