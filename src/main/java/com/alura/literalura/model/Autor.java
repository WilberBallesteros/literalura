package com.alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "autores")
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String nombre;
    private String fechaDeNacimiento;
    private String fechaDeFallecimiento;
    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Libro> libros = new ArrayList<>();

    public Autor() {

    }

    public Autor(DatosAutor datosAutor) {
        this.nombre = datosAutor.nombre();
        fechaDeNacimiento = datosAutor.fechaDeNacimiento();
        fechaDeFallecimiento = datosAutor.fechaDeFallecimiento();

    }

    //get y set

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    public void setFechaDeNacimiento(String fechaDeNacimiento) {
        this.fechaDeNacimiento = fechaDeNacimiento;
    }

    public String getFechaDeFallecimiento() {
        return fechaDeFallecimiento;
    }

    public void setFechaDeFallecimiento(String fechaDeFallecimiento) {
        this.fechaDeFallecimiento = fechaDeFallecimiento;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("----- AUTOR -----\n");
        sb.append("Nombre: ").append(nombre).append("\n");
        sb.append("Fecha de Nacimiento: ").append(fechaDeNacimiento).append("\n");
        sb.append("Fecha de Fallecimiento: ").append(fechaDeFallecimiento).append("\n");
        sb.append("Libros:\n");
        //recorro los libros de este autor, mostrandolos tabulados
        for (Libro libro : libros) {
            sb.append("\t- ").append(libro.getTitulo()).append("\n");
        }
        sb.append("------------------\n"); // Espacio en blanco adicional al final
        return sb.toString();
    }
}
