package com.guitar.db.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.guitar.db.model.Model;

@Repository
public interface ModelJpaRepository extends JpaRepository<Model, Long> {

	@Query("select m from Model m where m.price >= :lowest and m.price <= :highest and m.woodType like :wood")
	List<Model> queryByPriceRangeAndWoodType(
			@Param("lowest") BigDecimal lowest,
			@Param("highest") BigDecimal high, @Param("wood") String wood);

}
