package uo.ri.cws.application.service.acceptance.util.dbfixture.records;

import java.sql.Date;
import java.sql.Timestamp;

import javax.annotation.processing.Generated;

@Generated("LLM")
public class TVehiclesRecord {
    public String id;
    public Long version;
    public Timestamp createdAt;
    public Timestamp updatedAt;
    public String entityState;
    
    public String make;
    public String model;
    public String licensePlate;
    public Date registration_date;
    public String client_Id;
    public String vehicleType_Id;
}
