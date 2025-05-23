// Esta clase define una habilidad simple con su nombre, daño y descripción.
public class Skill {
    final String id; // Identificador único para la habilidad
    final String nombre;
    final int danoMin;
    final int danoMax;
    final String descripcion;

    // Constructor para crear una nueva habilidad
    public Skill(String id, String nombre, int danoMin, int danoMax, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.danoMin = danoMin;
        this.danoMax = danoMax;
        this.descripcion = descripcion;
    }

    // Método para calcular el daño que una habilidad inflige
    public int calcularDano(java.util.Random random) {
        return danoMin == danoMax ? danoMin : random.nextInt(danoMax - danoMin + 1) + danoMin;
    }
}
