import java.util.*;

public class Main {
    private static final Map<Character, Integer> PRECEDENCIA = Map.of('+', 1, '-', 1, '*', 2, '/', 2, '^', 3);

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Introduzca la expresión aritmética que deseas realizar :");
        String expresion = sc.nextLine();

        try {
            System.out.println("Resultado de la evalucacion del problema: " + evaluar(convertirAPostfijo(expresion)));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static List<String> convertirAPostfijo(String exp) throws Exception {
        List<String> salida = new ArrayList<>();
        Deque<Character> operadores = new ArrayDeque<>();
        for (int i = 0; i < exp.length(); i++) {
            char c = exp.charAt(i);
            if (Character.isWhitespace(c)) continue;
            if (Character.isDigit(c)) {
                StringBuilder num = new StringBuilder();
                while (i < exp.length() && (Character.isDigit(exp.charAt(i)) || exp.charAt(i) == '.')) {
                    num.append(exp.charAt(i++));
                }
                salida.add(num.toString());
                i--;
            } else if (c == '(') {
                operadores.push(c);
            } else if (c == ')') {
                while (!operadores.isEmpty() && operadores.peek() != '(') {
                    salida.add(operadores.pop().toString());
                }
                if (!operadores.isEmpty()) operadores.pop(); // Remover '('
            } else if (PRECEDENCIA.containsKey(c)) {
                while (!operadores.isEmpty() && PRECEDENCIA.getOrDefault(operadores.peek(), -1) >= PRECEDENCIA.get(c)) {
                    salida.add(operadores.pop().toString());
                }
                operadores.push(c);
            }
        }
        while (!operadores.isEmpty()) salida.add(operadores.pop().toString());
        return salida;
    }

    private static double evaluar(List<String> postfijo) throws Exception {
        Deque<Double> pila = new ArrayDeque<>();
        for (String token : postfijo) {
            if (token.matches("\\d+(\\.\\d+)?")) {
                pila.push(Double.parseDouble(token));
            } else {
                double b = pila.pop(), a = pila.pop();
                pila.push(switch (token.charAt(0)) {
                    case '+' -> a + b;
                    case '-' -> a - b;
                    case '*' -> a * b;
                    case '/' -> {
                        if (b == 0) throw new Exception("División con cero");
                        yield a / b;
                    }
                    case '^' -> Math.pow(a, b);
                    default -> throw new Exception("Dato inválido");
                });
            }
        }
        return pila.pop();
    }
}
