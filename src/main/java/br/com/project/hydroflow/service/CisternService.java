package br.com.project.hydroflow.service;

import br.com.project.hydroflow.domain.Cistern;
import br.com.project.hydroflow.domain.Family;
import br.com.project.hydroflow.repository.CisternRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CisternService {

    private static final Logger log = LoggerFactory.getLogger(CisternService.class);

    private final CisternRepository cisternRepository;

    public CisternService(CisternRepository cisternRepository) {
        this.cisternRepository = cisternRepository;
    }

    public BigDecimal fillCisterns(Family family, BigDecimal waterAmount, BigDecimal dailyConsumption) {
        BigDecimal remainingWater = waterAmount;

        for (Cistern cistern : family.getCisterns()) {
            if (remainingWater.compareTo(BigDecimal.ZERO) <= 0) break;

            BigDecimal availableSpace = cistern.getCapacityLiters().subtract(cistern.getCurrentLevelLiters());
            BigDecimal toFill = remainingWater.min(availableSpace);

            BigDecimal newLevel = cistern.getCurrentLevelLiters().add(toFill);
            cistern.updateLevel(newLevel, calculateRemainingDays(cistern.getCurrentLevelLiters(), dailyConsumption));

            log.info("Cisterna id: {} | Adicionado: {}L | Novo nível: {}L", cistern.getId(), toFill, newLevel);

            remainingWater = remainingWater.subtract(toFill);
        }

        cisternRepository.saveAll(family.getCisterns());
        return remainingWater;
    }

    public void consumeDailyWater(Family family, BigDecimal dailyConsumption) {
        BigDecimal remainingConsumption = dailyConsumption;

        for (Cistern cistern : family.getCisterns()) {
            if (remainingConsumption.compareTo(BigDecimal.ZERO) <= 0) break;

            BigDecimal toConsume = remainingConsumption.min(cistern.getCurrentLevelLiters());
            BigDecimal newLevel = cistern.getCurrentLevelLiters().subtract(toConsume);

            remainingConsumption = remainingConsumption.subtract(toConsume);

            int remainingDays = calculateRemainingDays(cistern.getCurrentLevelLiters(), dailyConsumption);

            log.info(
                    "Família id: {} | Cisterna id: {} | Consumido: {}L | Novo nível: {}L | Restante consumo: {}L",
                    family.getId(),
                    cistern.getId(),
                    toConsume,
                    newLevel,
                    remainingConsumption);

            cistern.updateLevel(newLevel, remainingDays);
        }
    }

    public int calculateRemainingDays(BigDecimal waterAmount, BigDecimal dailyConsumption) {
        if (dailyConsumption.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("Daily consumption must be greater than zero");
        }

        return waterAmount.divide(dailyConsumption, 0, RoundingMode.FLOOR).intValue();
    }
}
