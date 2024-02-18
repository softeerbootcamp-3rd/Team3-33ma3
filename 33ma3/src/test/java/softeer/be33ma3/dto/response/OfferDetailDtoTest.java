package softeer.be33ma3.dto.response;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import softeer.be33ma3.domain.Center;
import softeer.be33ma3.domain.Member;
import softeer.be33ma3.domain.Offer;
import softeer.be33ma3.repository.CenterRepository;
import softeer.be33ma3.repository.MemberRepository;
import softeer.be33ma3.repository.OfferRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

@ActiveProfiles("test")
@SpringBootTest
class OfferDetailDtoTest {

    @Autowired
    private OfferRepository offerRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private CenterRepository centerRepository;

    @AfterEach
    void tearDown() {
        offerRepository.deleteAllInBatch();
        centerRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("Offer Entity 객체의 필드를 이용하여 OfferDetailDto로 변환한다.")
    @Test
    void fromEntity() {
        // given
        Member member = Member.createMember(2, "center1Id", "center1Pw");
        Member savedMember = memberRepository.save(member);
        Center center = Center.createCenter("center1", 0.0, 0.0, savedMember);
        Center savedCenter = centerRepository.save(center);
        Offer savedOffer = createOffer(10, "offer1", savedCenter);
        // when
        OfferDetailDto actual = OfferDetailDto.fromEntity(savedOffer);
        // then
        assertThat(actual).extracting("offerId", "memberId", "centerName", "price", "contents", "selected")
                .containsExactly(savedOffer.getOfferId(), savedMember.getMemberId(), savedCenter.getCenterName(),
                        savedOffer.getPrice(), savedOffer.getContents(), savedOffer.isSelected());
    }

    @DisplayName("Offer Entity 목록을 받아 이를 OfferDetailDto 목록으로 변환하고 저렴한 순으로 정렬하여 반환한다.")
    @Test
    void fromEntityList() {
        // given
        Member member1 = Member.createMember(2, "center1Id", "center1Pw");
        Member member2 = Member.createMember(2, "center2Id", "center2Pw");
        Member member3 = Member.createMember(2, "center3Id", "center3Pw");
        List<Member> members = memberRepository.saveAll(List.of(member1, member2, member3));
        Center center1 = Center.createCenter("center1", 0.0, 0.0, members.get(0));
        Center center2 = Center.createCenter("center2", 0.0, 0.0, members.get(1));
        Center center3 = Center.createCenter("center3", 0.0, 0.0, members.get(2));
        List<Center> centers = centerRepository.saveAll(List.of(center1, center2, center3));
        Offer offer1 = createOffer(3, "offer1", centers.get(0));
        Offer offer2 = createOffer(2, "offer2", centers.get(1));
        Offer offer3 = createOffer(1, "offer3", centers.get(2));
        // when
        List<OfferDetailDto> actual = OfferDetailDto.fromEntityList(List.of(offer1, offer2, offer3));
        // then
        assertThat(actual).hasSize(3)
                .extracting("offerId", "price")
                .containsExactly(
                        tuple(offer3.getOfferId(), offer3.getPrice()),
                        tuple(offer2.getOfferId(), offer2.getPrice()),
                        tuple(offer1.getOfferId(), offer1.getPrice())
                );
    }

    private Offer createOffer(int price, String contents, Center center) {
        Offer offer = Offer.builder()
                .price(price)
                .contents(contents)
                .post(null)
                .center(center).build();
        return offerRepository.save(offer);
    }
}
