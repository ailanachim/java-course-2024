package edu.java.bot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class InMemoryUserService implements UserService {

    private final Map<Long, List<String>> userLinks = new HashMap<>();
    private final Map<Long, UserState> userStates = new HashMap<>();

    @Override
    public void addUser(Long userId) {
        userStates.put(userId, UserState.DEFAULT);
        userLinks.putIfAbsent(userId, new ArrayList<>());
    }

    @Override
    public boolean isAddingTrack(Long userId) {
        return UserState.IS_ADDING_TRACK.equals(userStates.get(userId));
    }

    @Override
    public void setAddingTrack(Long userId) {
        userStates.put(userId, UserState.IS_ADDING_TRACK);
    }

    @Override
    public boolean addTrack(Long userId, String url) {
        if (userLinks.containsKey(userId)) {
            var links = userLinks.get(userId);
            if (links.contains(url)) {
                return false;
            }
            links.add(url);
        } else {
            userLinks.put(userId, new ArrayList<>() {{add(url);}});
        }
        userStates.put(userId, UserState.DEFAULT);
        return true;
    }

    @Override
    public boolean isRemovingTrack(Long userId) {
        return UserState.IS_REMOVING_TRACK.equals(userStates.get(userId));
    }

    @Override
    public void setRemovingTrack(Long userId) {
        userStates.put(userId, UserState.IS_REMOVING_TRACK);
    }

    @Override
    public boolean removeTrack(Long userId, String url) {
        if (userLinks.containsKey(userId)) {
            return userLinks.get(userId).remove(url);
        }
        return false;
    }

    @Override
    public List<String> getTrackedLinks(Long userId) {
        return userLinks.getOrDefault(userId, new ArrayList<>());
    }
}
