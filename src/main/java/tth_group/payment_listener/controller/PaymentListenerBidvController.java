package tth_group.payment_listener.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tth_group.payment_listener.client.QrPaymentClient;
import tth_group.payment_listener.dto.his_tth.CustomResponse;
import tth_group.payment_listener.dto.request.VnpayCallbackRequest;
import tth_group.payment_listener.dto.response.VnpayCallbackResponse;
import tth_group.payment_listener.entity.TblVnpayCallback;
import tth_group.payment_listener.service.iservices.ITblVnpayCallbackService;
import tth_group.payment_listener.utils.ThanhToanOnlineUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("tthgroup/api")
@Tag(name = "Payment Listener Bidv")
public class PaymentListenerBidvController {
    @Value("${vnpay.secretKey}")
    private String secretKey;

    @Autowired
    private ITblVnpayCallbackService iTblVnpayCallbackService;
    @Autowired
    private QrPaymentClient qrPaymentClient;

    @PostMapping(
            value = "thanhtoanqrcode",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VnpayCallbackResponse> onPaymentResult(
            @RequestBody VnpayCallbackRequest req) {

        // Lưu bản ghi thô ngay khi nhận callback
        TblVnpayCallback rec = iTblVnpayCallbackService.saveCallback(req);

        // Tính checksum kỳ vọng
        String raw = String.join("|",
                req.getCode(), req.getMsgType(), req.getTxnId(), req.getQrTrace(),
                req.getBankCode(), req.getMobile(), req.getAccountNo(),
                req.getAmount(), req.getPayDate(),
                req.getMerchantCode(), secretKey);
        String expected = ThanhToanOnlineUtils.encodeMD5LowCase(raw);

        HttpStatus status = HttpStatus.OK;
        VnpayCallbackResponse resp;

        // 1) Sai checksum
        if (!expected.equalsIgnoreCase(req.getChecksum())) {
            String respChk = ThanhToanOnlineUtils.encodeMD5LowCase("06" + secretKey);
            resp = new VnpayCallbackResponse("06", "sai thông tin xác thực", respChk);
            status = HttpStatus.BAD_REQUEST;
        }
        // 2) Nghiệp vụ thanh toán thành công từ phía VNPAY
        else if ("00".equals(req.getCode())) {
            CustomResponse<Map<String, Object>> hisResp = qrPaymentClient.getByBillNumber(req.getTxnId());

            if (hisResp == null || hisResp.getStatus() != 200 || hisResp.getData() == null) {
                String respChk = ThanhToanOnlineUtils.generateSecureCode("07" + secretKey);
                resp = new VnpayCallbackResponse(
                        "09",
                        "không tìm thấy hóa đơn hoặc lỗi kết nối đến server his tth",
                        Map.of("txnId", req.getTxnId()),
                        respChk
                );
                status = HttpStatus.BAD_REQUEST;
            } else {
                BigDecimal hisAmount = new BigDecimal(hisResp.getData().get("amount").toString());
                BigDecimal reqAmount = new BigDecimal(req.getAmount());
                if (hisAmount.compareTo(reqAmount) != 0) {
                    String respChk = ThanhToanOnlineUtils.generateSecureCode("07" + secretKey);
                    resp = new VnpayCallbackResponse(
                            "07",
                            "số tiền không chính xác",
                            Map.of("amount", hisAmount),
                            respChk
                    );
                    status = HttpStatus.BAD_REQUEST;
                } else {
                    // OK hết
                    Map<String, String> dataMap = new HashMap<>();
                    dataMap.put("txnId", req.getTxnId());
                    String respChecksum = ThanhToanOnlineUtils.generateSecureCode("00" + secretKey);
                    resp = new VnpayCallbackResponse("00", "đặt hàng thành công", dataMap, respChecksum);
                    status = HttpStatus.OK;
                }
            }
        }
        // 3) VNPAY báo lỗi code != 00
        else {
            // Tùy quy ước, có thể trả "00" ack hay phản ánh mã lỗi
            String respChecksum = ThanhToanOnlineUtils.generateSecureCode("00" + secretKey);
            resp = new VnpayCallbackResponse("88", "VNPAY báo lỗi code != 00", Map.of("code", req.getCode()), respChecksum);
            status = HttpStatus.BAD_REQUEST; // thường vẫn 200 để ACK
        }

        // GHI ketQua vào DB bất kể status
        try {
            iTblVnpayCallbackService.updateKetQua(rec.getId(), resp);
            qrPaymentClient.sendPaymentResult(resp);
        } catch (Exception _) {
        }

        return ResponseEntity.status(status).body(resp);
    }
}
