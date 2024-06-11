package restapi.banking.app.dto.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Component;
import restapi.banking.app.dto.UserDTO;
import restapi.banking.app.model.User;

import java.util.List;

@Component
public class UserMapper {
    private final ModelMapper modelMapper;

    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public UserDTO convertUserToUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    public User convertUserDTOToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    public List<UserDTO> convertUserListToUserDTOList(List<User> userList) {
        return modelMapper.map(userList, new TypeToken<List<UserDTO>>() {
        }.getType());
    }
}
