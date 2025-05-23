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
        System.out.println("❤️ Vida " + victimaActual.getNombre() + ": " + victimaActual.getVidaActual());
        System.out.println("💀 Vida " + monstruoActual.getNombre() + ": " + monstruoActual.getVidaActual());
        System.out.println("---------------------");
    }

    // Acción del oponente controlado por la IA
    public static void accionOponente(boolean esVictima) {
        int eleccion = random.nextInt(2);
        if (esVictima) { // Monstruo contra Víctima
            if (eleccion == 0) {
                int dano = random.nextInt(30) + 10;
                victimaActual.recibirDano(dano);
                System.out.println("👹 " + monstruoActual.getNombre() + " ataca con un golpe directo a " + victimaActual.getNombre() + ". Daño: " + dano);
            } else {
                System.out.println("👹 " + monstruoActual.getNombre() + " te observa fijamente, llenándote de terror.");
            }
        } else { // Víctima contra Monstruo
            if (eleccion == 0) {
                int dano = random.nextInt(15) + 5;
                monstruoActual.recibirDano(dano);
                System.out.println("😨 " + victimaActual.getNombre() + " te lanza algo desesperadamente. Daño a " + monstruoActual.getNombre() + ": " + dano);
            } else {
                System.out.println("😰 " + victimaActual.getNombre() + " intenta esquivarte, moviéndose con miedo.");
            }
        }
    }

    // Método genérico para jugar una ronda
    public static void jugarRonda(boolean esVictima) {
        while (!terminarJuego && victimaActual.getVidaActual() > 0 && monstruoActual.getVidaActual() > 0) {
            // Obtener el personaje activo y su oponente
            Object personajeActivo = esVictima ? victimaActual : monstruoActual;
            Object oponente = esVictima ? monstruoActual : victimaActual;

            // Obtener habilidades disponibles
            List<Skill> habilidadesDisponibles = esVictima ?
                    victimaActual.getAvailableSkills() :
                    monstruoActual.getAvailableSkills();

            if (habilidadesDisponibles.isEmpty()) {
                System.out.println("\n" + (esVictima ? victimaActual.getNombre() : monstruoActual.getNombre()) +
                        " se ha quedado sin opciones. Fin del juego.");
                terminarJuego = true;
                break;
            }

            // Mostrar opciones
            System.out.println("\n🔹 " + (esVictima ? "Opciones" : "Habilidades") + " de " +
                    (esVictima ? victimaActual.getNombre() : monstruoActual.getNombre()) + ":");
            for (int i = 0; i < habilidadesDisponibles.size(); i++) {
                Skill s = habilidadesDisponibles.get(i);
                System.out.println((i + 1) + ". " + s.nombre + " (Daño: " + s.danoMin + "-" + s.danoMax + ") - " + s.descripcion);
            }

            // Elegir acción
            int accionIndex = getValidIntegerInput("Elige tu " + (esVictima ? "acción" : "habilidad") + ": ",
                    1, habilidadesDisponibles.size()) - 1;
            Skill skillElegida = habilidadesDisponibles.get(accionIndex);

            // Ejecutar acción
            boolean victoria = esVictima ?
                    victimaActual.ejecutarAccion(skillElegida, monstruoActual, random) :
                    monstruoActual.ejecutarAccion(skillElegida, victimaActual, random);

            if (victoria) {
                terminarJuego = true;
                break;
            }

            // Verificar si el oponente ha sido derrotado
            if ((esVictima && monstruoActual.getVidaActual() <= 0) ||
                    (!esVictima && victimaActual.getVidaActual() <= 0)) {
                System.out.println("\n" + (esVictima ?
                        "🎉 " + victimaActual.getNombre() + " ha derrotado a " + monstruoActual.getNombre() + ". ¡Sobreviviste!" :
                        "💀 " + victimaActual.getNombre() + " ha sido destruida. Victoria monstruosa para " + monstruoActual.getNombre() + "."));
                terminarJuego = true;
                break;
            }

            // Acción del oponente
            accionOponente(esVictima);

            // Verificar si el jugador ha sido derrotado
            if ((esVictima && victimaActual.getVidaActual() <= 0) ||
                    (!esVictima && monstruoActual.getVidaActual() <= 0)) {
                System.out.println("\n" + (esVictima ?
                        "☠️ " + monstruoActual.getNombre() + " ha eliminado a " + victimaActual.getNombre() + "." :
                        "❌ " + victimaActual.getNombre() + " logró vencerte... " + monstruoActual.getNombre() + " has fallado."));
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

            System.out.println("⚠️ ADVERTENCIA: estás a punto de entrar en un juego peligroso y psicológico.");
            String seguir = getStringInput("¿Estás dispuesto a continuar? (s/n): ").toLowerCase();

            if (!seguir.equals("s")) {
                System.out.println("Has abandonado el juego.");
                break;
            }

            System.out.println("\n--- Estadísticas Iniciales ---");
            System.out.println("❤️ Vida Víctima: " + vidaVictimaInicial);
            System.out.println("💀 Vida Monstruo: " + vidaMonstruoInicial);
            System.out.println("------------------------------");

            System.out.println("\nElige tu rol:");
            System.out.println("1. Víctima");
            System.out.println("2. Monstruo");
            int eleccionRol = getValidIntegerInput("Ingresa 1 o 2: ", 1, 2);

            if (eleccionRol == 1) {
                String nombreVictimaInput = getStringInput("¡Valiente elección! Por favor, ingresa tu nombre como Víctima: ");
                victimaActual = new Victima(nombreVictimaInput, vidaVictimaInicial);
                monstruoActual = new Monstruo(nombreMonstruoFijo, vidaMonstruoInicial);
                System.out.println("¡Prepárate, " + victimaActual.getNombre() + "! El " + monstruoActual.getNombre() + " te espera.");
                jugarRonda(true); // Jugar como víctima
            } else {
                monstruoActual = new Monstruo(nombreMonstruoFijo, vidaMonstruoInicial);
                victimaActual = new Victima("La Víctima", vidaVictimaInicial);
                System.out.println("¡Bienvenido, " + monstruoActual.getNombre() + "! La caza comienza.");
                jugarRonda(false); // Jugar como monstruo
            }

            jugarDeNuevo = getStringInput("\n¿Quieres jugar de nuevo? (s/n): ").toLowerCase().equals("s");

        } while (jugarDeNuevo);

        System.out.println("¡Gracias por jugar!");
        scanner.close();
    }
}