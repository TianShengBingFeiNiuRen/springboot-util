package com.andon.springbootutil.repository;

import com.andon.springbootutil.entity.H2Table;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Andon
 * 2022/10/26
 */
public interface H2TableRepository extends JpaRepository<H2Table, String> {
}
