package tth_group.payment_listener.seeder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import tth_group.payment_listener.entity.FilesUploadDropbox;
import tth_group.payment_listener.entity.Project;
import tth_group.payment_listener.entity.User;
import tth_group.payment_listener.repository.FilesUploadDropboxRepository;
import tth_group.payment_listener.repository.ProjectRepository;
import tth_group.payment_listener.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@Order(8)
public class FilesUploadDropboxSeeder implements CommandLineRunner {

    @Value("${seeder.enabled}")
    private boolean isSeederEnabled;

    @Autowired
    private FilesUploadDropboxRepository filesUploadDropboxRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        if (!isSeederEnabled) {
            return;
        }
        if (filesUploadDropboxRepository.count() == 0) {
            // Giả sử đã có dữ liệu trong bảng Project và User
            List<Project> projects = projectRepository.findAll();
            Optional<User> userOpt = userRepository.findById(UUID.fromString("3e4f780e-593b-48ef-a971-063b7b75e27a"));

            if (!projects.isEmpty() && userOpt.isPresent()) {
                Project project = projects.getFirst();
                User user = userOpt.get();

                // Tạo dữ liệu mẫu
                FilesUploadDropbox file1 = new FilesUploadDropbox();
                file1.setId(UUID.randomUUID());
                file1.setName("Sample File 1");
                file1.setPathLocal("/local/path/file1");
                file1.setPathDisplay("/display/path/file1");
                file1.setPathLower("/lower/path/file1");
                file1.setSize(1024);
                file1.setUpload(true);
                file1.setProject(project);
                file1.setUploader(user);

                FilesUploadDropbox file2 = new FilesUploadDropbox();
                file2.setId(UUID.randomUUID());
                file2.setName("Sample File 2");
                file2.setPathLocal("/local/path/file2");
                file2.setPathDisplay("/display/path/file2");
                file2.setPathLower("/lower/path/file2");
                file2.setSize(2048);
                file2.setUpload(false);
                file2.setProject(project);
                file2.setUploader(user);

                // Lưu dữ liệu vào database
                filesUploadDropboxRepository.save(file1);
                filesUploadDropboxRepository.save(file2);

                System.out.println("Seeded data for FilesUploadDropbox entity.");
            } else {
                System.out.println("Project or User not found.");
            }
        }
    }
}
