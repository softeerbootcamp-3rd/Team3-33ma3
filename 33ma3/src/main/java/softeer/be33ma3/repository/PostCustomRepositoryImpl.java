package softeer.be33ma3.repository;

import softeer.be33ma3.domain.Post;

import java.util.List;

public class PostCustomRepositoryImpl implements PostCustomRepository {
    @Override
    public List<Post> findAllByConditions(Boolean done, List<String> regions, List<String> repairs, List<String> tuneUps, List<Long> postIds) {
        return null;
    }
}
