package org.lsh.jpademo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tbl_board")
public class Board extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;
    @Column(nullable = false, length = 200)
    private String title;
    @Column(nullable = false, length = 2000)
    private String content;
    @Column(nullable = false, length = 50)
    private String writer;
    @ColumnDefault("0")
    private int visitCount;
    public void UpdateVisitCount() {
        this.visitCount++;
    }
    public void change(String title, String content, String writer){
        this.title = title;
        this.content = content;
        this.writer = writer;
    }
    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY,
            cascade = {CascadeType.ALL}, orphanRemoval = true)
    @Builder.Default
    private Set<BoardImage> imageSet=new HashSet<>();
    public void addImage(String uuid, String fileName) {
        BoardImage image = BoardImage.builder()
                .uuid(uuid)
                .fileName(fileName)
                .board(this)
                .ord(imageSet.size())
                .build();
        imageSet.add(image);
    }
    public void clearImages() {
        imageSet.forEach(boardImage -> boardImage.changeBoard(null));
        this.imageSet.clear();
    }
}

