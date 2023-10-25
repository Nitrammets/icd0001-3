import java.util.*;

/** Stack manipulation.
 * @since 1.8
 */
public class DoubleStack implements Cloneable{

   private LinkedList<Double> stack;
   private static final List<String> operations = Arrays.asList("+", "-", "*", "/", "SWAP", "DUP", "ROT");

   public static void main (String[] argum) {
      interpret("2 3 SWAP -");
   }

   DoubleStack() {
      stack = new LinkedList<>();
   }

   @Override
   public Object clone() throws CloneNotSupportedException {
      DoubleStack clone = new DoubleStack();
      Iterator<Double> iterator = stack.descendingIterator();
      while (iterator.hasNext()) {
         clone.push(iterator.next());
      }
      return clone;
   }

   public boolean stEmpty() {
      return stack.isEmpty();
   }

   public void push (double a) {
      stack.push(a);
   }

   public double pop() {
      if (stEmpty()) {
         throw new RuntimeException("Stack is empty!");
      }
      return stack.pop();
   }

   public void op(String s) {
      if (!operations.contains(s)) {
         throw new RuntimeException("Invalid operation: " + s + ".");
      } else if (stEmpty() && s.equals("DUP")) {
         throw new RuntimeException("Not enough elements for operation DUP: " + s +" !");
      } else if (stack.size() < 3 && Objects.equals(s, "ROT")) {
         throw new RuntimeException("Not enough elements for operation: " + s +" !");
      } else if (stack.size() < 2 &&
              (Objects.equals(s, "SWAP")
                      || Objects.equals(s, "+")
                      || Objects.equals(s, "-")
                      || Objects.equals(s, "/")
                      || Objects.equals(s, "*"))) {
         throw new RuntimeException("Not enough elements for operation: " + s +" !");
      }
      switch (s) {
         case "+": {
            double a = pop();
            double b = pop();
            push(a + b);
            break;
         }
         case "-": {
            double a = pop();
            double b = pop();
            push(b - a);
            break;
         }
         case "/": {
            double a = pop();
            double b = pop();
            if (a == 0) throw new RuntimeException("Illegal operation: " + s + " (division by zero: " + b + " / " + a + ").");
            else push(b / a);
            break;
         }
         case "*": {
            double a = pop();
            double b = pop();
            push(a * b);
            break;
         }
         case "SWAP":{
            double a = pop();
            double b = pop();
            push(a);
            push(b);
            break;
         }
         case "DUP":{
            double top = tos();
            push(top);
            break;
         }
         case "ROT":{
            double first = pop();
            double second = pop();
            double third = pop();
            push(second);
            push(first);
            push(third);
            break;
         }
      }
   }

   public double tos() {
      if (stEmpty()) {
         throw new RuntimeException("Stack is empty!");
      }
      return stack.peek();
   }

   @Override
   public boolean equals (Object o) {
      return hashCode() == o.hashCode();
   }

   @Override
   public int hashCode() {
      return Objects.hash(stack);
   }

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder();

      Iterator<Double> iterator = stack.descendingIterator();

      while (iterator.hasNext()) {
         sb.append(iterator.next());
         if (iterator.hasNext()) sb.append(" ,");
         else sb.append(".");
      }
      return sb.toString();
   }

   public static double interpret (String pol) {
      if (pol.isEmpty()) throw new RuntimeException("No arguments entered! " + pol);

      DoubleStack doubleStack = new DoubleStack();

      String[] array = pol.trim().split("\\s+");
      for (String element: array) {
         try {
            doubleStack.push(Double.parseDouble(element));
         } catch (NumberFormatException e) {
            try {
               doubleStack.op(element);
            } catch (RuntimeException message) {
               throw new RuntimeException(message + " Original string: " + pol);
            }
         }
      }

      double value;

      try {
         value = doubleStack.tos();
      } catch (RuntimeException empty) {
         throw new RuntimeException(empty + " Original string: " + pol);
      }
      if (doubleStack.stack.size() != 1) throw new RuntimeException("Too many numbers! Original string: " + pol );

      return value;
   }

}