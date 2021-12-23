package com.adt.ibp.backend_automation.RequestBuilder;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

public class TokenBuilder {
    @JsonPropertyOrder({"password"})
    private String password;


    public String getPassword(){return password; }
    public void setPassword(String password){ this.password = password; }

}
