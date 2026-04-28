package com.example.NoLimits.Multimedia.repository.producto;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.example.NoLimits.Multimedia.model.producto.ProductoLinkCompraModel;

public interface ProductoLinkCompraRepository extends JpaRepository<ProductoLinkCompraModel, Long> {

    List<ProductoLinkCompraModel> findByProductoId(Long productoId);

    Optional<ProductoLinkCompraModel> findByProductoIdAndPlataformaId(Long productoId, Long plataformaId);

    Optional<ProductoLinkCompraModel> findByProductoIdAndAppId(Long productoId, String appId);

    Optional<ProductoLinkCompraModel> findByAppId(String appId);

    @Transactional
    void deleteByProductoIdAndPlataformaId(Long productoId, Long plataformaId);
}