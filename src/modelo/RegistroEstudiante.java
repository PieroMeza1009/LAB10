package modelo;

// Esta es la clase que representa a un estudiante con código y nombre
public class RegistroEstudiante implements Comparable<RegistroEstudiante> {
    private int codigo;     // Código universitario del estudiante
    private String nombre;  // Nombre del estudiante

    public RegistroEstudiante(int codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
    }
    public int getCodigo() { 
        return codigo; 
    }

    public String getNombre() { 
        return nombre;
     }
    @Override
    public int compareTo(RegistroEstudiante otro) {
        return Integer.compare(this.codigo, otro.codigo);
    }

    @Override
    public String toString() {
        return codigo + " - " + nombre;
    }
}
