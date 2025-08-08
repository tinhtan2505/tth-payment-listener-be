package tth_group.payment_listener.service.iservices;

import tth_group.payment_listener.dto.request.VnpayCallbackRequest;
import tth_group.payment_listener.entity.TblVnpayCallback;

public interface ITblVnpayCallbackService {
    TblVnpayCallback saveCallback(VnpayCallbackRequest req);
}
