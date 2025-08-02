package tth_group.payment_listener.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tth_group.payment_listener.enums.ProjectCardStatus;
import tth_group.payment_listener.enums.ProjectCardType;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "project_card")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectCard extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ProjectCardType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ProjectCardStatus status;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "is_default", nullable = false)
    private boolean isDefault;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private User creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToMany(mappedBy = "projectCard", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProjectCardImage> cardImages;
}
