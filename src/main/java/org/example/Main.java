package org.example;

import javax.swing.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        /*
        * Plantear un programa que ejecute DOS HILOS de programa al mismo tiempo, para esto se requiere que uno de los hilos realice
        *  un conteo regresivo temporizado partiendo de un número establecido por el usuario, la temporización entre cada ejecución del hilo
        * debe ser de 600 ms, el otro hilo, debe mostrar las letras del alfabeto partiendo desde la A hasta una letra ingresada por el usuario,
        *  este hilo debe estar temporizado a 500 ms.
        * Ambos hilos deben detenerse cuando el hilo numero 1 encuentre la letra ingresada por el usuario.
        * Al ejecutar el programa en consola se debe evidenciar la salida entregada por ambos hilos, al final de su ejecución
        * cada hilo debe mostrar el mensaje “Trabajo del hilo [nombre] terminado”.
        * */

        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

        AtomicBoolean found = new AtomicBoolean(false);

        //Buscar letra
        char character = JOptionPane.showInputDialog("Ingresa una letra").charAt(0);

        Runnable thread1 = () -> {
            try {
                Thread.currentThread().setName("Thread charcater");
                for (char i = 'A'; i <= character; i++) {
                    Thread.sleep(500);
                    System.out.print(i);
                    if (i == character) {
                        found.set(true);
                        System.out.println("Letra " + i + " encontrada");
                        System.out.println("trabajo del hilo " + Thread.currentThread().getName() + " terminado");
                    }
                }
            }catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        };


        //Contar hacia atrás
        int number = Integer.parseInt(JOptionPane.showInputDialog("Ingresa un número"));
        AtomicInteger atomicInteger = new AtomicInteger(number);

        Runnable thread2 = () -> {
            try {
                Thread.currentThread().setName("Thread number");
                while (found.get()==false) {
                    Thread.sleep(600);
                    System.out.println(atomicInteger.getAndDecrement());
                }
                System.out.println("trabajo del hilo " + Thread.currentThread().getName() + " terminado");
            }catch (Exception e) {
                System.out.println(e.getMessage());
            }
        };

        executor.submit(thread1);
        executor.submit(thread2);
        executor.awaitTermination(30, TimeUnit.SECONDS);
        executor.shutdown();
    }
}
