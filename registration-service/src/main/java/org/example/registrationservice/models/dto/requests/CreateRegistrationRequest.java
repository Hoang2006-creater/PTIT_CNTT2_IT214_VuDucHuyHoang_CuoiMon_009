package org.example.registrationservice.models.dto.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateRegistrationRequest(
        @NotBlank(message = "Tên người đăng ký không được để trống")
        String participantName,

        @NotBlank(message = "Email người đăng ký không được để trống")
        @Email(message = "Email người đăng ký không hợp lệ")
        String participantEmail,

        @NotEmpty(message = "Danh sách sự kiện không được để trống")
        List<@Valid CreateRegistrationDetailRequest> items
) {
}
