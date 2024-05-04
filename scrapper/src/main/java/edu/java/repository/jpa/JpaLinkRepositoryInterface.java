package edu.java.repository.jpa;

import edu.java.dto.entity.Link;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface JpaLinkRepositoryInterface extends JpaRepository<Link, Long> {

    Optional<Link> findByUrl(String url);

    @Query("select link from Link link where link.checkedAt < :time")
    List<Link> getLinksToUpdate(OffsetDateTime time);

    @Modifying
    @Query("update Link set lastUpdated = :lastUpdated where url = :url")
    void updateLastUpdatedAt(String url, OffsetDateTime lastUpdated);

    @Modifying
    @Query("update Link set checkedAt = :checkedAt where url = :url")
    void updateCheckedAt(String url, OffsetDateTime checkedAt);

    @Query("select c.id from Chat c join c.trackedLinks l where l = :link")
    List<Long> findChatIdsByLink(Link link);

    @Query("select l from Link l join l.trackingChats c where c.id = :chatId")
    List<Link> getLinksByChatId(Long chatId);
}
