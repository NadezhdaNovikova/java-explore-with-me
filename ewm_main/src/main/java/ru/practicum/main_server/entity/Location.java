package ru.practicum.main_server.entity;

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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id", nullable = false)
    private Long id;

    @Column(name = "lat", nullable = false)
    private float lat;

    @Column(name = "lon", nullable = false)
    private float lon;

    @Column(name = "radius")
    private Float radius;

    @Column(name = "location_name")
    private String name;

    @Column(name = "location_description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private Type type;

    @Column(name = "distance")
    private Float distance;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return id.equals(location.id) &&
                Objects.equals(name, location.name) &&
                Objects.equals(lat, location.lat) &&
                Objects.equals(lon, location.lon) &&
                Objects.equals(radius, location.radius);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, lat, lon, radius);
    }
}
