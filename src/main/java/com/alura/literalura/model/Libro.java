package com.alura.literalura.model;


import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "libros")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(unique = true)
    private String titulo;

    //private List<DatosAutor> autor;
    private String idiomas;
    private Double numeroDeDescargas;

    @ManyToOne()
    private Autor autor;

    public Libro() {

    }

    public Libro(DatosLibros datosLibros) {
        this.titulo = datosLibros.titulo();
        this.idiomas = datosLibros.idiomas().get(0);
        this.numeroDeDescargas = datosLibros.numeroDeDescargas();
        this.autor = new Autor(datosLibros.autor().get(0));
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(String idiomas) {
        this.idiomas = idiomas;
    }

    public Double getNumeroDeDescargas() {
        return numeroDeDescargas;
    }

    public void setNumeroDeDescargas(Double numeroDeDescargas) {
        this.numeroDeDescargas = numeroDeDescargas;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    //mostrar la informacion tabulada del toString
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("----- LIBRO -----\n");
        sb.append("Titulo: ").append(titulo).append("\n");
        sb.append("Autor: ").append(autor.getNombre()).append("\n");
        sb.append("Idioma: ").append(idiomas).append("\n");
        sb.append("Numero de descargas: ").append(numeroDeDescargas).append("\n");
        sb.append("------------------\n");
        return sb.toString();
    }
}
