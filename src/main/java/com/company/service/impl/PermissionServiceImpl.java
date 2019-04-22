package com.company.service.impl;

import com.company.service.PermissionService;
import org.springframework.stereotype.Service;

@Service("permissionService")
public class PermissionServiceImpl implements PermissionService {

    @Override
    public boolean hasPermission(String permission) {
        return false;
    }

}
