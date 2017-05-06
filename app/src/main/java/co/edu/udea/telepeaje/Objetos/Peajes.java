package co.edu.udea.telepeaje.Objetos;

import java.util.ArrayList;
import java.util.List;

public final class Peajes {

    public static final List<Peaje> PEAJES = new ArrayList<>();

    static {
        PEAJES.add(new Peaje("Trapiche", 6.3996935, -75.4329966, 9700));
        PEAJES.add(new Peaje("Las Palmas", 6.1709063, -75.4788939, 10046 ));
        PEAJES.add(new Peaje("Tunel de Occidente", 6.2968329, -75.6550233, 12500));
        PEAJES.add(new Peaje("Niquia", 6.3451594, -75.5273835, 2400));
        PEAJES.add(new Peaje("Autopista Medellín-Bogotá", 6.3280716, -75.5160309, 10046));
        PEAJES.add(new Peaje("Santa Elena", 6.1797362, -75.4616403,  10046));
        PEAJES.add(new Peaje("Cabildo", 6.3957576, -75.4237195, 9700));
    }
}