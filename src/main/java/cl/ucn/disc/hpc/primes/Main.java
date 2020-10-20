package cl.ucn.disc.hpc.primes;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang3.time.StopWatch;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;



public class Main {

    /*
      logger remplaza al system.out.print();
     */
    private static  final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws InterruptedException {
        //calcula tiempo de ejecucion de un programa
        long max=1000000;
        final StopWatch time = StopWatch.createStarted();
        final ExecutorService executorService = Executors.newFixedThreadPool(2);
        for (int i = 0; i <max ; i++) {
            executorService.submit(new Task(i));
        }

        //cancelar todas las otras tareas.
        executorService.shutdown();

        //si no se termina en 30 minutos, lo bota.
        if(executorService.awaitTermination(30, TimeUnit.MINUTES))
            log.debug("cantidad de n primos encontrados {} ", Task.counter);

        log.debug("hecho en : {}",time);
    }

    /*
    Clase que ejecuta metodo run el cual contiene la funcion isPrime
    la cual calcula si un numero es primo.
     */
    private static class Task extends Thread {

        private final long number;
        private static final AtomicInteger counter = new AtomicInteger(0);

        public Task (final long number){
            this.number=number;
        }


        @Override
        public void run() {
            if(isPrime(this.number))
                counter.getAndIncrement();

        }
        //funcion que calcula si un numero es primo o no.
        public static final boolean isPrime(long number){
            //menor que 1 o negativo,devuelve falso(no es un primo)
            if(number<=1)return false;
            //para todos los numeros.
            for (int i = 2; i < number ; i++) {
                if(number % i==0) return false;
            }
            return true;
        }

    }




}




