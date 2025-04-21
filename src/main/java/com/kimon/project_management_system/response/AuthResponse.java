package com.kimon.project_management_system.response;

import com.kimon.project_management_system.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    @Autowired
    private User user;
    private String message;
    private String jwt = null;
}
