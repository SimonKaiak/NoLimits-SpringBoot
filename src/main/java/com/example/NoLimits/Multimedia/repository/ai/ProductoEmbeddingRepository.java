package com.example.NoLimits.Multimedia.repository.ai;

import com.example.NoLimits.Multimedia.model.ai.ProductoEmbeddingModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoEmbeddingRepository extends JpaRepository<ProductoEmbeddingModel, Long> {

    boolean existsByTitulo(String titulo);

}