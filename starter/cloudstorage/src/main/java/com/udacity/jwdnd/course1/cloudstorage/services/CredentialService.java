package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.CredentialModel;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {
    private final CredentialMapper credentialMapper;

    private final EncryptionService encryptionService;

    private CredentialModel encryptPassword(CredentialModel credential) {
        String encodedKey = getKey(credential);

        credential.setDecryptedPassword(null);
        credential.setKey(encodedKey);
        credential.setPassword(encryptionService.encryptValue(credential.getPassword(), encodedKey));
        return credential;
    }

    private String getKey(CredentialModel credentialModel) {
        List<CredentialModel> credentialModels = credentialMapper.getCredentialById(credentialModel.getCredentialId());

        if (credentialModels.isEmpty() || credentialModels.get(0).getKey() == null) {
            return createNewEncodedKey();
        } else {
            return credentialModels.get(0).getKey();
        }
    }

    private String createNewEncodedKey() {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        return Base64.getEncoder().encodeToString(key);
    }

    public String decryptPassword(CredentialModel credential) {
        return encryptionService.decryptValue(credential.getPassword(), credential.getKey());
    }

    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public List<CredentialModel> getCredentialList(Integer userId) {
        List<CredentialModel> credentialModels = credentialMapper.getCredentials(userId);
        credentialModels.forEach(credentialModel -> {
            credentialModel.setDecryptedPassword(decryptPassword(credentialModel));
        });

        return credentialModels;
    }

    public int createCredentialModel(CredentialModel credentialModel) {
        return credentialMapper.insert(encryptPassword(credentialModel));
    }

    public int updateCredentialModel(CredentialModel credentialModel) {
        return credentialMapper.update(encryptPassword(credentialModel));
    }

    public int deleteCredential(Integer credentialId) {
        return credentialMapper.delete(credentialId);
    }
}
