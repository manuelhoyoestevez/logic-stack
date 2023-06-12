package com.mhe.dev.logic.api.infrastructure.rest.spring.dto;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.mhe.dev.logic.api.infrastructure.rest.spring.dto.TruthTableDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.openapitools.jackson.nullable.JsonNullable;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * DecisionTreeDto
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-06-12T16:57:08.220+02:00[Europe/Paris]")
public class DecisionTreeDto   {
  @JsonProperty("type")
  private String type;

  @JsonProperty("mode")
  private Boolean mode;

  @JsonProperty("literal")
  private String literal;

  @JsonProperty("expression")
  private String expression;

  @JsonProperty("truthTable")
  private TruthTableDto truthTable;

  @JsonProperty("zero")
  private DecisionTreeDto zero;

  @JsonProperty("one")
  private DecisionTreeDto one;

  public DecisionTreeDto type(String type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
  */
  @ApiModelProperty(value = "")


  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public DecisionTreeDto mode(Boolean mode) {
    this.mode = mode;
    return this;
  }

  /**
   * Get mode
   * @return mode
  */
  @ApiModelProperty(value = "")


  public Boolean getMode() {
    return mode;
  }

  public void setMode(Boolean mode) {
    this.mode = mode;
  }

  public DecisionTreeDto literal(String literal) {
    this.literal = literal;
    return this;
  }

  /**
   * Get literal
   * @return literal
  */
  @ApiModelProperty(value = "")


  public String getLiteral() {
    return literal;
  }

  public void setLiteral(String literal) {
    this.literal = literal;
  }

  public DecisionTreeDto expression(String expression) {
    this.expression = expression;
    return this;
  }

  /**
   * Get expression
   * @return expression
  */
  @ApiModelProperty(value = "")


  public String getExpression() {
    return expression;
  }

  public void setExpression(String expression) {
    this.expression = expression;
  }

  public DecisionTreeDto truthTable(TruthTableDto truthTable) {
    this.truthTable = truthTable;
    return this;
  }

  /**
   * Get truthTable
   * @return truthTable
  */
  @ApiModelProperty(value = "")

  @Valid

  public TruthTableDto getTruthTable() {
    return truthTable;
  }

  public void setTruthTable(TruthTableDto truthTable) {
    this.truthTable = truthTable;
  }

  public DecisionTreeDto zero(DecisionTreeDto zero) {
    this.zero = zero;
    return this;
  }

  /**
   * Get zero
   * @return zero
  */
  @ApiModelProperty(value = "")

  @Valid

  public DecisionTreeDto getZero() {
    return zero;
  }

  public void setZero(DecisionTreeDto zero) {
    this.zero = zero;
  }

  public DecisionTreeDto one(DecisionTreeDto one) {
    this.one = one;
    return this;
  }

  /**
   * Get one
   * @return one
  */
  @ApiModelProperty(value = "")

  @Valid

  public DecisionTreeDto getOne() {
    return one;
  }

  public void setOne(DecisionTreeDto one) {
    this.one = one;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DecisionTreeDto decisionTree = (DecisionTreeDto) o;
    return Objects.equals(this.type, decisionTree.type) &&
        Objects.equals(this.mode, decisionTree.mode) &&
        Objects.equals(this.literal, decisionTree.literal) &&
        Objects.equals(this.expression, decisionTree.expression) &&
        Objects.equals(this.truthTable, decisionTree.truthTable) &&
        Objects.equals(this.zero, decisionTree.zero) &&
        Objects.equals(this.one, decisionTree.one);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, mode, literal, expression, truthTable, zero, one);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DecisionTreeDto {\n");
    
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    mode: ").append(toIndentedString(mode)).append("\n");
    sb.append("    literal: ").append(toIndentedString(literal)).append("\n");
    sb.append("    expression: ").append(toIndentedString(expression)).append("\n");
    sb.append("    truthTable: ").append(toIndentedString(truthTable)).append("\n");
    sb.append("    zero: ").append(toIndentedString(zero)).append("\n");
    sb.append("    one: ").append(toIndentedString(one)).append("\n");
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

