import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class LineCounter implements Runnable {

    private String archivoNombre;
    private AtomicInteger totalLineas;
    private AtomicInteger totalCaracteres;

    public LineCounter(String archivoNombre, AtomicInteger totalLineas, AtomicInteger totalCaracteres) {
        this.archivoNombre = archivoNombre;
        this.totalLineas = totalLineas;
        this.totalCaracteres = totalCaracteres;
    }

    @Override
    public void run() {
        int lineasEnArchivo = 0;
        int caracteresEnArchivo = 0;

        try (BufferedReader lector = new BufferedReader(new FileReader(archivoNombre))) {
            String linea;
            long tiempoInicio = System.currentTimeMillis();
            while ((linea = lector.readLine()) != null) {
                lineasEnArchivo++;
                caracteresEnArchivo += linea.length();
            }
            totalLineas.addAndGet(lineasEnArchivo);
            totalCaracteres.addAndGet(caracteresEnArchivo);
            System.out.printf("%s: %-50s %,7d líneas %,7d caracteres (%d ms)%n",
                    Thread.currentThread().getName(), archivoNombre,
                    lineasEnArchivo, caracteresEnArchivo, System.currentTimeMillis() - tiempoInicio);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
