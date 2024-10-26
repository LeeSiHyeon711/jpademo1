package org.lsh.jpademo.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_item")
public class Item {
    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 50)
    private String itemNm;
    @Column(name = "item_price", nullable = false)
    private Long price;
    @Column(nullable = false)
    private Long stockNumber;
    @Lob
    @Column(nullable = false)
    private String itemDetail;
    @CreationTimestamp
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date regTime;
    @UpdateTimestamp
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date updateTime;
}
