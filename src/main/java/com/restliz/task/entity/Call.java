package com.restliz.task.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Entity
@Setter
@Getter
@NoArgsConstructor
@JsonPropertyOrder({"message_type", "timestamp", "origin", "destination", "duration", "status_code", "status_description"})
public class Call extends Record {
    @Id
    @GeneratedValue
    private long id;
    @Positive
    private long duration;
    @JsonProperty("status_code")
    @NotNull
    private CallStatusCode statusCode;
    @JsonProperty("status_description")
    @NotEmpty
    private String statusDescription;

    public Call(long timestamp, long origin, long destination, long duration, CallStatusCode statusCode, String statusDescription) {
        super(RecordType.CALL, timestamp, origin, destination);
        this.duration = duration;
        this.statusCode = statusCode;
        this.statusDescription = statusDescription;
    }


    @Override
    public String toString() {
        return "Call{" +
                "id=" + id +
                ", duration=" + duration +
                ", statusCode=" + statusCode +
                ", timestamp=" + getTimestamp() +
                ", statusDescription='" + statusDescription + '\'' +
                '}';
    }
}
