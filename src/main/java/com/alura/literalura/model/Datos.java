package com.alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

//datos generales, todos los libros

@JsonIgnoreProperties(ignoreUnknown = true) //ignore lo q no deseo mapear
public record Datos(
        @JsonAlias("results") List<DatosLibros> resultados
) {

}
