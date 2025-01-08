package com.absolute.chessplatform.userservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSettingsDTO {

    private String preferences;
    private boolean notifications;
}
