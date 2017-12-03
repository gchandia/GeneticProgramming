package tree;

public interface OperatorNode {
  public int operate();
  public boolean isTerminal();
  public boolean isFunction();
  public OperatorNode getLeft();
  public OperatorNode getRight();
  public void setLeft(OperatorNode son);
  public void setRight(OperatorNode son);
  public OperatorNode cloneTree();
  public void copyLeft(OperatorNode copy);
  public void copyRight(OperatorNode copy);
  public String printContent();
  public void setFitness(int fitness);
  public int getFitness();
  public void setNormalizedFitness(double normFitness);
  public double getNormalizedFitness();
}