package tth_group.payment_listener.seeder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import tth_group.payment_listener.entity.Category;
import tth_group.payment_listener.entity.Project;
import tth_group.payment_listener.entity.User;
import tth_group.payment_listener.repository.CategoryRepository;
import tth_group.payment_listener.repository.ProjectRepository;
import tth_group.payment_listener.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@Order(3)
public class ProjectSeeder implements CommandLineRunner {

    @Value("${seeder.enabled}")
    private boolean isSeederEnabled;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void run(String... args) throws Exception {
        if (!isSeederEnabled) {
            return;
        }
        if (projectRepository.count() == 0) {
            // Giả sử đã có dữ liệu trong bảng User và Category
            Optional<User> creatorOpt = userRepository.findById(UUID.fromString("3e4f780e-593b-48ef-a971-063b7b75e27a"));
            Optional<User> salerOpt = userRepository.findById(UUID.fromString("efa8a2fa-3952-48b8-8399-cc31f0e5905e"));
            Optional<User> customerOpt = userRepository.findById(UUID.fromString("3e4f780e-593b-48ef-a971-063b7b75e27a"));
            List<Category> categories = categoryRepository.findAll();

            if (creatorOpt.isPresent() && salerOpt.isPresent() && customerOpt.isPresent() && !categories.isEmpty()) {
                User creator = creatorOpt.get();
                User saler = salerOpt.get();
                User customer = customerOpt.get();

                // Tạo dữ liệu mẫu cho Project
                Project project1 = new Project();
                project1.setId(UUID.randomUUID());
                project1.setName("Sample Project 1");
                project1.setGlobalName("Global Project 1");
                project1.setPath("/path/to/project1");
                project1.setPathDone("/path/to/project1/done");
                project1.setImageLink("https://example.com/image1.png");
                project1.setCustomerKeyRs("customer_key_1");
                project1.setImageCount(100);
                project1.setPrice(500.00);
                project1.setTotalAmount(50000.00);
                project1.setTesting(false);
                project1.setDeadline(LocalDateTime.now().plusDays(30));
                project1.setCreator(creator);
                project1.setSaler(saler);
                project1.setCustomer(customer);
                project1.setStatus(categories.getFirst());
                project1.setStatusForCustomer(categories.getLast());

                // Lưu project vào database
                projectRepository.save(project1);

                System.out.println("Seeded data for Project entity.");
            } else {
                System.out.println("Creator, Saler, Customer, Status or StatusForCustomer not found.");
            }
        }
    }
}
