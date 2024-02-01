package softeer.be33ma3.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class SelectedCenterService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long selectedServiceId;
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
    @ManyToOne
    @JoinColumn(name = "center_service_id")
    private CenterService centerService;
}
