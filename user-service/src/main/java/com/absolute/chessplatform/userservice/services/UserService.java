package com.absolute.chessplatform.userservice.services;

import com.absolute.chessplatform.userservice.dtos.UserResponseDTO;

import java.util.Optional;
import java.util.UUID;


public interface UserService {
    public UserResponseDTO getUserById(String id);
}
