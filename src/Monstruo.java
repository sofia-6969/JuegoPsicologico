import java.util.Random; // Solo se importa Random para calcular daño
import java.util.List;
import java.util.ArrayList;

// Esta clase representa al personaje del Monstruo en el juego.
public class Monstruo {
    String nombre;
    int vidaInicial;
    int vidaActual;
    String currentState;

    // Todas las habilidades del monstruo, definidas como atributos individuales
    private Skill mZarpazoBrutal;
    private Skill mAmenazar;
    private Skill mGritoAtormentador;
    private Skill mPerseguir;
    private Skill mEmbestidaImparable;
    private Skill mEsconderseSombra;
    private Skill mSombrasAcechantes;
    private Skill mAtrapar;
    private Skill mDrenajeVida;
    private Skill mGolpeFinal;

    // Constructor para inicializar el monstruo
    public Monstruo(String nombre, int vidaInicial) {
        this.nombre = nombre;
        this.vidaInicial = vidaInicial;
        this.vidaActual = vidaInicial;
        this.currentState = "M_INITIAL";
        inicializarHabilidades();
    }

    // Método para definir todas las habilidades del Monstruo
    private void inicializarHabilidades() {
        mZarpazoBrutal = new Skill("M_ZARPAZO_BRUTAL", "Zarpazo Brutal", 40, 60, "Un ataque poderoso con tus garras afiladas.");
        mAmenazar = new Skill("M_AMENAZAR", "Amenazar", 0, 0, "Muestras tu poder para infundir miedo.");
        mGritoAtormentador = new Skill("M_GRITO_ATORMENTADOR", "Grito Atormentador", 0, 0, "Un grito que causa pánico y desorientación.");
        mPerseguir = new Skill("M_PERSEGUIR", "Perseguir", 0, 0, "Cierras la distancia, sin dejarle escapar.");
        mEmbestidaImparable = new Skill("M_EMBESTIDA_IMPARABLE", "Embestida Imparable", 50, 75, "Cargas con toda tu fuerza, derribando obstáculos.");
        mEsconderseSombra = new Skill("M_ESCONDERSE_SOMBRA", "Esconderse en Sombra", 0, 0, "Te fundes con las sombras para preparar una emboscada.");
        mSombrasAcechantes = new Skill("M_SOMBRAS_ACECHANTES", "Sombras Acechantes", 0, 0, "Te fundes con la oscuridad, volviéndote casi invisible.");
        mAtrapar = new Skill("M_ATRAPAR", "Atrapar con Tentáculo", 35, 55, "Un tentáculo emerge para inmovilizar a la víctima.");
        mDrenajeVida = new Skill("M_DRENAJE_VIDA", "Drenaje de Vida", 50, 70, "Absorbes su energía vital antes del golpe final.");
        mGolpeFinal = new Skill("M_GOLPE_FINAL", "Golpe de Aniquilación", 70, 100, "Tu ataque más letal para acabar con todo.");
    }

    public List<Skill> getAvailableSkills() {
        List<Skill> skills = new ArrayList<>();
        switch (currentState) {
            case "M_INITIAL":
                skills.add(mZarpazoBrutal);
                skills.add(mAmenazar);
                break;
            case "M_STEP_1":
                skills.add(mGritoAtormentador);
                skills.add(mPerseguir);
                break;
            case "M_STEP_2":
                skills.add(mEmbestidaImparable);
                skills.add(mEsconderseSombra);
                break;
            case "M_STEP_3":
                skills.add(mSombrasAcechantes);
                skills.add(mAtrapar);
                break;
            case "M_STEP_4":
                skills.add(mDrenajeVida);
                skills.add(mGolpeFinal);
                break;
            case "M_FINAL_MONSTER_WIN":
                // No se añaden habilidades
                break;
        }
        return skills;
    }

    public boolean ejecutarAccion(Skill skill, Victima victima, Random random) {
        System.out.println(this.nombre + " usa " + skill.nombre + "...");
        int damageDealt = skill.calcularDano(random);
        boolean monstruoGana = false;

        switch (skill.id) {
            case "M_ZARPAZO_BRUTAL":
                victima.recibirDano(damageDealt);
                System.out.println(this.nombre + " golpea con un zarpazo. Daño a " + victima.getNombre() + ": " + damageDealt);
                this.currentState = "M_STEP_1";
                break;
            case "M_AMENAZAR":
                System.out.println(" menacingly " + this.nombre + " ruge y muestra sus garras, infundiendo pánico en " + victima.getNombre() + ".");
                this.currentState = "M_STEP_1";
                break;
            case "M_GRITO_ATORMENTADOR":
                System.out.println(victima.getNombre() + " se paraliza de miedo ante tu grito.");
                this.currentState = "M_STEP_2";
                break;
            case "M_PERSEGUIR":
                System.out.println(this.nombre + " acorta la distancia, " + victima.getNombre() + " no puede escapar fácilmente.");
                this.currentState = "M_STEP_2";
                break;
            case "M_EMBESTIDA_IMPARABLE":
                victima.recibirDano(damageDealt);
                System.out.println(this.nombre + " embiste con fuerza. Daño a " + victima.getNombre() + ": " + damageDealt);
                this.currentState = "M_STEP_3";
                break;
            case "M_ESCONDERSE_SOMBRA":
                System.out.println(this.nombre + " se disuelve en las sombras, esperando el momento de emboscar.");
                this.currentState = "M_STEP_3";
                break;
            case "M_SOMBRAS_ACECHANTES":
                System.out.println(this.nombre + " emerge de las sombras, volviéndote casi invisible antes de golpear.");
                this.currentState = "M_STEP_4";
                break;
            case "M_ATRAPAR":
                victima.recibirDano(damageDealt);
                System.out.println(this.nombre + " lanza un tentáculo que atrapa a " + victima.getNombre() + ". Daño: " + damageDealt);
                this.currentState = "M_STEP_4";
                break;
            case "M_DRENAJE_VIDA":
                victima.recibirDano(damageDealt);
                System.out.println(" drained " + this.nombre + " absorbe la energía vital de " + victima.getNombre() + ". Daño: " + damageDealt);
                break;
            case "M_GOLPE_FINAL":
                victima.recibirDano(damageDealt);
                System.out.println(this.nombre + " desata su ataque más letal. " + victima.getNombre() + " no puede resistir. ¡Has ganado!");
                System.out.println("Daño final: " + damageDealt);
                this.currentState = "M_FINAL_MONSTER_WIN";
                monstruoGana = true;
                break;
            default:
                System.out.println("Habilidad de " + this.nombre + " no reconocida (esto no debería ocurrir).");
                break;
        }
        return monstruoGana;
    }

    public void recibirDano(int damage) {
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
        this.currentState = "M_INITIAL";
    }
}