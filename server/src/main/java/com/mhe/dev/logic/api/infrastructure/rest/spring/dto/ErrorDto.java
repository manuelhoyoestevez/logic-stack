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
 * ErrorDto
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-06-12T16:57:08.220+02:00[Europe/Paris]")
public class ErrorDto   {
  @JsonProperty("code")
  private Integer code;

  @JsonProperty("title")
  private String title;

  @JsonProperty("description")
  private String description;

  public ErrorDto code(Integer code) {
    this.code = code;
    return this;
  }

  /**
   * Http error code
   * @return code
  */
  @ApiModelProperty(example = "400", value = "Http error code")


  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

  public ErrorDto title(String title) {
    this.title = title;
    return this;
  }

  /**
   * Http error message
   * @return title
  */
  @ApiModelProperty(example = "Bad request", value = "Http error message")


  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public ErrorDto description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Error description
   * @return description
  */
  @ApiModelProperty(example = "Wrong user or password provided", value = "Error description")


  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ErrorDto error = (ErrorDto) o;
    return Objects.equals(this.code, error.code) &&
        Objects.equals(this.title, error.title) &&
        Objects.equals(this.description, error.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code, title, description);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ErrorDto {\n");
    
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
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

