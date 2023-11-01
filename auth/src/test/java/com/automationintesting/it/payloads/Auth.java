package com.automationintesting.it.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Auth {
   @JsonProperty
   private String username;

   @JsonProperty
   private String password;

   public Auth(String username, String password) {
      this.username = username;
      this.password = password;
   }

   public String getUsername() {
      return username;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }
}