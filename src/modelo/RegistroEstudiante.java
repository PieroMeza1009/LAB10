package modelo;

// Esta es la clase que representa a un estudiante con código y nombre
public class RegistroEstudiante implements Comparable<RegistroEstudiante> {
    private int codigo;     // Código universitario del estudiante
    private String nombre;  // Nombre del estudiante

    ///aca con este constructor se inicializa un estudiante con su código y nombre
    public RegistroEstudiante(int codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
    }
    // Devuelve el código del estudiante
    public int getCodigo() { 
        return codigo; 
    }

    // Devuelve el nombre del estudiante
    public String getNombre() { 
        return nombre;
     }

    // Compara dos objetos RegistroEstudiante por su código
    @Override
    public int compareTo(RegistroEstudiante otro) {
        return Integer.compare(this.codigo, otro.codigo);
    }

    @Override
    public String toString() {
        return codigo + " - " + nombre;
    }
}
