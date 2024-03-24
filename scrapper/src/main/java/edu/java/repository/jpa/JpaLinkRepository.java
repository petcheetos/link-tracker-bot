package edu.java.repository.jpa;

import edu.java.dto.entity.Link;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface JpaLinkRepository extends JpaRepository<Link, Long> {

    Optional<Link> findByUrl(String url);

    @Query("select link from Link link where link.checkedAt < :time")
    List<Link> getLinksToUpdate(OffsetDateTime time);

    @Modifying
    @Query("update Link set lastUpdated = :lastUpdated where url = :url")
    void updateLastUpdatedAt(String url, OffsetDateTime lastUpdated);

    @Modifying
    @Query("update Link set checkedAt = :checkedAt where url = :url")
    void updateCheckedAt(String url, OffsetDateTime checkedAt);

    @Query("select cl.chat.id from ChatLink cl where cl.link.id = :linkId")
    List<Long> findChatIdsByLink(Long linkId);
}
