package com.group.libraryapp.service.user;

import com.group.libraryapp.domain.user.User;
import com.group.libraryapp.domain.user.UserRepository;
import com.group.libraryapp.dto.user.request.UserCreateRequest;
import com.group.libraryapp.dto.user.request.UserUpdateRequest;
import com.group.libraryapp.dto.user.response.UserResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserServiceV2 {

    private final UserRepository userRepository;

    public UserServiceV2(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // @Transactional 은 아래 있는 함수가 시작할 때 start transaction;을 해준다(트랜잭션을 시작!)
    // 함수가 예외 없이 잘 끝났다면 commit;
    // 문제가 생기면 rollback;
    @Transactional
    public void saveUser(UserCreateRequest request){
       User u = userRepository.save(new User(request.getName(), request.getAge()));
       throw new IllegalArgumentException();
   }

    @Transactional(readOnly = true) // select 절일 때 활용
   public List<UserResponse> getUsers(){
       return  userRepository.findAll().stream()
//               .map(user -> new UserResponse(user.getId(), user.getName(), user.getAge()))
               .map(UserResponse::new)
               .collect(Collectors.toList());
   }

    @Transactional
   public void updateUser(UserUpdateRequest request){
       // select * from user where id = ?;
       // Optional <user> // 있는지 없는지 옵셔널 자바
       User user = userRepository.findById(request.getId())
                .orElseThrow(IllegalArgumentException::new);

       user.updateName(request.getName());
//       userRepository.save(user);

   }
    @Transactional
   public void deleteUser(String name){
        //select * from where name = ?
//       User user =  userRepository.findByName(name);
//       if(user ==null){
//           throw new IllegalArgumentException("User not found");
//       }
//
//       userRepository.delete(user);
   }
}
