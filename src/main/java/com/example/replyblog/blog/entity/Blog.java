package com.example.replyblog.blog.entity;

import com.example.replyblog.blog.dto.BlogRequestDto;
import com.example.replyblog.replay.entity.Reply;
import com.example.replyblog.entity.Timestamped;
import com.example.replyblog.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Getter
@Entity
@NoArgsConstructor
public class Blog extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)     // AUTO말고 다른 타입들 공부하기
    private Long id; // 기본키 설정!
    @Column(nullable = false)
    private String contents;
    @Column(nullable = false)
    private String title;
//    @Column(nullable = false)
//    private String password;

    // 관계형성
    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @OneToMany(mappedBy = "Blog", cascade = CascadeType.REMOVE)
    @OrderBy(value = "createdAt DESC")
    private List<Reply> comments = new ArrayList<>();


    @Builder    // 빌더 기술매니저님이 연습하라했다!) 생성자와 세터 그리고 빌더에 대한 내용은 노션에서 다시 확인하기! 기억날때마다 틈틈히 보기~
    public Blog(BlogRequestDto blogRequestDto, User user) {
        this.contents = blogRequestDto.getContents();
        this.title = blogRequestDto.getTitle();
        this.user = user;

    }
    public void update(BlogRequestDto blogRequestDto, User user) {
        this.contents = blogRequestDto.getContents();
        this.title = blogRequestDto.getTitle();
        this.user = user;
    }

}
