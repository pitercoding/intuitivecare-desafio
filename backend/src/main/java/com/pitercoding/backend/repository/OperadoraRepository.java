package com.pitercoding.backend.repository;

import com.pitercoding.backend.domain.Operadora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperadoraRepository extends JpaRepository<Operadora, String> {
}
