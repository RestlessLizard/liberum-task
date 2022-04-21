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

@Entity
@Setter
@Getter
@NoArgsConstructor
@JsonPropertyOrder({"message_type", "timestamp", "origin", "destination", "message_content", "message_status"})
public class Message extends Record {
    @Id
    @GeneratedValue
    private long id;
    @JsonProperty("message_content")
    @NotEmpty
    private String content;
    @JsonProperty("message_status")
    @NotEmpty
    private MessageStatus status;

    public Message(long timestamp, long origin, long destination, String content, MessageStatus status) {
        super(RecordType.MSG, timestamp, origin, destination);
        this.content = content;
        this.status = status;
    }
}
