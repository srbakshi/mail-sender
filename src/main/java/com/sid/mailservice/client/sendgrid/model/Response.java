package com.sid.mailservice.client.sendgrid.model;

import java.util.ArrayList;
import java.util.List;

public class Response {

    private List<Error> errors;


    public List<Error> getErrors() {
        if (errors == null) {
            errors = new ArrayList<>();
        }
        return errors;
    }

    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }
}
