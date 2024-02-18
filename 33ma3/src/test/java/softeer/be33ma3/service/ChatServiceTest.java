package softeer.be33ma3.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import softeer.be33ma3.domain.Member;
import softeer.be33ma3.domain.Post;
import softeer.be33ma3.dto.request.PostCreateDto;
import softeer.be33ma3.repository.ChatRoomRepository;
import softeer.be33ma3.repository.MemberRepository;
import softeer.be33ma3.repository.PostRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class ChatServiceTest {
    @Autowired ChatService chatService;
    @Autowired MemberRepository memberRepository;
    @Autowired ChatRoomRepository chatRoomRepository;
    @Autowired PostRepository postRepository;

    @DisplayName("채팅을 할 채팅방을 만든다.")
    @Test
    void createRoom(){
        //given
        Member client = new Member(1, "client1", "1234");
        Member savedClient = memberRepository.save(client);

        Long postId = createPost(savedClient).getPostId();

        //when
        Long roomId = chatService.createRoom(client, savedClient.getMemberId(), postId);

        //then
        assertThat(roomId).isNotNull();
    }

    private Post createPost(Member client) {
        Post post = Post.createPost(Mockito.mock(PostCreateDto.class), null, client);
        return postRepository.save(post);
    }
}
