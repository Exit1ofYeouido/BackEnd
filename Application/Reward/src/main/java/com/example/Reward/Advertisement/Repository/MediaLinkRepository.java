package com.example.Reward.Advertisement.Repository;

import com.example.Reward.Advertisement.Entity.MediaLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MediaLinkRepository extends JpaRepository<MediaLink,Long> {

    @Query("SELECT m FROM MediaLink m WHERE (:enterprises IS NULL OR m.enterpriseName NOT IN :enterprises) AND (:medialinks IS NULL OR m.id NOT IN :medialinks)")
    List<MediaLink> findmedialink(@Param("medialinks") List<Long> mediaLinkIdList,@Param("enterprises") List<String> enterpriseNoneList);
}

