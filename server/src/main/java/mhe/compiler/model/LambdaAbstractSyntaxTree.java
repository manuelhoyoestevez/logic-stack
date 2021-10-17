package mhe.compiler.model;

public interface LambdaAbstractSyntaxTree<T> extends AbstractSyntaxTree<T> {
  boolean isNotLambda();
}
