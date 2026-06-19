package br.com.project.hydroflow.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.project.hydroflow.domain.Family.FamilyStatus;
import br.com.project.hydroflow.dto.CisternDTO;
import br.com.project.hydroflow.dto.FamilyDTO;
import br.com.project.hydroflow.dto.MemberDTO;
import br.com.project.hydroflow.security.JwtService;
import br.com.project.hydroflow.security.UserDetailsServiceImpl;
import br.com.project.hydroflow.service.FamilyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = FamilyController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("Contrato HTTP — FamilyController")
class FamilyControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private FamilyService familyService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;

    @Test
    @DisplayName("POST /hf/families cria uma nova família")
    void createFamily() throws Exception {
        List<MemberDTO> members = List.of(new MemberDTO(1L, "Membro", 30, false));
        List<CisternDTO> cisterns = List.of(new CisternDTO(1L, BigDecimal.valueOf(1000.0), BigDecimal.ZERO));
        FamilyDTO request = new FamilyDTO(
                null,
                "Familia Teste",
                true,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                FamilyStatus.NORMAL,
                members,
                cisterns,
                null,
                null,
                null);
        FamilyDTO response = new FamilyDTO(
                1L,
                "Familia Teste",
                true,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                FamilyStatus.NORMAL,
                members,
                cisterns,
                null,
                null,
                null);

        when(familyService.saveFamily(any(FamilyDTO.class))).thenReturn(response);

        mockMvc.perform(post("/hf/families")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Familia Teste"));
    }

    @Test
    @DisplayName("PUT /hf/families/{id} atualiza uma família")
    void updateFamily() throws Exception {
        List<MemberDTO> members = List.of(new MemberDTO(1L, "Membro", 30, false));
        List<CisternDTO> cisterns = List.of(new CisternDTO(1L, BigDecimal.valueOf(1000.0), BigDecimal.ZERO));
        FamilyDTO request = new FamilyDTO(
                1L,
                "Familia Atualizada",
                true,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                FamilyStatus.NORMAL,
                members,
                cisterns,
                null,
                null,
                null);

        when(familyService.updateFamily(eq(1L), any(FamilyDTO.class))).thenReturn(request);

        mockMvc.perform(put("/hf/families/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Familia Atualizada"));
    }

    @Test
    @DisplayName("GET /hf/families/{id} busca uma família pelo ID")
    void findFamilyById() throws Exception {
        FamilyDTO response = new FamilyDTO(
                1L,
                "Familia Teste",
                true,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                FamilyStatus.NORMAL,
                List.of(),
                List.of(),
                null,
                null,
                null);

        when(familyService.findFamilyById(1L)).thenReturn(response);

        mockMvc.perform(get("/hf/families/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Familia Teste"));
    }

    @Test
    @DisplayName("GET /hf/families lista todas as famílias com paginação")
    void findAllFamilies() throws Exception {
        FamilyDTO family = new FamilyDTO(
                1L,
                "Familia Teste",
                true,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                FamilyStatus.NORMAL,
                List.of(),
                List.of(),
                null,
                null,
                null);
        Page<FamilyDTO> page = new PageImpl<>(List.of(family));

        when(familyService.findAllFamilies(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/hf/families"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Familia Teste"));
    }

    @Test
    @DisplayName("GET /hf/families filtra pelo nome")
    void findFamiliesByName() throws Exception {
        FamilyDTO family = new FamilyDTO(
                1L,
                "Familia Filtro",
                true,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                FamilyStatus.NORMAL,
                List.of(),
                List.of(),
                null,
                null,
                null);
        Page<FamilyDTO> page = new PageImpl<>(List.of(family));

        when(familyService.findFamiliesByName(eq("Filtro"), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/hf/families").param("name", "Filtro"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Familia Filtro"));
    }

    @Test
    @DisplayName("GET /hf/families filtra por status")
    void findFamiliesByStatus() throws Exception {
        FamilyDTO family = new FamilyDTO(
                1L,
                "Familia URGENTE",
                true,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                FamilyStatus.URGENT,
                List.of(),
                List.of(),
                null,
                null,
                null);
        Page<FamilyDTO> page = new PageImpl<>(List.of(family));

        when(familyService.findFamiliesByStatus(eq(FamilyStatus.URGENT), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/hf/families").param("status", "URGENT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].familyStatus").value("URGENT"));
    }
}
