package com.smartlostfound.itemservice.repository;

import com.smartlostfound.itemservice.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByType(Item.ItemType type);

    List<Item> findByStatus(Item.ItemStatus status);

    List<Item> findByTypeAndStatus(
            Item.ItemType type,
            Item.ItemStatus status
    );

    List<Item> findByReportedBy(Long reportedBy);
}