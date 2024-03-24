package edu.java.dto.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "chatLinks")
@EqualsAndHashCode(of = "url")
@Table(name = "link")
public class Link {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "url", unique = true, nullable = false)
    private String url;

    @Column(name = "last_updated")
    private OffsetDateTime lastUpdated;

    @Column(name = "checked_at")
    private OffsetDateTime checkedAt;

    @OneToMany(mappedBy = "link")
    private List<ChatLink> chatLinks = new ArrayList<>();
}
