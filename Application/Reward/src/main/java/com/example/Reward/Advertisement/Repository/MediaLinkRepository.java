package com.example.Reward.Advertisement.Repository;

import com.example.Reward.Advertisement.Entity.MediaLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MediaLinkRepository extends JpaRepository<MediaLink,Long> {

    @Query(nativeQuery = true,value="SELECT * FROM Media_link as m where m.enterprise_name NOT IN (:enterprises) AND m.id NOT IN (:medialinks)")
    List<MediaLink> findmedialink(@Param("medialinks") List<Long> mediaLinkIdList,@Param("enterprises") List<String> enterpriseNoneList);
}

