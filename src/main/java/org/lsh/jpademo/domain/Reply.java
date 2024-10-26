package org.lsh.jpademo.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "board")
public class Reply extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;
    private String replyText;
    private String replyer;
    public void changeText(String newText) {
        this.replyText = newText;
    }
}
