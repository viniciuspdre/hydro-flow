package br.com.project.hydroflow.repository;

import br.com.project.hydroflow.domain.Cistern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CisternRepository extends JpaRepository<Cistern, Long> {}
