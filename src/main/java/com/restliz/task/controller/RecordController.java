package com.restliz.task.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restliz.task.entity.Call;
import com.restliz.task.entity.Message;
import com.restliz.task.entity.RecordType;
import com.restliz.task.exception.ApiException;
import com.restliz.task.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class RecordController {
    private final RecordService recordService;

    @Autowired
    public RecordController(RecordService recordService) {
        this.recordService = recordService;
    }

    @PostMapping("/messages")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void addRecord(@RequestBody JsonNode jsonNode ){
        if(!jsonNode.has("message_type")){
            throw new ApiException("No message type specified", HttpStatus.BAD_REQUEST);
        }

        ObjectMapper objectMapper = new ObjectMapper();

        RecordType recordType;
        try{
            recordType = objectMapper.convertValue(jsonNode.get("message_type"), RecordType.class);
        }
        catch (IllegalArgumentException e){
            throw new ApiException("message_type must have value MSG or CALL", HttpStatus.BAD_REQUEST);
        }

        if(recordType == RecordType.MSG){
            Message message;
            try {
                message = objectMapper.convertValue(jsonNode, Message.class);
            }
            catch (IllegalArgumentException e){
                throw new ApiException("message_status must be either DELIVERED or SEEN", HttpStatus.BAD_REQUEST);
            }
            recordService.addRecord(message);
        }
        else{
            Call call;
            try {
                call = objectMapper.convertValue(jsonNode, Call.class);
            }
            catch (IllegalArgumentException e){
                throw new ApiException("status_code must be either OK or KO", HttpStatus.BAD_REQUEST);
            }
            recordService.addRecord(call);
        }
    }

    @GetMapping("/metrics")
    public Map<String, Long> getMetrics(){
        return recordService.getMetrics();
    }

    @GetMapping("/metrics/{date}")
    public Map<String, Long> getMetrics(@PathVariable String date){
        return recordService.getMetrics(date);
    }
}
