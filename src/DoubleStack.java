import java.util.*;

/** Stack manipulation.
 * @since 1.8
 */
public class DoubleStack {

   private Element last;

   public static class EmptyStackException extends RuntimeException {

      public EmptyStackException() {
         super();
      }

      public EmptyStackException(String message) {
         super(message);
      }
   }

   private static class Element {
      private double value;
      private Element next;

      Element(double value){
         this.value = value;
         next = null;
      }
   }

   public static void main (String[] argum) {
      System.out.println(DoubleStack.interpret("2. 15. -"));
   }

   DoubleStack() {
      last = null;
   }

   @Override
   public DoubleStack clone() throws CloneNotSupportedException {
      DoubleStack newStack = new DoubleStack();

      if(this.last == null){
         return newStack;
      }

      Element curr = this.last;
      DoubleStack temporaryStack = new DoubleStack();

      while(curr != null){
         temporaryStack.push(curr.value);
         curr = curr.next;
      }

      while(!temporaryStack.stEmpty()){
         newStack.push(temporaryStack.pop());
      }

      return newStack;
   }

   public boolean stEmpty() {
      return last == null;
   }

   public void push (double a) {
      Element newElement = new Element(a);
      newElement.next = this.last;
      this.last = newElement;
   }

   public double pop() {
      if(stEmpty()){
         throw new EmptyStackException();
      } else {
         double value = this.last.value;
         last = last.next;
         return value;
      }
   } // pop

   public void op(String s) {
      if(last == null || last.next == null) throw new IllegalArgumentException("Missing elements for operation: " + s);

      double first = pop();
      double second = pop();
      switch (s) {
         case "/":
            if(first == 0) throw new ArithmeticException("Division by zero is not legal");
            push(second / first);
            break;
         case "*":
            push(second * first);
            break;
         case "+":
            push(second + first);
            break;
         case "-":
            push(second - first);
            break;
         default:
            throw new IllegalArgumentException("Invalid operation" + s);
      }
   }

   public double tos() {
      if(stEmpty()){
         throw new EmptyStackException("Stack is empty");
      }
      return last.value;
   }

   @Override
   public boolean equals (Object o) {
      if(this == o) return true;

      DoubleStack comparable = (DoubleStack) o;

      Element currentLast = this.last;
      Element comparableLast = comparable.last;

      while(currentLast !=null && comparableLast != null){
         if(currentLast.value != comparableLast.value) return false;
         currentLast = currentLast.next;
         comparableLast = comparableLast.next;
      }

      return currentLast == null && comparableLast == null;
   }

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder();

      DoubleStack tempStack = new DoubleStack();
      Element current = last;
      while (current != null) {
         tempStack.push(current.value);
         current = current.next;
      }

      while (!tempStack.stEmpty()) {
         sb.append(tempStack.pop()).append(" ");
      }

      return sb.toString().trim();
   }

   public static double interpret (String pol) {
      if (pol == null || pol.trim().isEmpty()) {
         throw new IllegalArgumentException("Expression is missing or empty: " + pol);
      }

      DoubleStack operandsStack = new DoubleStack();
      String[] splitNotation = pol.trim().split("\\s+");
      int countOperands = 0;

      for (String element : splitNotation) {
         try {
            double operand = Double.parseDouble(element);
            operandsStack.push(operand);
            countOperands++;
         } catch (NumberFormatException e) {
            if (!Arrays.asList("/", "*", "+", "-").contains(element)) {
               throw new IllegalArgumentException("Invalid operation in expression: " + pol);
            }
            try {
               operandsStack.op(element);
               countOperands--;
            } catch (IllegalArgumentException opException) {
               throw new IllegalArgumentException("Invalid operation or operand in expression: " + pol);
            }
         }
      }

      if (countOperands > 1) {
         throw new IllegalArgumentException("Too many numbers in expression: " + pol);
      } else if (countOperands < 1) {
         throw new IllegalArgumentException("Not enough numbers in expression: " + pol);
      }

      return operandsStack.tos();
   }

}