package tth_group.payment_listener.seeder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import tth_group.payment_listener.entity.Category;
import tth_group.payment_listener.entity.Project;
import tth_group.payment_listener.entity.ProjectFileType;
import tth_group.payment_listener.repository.CategoryRepository;
import tth_group.payment_listener.repository.ProjectFileTypeRepository;
import tth_group.payment_listener.repository.ProjectRepository;

import java.util.List;
import java.util.UUID;

@Component
@Order(6)
public class ProjectFileTypeSeeder implements CommandLineRunner {

    @Value("${seeder.enabled}")
    private boolean isSeederEnabled;

    @Autowired
    private ProjectFileTypeRepository projectFileTypeRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void run(String... args) throws Exception {
        if (!isSeederEnabled) {
            return;
        }
        if (projectFileTypeRepository.count() == 0) {
            // Giả sử đã có dữ liệu trong bảng Project và Category
            List<Project> projects = projectRepository.findAll();
            List<Category> categories = categoryRepository.findAll();

            if (!projects.isEmpty() && !categories.isEmpty()) {
                Project project = projects.getFirst();
                Category category = categories.getFirst();

                // Dữ liệu mẫu
                ProjectFileType projectFileType1 = new ProjectFileType();
                projectFileType1.setId(UUID.randomUUID());
                projectFileType1.setProject(project);
                projectFileType1.setFileType(category);  // File type là Category

                // Lưu dữ liệu vào database
                projectFileTypeRepository.save(projectFileType1);

                System.out.println("Seeded data for ProjectFileType entity.");
            } else {
                System.out.println("Project or Category not found.");
            }
        }
    }
}
