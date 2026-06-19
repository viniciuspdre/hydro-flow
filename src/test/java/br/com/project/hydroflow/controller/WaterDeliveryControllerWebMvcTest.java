package br.com.project.hydroflow.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.project.hydroflow.dto.WaterDeliveryDTO;
import br.com.project.hydroflow.security.JwtService;
import br.com.project.hydroflow.security.UserDetailsServiceImpl;
import br.com.project.hydroflow.service.WaterDeliveryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = WaterDeliveryController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("Testes para WaterDeliveryController")
class WaterDeliveryControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper =
            new ObjectMapper().registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

    @MockitoBean
    private WaterDeliveryService waterDeliveryService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;

    @Nested
    @DisplayName("save")
    class Save {

        @Test
        @DisplayName("deve salvar entrega")
        void testSaveDelivery() throws Exception {
            WaterDeliveryDTO request = new WaterDeliveryDTO(
                    null, LocalDate.of(2024, 3, 15), BigDecimal.valueOf(1000.0), BigDecimal.valueOf(1000.0), 1L);
            WaterDeliveryDTO response = new WaterDeliveryDTO(
                    1L, LocalDate.of(2024, 3, 15), BigDecimal.valueOf(1000.0), BigDecimal.valueOf(1000.0), 1L);

            when(waterDeliveryService.saveWaterDelivery(any(WaterDeliveryDTO.class)))
                    .thenReturn(response);

            mockMvc.perform(post("/hf/water-deliveries")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(1L));
        }
    }

    @Nested
    @DisplayName("findByYearAndFamilyId")
    class FindByYearAndFamilyId {

        @Test
        @DisplayName("deve buscar por ano e familia")
        void testFindByYearAndFamilyId() throws Exception {
            WaterDeliveryDTO response = new WaterDeliveryDTO(
                    1L, LocalDate.of(2024, 3, 15), BigDecimal.valueOf(1000.0), BigDecimal.valueOf(1000.0), 1L);

            when(waterDeliveryService.findByYearAndFamilyId(2024, 1L)).thenReturn(List.of(response));

            mockMvc.perform(get("/hf/water-deliveries/year/2024/family/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].deliveredAmountLiters").value(1000.0));
        }
    }
}
