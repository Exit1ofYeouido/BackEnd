package com.example.Reward.Advertisement.Repository;

import com.example.Reward.Advertisement.Entity.CheckToday;
import com.example.Reward.Advertisement.Entity.MediaHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MediaHistoryRepository extends JpaRepository<MediaHistory,Long> {
    List<MediaHistory> findBymemberId(Long memId);

    @Query("SELECT m FROM media_history  m WHERE :memId =m.member_id AND :id=m.media_link_id")
    MediaHistory findByMemberIdAndMediaId(@Param("memId") Long memId,@Param("id") Long id);
}

