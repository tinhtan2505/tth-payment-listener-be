package tth_group.payment_listener.seeder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import tth_group.payment_listener.entity.Attachment;
import tth_group.payment_listener.repository.AttachmentRepository;

import java.util.UUID;

@Component
@Order(1)
public class AttachmentSeeder implements CommandLineRunner {

    @Value("${seeder.enabled}")
    private boolean isSeederEnabled;

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Override
    public void run(String... args) throws Exception {
        if (!isSeederEnabled) {
            return;
        }
        if (attachmentRepository.count() == 0) {
            // Dữ liệu mẫu
            Attachment attachment1 = new Attachment(
                    UUID.fromString("e07e56cf-e9b2-4db0-b126-d6dc35e04cf7"),
                    "Sample Attachment 1",
                    "https://example.com/file1.png",
                    "https://example.com/thumbnail1.png",
                    "png",
                    2048L,
                    "image/png",
                    "done_image1.png",
                    "folder1/subfolder1"
            );

            Attachment attachment2 = new Attachment(
                    UUID.fromString("6c7ab75b-0253-456f-8a9e-499050b2dc22"),
                    "Sample Attachment 2",
                    "https://example.com/file2.jpg",
                    "https://example.com/thumbnail2.jpg",
                    "jpg",
                    4096L,
                    "image/jpeg",
                    "done_image2.jpg",
                    "folder2/subfolder2"
            );

            // Lưu dữ liệu vào database
            attachmentRepository.save(attachment1);
            attachmentRepository.save(attachment2);

            System.out.println("Seeded data for Attachment entity.");
        }
    }
}
