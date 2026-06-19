package br.com.project.hydroflow.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import br.com.project.hydroflow.domain.Cistern;
import br.com.project.hydroflow.domain.Family;
import br.com.project.hydroflow.repository.CisternRepository;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("CisternService Tests")
class CisternServiceTest {

    @Mock
    private CisternRepository cisternRepository;

    @InjectMocks
    private CisternService cisternService;

    @Test
    @DisplayName("fillCisterns deve encher cisternas parcialmente e retornar resto zero")
    void fillCisterns() {
        Family family = Family.builder().id(1L).build();
        Cistern cistern = Cistern.builder()
                .capacityLiters(BigDecimal.valueOf(1000))
                .currentLevelLiters(BigDecimal.valueOf(500))
                .build();
        family.addCistern(cistern);

        BigDecimal waterAmount = BigDecimal.valueOf(300);
        BigDecimal dailyConsumption = BigDecimal.valueOf(10);

        BigDecimal remainingWater = cisternService.fillCisterns(family, waterAmount, dailyConsumption);

        assertEquals(BigDecimal.valueOf(0), remainingWater);
        assertEquals(BigDecimal.valueOf(800), cistern.getCurrentLevelLiters());
        verify(cisternRepository, times(1)).saveAll(family.getCisterns());
    }

    @Test
    @DisplayName("consumeDailyWater deve reduzir o nivel das cisternas")
    void consumeDailyWater() {
        Family family = Family.builder().id(1L).build();
        Cistern cistern = Cistern.builder()
                .capacityLiters(BigDecimal.valueOf(1000))
                .currentLevelLiters(BigDecimal.valueOf(500))
                .build();
        family.addCistern(cistern);

        BigDecimal dailyConsumption = BigDecimal.valueOf(50);

        cisternService.consumeDailyWater(family, dailyConsumption);

        assertEquals(BigDecimal.valueOf(450), cistern.getCurrentLevelLiters());
    }

    @Test
    @DisplayName("calculateRemainingDays deve calcular corretamente e arredondar para baixo")
    void calculateRemainingDays() {
        BigDecimal waterAmount = BigDecimal.valueOf(100);
        BigDecimal dailyConsumption = BigDecimal.valueOf(30);

        int days = cisternService.calculateRemainingDays(waterAmount, dailyConsumption);

        assertEquals(3, days);
    }

    @Test
    @DisplayName("calculateRemainingDays deve lancar excecao se consumo diario for zero ou negativo")
    void calculateRemainingDaysThrowsException() {
        BigDecimal waterAmount = BigDecimal.valueOf(100);
        BigDecimal dailyConsumption = BigDecimal.ZERO;

        assertThrows(IllegalStateException.class, () -> cisternService.calculateRemainingDays(waterAmount, dailyConsumption));
    }
}
