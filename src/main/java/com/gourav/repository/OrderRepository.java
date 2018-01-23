package com.gourav.repository;

import com.gourav.models.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by gouravsoni on 23/01/18.
 */
@Repository
public interface OrderRepository extends JpaRepository<Orders, String> {
}
