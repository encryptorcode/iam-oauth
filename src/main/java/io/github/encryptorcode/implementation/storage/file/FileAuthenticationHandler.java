package io.github.encryptorcode.implementation.storage.file;

import io.github.encryptorcode.entity.AuthenticationDetail;
import io.github.encryptorcode.handlers.AAuthenticationHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

/**
 * A ready made implementation for handling storage for {@link AuthenticationDetail} using files
 */
public class FileAuthenticationHandler extends AAuthenticationHandler {

    private final HashMap<String, ArrayList<AuthenticationDetail>> data;

    private final String filePath;

    public FileAuthenticationHandler(String filePath) {
        this.filePath = filePath;
        //noinspection unchecked
        this.data = (HashMap<String, ArrayList<AuthenticationDetail>>) FileUtils.readFromFile(filePath).orElse(new HashMap<>());
    }

    @Override
    public AuthenticationDetail getAuthenticationDetail(String userId, String providerId) {
        return getFromStorage(userId, providerId).orElse(null);
    }

    @Override
    public AuthenticationDetail create(AuthenticationDetail detail) {
        if (!data.containsKey(detail.getUserId())) {
            data.put(detail.getUserId(), new ArrayList<>());
        }
        data.get(detail.getUserId()).add(detail);
        saveToStorage();
        return detail.clone();
    }

    @Override
    public AuthenticationDetail update(AuthenticationDetail detail) {
        Optional<AuthenticationDetail> detailFromStorage = getFromStorage(detail.getUserId(), detail.getProvider());
        if (!detailFromStorage.isPresent()) {
            throw new NullPointerException("Invalid details sent for update.");
        }
        AuthenticationDetail detailToReturn = detailFromStorage.get();
        detailToReturn.setAccessToken(detail.getAccessToken());
        detailToReturn.setExpiryTime(detail.getExpiryTime());
        detailToReturn.setRefreshToken(detail.getRefreshToken());
        saveToStorage();
        return detailToReturn.clone();
    }

    @Override
    public void delete(AuthenticationDetail detail) {
        Optional<AuthenticationDetail> detailFromStorage = getFromStorage(detail.getUserId(), detail.getProvider());
        detailFromStorage.ifPresent(authenticationDetail -> data.get(detail.getUserId()).remove(authenticationDetail));
        saveToStorage();
    }

    private Optional<AuthenticationDetail> getFromStorage(String userId, String provider) {
        return data.get(userId)
                .stream()
                .filter((d) -> provider.equals(d.getProvider()))
                .findFirst();
    }

    private void saveToStorage() {
        FileUtils.writeToFile(filePath, data);
    }
}
