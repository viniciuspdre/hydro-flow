package br.com.project.hydroflow.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.project.hydroflow.domain.SystemSettings;
import br.com.project.hydroflow.dto.SystemSettingsDTO;
import br.com.project.hydroflow.repository.SystemSettingsRepository;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para SystemSettingsService")
class SystemSettingsServiceTest {

    @Mock
    private SystemSettingsRepository systemSettingsRepository;

    private SystemSettingsService systemSettingsService;

    private SystemSettings defaultSystemSettings;

    @BeforeEach
    void setUp() {
        systemSettingsService = new SystemSettingsService(systemSettingsRepository);

        defaultSystemSettings = SystemSettings.builder()
                .id(1L)
                .dailyWaterConsumption(new BigDecimal("14"))
                .build();
    }

    @Nested
    @DisplayName("getSystemSettings")
    class GetSystemSettings {

        @Test
        @DisplayName("deve retornar configurações quando existirem")
        void testReturnSettingsWhenExists() {
            when(systemSettingsRepository.findById(1L)).thenReturn(Optional.of(defaultSystemSettings));

            SystemSettings result = systemSettingsService.getSystemSettings();

            assertThat(result).isSameAs(defaultSystemSettings);
        }

        @Test
        @DisplayName("deve lançar EntityNotFoundException quando não existir registro")
        void testThrowEntityNotFoundExceptionWhenRecordDoesNotExist() {
            when(systemSettingsRepository.findById(1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> systemSettingsService.getSystemSettings())
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("Configurações não encontradas");
        }
    }

    @Nested
    @DisplayName("updateSystemSettings")
    class UpdateSystemSettings {

        private SystemSettingsDTO input;

        @BeforeEach
        void setUp() {
            input = new SystemSettingsDTO(null, new BigDecimal("25.5"));
        }

        @Test
        @DisplayName("deve atualizar consumo diário, persistir e retornar DTO")
        void testUpdateDailyConsumptionPersistAndReturnDto() {
            defaultSystemSettings.setDailyWaterConsumption(new BigDecimal("10"));
            when(systemSettingsRepository.findById(1L)).thenReturn(Optional.of(defaultSystemSettings));
            when(systemSettingsRepository.save(defaultSystemSettings)).thenReturn(defaultSystemSettings);

            SystemSettingsDTO result = systemSettingsService.updateSystemSettings(input);

            assertThat(defaultSystemSettings.getDailyWaterConsumption()).isEqualByComparingTo("25.5");
            assertThat(result.dailyWaterConsumption()).isEqualByComparingTo("25.5");
            assertThat(result.id()).isEqualTo(1L);
            verify(systemSettingsRepository).save(defaultSystemSettings);
        }

        @Test
        @DisplayName("deve lançar EntityNotFoundException quando não existir para atualização")
        void testThrowEntityNotFoundExceptionWhenRecordDoesNotExistForUpdate() {
            when(systemSettingsRepository.findById(1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> systemSettingsService.updateSystemSettings(input))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("Configurações não encontradas");

            verify(systemSettingsRepository, never()).save(any());
        }
    }
}
