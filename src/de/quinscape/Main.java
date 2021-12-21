package de.quinscape;

import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException {
        WebScan webScan = new WebScan();
        if (args.length > 0) {
            webScan.scanWebAddress(args[0]);
        } else {
            System.out.println("Please enter a program argument.");
        }
    }
}