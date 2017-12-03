package tree;

public class TerminalY extends TerminalNode {
  public TerminalY() {
    super(0, "y");
  }
  
  public TerminalY(int number) {
    super(number, String.valueOf(number));
  }
  
  public OperatorNode cloneTree() {
    if (this.rep == "y") {
      return new TerminalY();
    }
    return new TerminalY(this.number);
  }
}