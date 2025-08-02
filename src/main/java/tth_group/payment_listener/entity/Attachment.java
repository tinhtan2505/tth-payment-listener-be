package tth_group.payment_listener.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "attachment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Attachment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "title", length = 300)
    private String title;

    @Column(name = "url", length = 300)
    private String url;

    @Column(name = "thumbnail", length = 300)
    private String thumbnail;

    @Column(name = "extension", length = 10)
    private String extension;

    @Column(name = "size")
    private Long size;

    @Column(name = "type", length = 200)
    private String type;

    @Column(name = "image_done_name")
    private String imageDoneName;

    @Column(name = "sub_folder")
    private String subFolder;
}
