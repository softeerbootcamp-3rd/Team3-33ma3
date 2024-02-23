package softeer.be33ma3.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;
import softeer.be33ma3.domain.Member;
import softeer.be33ma3.domain.Post;
import softeer.be33ma3.domain.Region;
import softeer.be33ma3.dto.request.PostCreateDto;
import softeer.be33ma3.exception.BusinessException;
import softeer.be33ma3.exception.ErrorCode;
import softeer.be33ma3.repository.ImageRepository;
import softeer.be33ma3.repository.MemberRepository;
import softeer.be33ma3.repository.post.PostRepository;
import softeer.be33ma3.repository.RegionRepository;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PostServiceTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private PostService postService;
    @Autowired private PostRepository postRepository;
    @Autowired private MemberRepository memberRepository;
    @Autowired private RegionRepository regionRepository;
    @Autowired private ImageRepository imageRepository;

    @BeforeEach
    void setUp(){
        //회원 저장
        Member member1 = Member.createClient( "client1", "1234", null);
        Member member2 = Member.createClient( "client2", "1234", null);
        Member member3 = Member.createCenter( "center1", "1234", null);
        memberRepository.saveAll(List.of(member1, member2, member3));
        regionRepository.save(new Region(1L, "강남구"));
    }

    @AfterEach
    void tearDown(){
        imageRepository.deleteAllInBatch();
        postRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
        regionRepository.deleteAllInBatch();
    }

    @DisplayName("게시글을 생성할 수 있다.")
    @Test
    void createPostWithImage() throws IOException {
        //given
        List<MultipartFile> multipartFiles = createMultipartFile();

        Member member = memberRepository.findMemberByLoginId("client1").get();
        PostCreateDto postCreateDto = new PostCreateDto("승용차", "제네시스", 3, "서울시 강남구", "기스, 깨짐", "오일 교체", new ArrayList<>(),"게시글 생성 이미지 포함");

        //when
        Long postId = postService.createPost(member, postCreateDto, multipartFiles);

        //then
        Post post = postRepository.findById(postId).get();
        assertThat(post.getContents()).isEqualTo("게시글 생성 이미지 포함");
    }

    @DisplayName("이미지 없이 게시글을 생성할 수 있다.")
    @Test
    void createPostWithoutImage(){
        //given
        Member member = memberRepository.findMemberByLoginId("client1").get();
        PostCreateDto postCreateDto = new PostCreateDto("승용차", "제네시스", 3, "서울시 강남구", "기스, 깨짐", "오일 교체", new ArrayList<>(),"게시글 생성 이미지 미포함");

        //when
        Long postId = postService.createPost(member, postCreateDto, null);

        //then
        Post post = postRepository.findById(postId).get();
        assertThat(post.getContents()).isEqualTo("게시글 생성 이미지 미포함");
    }

    @DisplayName("센터가 글을 작성하려고 하면 예외가 발생한다.")
    @Test
    void createPostWithCenter(){
        //given
        Member member = memberRepository.findMemberByLoginId("center1").get();
        PostCreateDto postCreateDto = new PostCreateDto("승용차", "제네시스", 3, "서울시 강남구", "기스, 깨짐", "오일 교체", new ArrayList<>(),"게시글 생성");

        //when  //then
        assertThatThrownBy(() -> postService.createPost(member, postCreateDto, null))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.POST_CREATION_DISABLED);
    }

    @DisplayName("게시글을 수정할 수 있다.")
    @Test
    void editPost(){
        //given
        Member member = memberRepository.findMemberByLoginId("client1").get();
        Region region = regionRepository.findByRegionName("강남구").get();

        Post savedPost = savePost(region, member);

        PostCreateDto postEditDto = new PostCreateDto("승용차", "제네시스", 3, "서울시 강남구", "기스, 깨짐", "오일 교체", new ArrayList<>(),"수정후 내용");

        //when
        postService.editPost(member, savedPost.getPostId(), postEditDto);

        //then
        Post editPost = postRepository.findById(savedPost.getPostId()).get();
        assertThat(editPost.getContents()).isEqualTo("수정후 내용");
    }

    @DisplayName("작성자와 다른 사람이 수정하면 예외가 발생한다.")
    @Test
    void editPostWithOtherMember(){
        //given
        Member member1 = memberRepository.findMemberByLoginId("client1").get();
        Member member2 = memberRepository.findMemberByLoginId("client2").get();
        Region region = regionRepository.findByRegionName("강남구").get();

        Post savedPost = savePost(region, member1);

        PostCreateDto postEditDto = new PostCreateDto("승용차", "제네시스", 3, "서울시 강남구", "기스, 깨짐", "오일 교체", new ArrayList<>(),"수정후 내용");

        //when //then
        assertThatThrownBy(() -> postService.editPost(member2, savedPost.getPostId(), postEditDto))
                .isInstanceOf(BusinessException.class);
    }

    private List<MultipartFile> createMultipartFile() throws IOException {
        String fileName = "testImage"; //파일명
        String contentType = "jpg"; //파일타입
        String filePath = "src/test/resources/testImage/"+fileName+"."+contentType;

        FileInputStream fileInputStream = new FileInputStream(filePath);
        List<MultipartFile> multipartFiles = new ArrayList<>();

        MockMultipartFile multipartFile = new MockMultipartFile(
                "images",
                fileName + "." + contentType,
                contentType,
                fileInputStream
        );

        multipartFiles.add(multipartFile);
        return multipartFiles;
    }

    private Post savePost(Region region, Member member1) {
        PostCreateDto postCreateDto = new PostCreateDto("승용차", "제네시스", 3, "서울시 강남구", "기스, 깨짐", "오일 교체", new ArrayList<>(),"수정전 내용");
        Post post = Post.createPost(postCreateDto, region, member1);
        return postRepository.save(post);
    }
}
