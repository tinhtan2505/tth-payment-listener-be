package tth_group.payment_listener.service.iservices;

import tth_group.payment_listener.dto.request.VnpayCallbackRequest;
import tth_group.payment_listener.dto.response.VnpayCallbackResponse;
import tth_group.payment_listener.entity.TblVnpayCallback;

import java.util.UUID;

public interface ITblVnpayCallbackService {
    TblVnpayCallback saveCallback(VnpayCallbackRequest req);
    void updateKetQua(UUID id, VnpayCallbackResponse ketQua);
}
