package com.ms_productos_categorias.service;


import com.ms_productos_categorias.repository.ProductoRepository;
import com.ms_productos_categorias.repository.CategoriaRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.ms_productos_categorias.dto.ProductoDTO;
import com.ms_productos_categorias.model.Categoria;
import com.ms_productos_categorias.model.Producto;

import java.util.List;
import java.util.Base64; // Importar Base64 esto sirve para convertir la imagen a String

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;


    // Listar productos filtrando por nombre de categoría
    public List<ProductoDTO> listarPorCategoria(String nombreCategoria) {
        return productoRepository.findByCategoriaNombre(nombreCategoria)
                .stream()
                .map(this::toDTO)
                .toList();
    }


    //Obtener producto por ID
    public ProductoDTO obtenerProductoPorId(Long id) {
        Producto p = productoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        return toDTO(p);
    }

    //Crear categoría (recibiendo un ProductoDTO)
    public ProductoDTO crear(ProductoDTO dto) {
        byte[] imagenBytes = null;
        if (dto.getImagenBase64()!= null) {
            imagenBytes = Base64.getDecoder().decode(dto.getImagenBase64());
        }
        Categoria categoria = categoriaRepository.findByNombre(dto.getCategoria())
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));
        
        Producto producto = Producto.builder()
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .precio(dto.getPrecio())
                .categoria(categoria)
                .stock(dto.getStock())
                .estado(dto.getEstado())
                .imagen(imagenBytes)
                .build();
        
        Producto guardado = productoRepository.save(producto);
        return toDTO(guardado);
    }


    // Convertir de model a DTO
    private ProductoDTO toDTO(Producto producto) {
        String  imagenBase64 = null;
        if (producto.getImagen() != null) {
            imagenBase64 = Base64.getEncoder().encodeToString(producto.getImagen());
        }
        return ProductoDTO.builder()
                .id(producto.getId())
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .precio(producto.getPrecio())
                .categoria(producto.getCategoria().getNombre())
                .stock(producto.getStock())
                .estado(producto.getEstado())
                .disponibilidad(calcularDisponibilidad(producto.getStock()))
                .sku(producto.getSku())
                .imagenBase64(imagenBase64)
                .build();
    }

    //Logica de disponibilidad basada en el stock
    private String calcularDisponibilidad(Integer stock) {

        if (stock == null || stock <= 0) {
            return "Sin Stock";

        } else if (stock == 1) {
            return "Ultima Unidade";

        } else if (stock <= 5) {
            return "Quedan pocas unidades ("+ stock + ")";

        }else {
            return "Hay Stock disponible";
        }
    }

    //Actualizar producto por ID
    public ProductoDTO actualizar(Long id, ProductoDTO dto) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        Categoria categoria = categoriaRepository.findByNombre(dto.getCategoria())
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));

        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setCategoria(categoria);
        producto.setStock(dto.getStock());
        producto.setEstado(dto.getEstado());

        if (dto.getImagenBase64() != null) {
            producto.setImagen(Base64.getDecoder().decode(dto.getImagenBase64()));
        }

        Producto actualizado = productoRepository.save(producto);

        return toDTO(actualizado);
    }

    //Eliminar producto por ID
    public void eliminar(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new IllegalArgumentException("Producto no encontrado");
        }
        productoRepository.deleteById(id);
    }

    // Listar todos los productos
    public List<ProductoDTO> listarProductos() {
        return productoRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    // Actualizar solo el stock de un producto (sobre carga usada por el controller)
    public ProductoDTO actualizar(Long id, Integer stock) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        producto.setStock(stock);
        Producto actualizado = productoRepository.save(producto);

        return toDTO(actualizado);
    }
}