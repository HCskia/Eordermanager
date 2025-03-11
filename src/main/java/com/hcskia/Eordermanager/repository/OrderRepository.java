package com.hcskia.Eordermanager.repository;
import com.hcskia.Eordermanager.pojo.OrderList;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderList, Long> {
    OrderList findByOrderId(String orderId);
    @Transactional
    void deleteByOrderId(String orderId);
    boolean existsByOrderId(String orderId);


    @Query(value = "SELECT *,DATE_FORMAT(orderDate, '%Y-%m-%d') AS orderDateConvert FROM orderlist WHERE userid = ?1 ORDER BY orderDate LIMIT ?2",nativeQuery = true)
    List<OrderList> findOrderListByOrderId(String userId,Integer columns);

    @Query(value = "SELECT COUNT(*) FROM orderlist WHERE userid =?1",nativeQuery = true)
    List<Integer> getOrderListCountByOrderId(String userId);
}