package IG.application.utils;

public class Converter {
    public static double litroAKilogramo(double litros) {
        return litros * 1.0;
    }

    public static double gramoAKilogramo(double gramos) {
        return gramos / 1000.0;
    }

    public static double mililitroAKilogramo(double mililitros) {
        // 1 litro = 1000 mililitros
        double litros = mililitros / 1000.0;
        return litroAKilogramo(litros);
    }

    public static double centilitroAGramo(double centilitros) {
        double mililitros = centilitros * 10.0;
        return mililitroAKilogramo(mililitros);
    }
}
