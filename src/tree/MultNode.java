package tree;

public class MultNode extends Node {
  public MultNode(OperatorNode left, OperatorNode right) {
    super(0, left, right, "*");
  }
  
  public int operate() {
    return this.getLeft().operate() * this.getRight().operate();
  }
  
  public boolean isFunction() {
    return true;
  }
  
  public OperatorNode cloneTree() {
    return new MultNode(this.getLeft().cloneTree(), this.getRight().cloneTree());
  }
}