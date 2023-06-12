package com.mhe.dev.logic.api.infrastructure.rest.spring.dto;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.openapitools.jackson.nullable.JsonNullable;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * ExpressionTreeDto
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-06-12T16:57:08.220+02:00[Europe/Paris]")
public class ExpressionTreeDto   {
  @JsonProperty("expression")
  private String expression;

  /**
   * Logic operator
   */
  public enum OperatorEnum {
    AND("and"),
    
    OR("or"),
    
    NOT("not"),
    
    LITERAL("literal");

    private String value;

    OperatorEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static OperatorEnum fromValue(String value) {
      for (OperatorEnum b : OperatorEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  @JsonProperty("operator")
  private OperatorEnum operator;

  @JsonProperty("literal")
  private String literal;

  @JsonProperty("order")
  @Valid
  private List<String> order = null;

  @JsonProperty("children")
  @Valid
  private List<ExpressionTreeDto> children = null;

  public ExpressionTreeDto expression(String expression) {
    this.expression = expression;
    return this;
  }

  /**
   * Calculated expression after parse
   * @return expression
  */
  @ApiModelProperty(value = "Calculated expression after parse")


  public String getExpression() {
    return expression;
  }

  public void setExpression(String expression) {
    this.expression = expression;
  }

  public ExpressionTreeDto operator(OperatorEnum operator) {
    this.operator = operator;
    return this;
  }

  /**
   * Logic operator
   * @return operator
  */
  @ApiModelProperty(required = true, value = "Logic operator")
  @NotNull


  public OperatorEnum getOperator() {
    return operator;
  }

  public void setOperator(OperatorEnum operator) {
    this.operator = operator;
  }

  public ExpressionTreeDto literal(String literal) {
    this.literal = literal;
    return this;
  }

  /**
   * Literal if operator is literal
   * @return literal
  */
  @ApiModelProperty(value = "Literal if operator is literal")


  public String getLiteral() {
    return literal;
  }

  public void setLiteral(String literal) {
    this.literal = literal;
  }

  public ExpressionTreeDto order(List<String> order) {
    this.order = order;
    return this;
  }

  public ExpressionTreeDto addOrderItem(String orderItem) {
    if (this.order == null) {
      this.order = new ArrayList<>();
    }
    this.order.add(orderItem);
    return this;
  }

  /**
   * Literals order
   * @return order
  */
  @ApiModelProperty(value = "Literals order")


  public List<String> getOrder() {
    return order;
  }

  public void setOrder(List<String> order) {
    this.order = order;
  }

  public ExpressionTreeDto children(List<ExpressionTreeDto> children) {
    this.children = children;
    return this;
  }

  public ExpressionTreeDto addChildrenItem(ExpressionTreeDto childrenItem) {
    if (this.children == null) {
      this.children = new ArrayList<>();
    }
    this.children.add(childrenItem);
    return this;
  }

  /**
   * Nested ExpressionTree
   * @return children
  */
  @ApiModelProperty(value = "Nested ExpressionTree")

  @Valid

  public List<ExpressionTreeDto> getChildren() {
    return children;
  }

  public void setChildren(List<ExpressionTreeDto> children) {
    this.children = children;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ExpressionTreeDto expressionTree = (ExpressionTreeDto) o;
    return Objects.equals(this.expression, expressionTree.expression) &&
        Objects.equals(this.operator, expressionTree.operator) &&
        Objects.equals(this.literal, expressionTree.literal) &&
        Objects.equals(this.order, expressionTree.order) &&
        Objects.equals(this.children, expressionTree.children);
  }

  @Override
  public int hashCode() {
    return Objects.hash(expression, operator, literal, order, children);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ExpressionTreeDto {\n");
    
    sb.append("    expression: ").append(toIndentedString(expression)).append("\n");
    sb.append("    operator: ").append(toIndentedString(operator)).append("\n");
    sb.append("    literal: ").append(toIndentedString(literal)).append("\n");
    sb.append("    order: ").append(toIndentedString(order)).append("\n");
    sb.append("    children: ").append(toIndentedString(children)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

