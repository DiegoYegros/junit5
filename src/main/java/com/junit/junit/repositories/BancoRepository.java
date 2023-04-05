package com.junit.junit.repositories;

import com.junit.junit.models.Banco;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BancoRepository extends JpaRepository<Banco, Long> {
}
