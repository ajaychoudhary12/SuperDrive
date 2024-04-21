package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.CredentialModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CredentialService {
    private final CredentialMapper credentialMapper;

    public CredentialService(CredentialMapper credentialMapper) {
        this.credentialMapper = credentialMapper;
    }

    public List<CredentialModel> getCredentialList(Integer userId) {
        return credentialMapper.getCredentials(userId);
    }

    public int createCredentialModel(CredentialModel credentialModel) {
        return credentialMapper.insert(credentialModel);
    }

    public int updateCredentialModel(CredentialModel credentialModel) {
        return credentialMapper.update(credentialModel);
    }

    public int deleteCredential(Integer credentialId) {
        return credentialMapper.delete(credentialId);
    }
}
