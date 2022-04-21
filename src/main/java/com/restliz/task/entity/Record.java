package com.restliz.task.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.*;

@MappedSuperclass
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Record {
    @JsonProperty("message_type")
    @NotNull
    private RecordType recordType;
    @Positive
    private long timestamp;
    @Min(100)
    @Max(999999999999999L)
    private long origin;
    @Min(100)
    @Max(999999999999999L)
    private long destination;
}
