package com.alura.literalura.principal;

import com.alura.literalura.model.Autor;
import com.alura.literalura.model.Datos;
import com.alura.literalura.model.Libro;
import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.repository.LibroRepository;
import com.alura.literalura.service.ConsumoAPI;
import com.alura.literalura.service.ConvierteDatos;


import java.util.*;
import java.util.stream.Collectors;


public class Principal {

    private static final String URL_BASE = "https://gutendex.com/books/";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private Scanner teclado = new Scanner(System.in);
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;

    private List<Libro> libros;
    private List<Autor> autores;

    //constructores
    public Principal() {
    }

    public Principal(LibroRepository repository, AutorRepository autorRepository) {
        this.libroRepository = repository;
        this.autorRepository = autorRepository;
    }

    public void muestraElMenu() {

        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Buscar libro por titulo
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idioma
                    6 - Top 10 mejores libros
                    7 - Generando estadisticas de libros en BD
                    8 - Buscar autor por nombre
                                        
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    listarLibrosRegistrados();
                    break;
                case 3:
                    listarAutoresRegistrados();
                    break;
                case 4:
                    listarAutoresVivosPorAnio();
                    break;
                case 5:
                    listarLibrosPorIdioma();
                    break;
                case 6:
                    listarTop10Libros();
                    break;
                case 7:
                    generandoEstaisticas();
                    break;
                case 8:
                    buscarAutorPorNombre();
                    break;


                case 0:
                    System.out.println("Cerrando la aplicación, gracias por preferirnos...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }
    }
    //opcion 1
    private void buscarLibroPorTitulo() {
        System.out.println("Ingrese el nombre del libro que desea buscar: ");
        String tituloLibro = teclado.nextLine();

        //llamada a la api por titulo
        var json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + tituloLibro.replace(" ", "+"));
        Datos datosApi = conversor.obtenerDatos(json, Datos.class); //convierte datos json a  objetos java

        Optional<Libro> libroBuscado = datosApi.resultados().stream()
                .map(Libro::new)
                .findFirst(); //busca el primer libro

        //si encuentra el libro busca el autor en la BD
        if (libroBuscado.isPresent()) {
            Autor autor = autorRepository.findByNombreContainsIgnoreCase(libroBuscado.get().getAutor().getNombre());

            if (autor == null) {
                //si el autor no existe lo crea con la info q encontro en el libro buscado
                Autor autorNuevo = libroBuscado.get().getAutor();

                //me retorna el autor con el id, entonces si paso el autor al libro con el id, al guardarlo no me duplicara el autor!!!!
                //guarda los datos del autor en la BD
                autor = autorRepository.save(autorNuevo);
            }

            //e establece el autor del libro encontrado
            // (ya sea el existente en la base de datos o el recién creado) y se guarda el libro
            // en la base de datos
            Libro libro = libroBuscado.get();

            try {
                libro.setAutor(autor);
                libroRepository.save(libro);
                System.out.println(libro);
            } catch (Exception ex) { //DataIntegrityViolationException
                System.out.println("este libro ya existe en la base de datos." + ex);
            }

        } else {
            System.out.println("libro no encontrado.");
        }
    }


      //opcion 2
      private void listarLibrosRegistrados() {
            libros = libroRepository.findAll();
            libros.stream().forEach(System.out::println);
      }

      //opcion 3
    private void listarAutoresRegistrados() {
        autores = autorRepository.findAll();
        autores.stream().forEach(System.out::println);
    }

   //opcion 4
   private void listarAutoresVivosPorAnio() {
       System.out.println("Ingrese el año vivo del autor que desea buscar: ");
       try {
           int year = Integer.parseInt(teclado.nextLine());
           if (year < 0) {
               System.out.println("Error: El año no puede ser negativo.");
               return; // Salir del método sin realizar más operaciones
           }
           List<Autor> autoresVivos = autorRepository.autorVivo(Integer.toString(year));

           if (autoresVivos.isEmpty()) {
               System.out.println("No existen autores para el año proporcionado.");
           } else {
               autoresVivos.forEach(System.out::println);
           }
       } catch (NumberFormatException e) {
           System.out.println("Por favor, ingrese un año válido en números ejm: (1900).");
       }
   }

   //opcion 5
   private void listarLibrosPorIdioma() {
       System.out.println("Ingrese el idioma para buscar los libros: ");
       System.out.println("es - español\nen - inglés\nfr - francés\npt - portugués\n");

       boolean entradaValida = false;
       do {
           try {
               var idioma = teclado.nextLine();
               // Verificamos si el idioma es numérico
               if (idioma.matches("\\d+")) {
                   throw new InputMismatchException("Debe ingresar un acrónimo de idioma válido, no números. Ejm:(es, en, fr, pt)");
               }

               List<Libro> listaLibros = libroRepository.idiomalibros(idioma);

               if (listaLibros.isEmpty()) {
                   System.out.println("No existen libros con el acrónimo que escribiste en la base de datos");
               } else {
                   listaLibros.forEach(System.out::println);
               }
               entradaValida = true; // La entrada es válida, salir del bucle
           } catch (InputMismatchException e) {
               System.out.println("Error: " + e.getMessage());
               // Solicitar al usuario que ingrese el idioma nuevamente
               System.out.println("Por favor, ingrese un acrónimo de idioma válido:");
           }
       } while (!entradaValida);
   }

   //opcion 6
   private void listarTop10Libros() {
        List<Libro> topLibros = libroRepository.findTop10ByOrderByNumeroDeDescargasDesc();
        topLibros.forEach(l ->
                System.out.println("Libro: " + l.getTitulo() + "Numero Descargas: " + l.getNumeroDeDescargas() ));
       System.out.println();
   }

   //opcion 7
    private void generandoEstaisticas() {
        List<Libro> libros = libroRepository.findAll();
        DoubleSummaryStatistics est = libros.stream()
                .filter(l -> l.getNumeroDeDescargas() > 0)
                .collect(Collectors.summarizingDouble(Libro::getNumeroDeDescargas));

        // Buscar el libro más descargado
        Libro libroMasDescargado = libros.stream()
                .filter(l -> l.getNumeroDeDescargas() == est.getMax())
                .findFirst()
                .orElse(null);

        // Buscar el libro menos descargado
        Libro libroMenosDescargado = libros.stream()
                .filter(l -> l.getNumeroDeDescargas() == est.getMin())
                .findFirst()
                .orElse(null);

        System.out.println("Media de descargas: " + est.getAverage());
        System.out.println("Libro más descargado: " + (libroMasDescargado != null ? libroMasDescargado.getTitulo() : "N/A") + ", Descargas: " + est.getMax());
        System.out.println("Libro menos descargado: " + (libroMenosDescargado != null ? libroMenosDescargado.getTitulo() : "N/A") + ", Descargas: " + est.getMin());
        System.out.println("Cantidad de libros evaluados para calcular las estadisticas: " +est.getCount() + "\n");

    }

    //opcion 8
    private void buscarAutorPorNombre() {
        System.out.println("Ingrese el nombre del autor que desea buscar (solo texto): ");
        String nombre = teclado.nextLine().trim(); // Eliminar espacios en blanco adicionales

        // Validar que el nombre solo contenga letras y espacios
        if (!nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
            System.out.println("Error: El nombre no puede contener numeros.");
            return;
        }

        List<Autor> listaAutores = autorRepository.autorPorNombre(nombre);

        if (listaAutores.isEmpty()) {
            System.out.println("No se encontraron autores con ese nombre.");
        } else {
            System.out.println("Autores encontrados: ");
            listaAutores.forEach(System.out::println);
        }
    }

}



