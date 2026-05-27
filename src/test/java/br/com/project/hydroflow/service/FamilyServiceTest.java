package br.com.project.hydroflow.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.project.hydroflow.domain.Cistern;
import br.com.project.hydroflow.domain.Family;
import br.com.project.hydroflow.domain.Member;
import br.com.project.hydroflow.domain.SystemSettings;
import br.com.project.hydroflow.dto.CisternDTO;
import br.com.project.hydroflow.dto.FamilyDTO;
import br.com.project.hydroflow.dto.MemberDTO;
import br.com.project.hydroflow.repository.FamilyRepository;
import jakarta.persistence.EntityNotFoundException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para FamilyService")
class FamilyServiceTest {

    @Mock
    private FamilyRepository familyRepository;

    @Mock
    private SystemSettingsService systemSettingsService;

    @InjectMocks
    private FamilyService familyService;

    private Pageable pageable;
    private Family defaultFamily;
    private SystemSettings systemSettings;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 5);

        defaultFamily = Instancio.of(Family.class)
                .set(field(Family::getId), 1L)
                .set(field(Family::getName), "Silva")
                .set(field(Family::isHasGutterSystem), true)
                .set(field(Family::getLatitude), BigDecimal.valueOf(-8))
                .set(field(Family::getLongitude), BigDecimal.valueOf(-36))
                .set(field(Family::getMembers), new ArrayList<>())
                .set(field(Family::getCisterns), new ArrayList<>())
                .create();
        defaultFamily.addMember(new Member("A", 20, false));
        defaultFamily.addMember(new Member("B", 25, false));
        defaultFamily.addCistern(new Cistern(new BigDecimal("10000"), new BigDecimal("100"), defaultFamily));
        defaultFamily.addCistern(new Cistern(new BigDecimal("10000"), new BigDecimal("50"), defaultFamily));

        systemSettings = new SystemSettings();
        systemSettings.setDailyWaterConsumption(new BigDecimal("14"));
    }

    /**
     * Copia estado da família recebida do save (nome, listas) e aplica id para simular retorno JPA.
     */
    private static Family familyWithId(Long id, Family source) {
        Family family =
                new Family(source.getName(), source.isHasGutterSystem(), source.getLatitude(), source.getLongitude());
        setField(family, "id", id);
        for (Member m : source.getMembers()) {
            family.addMember(new Member(m.getName(), m.getAge(), m.isBedridden()));
        }
        for (Cistern c : source.getCisterns()) {
            family.addCistern(new Cistern(c.getCapacityLiters(), c.getCurrentLevelLiters(), family));
        }
        return family;
    }

    private static void setField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    @Nested
    @DisplayName("getFamilyById")
    class GetFamilyById {

        @Test
        @DisplayName("deve retornar família quando existir")
        void testReturnFamilyWhenExists() {
            when(familyRepository.findById(1L)).thenReturn(Optional.of(defaultFamily));

            Family result = familyService.getFamilyById(1L);

            assertThat(result).isSameAs(defaultFamily);
        }

        @Test
        @DisplayName("deve lançar EntityNotFoundException quando id não existir")
        void testThrowEntityNotFoundExceptionWhenIdDoesNotExist() {
            when(familyRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> familyService.getFamilyById(99L))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("99");
        }
    }

    @Nested
    @DisplayName("saveFamily")
    class SaveFamily {

        @Test
        @DisplayName("deve persistir família com membros e cisternas e retornar DTO")
        void testPersistFamilyWithMembersAndCisternsAndReturnDto() {
            FamilyDTO input = Instancio.of(FamilyDTO.class)
                    .set(field(FamilyDTO::id), null)
                    .set(field(FamilyDTO::name), "Família Souza")
                    .set(field(FamilyDTO::hasGutterSystem), false)
                    .set(field(FamilyDTO::familyStatus), Family.FamilyStatus.NORMAL)
                    .set(field(FamilyDTO::members), List.of(new MemberDTO(null, "Ana", 32, false)))
                    .set(field(FamilyDTO::cisterns), List.of(new CisternDTO(null, new BigDecimal("5000"), new BigDecimal("2000"))))
                    .create();

            when(familyRepository.save(any(Family.class)))
                    .thenAnswer(invocation -> familyWithId(10L, invocation.getArgument(0)));

            FamilyDTO result = familyService.saveFamily(input);

            assertThat(result.id()).isEqualTo(10L);
            assertThat(result.name()).isEqualTo("Família Souza");
            assertThat(result.hasGutterSystem()).isFalse();
            assertThat(result.members()).hasSize(1);
            assertThat(result.members().getFirst().name()).isEqualTo("Ana");
            assertThat(result.cisterns()).hasSize(1);
            assertThat(result.cisterns().getFirst().capacityLiters()).isEqualByComparingTo("5000");

            ArgumentCaptor<Family> captor = ArgumentCaptor.forClass(Family.class);
            verify(familyRepository).save(captor.capture());
            Family saved = captor.getValue();
            assertThat(saved.getMembers()).hasSize(1);
            assertThat(saved.getCisterns()).hasSize(1);
            assertThat(saved.getCisterns().getFirst().getFamily()).isSameAs(saved);
        }
    }

    @Nested
    @DisplayName("updateFamily")
    class UpdateFamily {

        private Family existingFamily;
        private FamilyDTO input;

        @BeforeEach
        void setUp() {
            existingFamily = Instancio.of(Family.class)
                    .set(field(Family::getId), 2L)
                    .set(field(Family::getName), "Antigo")
                    .set(field(Family::isHasGutterSystem), true)
                    .create();
            existingFamily.addMember(new Member("Velho", 60, false));
            existingFamily.addCistern(new Cistern(new BigDecimal("1000"), new BigDecimal("100"), existingFamily));

            input = Instancio.of(FamilyDTO.class)
                    .set(field(FamilyDTO::id), null)
                    .set(field(FamilyDTO::name), "Novo Nome")
                    .set(field(FamilyDTO::hasGutterSystem), false)
                    .set(field(FamilyDTO::latitude), BigDecimal.ONE)
                    .set(field(FamilyDTO::longitude), BigDecimal.TEN)
                    .set(field(FamilyDTO::familyStatus), Family.FamilyStatus.NORMAL)
                    .set(field(FamilyDTO::members), List.of(new MemberDTO(null, "Novo", 10, false)))
                    .set(field(FamilyDTO::cisterns), List.of(new CisternDTO(null, new BigDecimal("2000"), new BigDecimal("500"))))
                    .create();
        }

        @Test
        @DisplayName("deve substituir dados, membros e cisternas")
        void testReplaceDataMembersAndCisterns() {
            when(familyRepository.findById(2L)).thenReturn(Optional.of(existingFamily));
            when(familyRepository.save(existingFamily)).thenReturn(existingFamily);

            FamilyDTO result = familyService.updateFamily(2L, input);

            assertThat(result.name()).isEqualTo("Novo Nome");
            assertThat(existingFamily.getName()).isEqualTo("Novo Nome");
            assertThat(existingFamily.getMembers()).hasSize(1);
            assertThat(existingFamily.getMembers().getFirst().getName()).isEqualTo("Novo");
            assertThat(existingFamily.getCisterns()).hasSize(1);
            assertThat(existingFamily.getCisterns().getFirst().getCapacityLiters()).isEqualByComparingTo("2000");
            verify(familyRepository).save(existingFamily);
        }

        @Test
        @DisplayName("deve lançar EntityNotFoundException quando id não existir")
        void testThrowEntityNotFoundExceptionWhenIdDoesNotExist() {
            when(familyRepository.findById(404L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> familyService.updateFamily(404L, input))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("404");
        }
    }

    @Nested
    @DisplayName("findFamilyById")
    class FindFamilyById {

        @Test
        @DisplayName("deve calcular consumo diário, dias restantes e data prevista")
        void testCalculateDailyConsumptionRemainingDaysAndNextDeliveryDate() {
            when(familyRepository.findById(1L)).thenReturn(Optional.of(defaultFamily));
            when(systemSettingsService.getSystemSettings()).thenReturn(systemSettings);

            FamilyDTO result = familyService.findFamilyById(1L);

            assertThat(result.dailyConsumption()).isEqualByComparingTo("28");
            assertThat(result.remainingDays()).isEqualTo(5);
            assertThat(result.nextDeliveryDate()).isEqualTo(LocalDate.now().plusDays(5));
        }
    }

    @Nested
    @DisplayName("consultas paginadas")
    class ConsultasPaginadas {

        @Test
        @DisplayName("findAllFamilies deve mapear página para FamilyDTO")
        void testFindAllFamiliesMapPageToFamilyDto() {
            when(familyRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(defaultFamily)));

            Page<FamilyDTO> page = familyService.findAllFamilies(pageable);

            assertThat(page.getContent()).hasSize(1);
            assertThat(page.getContent().getFirst().id()).isEqualTo(1L);
            assertThat(page.getContent().getFirst().name()).isEqualTo("Silva");
        }

        @Test
        @DisplayName("findFamiliesByName deve delegar ao repositório")
        void testFindFamiliesByNameDelegateToRepository() {
            when(familyRepository.findByNameContainingIgnoreCase(eq("sil"), eq(pageable)))
                    .thenReturn(Page.empty());

            Page<FamilyDTO> page = familyService.findFamiliesByName("sil", pageable);

            assertThat(page).isEmpty();
            verify(familyRepository).findByNameContainingIgnoreCase("sil", pageable);
        }

        @Test
        @DisplayName("findFamiliesByStatus deve delegar ao repositório")
        void testFindFamiliesByStatusDelegateToRepository() {
            when(familyRepository.findByFamilyStatus(Family.FamilyStatus.URGENT, pageable))
                    .thenReturn(Page.empty());

            Page<FamilyDTO> page = familyService.findFamiliesByStatus(Family.FamilyStatus.URGENT, pageable);

            assertThat(page).isEmpty();
            verify(familyRepository).findByFamilyStatus(Family.FamilyStatus.URGENT, pageable);
        }

        @Test
        @DisplayName("findAllOrderByCisternLevelAsc deve delegar ao repositório")
        void testFindAllOrderByCisternLevelAscDelegateToRepository() {
            when(familyRepository.findAllOrderByCisternLevelAsc(pageable)).thenReturn(Page.empty());

            assertThat(familyService.findAllOrderByCisternLevelAsc(pageable)).isEmpty();
            verify(familyRepository).findAllOrderByCisternLevelAsc(pageable);
        }

        @Test
        @DisplayName("findAllOrderByCisternLevelDesc deve delegar ao repositório")
        void testFindAllOrderByCisternLevelDescDelegateToRepository() {
            when(familyRepository.findAllOrderByCisternLevelDesc(pageable)).thenReturn(Page.empty());

            assertThat(familyService.findAllOrderByCisternLevelDesc(pageable)).isEmpty();
            verify(familyRepository).findAllOrderByCisternLevelDesc(pageable);
        }
    }

    @Nested
    @DisplayName("updateAllCisternLevels")
    class UpdateAllCisternLevels {

        @Test
        @DisplayName("deve consumir água das cisternas conforme consumo diário e persistir famílias")
        void testConsumeWaterFromCisternsAndPersistFamilies() {
            systemSettings.setDailyWaterConsumption(new BigDecimal("20"));
            when(systemSettingsService.getSystemSettings()).thenReturn(systemSettings);

            Family family = Instancio.of(Family.class)
                    .set(field(Family::getId), 1L)
                    .set(field(Family::getName), "F")
                    .set(field(Family::isHasGutterSystem), true)
                    .set(field(Family::getMembers), new ArrayList<>())
                    .set(field(Family::getCisterns), new ArrayList<>())
                    .create();
            family.addMember(new Member("M", 30, false));
            Cistern c = new Cistern(new BigDecimal("1000"), new BigDecimal("100"), family);
            family.addCistern(c);

            when(familyRepository.findAll()).thenReturn(List.of(family));

            familyService.updateAllCisternLevels();

            assertThat(c.getCurrentLevelLiters()).isEqualByComparingTo("80");
            verify(familyRepository).saveAll(List.of(family));
        }
    }

    @Nested
    @DisplayName("calculateRemainingDays")
    class CalculateRemainingDays {

        @ParameterizedTest(name = "nível {0}L com consumo diário {1}L deve retornar {2} dias")
        @CsvSource({"100, 30, 3", "500, 10, 50", "999, 100, 9"})
        @DisplayName("deve calcular dias restantes conforme nível e consumo diário")
        void testCalculateRemainingDays(String currentLevel, String dailyConsumption, int expectedDays) {
            int days = familyService.calculateRemainingDays(
                    new BigDecimal(currentLevel), new BigDecimal(dailyConsumption));

            assertThat(days).isEqualTo(expectedDays);
        }

        @Test
        @DisplayName("deve lançar IllegalStateException quando consumo diário for zero")
        void testThrowIllegalStateExceptionWhenDailyConsumptionIsZero() {
            assertThatThrownBy(() -> familyService.calculateRemainingDays(
                    new BigDecimal("500"), BigDecimal.ZERO))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("Daily consumption must be greater than zero");
        }
    }


}
