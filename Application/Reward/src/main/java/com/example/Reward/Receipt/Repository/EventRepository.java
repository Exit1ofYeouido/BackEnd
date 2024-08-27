package com.example.Reward.Receipt.Repository;

import com.example.Reward.Receipt.Entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query(nativeQuery = true, value = "SELECT event_id, enterprise_name FROM event WHERE reward_amount > 1 AND content_id = 2")
    List<Event> findEventIdAndEnterpriseNameByRewardAmount();

}
