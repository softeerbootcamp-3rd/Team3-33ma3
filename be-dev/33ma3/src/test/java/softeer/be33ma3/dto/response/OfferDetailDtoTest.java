package softeer.be33ma3.dto.response;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import softeer.be33ma3.domain.*;
import softeer.be33ma3.repository.ImageRepository;
import softeer.be33ma3.repository.MemberRepository;
import softeer.be33ma3.repository.OfferRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class OfferDetailDtoTest {

    @Autowired private OfferRepository offerRepository;
    @Autowired private MemberRepository memberRepository;
    @Autowired private ImageRepository imageRepository;

    @AfterEach
    void tearDown() {
        offerRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
        imageRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("Offer Entity와 해당 견적을 작성한 센터의 정보를 넘겨받고 OfferDetailDto로 변환하여 반환한다.")
    void fromEntity() {
        // given
        Member savedMember = createCenter("center1", "center1");
        Offer savedOffer = saveOffer(10, "offer1", null, savedMember);
        OfferDetailDto expected = OfferDetailDto.builder()
                .offerId(savedOffer.getOfferId())
                .memberId(savedOffer.getCenter().getMemberId())
                .price(10)
                .contents("offer1")
                .centerName("center1")
                .profile("profile.png")
                .selected(false)
                .score(4.5).build();
        // when
        OfferDetailDto actual = OfferDetailDto.fromEntity(savedOffer, 4.5);
        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("댓글을 1순위는 제시가격이 저렴한 순으로, 2순위는 센터의 별점이 높은 순으로 정렬한다.")
    void compareTo() {
        // given
        Offer offer1 = saveOffer(3, "offer1", null, createCenter("center1", "center1"));
        Offer offer2 = saveOffer(1, "offer2", null, createCenter("center2", "center2"));
        Offer offer3 = saveOffer(2, "offer3", null, createCenter("center3", "center3"));
        Offer offer4 = saveOffer(2, "offer4", null, createCenter("center4", "center4"));
        OfferDetailDto offerDetailDto1 = OfferDetailDto.fromEntity(offer1, 5.0);
        OfferDetailDto offerDetailDto2 = OfferDetailDto.fromEntity(offer2, 0.0);
        OfferDetailDto offerDetailDto3 = OfferDetailDto.fromEntity(offer3, 4.9);
        OfferDetailDto offerDetailDto4 = OfferDetailDto.fromEntity(offer4, 5.0);
        List<OfferDetailDto> actual = Arrays.asList(offerDetailDto1, offerDetailDto2, offerDetailDto3, offerDetailDto4);
        List<OfferDetailDto> expected = Arrays.asList(offerDetailDto2, offerDetailDto4, offerDetailDto3, offerDetailDto1);
        // when
        Collections.sort(actual);
        // then
        assertThat(actual).containsExactlyElementsOf(expected);

    }

    private Offer saveOffer(int price, String contents, Post post, Member center) {
        Offer offer = Offer.builder()
                .price(price)
                .contents(contents)
                .post(post)
                .center(center).build();
        return offerRepository.save(offer);
    }
    private Member createCenter(String loginId, String password) {
        Image profile = Image.createImage("profile.png", "profile.png");
        Image savedProfile = imageRepository.save(profile);
        Member center = Member.createCenter(loginId, password, savedProfile);
        return memberRepository.save(center);
    }
}
