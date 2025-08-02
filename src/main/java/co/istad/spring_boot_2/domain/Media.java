package co.istad.spring_boot_2.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "medias")
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 100, unique = true, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String extension;

    @Column(length = 20, nullable = false)
    private String mimeTypeFile;

    @Column(nullable = false)
    private boolean isDeleted;

}
