package edu.java.dto.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "url")
@Table(name = "link")
public class Link {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "last_updated")
    private OffsetDateTime lastUpdated;

    @Column(name = "checked_at")
    private OffsetDateTime checkedAt;

    @ManyToMany(
        fetch = FetchType.LAZY,
        mappedBy = "trackedLinks"
    )
    private Set<Chat> trackingChats;

    public Link(String url) {
        this.url = url;
        this.lastUpdated = OffsetDateTime.now();
        this.checkedAt = OffsetDateTime.now().minusDays(1);
        this.trackingChats = new HashSet<>();
    }
}
