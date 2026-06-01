package br.com.project.hydroflow.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.project.hydroflow.domain.Cistern;
import br.com.project.hydroflow.domain.Family;
import br.com.project.hydroflow.domain.Member;
import br.com.project.hydroflow.domain.SystemSettings;
import br.com.project.hydroflow.domain.WaterDelivery;
import br.com.project.hydroflow.dto.WaterDeliveryDTO;
import br.com.project.hydroflow.repository.WaterDeliveryRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para WaterDeliveryService")
class WaterDeliveryServiceTest {

    @Mock
    private WaterDeliveryRepository waterDeliveryRepository;

    @Mock
    private FamilyService familyService;

    @Mock
    private CisternService cisternService;

    @Mock
    private SystemSettingsService systemSettingsService;

    @InjectMocks
    private WaterDeliveryService waterDeliveryService;

    private static final long FAMILY_ID = 1L;

    private SystemSettings defaultSettings;
    private Family defaultFamily;
    private WaterDeliveryDTO defaultSaveInput;

    @BeforeEach
    void setUp() {
        defaultSettings = SystemSettings.builder()
                .id(1L)
                .dailyWaterConsumption(new BigDecimal("2"))
                .build();

        Cistern defaultCistern = Cistern.builder()
                .id(10L)
                .capacityLiters(new BigDecimal("500"))
                .currentLevelLiters(new BigDecimal("0"))
                .build();

        defaultFamily = Family.builder()
                .id(FAMILY_ID)
                .members(List.of(new Member(), new Member()))
                .cisterns(List.of(defaultCistern))
                .build();

        defaultSaveInput =
                new WaterDeliveryDTO(null, LocalDate.now(), new BigDecimal("1000"), new BigDecimal("500"), FAMILY_ID);
    }

    @Nested
    @DisplayName("saveWaterDelivery")
    class SaveWaterDelivery {

        @Test
        @DisplayName("deve registrar entrega de água")
        void testSaveWaterDelivery() {
            // dailyConsumption = 2 * 2 membros = 4L/dia
            var savedEntity = WaterDelivery.builder()
                    .id(100L)
                    .family(defaultFamily)
                    .deliveryDate(defaultSaveInput.deliveryDate())
                    .requestedAmountLiters(new BigDecimal("1000"))
                    .deliveredAmountLiters(new BigDecimal("500"))
                    .build();

            when(systemSettingsService.getSystemSettings()).thenReturn(defaultSettings);
            when(familyService.getFamilyById(FAMILY_ID)).thenReturn(defaultFamily);
            when(cisternService.fillCisterns(defaultFamily, new BigDecimal("500"), new BigDecimal("4")))
                    .thenReturn(BigDecimal.ZERO);
            when(waterDeliveryRepository.save(any())).thenReturn(savedEntity);

            WaterDeliveryDTO result = waterDeliveryService.saveWaterDelivery(defaultSaveInput);

            assertThat(result.id()).isEqualTo(100L);
            assertThat(result.familyId()).isEqualTo(FAMILY_ID);
            assertThat(result.deliveryDate()).isEqualTo(defaultSaveInput.deliveryDate());
            assertThat(result.requestedAmountLiters()).isEqualByComparingTo("1000");
            assertThat(result.deliveredAmountLiters()).isEqualByComparingTo("500");
            verify(familyService).save(defaultFamily);
            verify(waterDeliveryRepository).save(any());
        }

        @Test
        @DisplayName("deve calcular água entregue descontando o restante")
        void testCalculatesDeliveredAmount() {
            // cisternas cheias no meio: sobram 100L
            var savedEntity = WaterDelivery.builder()
                    .id(1L)
                    .family(defaultFamily)
                    .deliveryDate(defaultSaveInput.deliveryDate())
                    .requestedAmountLiters(new BigDecimal("1000"))
                    .deliveredAmountLiters(new BigDecimal("400"))
                    .build();

            when(systemSettingsService.getSystemSettings()).thenReturn(defaultSettings);
            when(familyService.getFamilyById(FAMILY_ID)).thenReturn(defaultFamily);
            when(cisternService.fillCisterns(any(), any(), any())).thenReturn(new BigDecimal("100"));
            when(waterDeliveryRepository.save(any())).thenReturn(savedEntity);

            WaterDeliveryDTO result = waterDeliveryService.saveWaterDelivery(defaultSaveInput);

            // 500 entregues - 100 restantes = 400 efetivamente distribuídos
            assertThat(result.deliveredAmountLiters()).isEqualByComparingTo("400");
        }

        @Nested
        @DisplayName("findByYearAndFamilyId")
        class FindByYearAndFamilyId {

            @Test
            @DisplayName("deve retornar lista de entregas do ano")
            void testReturnsDeliveries() {
                var delivery = WaterDelivery.builder()
                        .id(1L)
                        .family(defaultFamily)
                        .deliveryDate(LocalDate.of(2024, 3, 10))
                        .requestedAmountLiters(new BigDecimal("1000"))
                        .deliveredAmountLiters(new BigDecimal("500"))
                        .build();

                when(waterDeliveryRepository.findByYearAndFamilyId(2024, FAMILY_ID))
                        .thenReturn(List.of(delivery));

                List<WaterDeliveryDTO> result = waterDeliveryService.findByYearAndFamilyId(2024, FAMILY_ID);

                assertThat(result).hasSize(1);
                assertThat(result.getFirst().id()).isEqualTo(1L);
                assertThat(result.getFirst().deliveredAmountLiters()).isEqualByComparingTo("500");
            }

            @Test
            @DisplayName("deve retornar lista vazia quando não há entregas")
            void testReturnsEmptyList() {
                when(waterDeliveryRepository.findByYearAndFamilyId(2024, FAMILY_ID))
                        .thenReturn(List.of());

                List<WaterDeliveryDTO> result = waterDeliveryService.findByYearAndFamilyId(2024, FAMILY_ID);

                assertThat(result).isEmpty();
            }
        }
    }
}
