package com.intern.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.intern.model.Event;

import java.io.IOException;

public class EventSerializer extends StdSerializer<Event> {
    public EventSerializer(){
        this(Event.class);
    }

    public EventSerializer(Class<Event> t) {
        super(t);
    }

    @Override
    public void serialize(Event event, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id",event.getId());
        jsonGenerator.writeStringField("courseCode",event.getCourseCode());
        jsonGenerator.writeObjectFieldStart("program");
        jsonGenerator.writeStringField("code",event.getProgram().getCode());
        jsonGenerator.writeStringField("name",event.getProgram().getName());
        jsonGenerator.writeNumberField("time",event.getProgram().getTime());
        jsonGenerator.writeEndObject();
        jsonGenerator.writeObjectFieldStart("university");
        jsonGenerator.writeStringField("code",event.getUniversity().getCode());
        jsonGenerator.writeStringField("name",event.getUniversity().getName());
        jsonGenerator.writeEndObject();
        jsonGenerator.writeObjectFieldStart("faculty");
        jsonGenerator.writeStringField("code",event.getFaculty().getCode());
        jsonGenerator.writeStringField("name",event.getFaculty().getName());
        jsonGenerator.writeEndObject();
        jsonGenerator.writeObjectFieldStart("site");
        jsonGenerator.writeStringField("name",event.getSite().getSite());
        jsonGenerator.writeEndObject();
        jsonGenerator.writeObjectFieldStart("skill");
        jsonGenerator.writeStringField("name",event.getSkill().getSkillName());
        jsonGenerator.writeEndObject();
        jsonGenerator.writeNumberField("students", event.getStatuses().size());
        jsonGenerator.writeStringField("subjectType",event.getSubjectType());
        jsonGenerator.writeStringField("format",event.getFormat());
        jsonGenerator.writeStringField("planStartDate",event.getPlannedStartDate()+"");
        jsonGenerator.writeStringField("planEndDate",event.getPlannedEndDate()+"");
        jsonGenerator.writeStringField("planExpanse",event.getPlannedExpense()+"");
        jsonGenerator.writeStringField("budgetCode",event.getBudgetCode()+"");
        jsonGenerator.writeStringField("actualStartDate",event.getActualStartDate()+"");
        jsonGenerator.writeStringField("actualEndDate",event.getActualEndDate()+"");
        jsonGenerator.writeStringField("actualLearningTime",event.getActualLearningTime()+"");
        jsonGenerator.writeEndObject();
    }
}
