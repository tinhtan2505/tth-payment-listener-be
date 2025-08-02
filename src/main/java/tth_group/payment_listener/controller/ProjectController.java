package tth_group.payment_listener.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tth_group.payment_listener.dto.CustomResponse;
import tth_group.payment_listener.dto.project.response.GetDetailProjectResponse;
import tth_group.payment_listener.service.iservices.IProjectService;
import tth_group.payment_listener.utils.ErrorUtils;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("api/v1.0/project")
@Tag(name = "Project")
public class ProjectController {
    @Autowired
    private IProjectService iService;

    @GetMapping("")
    public ResponseEntity<?> getAll() {
        try {
            List<GetDetailProjectResponse> result = iService.getAll();
            return ResponseEntity.ok(CustomResponse.success(result, "Thành công"));
        } catch (Exception e) {
            HttpStatus status = ErrorUtils.determineHttpStatus(e);
            return ResponseEntity.status(status)
                    .body(CustomResponse.error(e.getMessage(), status.value()));
        }
    }
}
