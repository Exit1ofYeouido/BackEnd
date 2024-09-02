package com.example.Reward.Common.Repository;

import com.example.Reward.Common.Entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event,Long> {

    Event findByEnterpriseNameContaining(String enterprisename);

    List<Event> findByRewardAmountGreaterThanEqualAndContentId(Long rewardAmount, Long contentId);

    Event findByEnterpriseNameContainingAndContentId(String name, Long contentId);

    List<Event> findByRewardAmountLessThan(Long rewardAmount);

    List<Event> findByRewardAmountGreaterThanEqual(Long rewardAmount);
}
