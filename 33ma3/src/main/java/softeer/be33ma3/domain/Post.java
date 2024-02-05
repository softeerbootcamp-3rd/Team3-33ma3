package softeer.be33ma3.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Post extends BaseTimeEntity{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long postId;

    private String modelName;

    private boolean done;       //낙찰 시 or 마감기한 끝난경우 true

    private int deadline;

    private String contents;

    private String carType;

    private String repairService;

    private String tuneUpService;

    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
