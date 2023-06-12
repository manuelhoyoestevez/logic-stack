package com.mhe.dev.logic.api.infrastructure.rest.spring.dto;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.openapitools.jackson.nullable.JsonNullable;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * ExpressionDto
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-06-12T16:57:08.220+02:00[Europe/Paris]")
public class ExpressionDto   {
  @JsonProperty("expression")
  private String expression;

  public ExpressionDto expression(String expression) {
    this.expression = expression;
    return this;
  }

  /**
   * expression
   * @return expression
  */
  @ApiModelProperty(example = "return a -> b;", required = true, value = "expression")
  @NotNull


  public String getExpression() {
    return expression;
  }

  public void setExpression(String expression) {
    this.expression = expression;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ExpressionDto expression = (ExpressionDto) o;
    return Objects.equals(this.expression, expression.expression);
  }

  @Override
  public int hashCode() {
    return Objects.hash(expression);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ExpressionDto {\n");
    
    sb.append("    expression: ").append(toIndentedString(expression)).append("\n");
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

