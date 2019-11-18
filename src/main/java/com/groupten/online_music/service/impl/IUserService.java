package com.groupten.online_music.service.impl;

import com.groupten.online_music.entity.User;

public interface IUserService {
    public boolean login(User user);
    public boolean register(User user);
    public boolean hasUser(User user);
}
