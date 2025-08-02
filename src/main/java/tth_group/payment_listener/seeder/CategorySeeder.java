package tth_group.payment_listener.seeder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import tth_group.payment_listener.entity.Category;
import tth_group.payment_listener.enums.CategoryType;
import tth_group.payment_listener.repository.CategoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Order(2)
public class CategorySeeder implements CommandLineRunner {

    @Value("${seeder.enabled}")
    private boolean isSeederEnabled;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void run(String... args) throws Exception {
        if (!isSeederEnabled) {
            return;
        }
        if (categoryRepository.count() < 3) {
            // Dữ liệu mẫu
            List<Category> categories = new ArrayList<>();
            categories.add(createCategory(UUID.fromString("86bede1e-81dc-414f-8485-143041a1bf0c"), "PNG", null, "#FFFFFF", 1, 0, CategoryType.FILE_TYPE, "PNG"));
            categories.add(createCategory(UUID.fromString("40271b60-e408-4055-be31-4608bb5f556c"), "PSD", null, "#FFFFFF", 2, 0, CategoryType.FILE_TYPE, "PSD"));
            categories.add(createCategory(UUID.randomUUID(), "JPG", null, "#FFFFFF", 3, 0, CategoryType.FILE_TYPE, "JPG"));
            categories.add(createCategory(UUID.randomUUID(), "JPEG", null, "#FFFFFF", 4, 0, CategoryType.FILE_TYPE, "JPEG"));
            categories.add(createCategory(UUID.randomUUID(), "TIFF", null, "#FFFFFF", 5, 0, CategoryType.FILE_TYPE, "TIFF"));
            categories.add(createCategory(UUID.randomUUID(), "DNG", null, "#FFFFFF", 7, 0, CategoryType.FILE_TYPE, "DNG"));
            categories.add(createCategory(UUID.fromString("f6a487cd-71fa-447a-b388-d99883ae6e9d"), "Waiting", "Waiting", "#FFFFFF", 3, 7, CategoryType.PROJECT_STATUS, "Waiting"));
            categories.add(createCategory(UUID.randomUUID(), "Processing", "Processing", "#FFFFFF", 4, 6, CategoryType.PROJECT_STATUS, "Processing"));
            categories.add(createCategory(UUID.randomUUID(), "Need Check", "Need Check", "#FFFFFF", 5, 5, CategoryType.PROJECT_STATUS, "NeedCheck"));
            categories.add(createCategory(UUID.randomUUID(), "Checked", "Checked", "#FFFFFF", 6, 1, CategoryType.PROJECT_STATUS, "Checked"));
            categories.add(createCategory(UUID.randomUUID(), "Revision Requested", "Need Fix", "#FFFFFF", 1, 2, CategoryType.PROJECT_STATUS, "NeedFix"));
            categories.add(createCategory(UUID.randomUUID(), "Completed", "Completed", "#FFFFFF", 7, 3, CategoryType.PROJECT_STATUS, "Completed"));
            categories.add(createCategory(UUID.randomUUID(), "Pending", "Pending", "#FFFFFF", 2, 4, CategoryType.PROJECT_STATUS, "Pending"));
            categories.add(createCategory(UUID.randomUUID(), "Draft", "Draft", "#FFFFFF", 0, 4, CategoryType.PROJECT_STATUS, "Draft"));


            // Lưu dữ liệu vào database
            categoryRepository.saveAll(categories);

            System.out.println("Seeded data for Category entity.");
        }
    }

    private Category createCategory(UUID id, String label, String labelHRM, String color, int order, int orderBySaler, CategoryType categoryType, String value) {
        Category category = new Category();
        category.setId(id);
        category.setLabel(label);
        category.setLabelHRM(labelHRM);
        category.setColor(color);
        category.setOrder(order);
        category.setOrderBySaler(orderBySaler);
        category.setCategoryType(categoryType);
        category.setValue(value);
        return category;
    }
}


