package org.lsh.jpademo.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "tbl_member")
public class JpaMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 50)
    private String name;
    @Column(nullable = false, length = 50)
    private String password;
    @Column(nullable = false, length = 50)
    private String email;
    private String memo;
    @Column(name = "addr")
    private String address;
}
