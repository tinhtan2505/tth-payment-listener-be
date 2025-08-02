package tth_group.payment_listener.seeder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import tth_group.payment_listener.entity.Attachment;
import tth_group.payment_listener.entity.Project;
import tth_group.payment_listener.entity.ProjectSampleImage;
import tth_group.payment_listener.repository.AttachmentRepository;
import tth_group.payment_listener.repository.ProjectRepository;
import tth_group.payment_listener.repository.ProjectSampleImageRepository;

import java.util.List;
import java.util.UUID;

@Component
@Order(7)
public class ProjectSampleImageSeeder implements CommandLineRunner {

    @Value("${seeder.enabled}")
    private boolean isSeederEnabled;

    @Autowired
    private ProjectSampleImageRepository projectSampleImageRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Override
    public void run(String... args) throws Exception {
        if (!isSeederEnabled) {
            return;
        }
        if (projectSampleImageRepository.count() == 0) {
            // Giả sử đã có dữ liệu trong bảng Project và Attachment
            List<Project> projects = projectRepository.findAll();
            List<Attachment> attachments = attachmentRepository.findAll();

            if (!projects.isEmpty() && !attachments.isEmpty()) {
                Project project = projects.getFirst();
                Attachment attachment = attachments.getFirst();

                // Tạo dữ liệu mẫu
                ProjectSampleImage projectSampleImage1 = new ProjectSampleImage();
                projectSampleImage1.setId(UUID.randomUUID());
                projectSampleImage1.setProject(project);
                projectSampleImage1.setAttachment(attachment);

                // Lưu dữ liệu vào database
                projectSampleImageRepository.save(projectSampleImage1);

                System.out.println("Seeded data for ProjectSampleImage entity.");
            } else {
                System.out.println("Project or Attachment not found.");
            }
        }
    }
}
