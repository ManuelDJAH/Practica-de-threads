import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Scanner;

public class ContadorLineas extends SimpleFileVisitor<Path> {

    private static AtomicInteger totalLineas = new AtomicInteger(0);
    private static AtomicInteger totalCaracteres = new AtomicInteger(0);

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

        String archivoNombre = file.toAbsolutePath().toString();

        if (archivoNombre.toLowerCase().endsWith(".txt")) {
            Thread hiloContador = new Thread(new LineCounter(archivoNombre, totalLineas, totalCaracteres));
            hiloContador.start();
            try {
                hiloContador.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return super.visitFile(file, attrs);
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        System.out.printf("Error al acceder al archivo: %30s%n", file.toString());
        return super.visitFileFailed(file, exc);
    }


    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Por favor, ingrese el directorio a procesar:");
        String inputPath = scanner.nextLine();
        Path directorioInicial = Paths.get(inputPath);

        ContadorLineas procesador = new ContadorLineas();
        Files.walkFileTree(directorioInicial, procesador);

        System.out.printf("Total acumulado de líneas: %,d%n", totalLineas.get());
        System.out.printf("Total acumulado de caracteres: %,d%n", totalCaracteres.get());
    }
}
