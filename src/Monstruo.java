import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

// Esta clase representa al personaje del Monstruo en el juego.
public class Monstruo {
    private final String nombre;
    private final int vidaInicial;
    private int vidaActual;
    private String currentState;

    // Mapa de habilidades para acceso más eficiente
    private final Map<String, Skill> habilidades;

    // Constructor para inicializar el monstruo
    public Monstruo(String nombre, int vidaInicial) {
        this.nombre = nombre;
        this.vidaInicial = vidaInicial;
        this.vidaActual = vidaInicial;
        this.currentState = "M_INITIAL";
        this.habilidades = new HashMap<>();
        inicializarHabilidades();
    }

    // Método para definir todas las habilidades del Monstruo
    private void inicializarHabilidades() {
        agregarHabilidad("M_ZARPAZO_BRUTAL", "Zarpazo Brutal", 40, 60, "Un ataque poderoso con tus garras afiladas.");
        agregarHabilidad("M_AMENAZAR", "Amenazar", 0, 0, "Muestras tu poder para infundir miedo.");
        agregarHabilidad("M_GRITO_ATORMENTADOR", "Grito Atormentador", 0, 0, "Un grito que causa pánico y desorientación.");
        agregarHabilidad("M_PERSEGUIR", "Perseguir", 0, 0, "Cierras la distancia, sin dejarle escapar.");
        agregarHabilidad("M_EMBESTIDA_IMPARABLE", "Embestida Imparable", 50, 75, "Cargas con toda tu fuerza, derribando obstáculos.");
        agregarHabilidad("M_ESCONDERSE_SOMBRA", "Esconderse en Sombra", 0, 0, "Te fundes con las sombras para preparar una emboscada.");
        agregarHabilidad("M_SOMBRAS_ACECHANTES", "Sombras Acechantes", 0, 0, "Te fundes con la oscuridad, volviéndote casi invisible.");
        agregarHabilidad("M_ATRAPAR", "Atrapar con Tentáculo", 35, 55, "Un tentáculo emerge para inmovilizar a la víctima.");
        agregarHabilidad("M_DRENAJE_VIDA", "Drenaje de Vida", 50, 70, "Absorbes su energía vital antes del golpe final.");
        agregarHabilidad("M_GOLPE_FINAL", "Golpe de Aniquilación", 70, 100, "Tu ataque más letal para acabar con todo.");
    }

    private void agregarHabilidad(String id, String nombre, int danoMin, int danoMax, String descripcion) {
        habilidades.put(id, new Skill(id, nombre, danoMin, danoMax, descripcion));
    }

    public List<Skill> getAvailableSkills() {
        List<Skill> skills = new ArrayList<>();
        switch (currentState) {
            case "M_INITIAL":
                skills.add(habilidades.get("M_ZARPAZO_BRUTAL"));
                skills.add(habilidades.get("M_AMENAZAR"));
                break;
            case "M_STEP_1":
                skills.add(habilidades.get("M_GRITO_ATORMENTADOR"));
                skills.add(habilidades.get("M_PERSEGUIR"));
                break;
            case "M_STEP_2":
                skills.add(habilidades.get("M_EMBESTIDA_IMPARABLE"));
                skills.add(habilidades.get("M_ESCONDERSE_SOMBRA"));
                break;
            case "M_STEP_3":
                skills.add(habilidades.get("M_SOMBRAS_ACECHANTES"));
                skills.add(habilidades.get("M_ATRAPAR"));
                break;
            case "M_STEP_4":
                skills.add(habilidades.get("M_DRENAJE_VIDA"));
                skills.add(habilidades.get("M_GOLPE_FINAL"));
                break;
        }
        return skills;
    }

    public boolean ejecutarAccion(Skill skill, Victima victima, Random random) {
        System.out.println("🗣️ " + this.nombre + " usa " + skill.nombre + "...");
        int damageDealt = skill.calcularDano(random);

        switch (skill.id) {
            case "M_ZARPAZO_BRUTAL":
            case "M_AMENAZAR":
                if (skill.id.equals("M_ZARPAZO_BRUTAL")) {
                    victima.recibirDano(damageDealt);
                    System.out.println("🩸 " + this.nombre + " golpea con un zarpazo. Daño a " + victima.getNombre() + ": " + damageDealt);
                } else {
                    System.out.println(" menacingly " + this.nombre + " ruge y muestra sus garras, infundiendo pánico en " + victima.getNombre() + ".");
                }
                this.currentState = "M_STEP_1";
                break;
                
            case "M_GRITO_ATORMENTADOR":
            case "M_PERSEGUIR":
                System.out.println(skill.id.equals("M_GRITO_ATORMENTADOR") ?
                    "😱 " + victima.getNombre() + " se paraliza de miedo ante tu grito." :
                    "🏃 " + this.nombre + " acorta la distancia, " + victima.getNombre() + " no puede escapar fácilmente.");
                this.currentState = "M_STEP_2";
                break;
                
            case "M_EMBESTIDA_IMPARABLE":
            case "M_ESCONDERSE_SOMBRA":
                if (skill.id.equals("M_EMBESTIDA_IMPARABLE")) {
                    victima.recibirDano(damageDealt);
                    System.out.println("💥 " + this.nombre + " embiste con fuerza. Daño a " + victima.getNombre() + ": " + damageDealt);
                } else {
                    System.out.println("⚫ " + this.nombre + " se disuelve en las sombras, esperando el momento de emboscar.");
                }
                this.currentState = "M_STEP_3";
                break;
                
            case "M_SOMBRAS_ACECHANTES":
            case "M_ATRAPAR":
                if (skill.id.equals("M_ATRAPAR")) {
                    victima.recibirDano(damageDealt);
                    System.out.println("🐙 " + this.nombre + " lanza un tentáculo que atrapa a " + victima.getNombre() + ". Daño: " + damageDealt);
                } else {
                    System.out.println("👻 " + this.nombre + " emerge de las sombras, volviéndote casi invisible antes de golpear.");
                }
                this.currentState = "M_STEP_4";
                break;
                
            case "M_DRENAJE_VIDA":
                victima.recibirDano(damageDealt);
                System.out.println(" drained " + this.nombre + " absorbe la energía vital de " + victima.getNombre() + ". Daño: " + damageDealt);
                break;
                
            case "M_GOLPE_FINAL":
                victima.recibirDano(damageDealt);
                System.out.println("\n☠️ " + this.nombre + " desata su ataque más letal. " + victima.getNombre() + " no puede resistir. ¡Has ganado!");
                System.out.println("Daño final: " + damageDealt);
                this.currentState = "M_FINAL_MONSTER_WIN";
                return true;
        }
        return false;
    }

    public void recibirDano(int damage) {
        this.vidaActual = Math.max(0, this.vidaActual - damage);
    }

    public String getNombre() {
        return nombre;
    }

    public int getVidaActual() {
        return vidaActual;
    }

    public String getCurrentState() {
        return currentState;
    }

    public void reset() {
        this.vidaActual = this.vidaInicial;
        this.currentState = "M_INITIAL";
    }
}