package tth_group.payment_listener.service.iservices;

import tth_group.payment_listener.dto.project.response.GetDetailProjectResponse;

import java.util.List;

public interface IProjectService {
    List<GetDetailProjectResponse> getAll();
}
