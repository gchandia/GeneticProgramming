package tree;

public class SumNode extends Node {
  public SumNode(OperatorNode left, OperatorNode right) {
    super(0, left, right, "+");
  }
  
  public int operate() {
    return this.getLeft().operate() + this.getRight().operate();
  }
  
  public boolean isFunction() {
    return true;
  }
  
  public OperatorNode cloneTree() {
    return new SumNode(this.getLeft().cloneTree(), this.getRight().cloneTree());
  }
}