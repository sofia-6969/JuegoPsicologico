import java.util.Random; // Solo se importa Random para calcular daño
import java.util.List;
import java.util.ArrayList;

// Esta clase representa al personaje de la Víctima en el juego.
public class Victima {
    String nombre;
    int vidaInicial;
    int vidaActual;
    String currentState; // Estado actual de la víctima para determinar habilidades disponibles

    // Todas las habilidades de la víctima, definidas como atributos individuales
    private Skill vEsquivar;
    private Skill vObservar;
    private Skill vGolpeSorpresa;
    private Skill vEngano;
    private Skill vDistraer;
    private Skill vBuscarObjeto;
    private Skill vAturdir;
    private Skill vContraataqueRapido;
    private Skill vUltimoAliento;
    private Skill vHuidaFinal;

    // Constructor para inicializar la víctima
    public Victima(String nombre, int vidaInicial) {
        this.nombre = nombre;
        this.vidaInicial = vidaInicial;
        this.vidaActual = vidaInicial;
        this.currentState = "V_INITIAL"; // Estado inicial de la víctima
        inicializarHabilidades(); // Llamamos al método para crear todas las habilidades
    }

    // Método para definir todas las habilidades de la Víctima
    private void inicializarHabilidades() {
        vEsquivar = new Skill("V_ESQUIVAR", "Esquivar", 0, 0, "Te mueves rápidamente para evitar un ataque.");
        vObservar = new Skill("V_OBSERVAR", "Observar", 0, 0, "Analizas el entorno en busca de debilidades.");
        vGolpeSorpresa = new Skill("V_GOLPE_SORPRESA", "Golpe Sorpresa", 30, 50, "Aprovechas su distracción para un golpe rápido.");
        vEngano = new Skill("V_ENGAÑO", "Engaño Sutil", 0, 0, "Finges debilidad para que se acerque.");
        vDistraer = new Skill("V_DISTRAER", "Distraer con Ruido", 0, 0, "Creas un sonido para confundirlo y ganar tiempo.");
        vBuscarObjeto = new Skill("V_BUSCAR_OBJETO", "Buscar Objeto", 0, 0, "Buscas algo útil en el entorno.");
        vAturdir = new Skill("V_ATURDIR", "Aturdir con Objeto", 40, 60, "Lanzas un objeto pesado para desorientarlo.");
        vContraataqueRapido = new Skill("V_CONTRATAQUE_RAPIDO", "Contraataque Rápido", 35, 55, "Golpeas justo después de un ataque del monstruo.");
        vUltimoAliento = new Skill("V_ULTIMO_ALIENTO", "Último Aliento", 50, 70, "Un ataque desesperado antes de la huida.");
        vHuidaFinal = new Skill("V_HUIDA_FINAL", "Huida Desesperada", 0, 0, "Tu último esfuerzo para escapar de la pesadilla.");
    }

    public List<Skill> getAvailableSkills() {
        List<Skill> skills = new ArrayList<>();
        switch (currentState) {
            case "V_INITIAL":
                skills.add(vEsquivar);
                skills.add(vObservar);
                break;
            case "V_STEP_1":
                skills.add(vGolpeSorpresa);
                skills.add(vEngano);
                break;
            case "V_STEP_2":
                skills.add(vDistraer);
                skills.add(vBuscarObjeto);
                break;
            case "V_STEP_3":
                skills.add(vAturdir);
                skills.add(vContraataqueRapido);
                break;
            case "V_STEP_4":
                skills.add(vUltimoAliento);
                skills.add(vHuidaFinal);
                break;
            case "V_FINAL_VICTIM_WIN":
                // No se añaden habilidades
                break;
        }
        return skills;
    }

    public boolean ejecutarAccion(Skill skill, Monstruo monstruo, Random random) {
        System.out.println(this.nombre + " usa " + skill.nombre + "...");
        int damageDealt = skill.calcularDano(random);
        boolean victimaGana = false;

        switch (skill.id) {
            case "V_ESQUIVAR":
                System.out.println(this.nombre + " se desliza y logra evitar el ataque de " + monstruo.getNombre() + ".");
                this.currentState = "V_STEP_1";
                break;
            case "V_OBSERVAR":
                System.out.println(this.nombre + " observa atentamente a " + monstruo.getNombre() + ", tratando de encontrar una debilidad.");
                this.currentState = "V_STEP_1";
                break;
            case "V_GOLPE_SORPRESA":
                monstruo.recibirDano(damageDealt); // LLAMADA CORREGIDA a recibirDano
                System.out.println(this.nombre + " asesta un golpe rápido. Daño a " + monstruo.getNombre() + ": " + damageDealt);
                this.currentState = "V_STEP_2";
                break;
            case "V_ENGAÑO":
                System.out.println(this.nombre + " finge un tropiezo, intentando que se acerque.");
                this.currentState = "V_STEP_2";
                break;
            case "V_DISTRAER":
                System.out.println(this.nombre + " lanza una piedra, " + monstruo.getNombre() + " se distrae un instante.");
                this.currentState = "V_STEP_3";
                break;
            case "V_BUSCAR_OBJETO":
                System.out.println(this.nombre + " busca frenéticamente algo útil en los escombros cercanos.");
                this.currentState = "V_STEP_3";
                break;
            case "V_ATURDIR":
                monstruo.recibirDano(damageDealt); // LLAMADA CORREGIDA a recibirDano
                System.out.println(this.nombre + " golpea a " + monstruo.getNombre() + " con un objeto contundente, aturdiéndolo. Daño: " + damageDealt);
                this.currentState = "V_STEP_4";
                break;
            case "V_CONTRATAQUE_RAPIDO":
                monstruo.recibirDano(damageDealt); // LLAMADA CORREGIDA a recibirDano
                System.out.println(this.nombre + " aprovecha una apertura para un contraataque veloz. Daño: " + damageDealt);
                this.currentState = "V_STEP_4";
                break;
            case "V_ULTIMO_ALIENTO":
                monstruo.recibirDano(damageDealt); // LLAMADA CORREGIDA a recibirDano
                System.out.println(this.nombre + " desata un ataque final desesperado antes de intentar huir. Daño: " + damageDealt);
                break;
            case "V_HUIDA_FINAL":
                System.out.println(this.nombre + " corre con todas sus fuerzas, finalmente encuentra la salida y escapa de " + monstruo.getNombre() + ". ¡Has ganado!");
                this.currentState = "V_FINAL_VICTIM_WIN";
                victimaGana = true;
                break;
            default:
                System.out.println("Acción de " + this.nombre + " no reconocida (esto no debería ocurrir).");
                break;
        }
        return victimaGana;
    }

    // Renombramos el método para ser consistente con "recibirDano"
    public void recibirDano(int damage) { // MÉTODO RENOMBRADO
        this.vidaActual -= damage;
        if (this.vidaActual < 0) {
            this.vidaActual = 0;
        }
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