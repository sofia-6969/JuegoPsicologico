import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

// Esta clase representa al personaje de la Víctima en el juego.
public class Victima {
    private final String nombre;
    private final int vidaInicial;
    private int vidaActual;
    private String currentState;

    // Mapa de habilidades para acceso más eficiente
    private final Map<String, Skill> habilidades;

    // Constructor para inicializar la víctima
    public Victima(String nombre, int vidaInicial) {
        this.nombre = nombre;
        this.vidaInicial = vidaInicial;
        this.vidaActual = vidaInicial;
        this.currentState = "V_INITIAL";
        this.habilidades = new HashMap<>();
        inicializarHabilidades();
    }

    // Método para definir todas las habilidades de la Víctima
    private void inicializarHabilidades() {
        agregarHabilidad("V_ESQUIVAR", "Esquivar", 0, 0, "Te mueves rápidamente para evitar un ataque.");
        agregarHabilidad("V_OBSERVAR", "Observar", 0, 0, "Analizas el entorno en busca de debilidades.");
        agregarHabilidad("V_GOLPE_SORPRESA", "Golpe Sorpresa", 30, 50, "Aprovechas su distracción para un golpe rápido.");
        agregarHabilidad("V_ENGAÑO", "Engaño Sutil", 0, 0, "Finges debilidad para que se acerque.");
        agregarHabilidad("V_DISTRAER", "Distraer con Ruido", 0, 0, "Creas un sonido para confundirlo y ganar tiempo.");
        agregarHabilidad("V_BUSCAR_OBJETO", "Buscar Objeto", 0, 0, "Buscas algo útil en el entorno.");
        agregarHabilidad("V_ATURDIR", "Aturdir con Objeto", 40, 60, "Lanzas un objeto pesado para desorientarlo.");
        agregarHabilidad("V_CONTRATAQUE_RAPIDO", "Contraataque Rápido", 35, 55, "Golpeas justo después de un ataque del monstruo.");
        agregarHabilidad("V_ULTIMO_ALIENTO", "Último Aliento", 50, 70, "Un ataque desesperado antes de la huida.");
        agregarHabilidad("V_HUIDA_FINAL", "Huida Desesperada", 0, 0, "Tu último esfuerzo para escapar de la pesadilla.");
    }

    private void agregarHabilidad(String id, String nombre, int danoMin, int danoMax, String descripcion) {
        habilidades.put(id, new Skill(id, nombre, danoMin, danoMax, descripcion));
    }

    public List<Skill> getAvailableSkills() {
        List<Skill> skills = new ArrayList<>();
        switch (currentState) {
            case "V_INITIAL":
                skills.add(habilidades.get("V_ESQUIVAR"));
                skills.add(habilidades.get("V_OBSERVAR"));
                break;
            case "V_STEP_1":
                skills.add(habilidades.get("V_GOLPE_SORPRESA"));
                skills.add(habilidades.get("V_ENGAÑO"));
                break;
            case "V_STEP_2":
                skills.add(habilidades.get("V_DISTRAER"));
                skills.add(habilidades.get("V_BUSCAR_OBJETO"));
                break;
            case "V_STEP_3":
                skills.add(habilidades.get("V_ATURDIR"));
                skills.add(habilidades.get("V_CONTRATAQUE_RAPIDO"));
                break;
            case "V_STEP_4":
                skills.add(habilidades.get("V_ULTIMO_ALIENTO"));
                skills.add(habilidades.get("V_HUIDA_FINAL"));
                break;
        }
        return skills;
    }

    public boolean ejecutarAccion(Skill skill, Monstruo monstruo, Random random) {
        System.out.println("⚔️ " + this.nombre + " usa " + skill.nombre + "...");
        int damageDealt = skill.calcularDano(random);

        switch (skill.id) {
            case "V_ESQUIVAR":
            case "V_OBSERVAR":
                System.out.println(skill.id.equals("V_ESQUIVAR") ? 
                    "💨 " + this.nombre + " se desliza y logra evitar el ataque de " + monstruo.getNombre() + "." :
                    "🧐 " + this.nombre + " observa atentamente a " + monstruo.getNombre() + ", tratando de encontrar una debilidad.");
                this.currentState = "V_STEP_1";
                break;
                
            case "V_GOLPE_SORPRESA":
            case "V_ENGAÑO":
                if (skill.id.equals("V_GOLPE_SORPRESA")) {
                    monstruo.recibirDano(damageDealt);
                    System.out.println("💥 " + this.nombre + " asesta un golpe rápido. Daño a " + monstruo.getNombre() + ": " + damageDealt);
                } else {
                    System.out.println("😈 " + this.nombre + " finge un tropiezo, intentando que se acerque.");
                }
                this.currentState = "V_STEP_2";
                break;
                
            case "V_DISTRAER":
            case "V_BUSCAR_OBJETO":
                System.out.println(skill.id.equals("V_DISTRAER") ?
                    "🪨 " + this.nombre + " lanza una piedra, " + monstruo.getNombre() + " se distrae un instante." :
                    "🔍 " + this.nombre + " busca frenéticamente algo útil en los escombros cercanos.");
                this.currentState = "V_STEP_3";
                break;
                
            case "V_ATURDIR":
            case "V_CONTRATAQUE_RAPIDO":
                monstruo.recibirDano(damageDealt);
                System.out.println(skill.id.equals("V_ATURDIR") ?
                    "😵 " + this.nombre + " golpea a " + monstruo.getNombre() + " con un objeto contundente, aturdiéndolo. Daño: " + damageDealt :
                    "⚡ " + this.nombre + " aprovecha una apertura para un contraataque veloz. Daño: " + damageDealt);
                this.currentState = "V_STEP_4";
                break;
                
            case "V_ULTIMO_ALIENTO":
                monstruo.recibirDano(damageDealt);
                System.out.println("🔥 " + this.nombre + " desata un ataque final desesperado antes de intentar huir. Daño: " + damageDealt);
                break;
                
            case "V_HUIDA_FINAL":
                System.out.println("\n🚪 " + this.nombre + " corre con todas sus fuerzas, finalmente encuentra la salida y escapa de " + monstruo.getNombre() + ". ¡Has ganado!");
                this.currentState = "V_FINAL_VICTIM_WIN";
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
        this.currentState = "V_INITIAL";
    }
}