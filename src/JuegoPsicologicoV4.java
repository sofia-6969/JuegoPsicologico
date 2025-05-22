import java.util.InputMismatchException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList; // Mantener si getAvailableSkills devuelve ArrayList

public class JuegoPsicologicoV4 {

    static int vidaVictimaInicial = 200;
    static int vidaMonstruoInicial = 250;
    static Scanner scanner = new Scanner(System.in);
    static Random random = new Random();
    static boolean terminarJuego = false; // Bandera para saber si el juego ha terminado

    // Instancias de las clases Victima y Monstruo
    // Estas son las variables que usaremos para interactuar con los objetos Victima y Monstruo
    static Victima victimaActual;
    static Monstruo monstruoActual;

    // --- Métodos de utilidad para la entrada de usuario y el juego ---

    public static int getValidIntegerInput(String prompt, int min, int max) {
        int input = -1;
        boolean isValid = false;
        while (!isValid) {
            System.out.print(prompt);
            try {
                input = scanner.nextInt();
                if (input >= min && input <= max) {
                    isValid = true;
                } else {
                    System.out.println("Por favor, ingresa un número entre " + min + " y " + max + ".");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, ingresa un número.");
                scanner.next(); // Consume la entrada inválida para evitar un bucle infinito
            }
        }
        scanner.nextLine(); // Consume la nueva línea pendiente después de nextInt()
        return input;
    }

    public static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim(); // trim() para quitar espacios en blanco al inicio/final
    }

    public static void mostrarEstado() {
        System.out.println("\n--- Estado Actual ---");
        System.out.println("Vida " + victimaActual.getNombre() + ": " + victimaActual.getVidaActual());
        System.out.println("Vida " + monstruoActual.getNombre() + ": " + monstruoActual.getVidaActual());
        System.out.println("---------------------");
    }

    // Estas acciones son las del "oponente" cuando el jugador no es ese rol.
    // Usarán el método `recibirDano` de las clases Victima y Monstruo.
    public static void accionMonstruoContraVictima() {
        int eleccion = random.nextInt(2); // 0: Ataque simple, 1: Intimidación/Observa

        if (eleccion == 0) {
            int dano = random.nextInt(30) + 10; // Daño moderado
            victimaActual.recibirDano(dano); // Llamada al método de la instancia
            System.out.println(monstruoActual.getNombre() + " ataca con un golpe directo a " + victimaActual.getNombre() + ". Daño: " + dano);
        } else {
            System.out.println(monstruoActual.getNombre() + " te observa fijamente, llenándote de terror.");
        }
    }

    public static void accionVictimaContraMonstruo() {
        int eleccion = random.nextInt(2); // 0: Contraataque débil, 1: Intento de evasión

        if (eleccion == 0) {
            int dano = random.nextInt(15) + 5; // Daño ligero
            monstruoActual.recibirDano(dano); // Llamada al método de la instancia
            System.out.println(victimaActual.getNombre() + " te lanza algo desesperadamente. Daño a " + monstruoActual.getNombre() + ": " + dano);
        } else {
            System.out.println(victimaActual.getNombre() + " intenta esquivarte, moviéndose con miedo.");
        }
    }

    // Lógica principal para jugar como la Víctima
    public static void jugarComoVictima() {
        while (!terminarJuego && victimaActual.getVidaActual() > 0 && monstruoActual.getVidaActual() > 0) {
            List<Skill> habilidadesDisponibles = victimaActual.getAvailableSkills();

            if (habilidadesDisponibles.isEmpty()) {
                System.out.println("\n" + victimaActual.getNombre() + " se ha quedado sin opciones. Fin del juego.");
                terminarJuego = true;
                break;
            }

            System.out.println("\n Opciones de " + victimaActual.getNombre() + ":");
            for (int i = 0; i < habilidadesDisponibles.size(); i++) {
                Skill s = habilidadesDisponibles.get(i);
                System.out.println((i + 1) + ". " + s.nombre + " (Daño: " + s.danoMin + "-" + s.danoMax + ") - " + s.descripcion);
            }

            int accionIndex = getValidIntegerInput("Elige tu acción: ", 1, habilidadesDisponibles.size()) - 1;
            Skill skillElegida = habilidadesDisponibles.get(accionIndex);

            // La acción de la víctima se ejecuta y puede llevar a la victoria
            boolean victimaGana = victimaActual.ejecutarAccion(skillElegida, monstruoActual, random);

            if (victimaGana) {
                terminarJuego = true; // La víctima ha ganado por su acción específica (ej. huida)
                break;
            }

            // Si el monstruo muere por la acción de la víctima
            if (monstruoActual.getVidaActual() <= 0) {
                System.out.println(victimaActual.getNombre() + " ha derrotado a " + monstruoActual.getNombre() + ". ¡Sobreviviste! Fin del juego.");
                terminarJuego = true;
                break;
            }

            // El monstruo enemigo realiza su acción
            accionMonstruoContraVictima();

            // Si la víctima muere por la acción del monstruo
            if (victimaActual.getVidaActual() <= 0) {
                System.out.println(monstruoActual.getNombre() + " ha eliminado a " + victimaActual.getNombre() + ". Fin del juego.");
                terminarJuego = true;
                break;
            }
            mostrarEstado(); // Muestra el estado después de ambos turnos
        }
    }

    // Lógica principal para jugar como el Monstruo
    public static void jugarComoMonstruo() {
        while (!terminarJuego && victimaActual.getVidaActual() > 0 && monstruoActual.getVidaActual() > 0) {
            List<Skill> habilidadesDisponibles = monstruoActual.getAvailableSkills();

            if (habilidadesDisponibles.isEmpty()) {
                System.out.println("\n" + monstruoActual.getNombre() + " se ha quedado sin opciones. Fin del juego.");
                terminarJuego = true;
                break;
            }

            System.out.println("\n🔹 Habilidades de " + monstruoActual.getNombre() + ":");
            for (int i = 0; i < habilidadesDisponibles.size(); i++) {
                Skill s = habilidadesDisponibles.get(i);
                System.out.println((i + 1) + ". " + s.nombre + " (Daño: " + s.danoMin + "-" + s.danoMax + ") - " + s.descripcion);
            }

            int accionIndex = getValidIntegerInput("Elige tu habilidad: ", 1, habilidadesDisponibles.size()) - 1;
            Skill skillElegida = habilidadesDisponibles.get(accionIndex);
            boolean monstruoGana = monstruoActual.ejecutarAccion(skillElegida, victimaActual, random);

            if (monstruoGana) {
                terminarJuego = true; // El monstruo ha ganado por su acción específica (ej. golpe final)
                break;
            }

            // Si la víctima muere por la acción del monstruo
            if (victimaActual.getVidaActual() <= 0) {
                System.out.println(victimaActual.getNombre() + " ha sido destruida. Victoria monstruosa para " + monstruoActual.getNombre() + ".");
                terminarJuego = true;
                break;
            }

            // La víctima enemiga realiza su acción
            accionVictimaContraMonstruo();

            // Si el monstruo muere por la acción de la víctima
            if (monstruoActual.getVidaActual() <= 0) {
                System.out.println(victimaActual.getNombre() + " logró vencerte... " + monstruoActual.getNombre() + " has fallado.");
                terminarJuego = true;
                break;
            }
            mostrarEstado();
        }
    }

    // Método principal donde se ejecuta el juego
    public static void main(String[] args) {
        boolean jugarDeNuevo;
        String nombreMonstruoFijo = "Yisus"; // Nombre del monstruo fijo

        do {
            terminarJuego = false; // Reinicia la bandera para una nueva partida

            System.out.println("ADVERTENCIA: estás a punto de entrar en un juego peligroso y psicológico.");
            String seguir = getStringInput("¿Estás dispuesto a continuar? (s/n): ").toLowerCase();

            if (!seguir.equals("s")) {
                System.out.println("Has abandonado el juego.");
                jugarDeNuevo = false; // No quiere jugar de nuevo
                break; // Sale del bucle do-while
            }

            System.out.println("\n--- Estadísticas Iniciales ---");
            System.out.println("Vida Víctima: " + vidaVictimaInicial);
            System.out.println("Vida Monstruo: " + vidaMonstruoInicial);
            System.out.println("------------------------------");

            System.out.println("\nElige tu rol:");
            System.out.println("1. Víctima");
            System.out.println("2. Monstruo");
            int eleccionRol = getValidIntegerInput("Ingresa 1 o 2: ", 1, 2);

            if (eleccionRol == 1) {
                String nombreVictimaInput = getStringInput("¡Valiente elección! Por favor, ingresa tu nombre como Víctima: ");
                // Creamos nuevas instancias de Victima y Monstruo para cada juego
                victimaActual = new Victima(nombreVictimaInput, vidaVictimaInicial);
                monstruoActual = new Monstruo(nombreMonstruoFijo, vidaMonstruoInicial);
                System.out.println("¡Prepárate, " + victimaActual.getNombre() + "! El " + monstruoActual.getNombre() + " te espera.");
                jugarComoVictima(); // Inicia el juego como víctima
            } else {
                // Creamos nuevas instancias de Victima y Monstruo para cada juego
                monstruoActual = new Monstruo(nombreMonstruoFijo, vidaMonstruoInicial);
                victimaActual = new Victima("La Víctima", vidaVictimaInicial); // Nombre por defecto para la víctima NPC
                System.out.println("¡Bienvenido, " + monstruoActual.getNombre() + "! La caza comienza.");
                jugarComoMonstruo(); // Inicia el juego como monstruo
            }

            System.out.print("\n¿Quieres jugar de nuevo? (s/n): ");
            String respuestaJugarDeNuevo = getStringInput("").toLowerCase();
            jugarDeNuevo = respuestaJugarDeNuevo.equals("s");

            if (jugarDeNuevo) {
                // Si juegas de nuevo, los objetos se re-inicializan en el siguiente do-while,
                // así que no necesitas un reset explícito aquí si usas 'new' en cada partida.
                // Sin embargo, si los personajes se re-usaran, los .reset() serían esenciales.
                // Como ya se maneja creando nuevas instancias, se puede omitir el reset aquí.
            }

        } while (jugarDeNuevo);

        System.out.println("¡Gracias por jugar!");
        scanner.close(); // Es buena práctica cerrar el scanner cuando ya no se va a usar
    }
}