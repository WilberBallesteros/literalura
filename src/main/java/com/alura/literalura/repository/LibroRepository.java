package com.alura.literalura.repository;

import com.alura.literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    //libros por idioma
    @Query("SELECT l FROM Libro l WHERE l.idiomas = :idioma")
    List<Libro> idiomalibros(String idioma);

    //top 10 libros
    List<Libro> findTop10ByOrderByNumeroDeDescargasDesc();

}
