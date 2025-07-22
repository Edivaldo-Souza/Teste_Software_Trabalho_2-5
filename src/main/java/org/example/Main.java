package org.example;

import org.example.usuarioInterface.UsuarioInterface;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UsuarioInterface usuario = new UsuarioInterface(scanner);
        usuario.iniciar();
    }
}