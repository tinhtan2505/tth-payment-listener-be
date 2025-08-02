package tth_group.payment_listener.seeder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import tth_group.payment_listener.entity.Project;
import tth_group.payment_listener.entity.ProjectCard;
import tth_group.payment_listener.entity.User;
import tth_group.payment_listener.enums.ProjectCardStatus;
import tth_group.payment_listener.enums.ProjectCardType;
import tth_group.payment_listener.repository.ProjectCardRepository;
import tth_group.payment_listener.repository.ProjectRepository;
import tth_group.payment_listener.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@Order(4)
public class ProjectCardSeeder implements CommandLineRunner {

    @Value("${seeder.enabled}")
    private boolean isSeederEnabled;

    @Autowired
    private ProjectCardRepository projectCardRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        if (!isSeederEnabled) {
            return;
        }
        if (projectCardRepository.count() == 0) {
            // Giả sử đã có dữ liệu trong bảng Project và User
            List<Project> projects = projectRepository.findAll();
            Optional<User> creatorOpt = userRepository.findById(UUID.fromString("3e4f780e-593b-48ef-a971-063b7b75e27a"));

            if (!projects.isEmpty() && creatorOpt.isPresent()) {
                Project project = projects.getFirst();
                User creator = creatorOpt.get();

                // Tạo dữ liệu mẫu cho ProjectCard
                ProjectCard projectCard1 = new ProjectCard();
                projectCard1.setId(UUID.randomUUID());
                projectCard1.setType(ProjectCardType.PROJECT_REQUIREMENT);
                projectCard1.setStatus(ProjectCardStatus.PENDING);
                projectCard1.setContent("This is the first project card content.");
                projectCard1.setDefault(false);
                projectCard1.setCreator(creator);
                projectCard1.setProject(project);

                // Lưu ProjectCard vào database
                projectCardRepository.save(projectCard1);

                System.out.println("Seeded data for ProjectCard entity.");
            } else {
                System.out.println("Project or Creator not found.");
            }
        }
    }
}
