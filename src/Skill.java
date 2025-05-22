// Esta clase define una habilidad simple con su nombre, daño y descripción.
public class Skill {
    String id; // Identificador único para la habilidad
    String nombre;
    int danoMin;
    int danoMax;
    String descripcion;

    // Constructor para crear una nueva habilidad
    public Skill(String id, String nombre, int danoMin, int danoMax, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.danoMin = danoMin;
        this.danoMax = danoMax;
        this.descripcion = descripcion;
    }

    // Método para calcular el daño que una habilidad inflige
    // Necesita un objeto Random para generar un número aleatorio dentro del rango de daño.
    public int calcularDano(java.util.Random random) {
        // Genera un número aleatorio entre danoMin y danoMax (inclusive)
        return random.nextInt(danoMax - danoMin + 1) + danoMin;
    }
}
