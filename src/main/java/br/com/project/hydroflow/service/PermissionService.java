package br.com.project.hydroflow.service;

import br.com.project.hydroflow.domain.Permission;
import br.com.project.hydroflow.dto.PermissionDTO;
import br.com.project.hydroflow.repository.PermissionRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PermissionService {

    private static final Logger log = LoggerFactory.getLogger(PermissionService.class);

    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public List<PermissionDTO> findAllPermissions() {
        log.info("Listando todas as permissões");
        return permissionRepository.findAll().stream().map(PermissionDTO::from).toList();
    }

    public Permission findById(Long id) {
        log.info("Buscando permissão com id: {}", id);
        return permissionRepository.findById(id).orElseThrow(() -> {
            log.warn("Permissão não encontrada. id: {}", id);
            return new EntityNotFoundException("Permissão não encontrada: " + id);
        });
    }
}
