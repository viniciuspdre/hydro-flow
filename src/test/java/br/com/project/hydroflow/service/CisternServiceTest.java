package br.com.project.hydroflow.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import br.com.project.hydroflow.domain.Cistern;
import br.com.project.hydroflow.domain.Family;
import br.com.project.hydroflow.repository.CisternRepository;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para CisternService")
class CisternServiceTest {

    @Mock
    private CisternRepository cisternRepository;

    @InjectMocks
    private CisternService cisternService;

    @Nested
    @DisplayName("fillCisterns")
    class FillCisterns {

        @Test
        @DisplayName("deve encher cisternas parcialmente e retornar resto zero")
        void testFillCisternsPartiallyAndReturnZero() {
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
    }

    @Nested
    @DisplayName("consumeDailyWater")
    class ConsumeDailyWater {

        @Test
        @DisplayName("deve reduzir o nivel das cisternas")
        void testReduceCisternLevel() {
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
    }

    @Nested
    @DisplayName("calculateRemainingDays")
    class CalculateRemainingDays {

        @Test
        @DisplayName("deve calcular corretamente e arredondar para baixo")
        void testCalculateRemainingDaysRoundedDown() {
            BigDecimal waterAmount = BigDecimal.valueOf(100);
            BigDecimal dailyConsumption = BigDecimal.valueOf(30);

            int days = cisternService.calculateRemainingDays(waterAmount, dailyConsumption);

            assertEquals(3, days);
        }

        @Test
        @DisplayName("deve lancar excecao se consumo diario for zero ou negativo")
        void testThrowExceptionWhenDailyConsumptionIsZeroOrNegative() {
            BigDecimal waterAmount = BigDecimal.valueOf(100);
            BigDecimal dailyConsumption = BigDecimal.ZERO;

            assertThrows(
                    IllegalStateException.class,
                    () -> cisternService.calculateRemainingDays(waterAmount, dailyConsumption));
        }
    }
}
