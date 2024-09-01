package com.example.Reward.Common.Repository;

import com.example.Reward.Common.Entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventRepository extends JpaRepository<Event,Long> {

    Event findByEnterpriseNameContaining(String enterprisename);

    List<Event> findByRewardAmountGreaterThanEqualAndContentId(Long rewardAmount, Long contentId);

    Event findByEnterpriseNameContainingAndContentId(String name, Long contentId);

    List<Event> findByRewardAmountLessThanAndContentId(Long rewardAmount, Long contentId);
}
