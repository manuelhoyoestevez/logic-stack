package com.mhe.dev.logic.api.infrastructure.rest.spring.dto;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openapitools.jackson.nullable.JsonNullable;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * LiteralDistributionDto
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-06-12T16:57:08.220+02:00[Europe/Paris]")
public class LiteralDistributionDto   {
  @JsonProperty("literal")
  private String literal;

  @JsonProperty("total")
  private Integer total;

  @JsonProperty("entropy")
  private Double entropy;

  @JsonProperty("totals")
  @Valid
  private Map<String, Integer> totals = null;

  @JsonProperty("subtotals")
  @Valid
  private Map<String, Map<String, Integer>> subtotals = null;

  public LiteralDistributionDto literal(String literal) {
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

  public LiteralDistributionDto total(Integer total) {
    this.total = total;
    return this;
  }

  /**
   * Get total
   * @return total
  */
  @ApiModelProperty(value = "")


  public Integer getTotal() {
    return total;
  }

  public void setTotal(Integer total) {
    this.total = total;
  }

  public LiteralDistributionDto entropy(Double entropy) {
    this.entropy = entropy;
    return this;
  }

  /**
   * Get entropy
   * @return entropy
  */
  @ApiModelProperty(value = "")


  public Double getEntropy() {
    return entropy;
  }

  public void setEntropy(Double entropy) {
    this.entropy = entropy;
  }

  public LiteralDistributionDto totals(Map<String, Integer> totals) {
    this.totals = totals;
    return this;
  }

  public LiteralDistributionDto putTotalsItem(String key, Integer totalsItem) {
    if (this.totals == null) {
      this.totals = new HashMap<>();
    }
    this.totals.put(key, totalsItem);
    return this;
  }

  /**
   * Map<Boolean, Integer>
   * @return totals
  */
  @ApiModelProperty(value = "Map<Boolean, Integer>")


  public Map<String, Integer> getTotals() {
    return totals;
  }

  public void setTotals(Map<String, Integer> totals) {
    this.totals = totals;
  }

  public LiteralDistributionDto subtotals(Map<String, Map<String, Integer>> subtotals) {
    this.subtotals = subtotals;
    return this;
  }

  public LiteralDistributionDto putSubtotalsItem(String key, Map<String, Integer> subtotalsItem) {
    if (this.subtotals == null) {
      this.subtotals = new HashMap<>();
    }
    this.subtotals.put(key, subtotalsItem);
    return this;
  }

  /**
   * Map<Boolean, Map<Boolean, Integer>>
   * @return subtotals
  */
  @ApiModelProperty(value = "Map<Boolean, Map<Boolean, Integer>>")

  @Valid

  public Map<String, Map<String, Integer>> getSubtotals() {
    return subtotals;
  }

  public void setSubtotals(Map<String, Map<String, Integer>> subtotals) {
    this.subtotals = subtotals;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LiteralDistributionDto literalDistribution = (LiteralDistributionDto) o;
    return Objects.equals(this.literal, literalDistribution.literal) &&
        Objects.equals(this.total, literalDistribution.total) &&
        Objects.equals(this.entropy, literalDistribution.entropy) &&
        Objects.equals(this.totals, literalDistribution.totals) &&
        Objects.equals(this.subtotals, literalDistribution.subtotals);
  }

  @Override
  public int hashCode() {
    return Objects.hash(literal, total, entropy, totals, subtotals);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LiteralDistributionDto {\n");
    
    sb.append("    literal: ").append(toIndentedString(literal)).append("\n");
    sb.append("    total: ").append(toIndentedString(total)).append("\n");
    sb.append("    entropy: ").append(toIndentedString(entropy)).append("\n");
    sb.append("    totals: ").append(toIndentedString(totals)).append("\n");
    sb.append("    subtotals: ").append(toIndentedString(subtotals)).append("\n");
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

