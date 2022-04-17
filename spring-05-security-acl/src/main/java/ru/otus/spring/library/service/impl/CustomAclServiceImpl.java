package ru.otus.spring.library.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.otus.spring.library.service.CustomAclService;

import java.util.Arrays;

@RequiredArgsConstructor
@Service
public class CustomAclServiceImpl implements CustomAclService {

    private final MutableAclService aclService;

    @Override
    public void createAcl(Long id, Class<?> aClass, Permission... permissions) {
        ObjectIdentity oid = new ObjectIdentityImpl(aClass, id);
        MutableAcl acl = aclService.createAcl(oid);
        acl.setOwner(getOwner());
        Arrays.stream(permissions).forEach(p -> acl.insertAce(acl.getEntries().size(), p, acl.getOwner(), true));
        aclService.updateAcl(acl);
    }

    public Sid getOwner() {
        return new PrincipalSid(SecurityContextHolder.getContext().getAuthentication());
    }

    public Sid getOwnerByDomain(Class<?> aClass, Long id) {
        return aclService.readAclById(new ObjectIdentityImpl(aClass, id)).getOwner();
    }

}