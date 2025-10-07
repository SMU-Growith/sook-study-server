package org.growith.be.growith.global.security.manager;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.growith.be.growith.global.security.domain.CustomUserDetails;

public interface TokenManager {
    void addToken(HttpServletRequest request, HttpServletResponse response, CustomUserDetails customUserDetails);
}
