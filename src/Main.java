import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Variables globales
        final String API_URL = "https://open.er-api.com/v6/latest/";
        final String API_KEY = "a93bd576c0001a424069a44d";

        Scanner scanner = new Scanner(System.in);

        System.out.println("******************************************");
        System.out.println("    Bienvenido al Conversor de Monedas");
        System.out.println("******************************************");
        System.out.println("1) Dólar (USD) -> Peso argentino (ARS)");
        System.out.println("2) Peso argentino (ARS) -> Dólar (USD)");
        System.out.println("3) Dólar (USD) -> Real brasileño (BRL)");
        System.out.println("4) Real brasileño (BRL) -> Dólar (USD)");
        System.out.println("5) Dólar (USD) -> Peso colombiano (COP)");
        System.out.println("6) Peso colombiano (COP) -> Dólar (USD)");
        System.out.println("7) Salir");
        System.out.print("Elija una opción válida: ");

        int opcion = scanner.nextInt();
        scanner.nextLine(); // Limpiar buffer

        String base = "", destino = "";
        if (opcion >= 1 && opcion <= 6) {
            switch (opcion) {
                case 1 -> { base = "USD"; destino = "ARS"; }
                case 2 -> { base = "ARS"; destino = "USD"; }
                case 3 -> { base = "USD"; destino = "BRL"; }
                case 4 -> { base = "BRL"; destino = "USD"; }
                case 5 -> { base = "USD"; destino = "COP"; }
                case 6 -> { base = "COP"; destino = "USD"; }
            }

            try {
                // Realizar llamada a la API
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(API_URL + base + "?apikey=" + API_KEY))
                        .GET()
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                // Parsear JSON
                JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();

                if (jsonObject.get("result").getAsString().equals("success")) {
                    double tasa = jsonObject.get("rates").getAsJsonObject().get(destino).getAsDouble();

                    System.out.print("Ingrese la cantidad en " + base + ": ");
                    double cantidad = scanner.nextDouble();

                    double resultado = cantidad * tasa;
                    System.out.printf("%.2f %s equivalen a %.2f %s\n", cantidad, base, resultado, destino);
                } else {
                    System.out.println("Error en la respuesta de la API: " + jsonObject.get("error-type").getAsString());
                }
            } catch (Exception e) {
                System.out.println("Error al conectar con la API: " + e.getMessage());
            }
        } else if (opcion == 7) {
            System.out.println("¡Gracias por usar el conversor!");
        } else {
            System.out.println("Opción no válida.");
        }
    }
}
