package org.growith.be.growith.domain.user.entity;


import jakarta.persistence.*;
import lombok.*;
import org.growith.be.growith.domain.user.dto.request.UserRequestDTO;
import org.growith.be.growith.domain.user.entity.enums.Major;
import org.growith.be.growith.domain.user.entity.enums.StudentStatus;
import org.growith.be.growith.domain.user.entity.enums.UserRole;
import org.growith.be.growith.global.common.BaseEntity;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String loginId;

    private String email;

    private String password;

    private String nickName;

    @Enumerated(EnumType.STRING)
    private Major major;

    // 학적 상태
    @Enumerated(EnumType.STRING)
    private StudentStatus studentStatus;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    private Boolean isNotice;

    private LocalDateTime isDeleted;

    public Long getUserId() {
        return this.id;
    }

    public void changeUserProfile(UserRequestDTO.ChangeInfo request){
        this.nickName = request.nickName();
        this.major = request.major();
        this.studentStatus = request.studentStatus();
    }

    public void encodePassword(String password) {
        this.password = password;
    }
}
