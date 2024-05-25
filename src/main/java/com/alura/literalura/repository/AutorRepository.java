package com.alura.literalura.repository;

import com.alura.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    //busca por nombre ignorando mayusculas y minusculas
    Autor findByNombreContainsIgnoreCase(String nombre);

    //buscar autor(es) vivos por el año q escriba el usuario JPQL
    @Query("SELECT a FROM Autor a " +
            "WHERE a.fechaDeNacimiento <= :fechaProporcionada " +
            "AND a.fechaDeFallecimiento >= :fechaProporcionada")
    List<Autor> autorVivo(@Param("fechaProporcionada") String fechaProporcionada);

    //buscar autor por nombre
    @Query("SELECT a FROM Autor a WHERE LOWER(a.nombre) LIKE LOWER(concat('%', :nombre, '%'))")
    List<Autor> autorPorNombre(String nombre);
}
