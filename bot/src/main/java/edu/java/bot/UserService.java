package edu.java.bot;

import java.util.List;

public interface UserService {

    void addUser(Long userId);

    boolean isAddingTrack(Long userId);

    void setAddingTrack(Long userId);

    boolean addTrack(Long userId, String url);

    boolean isRemovingTrack(Long userId);

    void setRemovingTrack(Long userId);

    boolean removeTrack(Long userId, String url);

    List<String> getTrackedLinks(Long userId);
}
