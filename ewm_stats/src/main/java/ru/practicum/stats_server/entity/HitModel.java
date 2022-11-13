package ru.practicum.stats_server.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hits")
public class HitModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hits_id")
    private Long id;

    @Column(name = "app", length = 50, nullable = false)
    @Size(max = 50)
    private String app;

    @Column(name = "uri", length = 254)
    @Size(max = 254)
    private String uri;

    @Column(name = "ip", length = 50, nullable = false)
    @Size(max = 50)
    private String ip;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;
}
