package softeer.be33ma3.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Post extends BaseTimeEntity{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long postId;
    private String modelName;
    private boolean done;       // 경매 완료시 true
    private int deadline;
    private String contents;
    private String carType;
    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
