package tth_group.payment_listener.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tth_group.payment_listener.entity.ProjectFileType;

import java.util.UUID;

@Repository
public interface ProjectFileTypeRepository extends JpaRepository<ProjectFileType, UUID> {
    // Bạn có thể định nghĩa các phương thức tùy chỉnh ở đây nếu cần
}
