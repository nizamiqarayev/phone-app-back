package app.service.impl;

import app.container.dto.UserDto;
import app.container.dto.request.UserRequestData;
import app.container.dto.response.ResponseDto;
import app.container.dto.response.StatusDto;
import app.container.entities.UserEntity;
import app.container.enums.OperationStatuses;
import app.container.enums.OperationTypes;
import app.repositories.UserRepository;
import app.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static app.container.enums.OperationTypes.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public ResponseDto addUser(UserRequestData userRequestData) {
        var userDto = mapFromRegistrationRequestToDto(userRequestData);
        var userEntity = getEntity(userDto);
        var status = save(userEntity);
        return getResponse(status, userEntity.getId(), OPERATION_ADD);
    }

    @Override
    public ResponseDto editUser(UUID uuid, UserRequestData userRequestData) {
        var user = mapFromRegistrationRequestToDto(userRequestData);
        var userFromRepo = repository.findCustomerById(uuid);
        if (userFromRepo.isEmpty()) return getResponse(false, uuid, OPERATION_EDIT);
        userFromRepo.get().setName(user.getName());
        userFromRepo.get().setPhone(user.getPhone());
        var status = save(userFromRepo.get());
        return getResponse(status, uuid, OPERATION_EDIT);
    }

    @Override
    public List<UserDto> getUsersList() {
        log.info(String.valueOf(repository.findAllCustomers()));
        return repository.findAllCustomers().stream()
                .map(this::mapFromEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ResponseDto deleteUser(UserRequestData userRequestData) {
        boolean status = true;
        var user = repository.findByPhone(userRequestData.getPhone());
        repository.deleteCustomer(user.get().getPhone());
        return getResponse(status, user.get().getId(), OPERATION_DELETE);
    }

    @Override
    public StatusDto getStatusInfo() {
        repository.findAll();
        return StatusDto.builder()
                .status(HttpStatus.OK)
                .build();
    }

    private ResponseDto getResponse(boolean status, UUID id, OperationTypes operation) {
        return ResponseDto.builder()
                .user_id(id)
                .operation_type(operation)
                .operation_status(getStatus(status)).build();
    }


    private OperationStatuses getStatus(boolean status) {
        return (status) ? OperationStatuses.SUCCESS : OperationStatuses.FAIL;
    }

    private boolean save(UserEntity userEntity) {
        var id = userEntity.getId();
        if (id == null) {
            id = UUID.randomUUID();
        }
        repository.saveCustomer(id, userEntity.getName(), userEntity.getPhone());
        return true;
    }

    private UserEntity getEntity(UserDto user) {
        return UserEntity.builder()
                .name(user.getName())
                .phone(user.getPhone()).build();
    }

    private UserDto mapFromEntityToDto(UserEntity userEntity) {
        return UserDto.builder()
                .name(userEntity.getName())
                .user_id(userEntity.getId())
                .phone(userEntity.getPhone()).build();
    }

    private UserDto mapFromRegistrationRequestToDto(UserRequestData userRequestData) {
        return UserDto.builder()
                .name(userRequestData.getName())
                .phone(userRequestData.getPhone())
                .build();
    }
}
