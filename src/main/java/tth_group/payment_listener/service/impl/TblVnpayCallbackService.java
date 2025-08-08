package tth_group.payment_listener.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tth_group.payment_listener.dto.request.VnpayCallbackRequest;
import tth_group.payment_listener.entity.TblVnpayCallback;
import tth_group.payment_listener.repository.TblVnpayCallbackRepository;
import tth_group.payment_listener.service.iservices.ITblVnpayCallbackService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class TblVnpayCallbackService implements ITblVnpayCallbackService {

    private final TblVnpayCallbackRepository repository;
    private final ObjectMapper objectMapper; // dùng serialize rawJson

    @Override
    @Transactional
    public TblVnpayCallback saveCallback(VnpayCallbackRequest req) {
        BigDecimal amountNumeric = parseAmount(req.getAmount());
        LocalDateTime parsedPayDate = parsePayDate(req.getPayDate());

        // rawJson dạng JsonNode để map vào jsonb
        JsonNode rawJson = objectMapper.valueToTree(req);

        TblVnpayCallback entity = TblVnpayCallback.fromRequest(
                req,
                rawJson,
                amountNumeric,
                parsedPayDate
        );

        return repository.save(entity);
    }

    private static BigDecimal parseAmount(String amountRaw) {
        if (amountRaw == null || amountRaw.isBlank()) return null;
        try {
            // Nếu VNPAY gửi số tiền theo đơn vị "xu" (vd: 10000 => 100.00), giữ rule movePointLeft(2)
            return new BigDecimal(amountRaw).movePointLeft(2);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static LocalDateTime parsePayDate(String payDateRaw) {
        if (payDateRaw == null || payDateRaw.length() != 14) return null;
        try {
            DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            return LocalDateTime.parse(payDateRaw, f);
        } catch (Exception e) {
            return null;
        }
    }
}
