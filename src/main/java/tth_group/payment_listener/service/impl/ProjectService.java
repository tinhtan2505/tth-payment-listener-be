package tth_group.payment_listener.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tth_group.payment_listener.dto.project.response.GetDetailProjectResponse;
import tth_group.payment_listener.repository.ProjectRepository;
import tth_group.payment_listener.service.iservices.IProjectService;

import java.util.List;

@Service
public class ProjectService implements IProjectService {
    @Autowired
    private ProjectRepository repository;

    @Override
    public List<GetDetailProjectResponse> getAll() {
        List<GetDetailProjectResponse> record = repository.getAll();
        return record;
    }
}
