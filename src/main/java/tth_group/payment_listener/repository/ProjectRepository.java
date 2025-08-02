package tth_group.payment_listener.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tth_group.payment_listener.dto.project.response.GetDetailProjectResponse;
import tth_group.payment_listener.entity.Project;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {

    @Query(
            "SELECT new tth_group.payment_listener.dto.project.response.GetDetailProjectResponse(" +
                    "p.id, " +
                    "p.name, " +
                    "p.globalName, " +
                    "p.path, " +
                    "p.pathDone, " +
                    "p.imageLink, " +
                    "p.customerKeyRs, " +
                    "p.imageCount, " +
                    "p.price, " +
                    "p.totalAmount, " +
                    "p.isTesting, " +
                    "p.deadline, " +
                    "c.label) " +
                    "FROM Project p " +
                    "LEFT JOIN Category c ON c.id = p.statusForCustomer.id ")
    List<GetDetailProjectResponse> getAll();
}
