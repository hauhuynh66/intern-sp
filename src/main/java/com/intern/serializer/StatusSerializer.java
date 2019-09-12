package com.intern.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.intern.model.Candidate;
import com.intern.model.Event;
import com.intern.model.Status;

import java.io.IOException;

public class StatusSerializer extends StdSerializer<Status> {

    public StatusSerializer(){
        this(Status.class);
    }

    public StatusSerializer(Class<Status> t) {
        super(t);
    }

    @Override
    public void serialize(Status status, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        Candidate candidate = status.getCandidate();
        Event event = status.getEvent();
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectFieldStart("candidate");
        jsonGenerator.writeNumberField("id",candidate.getId());
        jsonGenerator.writeStringField("name",candidate.getName());
        jsonGenerator.writeStringField("gender",candidate.getGender());
        jsonGenerator.writeStringField("name",candidate.getName());
        jsonGenerator.writeStringField("birthDay",candidate.getDateOfBirth()+"");
        jsonGenerator.writeStringField("email",candidate.getEmail());
        jsonGenerator.writeStringField("university",candidate.getUniversity().getName());
        jsonGenerator.writeStringField("faculty",candidate.getFaculty().getName());
        jsonGenerator.writeEndObject();
        jsonGenerator.writeObjectFieldStart("event");
        jsonGenerator.writeNumberField("id",event.getId());
        jsonGenerator.writeStringField("code",event.getCourseCode());
        jsonGenerator.writeStringField("name",event.getProgram().getName());
        jsonGenerator.writeEndObject();
        jsonGenerator.writeStringField("status",status.getStatus());
        jsonGenerator.writeNumberField("finalGrade",status.getFinalGrade());
        jsonGenerator.writeStringField("completionLevel",status.getCompletionLevel());
        jsonGenerator.writeStringField("certificateId",status.getCertificateId());
        jsonGenerator.writeEndObject();
    }
}
