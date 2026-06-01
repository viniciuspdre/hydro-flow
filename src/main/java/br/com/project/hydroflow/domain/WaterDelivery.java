package br.com.project.hydroflow.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "hf_water_delivery")
public class WaterDelivery {

    @Id
    @SequenceGenerator(name = "hf_water_delivery_id", sequenceName = "hf_water_delivery_id", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hf_water_delivery_id")
    private Long id;

    @Column(name = "delivery_date", nullable = false)
    private LocalDate deliveryDate;

    @Column(name = "requested_amount_liters", nullable = false)
    private BigDecimal requestedAmountLiters;

    @Column(name = "delivered_amount_liters", nullable = false)
    private BigDecimal deliveredAmountLiters;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_family", nullable = false)
    private Family family;

    public WaterDelivery() {}

    public WaterDelivery(
            LocalDate deliveryDate, BigDecimal requestedAmountLiters, BigDecimal deliveredAmountLiters, Family family) {
        this.deliveryDate = deliveryDate;
        this.requestedAmountLiters = requestedAmountLiters;
        this.deliveredAmountLiters = deliveredAmountLiters;
        this.family = family;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public BigDecimal getRequestedAmountLiters() {
        return requestedAmountLiters;
    }

    public BigDecimal getDeliveredAmountLiters() {
        return deliveredAmountLiters;
    }

    public Family getFamily() {
        return family;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private LocalDate deliveryDate;
        private BigDecimal requestedAmountLiters;
        private BigDecimal deliveredAmountLiters;
        private Family family;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder deliveryDate(LocalDate deliveryDate) {
            this.deliveryDate = deliveryDate;
            return this;
        }

        public Builder requestedAmountLiters(BigDecimal requestedAmountLiters) {
            this.requestedAmountLiters = requestedAmountLiters;
            return this;
        }

        public Builder deliveredAmountLiters(BigDecimal deliveredAmountLiters) {
            this.deliveredAmountLiters = deliveredAmountLiters;
            return this;
        }

        public Builder family(Family family) {
            this.family = family;
            return this;
        }

        public WaterDelivery build() {
            WaterDelivery waterDelivery = new WaterDelivery();
            waterDelivery.id = this.id;
            waterDelivery.deliveryDate = this.deliveryDate;
            waterDelivery.requestedAmountLiters = this.requestedAmountLiters;
            waterDelivery.deliveredAmountLiters = this.deliveredAmountLiters;
            waterDelivery.family = this.family;
            return waterDelivery;
        }
    }
}
