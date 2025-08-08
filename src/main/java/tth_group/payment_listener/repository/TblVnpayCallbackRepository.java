package tth_group.payment_listener.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tth_group.payment_listener.entity.TblVnpayCallback;

import java.util.UUID;

public interface TblVnpayCallbackRepository extends JpaRepository<TblVnpayCallback, UUID> {
}