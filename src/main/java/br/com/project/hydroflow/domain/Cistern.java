package br.com.project.hydroflow.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "hf_cistern")
public class Cistern {

    @Id
    @SequenceGenerator(name = "hf_cistern_id", sequenceName = "hf_cistern_id", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hf_cistern_id")
    private Long id;

    @Column(name = "cistern_capacity_liters", nullable = false)
    private BigDecimal capacityLiters;

    @Column(name = "cistern_current_level_liters", nullable = false)
    private BigDecimal currentLevelLiters = BigDecimal.ZERO;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_family", nullable = false)
    private Family family;

    public Cistern() {}

    public Cistern(BigDecimal capacityLiters, BigDecimal currentLevelLiters, Family family) {
        this.capacityLiters = capacityLiters;
        this.currentLevelLiters = currentLevelLiters.max(BigDecimal.ZERO).min(capacityLiters);
        this.family = family;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getCapacityLiters() {
        return capacityLiters;
    }

    public BigDecimal getCurrentLevelLiters() {
        return currentLevelLiters;
    }

    public void setFamily(Family family) {
        this.family = family;
    }

    public Family getFamily() {
        return family;
    }

    public void updateLevel(BigDecimal newLevel, int remainingDays) {
        this.currentLevelLiters = newLevel.min(this.capacityLiters);
        this.family.updateStatus(remainingDays);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private BigDecimal capacityLiters;
        private BigDecimal currentLevelLiters = BigDecimal.ZERO;
        private Family family;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder capacityLiters(BigDecimal capacityLiters) {
            this.capacityLiters = capacityLiters;
            return this;
        }

        public Builder currentLevelLiters(BigDecimal currentLevelLiters) {
            this.currentLevelLiters = currentLevelLiters;
            return this;
        }

        public Builder family(Family family) {
            this.family = family;
            return this;
        }

        public Cistern build() {
            Cistern cistern = new Cistern();
            cistern.id = this.id;
            cistern.capacityLiters = this.capacityLiters;
            cistern.currentLevelLiters =
                    this.currentLevelLiters.max(BigDecimal.ZERO).min(this.capacityLiters);
            cistern.family = this.family;
            return cistern;
        }
    }
}
