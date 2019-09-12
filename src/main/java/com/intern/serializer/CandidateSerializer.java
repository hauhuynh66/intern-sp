package com.intern.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.intern.model.Candidate;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class CandidateSerializer extends StdSerializer<Candidate> {
    public CandidateSerializer(){
        this(Candidate.class);
    }
    public CandidateSerializer(Class<Candidate> t) {
        super(t);
    }
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
    @Override
    public void serialize(Candidate candidate, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id",candidate.getId());
        jsonGenerator.writeNumberField("nationalId",candidate.getNationalId());
        jsonGenerator.writeStringField("name",candidate.getName());
        jsonGenerator.writeStringField("birthday",candidate.getDateOfBirth()+"");
        jsonGenerator.writeStringField("gender",candidate.getGender());
        jsonGenerator.writeStringField("account",candidate.getAccount());
        jsonGenerator.writeStringField("email",candidate.getEmail());
        jsonGenerator.writeStringField("phone",candidate.getPhone());
        jsonGenerator.writeStringField("facebook",candidate.getFacebook());
        jsonGenerator.writeObjectFieldStart("university");
        jsonGenerator.writeStringField("name",candidate.getUniversity().getName());
        jsonGenerator.writeStringField("code",candidate.getUniversity().getCode());
        jsonGenerator.writeEndObject();
        jsonGenerator.writeObjectFieldStart("faculty");
        jsonGenerator.writeStringField("name",candidate.getFaculty().getName());
        jsonGenerator.writeStringField("code",candidate.getFaculty().getCode());
        jsonGenerator.writeEndObject();
        jsonGenerator.writeStringField("graduationDate",candidate.getGraduationDate()+"");
        jsonGenerator.writeStringField("fulltimeDate",candidate.getFulltimeDate()+"");
        jsonGenerator.writeObjectFieldStart("skill");
        jsonGenerator.writeStringField("name",candidate.getSkill().getSkillName());
        jsonGenerator.writeEndObject();
        jsonGenerator.writeStringField("gpa",candidate.getGpa()+"");
        jsonGenerator.writeEndObject();
    }
}
