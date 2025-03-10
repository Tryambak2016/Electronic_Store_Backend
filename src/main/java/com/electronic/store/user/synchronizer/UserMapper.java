package com.electronic.store.user.synchronizer;

import com.electronic.store.user.User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserMapper {

    public User fromTokenAttributes(Map<String, Object> attributes) {
        User user = new User();

        if (attributes.containsKey("sub")) {
            user.setId(attributes.get("sub").toString());
        }

        if (attributes.containsKey("given_name")) {
            user.setFirstname(attributes.get("given_name").toString());
        } else if (attributes.containsKey("nickname")) {
            user.setFirstname(attributes.get("nickname").toString());
        }

        if (attributes.containsKey("family_name")) {
            user.setLastname(attributes.get("family_name").toString());
        }

        if (attributes.containsKey("email")) {
            user.setEmail(attributes.get("email").toString());
        }

        return user;
    }


}
