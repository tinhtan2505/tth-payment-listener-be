package tth_group.payment_listener.seeder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import tth_group.payment_listener.entity.Attachment;
import tth_group.payment_listener.entity.ProjectCard;
import tth_group.payment_listener.entity.ProjectCardImage;
import tth_group.payment_listener.repository.AttachmentRepository;
import tth_group.payment_listener.repository.ProjectCardImageRepository;
import tth_group.payment_listener.repository.ProjectCardRepository;

import java.util.List;
import java.util.UUID;

@Component
@Order(5)
public class ProjectCardImageSeeder implements CommandLineRunner {

    @Value("${seeder.enabled}")
    private boolean isSeederEnabled;

    @Autowired
    private ProjectCardImageRepository projectCardImageRepository;

    @Autowired
    private ProjectCardRepository projectCardRepository;

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Override
    public void run(String... args) throws Exception {
        if (!isSeederEnabled) {
            return;
        }
        if (projectCardImageRepository.count() == 0) {
            // Giả sử đã có dữ liệu trong bảng ProjectCard và Attachment
            List<ProjectCard> projectCards = projectCardRepository.findAll();
            List<Attachment> attachments = attachmentRepository.findAll();

            if (!projectCards.isEmpty() && !attachments.isEmpty()) {
                ProjectCard projectCard = projectCards.getFirst();
                Attachment attachment = attachments.getFirst();

                // Tạo dữ liệu mẫu cho ProjectCardImage
                ProjectCardImage projectCardImage1 = new ProjectCardImage();
                projectCardImage1.setId(UUID.randomUUID());
                projectCardImage1.setImageDoneName("image_done_1.png");
                projectCardImage1.setSubFolder("folder1/subfolder1");
                projectCardImage1.setIsAutoCapture(true);
                projectCardImage1.setProjectCard(projectCard);
                projectCardImage1.setAttachment(attachment);

                // Lưu ProjectCardImage vào database
                projectCardImageRepository.save(projectCardImage1);

                System.out.println("Seeded data for ProjectCardImage entity.");
            } else {
                System.out.println("ProjectCard or Attachment not found.");
            }
        }
    }
}
