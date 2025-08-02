package tth_group.payment_listener.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "files_upload_dropbox")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilesUploadDropbox extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    private String pathLocal;
    private String pathDisplay;
    private String pathLower;
    private int size;
    private boolean isUpload;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "uploader_id")
    private User uploader;
}
