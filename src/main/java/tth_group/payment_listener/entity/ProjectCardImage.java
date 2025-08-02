package tth_group.payment_listener.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "project_card_image")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectCardImage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "image_done_name")
    private String imageDoneName;

    @Column(name = "sub_folder")
    private String subFolder;

    @Column(name = "is_auto_capture")
    private Boolean isAutoCapture;

    @ManyToOne
    @JoinColumn(name = "project_card_id")
    private ProjectCard projectCard;

    @ManyToOne
    @JoinColumn(name = "attachment_id")
    private Attachment attachment;
}
