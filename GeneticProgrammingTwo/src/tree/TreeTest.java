package tree;

public class TreeTest {
  public static void main(String[] args) {
    OperatorNode root = new MultNode(new TerminalZ(), new TerminalY());
    System.out.println(root.operate());
    System.out.println(root.printContent());
    root = new MultNode(new TerminalZ(7) , new TerminalY(5));
    System.out.println(root.operate());
    System.out.println(root.printContent());
  }
}