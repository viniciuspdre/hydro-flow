package br.com.project.hydroflow.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "hf_system_settings")
public class SystemSettings {

    @Id
    private Long id;

    @Column(name = "daily_water_consumption", nullable = false)
    private BigDecimal dailyWaterConsumption;

    public SystemSettings() {}

    public Long getId() {
        return id;
    }

    public BigDecimal getDailyWaterConsumption() {
        return dailyWaterConsumption;
    }

    public void setDailyWaterConsumption(BigDecimal dailyWaterConsumption) {
        this.dailyWaterConsumption = dailyWaterConsumption;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private BigDecimal dailyWaterConsumption;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder dailyWaterConsumption(BigDecimal dailyWaterConsumption) {
            this.dailyWaterConsumption = dailyWaterConsumption;
            return this;
        }

        public SystemSettings build() {
            SystemSettings systemSettings = new SystemSettings();
            systemSettings.id = this.id;
            systemSettings.dailyWaterConsumption = this.dailyWaterConsumption;
            return systemSettings;
        }
    }
}
