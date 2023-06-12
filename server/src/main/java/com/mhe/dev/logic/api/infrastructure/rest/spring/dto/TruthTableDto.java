package com.mhe.dev.logic.api.infrastructure.rest.spring.dto;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.mhe.dev.logic.api.infrastructure.rest.spring.dto.LiteralDistributionDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openapitools.jackson.nullable.JsonNullable;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * TruthTableDto
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-06-12T16:57:08.220+02:00[Europe/Paris]")
public class TruthTableDto   {
  @JsonProperty("literals")
  @Valid
  private List<String> literals = null;

  @JsonProperty("values")
  private String values = "0";

  @JsonProperty("minLiteral")
  private String minLiteral;

  @JsonProperty("maxLiteral")
  private String maxLiteral;

  @JsonProperty("average")
  private Double average;

  @JsonProperty("entropy")
  private Double entropy;

  @JsonProperty("distribution")
  @Valid
  private Map<String, Integer> distribution = null;

  @JsonProperty("literalPartition")
  @Valid
  private Map<String, LiteralDistributionDto> literalPartition = null;

  public TruthTableDto literals(List<String> literals) {
    this.literals = literals;
    return this;
  }

  public TruthTableDto addLiteralsItem(String literalsItem) {
    if (this.literals == null) {
      this.literals = new ArrayList<>();
    }
    this.literals.add(literalsItem);
    return this;
  }

  /**
   * Literals
   * @return literals
  */
  @ApiModelProperty(value = "Literals")

@Size(min=0) 
  public List<String> getLiterals() {
    return literals;
  }

  public void setLiterals(List<String> literals) {
    this.literals = literals;
  }

  public TruthTableDto values(String values) {
    this.values = values;
    return this;
  }

  /**
   * Values
   * @return values
  */
  @ApiModelProperty(value = "Values")

@Size(min=1) 
  public String getValues() {
    return values;
  }

  public void setValues(String values) {
    this.values = values;
  }

  public TruthTableDto minLiteral(String minLiteral) {
    this.minLiteral = minLiteral;
    return this;
  }

  /**
   * Get minLiteral
   * @return minLiteral
  */
  @ApiModelProperty(readOnly = true, value = "")


  public String getMinLiteral() {
    return minLiteral;
  }

  public void setMinLiteral(String minLiteral) {
    this.minLiteral = minLiteral;
  }

  public TruthTableDto maxLiteral(String maxLiteral) {
    this.maxLiteral = maxLiteral;
    return this;
  }

  /**
   * Get maxLiteral
   * @return maxLiteral
  */
  @ApiModelProperty(readOnly = true, value = "")


  public String getMaxLiteral() {
    return maxLiteral;
  }

  public void setMaxLiteral(String maxLiteral) {
    this.maxLiteral = maxLiteral;
  }

  public TruthTableDto average(Double average) {
    this.average = average;
    return this;
  }

  /**
   * Get average
   * @return average
  */
  @ApiModelProperty(readOnly = true, value = "")


  public Double getAverage() {
    return average;
  }

  public void setAverage(Double average) {
    this.average = average;
  }

  public TruthTableDto entropy(Double entropy) {
    this.entropy = entropy;
    return this;
  }

  /**
   * Get entropy
   * @return entropy
  */
  @ApiModelProperty(readOnly = true, value = "")


  public Double getEntropy() {
    return entropy;
  }

  public void setEntropy(Double entropy) {
    this.entropy = entropy;
  }

  public TruthTableDto distribution(Map<String, Integer> distribution) {
    this.distribution = distribution;
    return this;
  }

  public TruthTableDto putDistributionItem(String key, Integer distributionItem) {
    if (this.distribution == null) {
      this.distribution = new HashMap<>();
    }
    this.distribution.put(key, distributionItem);
    return this;
  }

  /**
   * Map<Boolean, Integer>
   * @return distribution
  */
  @ApiModelProperty(value = "Map<Boolean, Integer>")


  public Map<String, Integer> getDistribution() {
    return distribution;
  }

  public void setDistribution(Map<String, Integer> distribution) {
    this.distribution = distribution;
  }

  public TruthTableDto literalPartition(Map<String, LiteralDistributionDto> literalPartition) {
    this.literalPartition = literalPartition;
    return this;
  }

  public TruthTableDto putLiteralPartitionItem(String key, LiteralDistributionDto literalPartitionItem) {
    if (this.literalPartition == null) {
      this.literalPartition = new HashMap<>();
    }
    this.literalPartition.put(key, literalPartitionItem);
    return this;
  }

  /**
   * Get literalPartition
   * @return literalPartition
  */
  @ApiModelProperty(value = "")

  @Valid

  public Map<String, LiteralDistributionDto> getLiteralPartition() {
    return literalPartition;
  }

  public void setLiteralPartition(Map<String, LiteralDistributionDto> literalPartition) {
    this.literalPartition = literalPartition;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TruthTableDto truthTable = (TruthTableDto) o;
    return Objects.equals(this.literals, truthTable.literals) &&
        Objects.equals(this.values, truthTable.values) &&
        Objects.equals(this.minLiteral, truthTable.minLiteral) &&
        Objects.equals(this.maxLiteral, truthTable.maxLiteral) &&
        Objects.equals(this.average, truthTable.average) &&
        Objects.equals(this.entropy, truthTable.entropy) &&
        Objects.equals(this.distribution, truthTable.distribution) &&
        Objects.equals(this.literalPartition, truthTable.literalPartition);
  }

  @Override
  public int hashCode() {
    return Objects.hash(literals, values, minLiteral, maxLiteral, average, entropy, distribution, literalPartition);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TruthTableDto {\n");
    
    sb.append("    literals: ").append(toIndentedString(literals)).append("\n");
    sb.append("    values: ").append(toIndentedString(values)).append("\n");
    sb.append("    minLiteral: ").append(toIndentedString(minLiteral)).append("\n");
    sb.append("    maxLiteral: ").append(toIndentedString(maxLiteral)).append("\n");
    sb.append("    average: ").append(toIndentedString(average)).append("\n");
    sb.append("    entropy: ").append(toIndentedString(entropy)).append("\n");
    sb.append("    distribution: ").append(toIndentedString(distribution)).append("\n");
    sb.append("    literalPartition: ").append(toIndentedString(literalPartition)).append("\n");
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

