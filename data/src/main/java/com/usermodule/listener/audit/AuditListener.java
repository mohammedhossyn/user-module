package com.usermodule.listener.audit;

import com.google.gson.JsonObject;
import com.usermodule.model.system.OperationType;
import com.usermodule.model.system.AuditEntity;
import com.usermodule.model.user.UserEntity;
import com.usermodule.repository.system.AuditRepository;
import com.usermodule.utils.BeanUtil;
import jakarta.persistence.*;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class AuditListener {

    @PrePersist
    private void onPrePersist(Object object) {
    }

    @PreUpdate
    private void onPreUpdate(Object object) {

    }

    @PreRemove
    private void onPreRemove(Object object) {
    }

    @PostLoad
    private void onPostLoad(Object object) {
    }

    @PostPersist
    private void onPostCreate(Object object) {
        CompletableFuture.supplyAsync(() -> {
            try {
                audit(OperationType.INSERT, object);
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
            return null;
        });
    }

    @PostUpdate
    private void onPostUpdate(Object object) {
        CompletableFuture.supplyAsync(() -> {
            try {
                audit(OperationType.UPDATE, object);
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
            return null;
        });
    }

    @PostRemove
    private void onPostRemove(Object object) {
        CompletableFuture.supplyAsync(() -> {
            try {
                audit(OperationType.DELETE, object);
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
            return null;
        });
    }

    private void audit(OperationType operation, Object object) {
        AuditRepository auditRepository = BeanUtil.getBean(AuditRepository.class);

        Long referenceId = null;
        Date actionDate = null;
        UserEntity actionBy = null;
        JsonObject jsonObject = new JsonObject();
        String value;
        try {
            for (Field field : object.getClass().getDeclaredFields()) {
                if (field.getModifiers() == Modifier.PRIVATE && !field.getType().equals(List.class) &&
                        !field.getType().equals(Set.class)) {
                    field.setAccessible(true);
                    for (Annotation annotation : field.getAnnotations()) {
                        if (annotation.toString().equals("@jakarta.persistence.Id()")) {
                            referenceId = (Long) field.get(object);
                        }
                    }
                    if (field.getName().equals("createdDate") || field.getName().equals("createdBy") ||
                            field.getName().equals("lastModifiedDate") || field.getName().equals("lastModifiedBY") ||
                            field.getName().equals("version") || field.getName().equals("actionDate") ||
                            field.getName().equals("userNameIdentifier") || field.getName().endsWith("Entity")) {
                        continue;
                    }
                    if (field.get(object) != null) {
                        if (field.get(object) instanceof UserEntity user) {
                            value = user.getUsername();
                        } else {
                            value = String.valueOf(field.get(object));
                        }
                    } else {
                        value = null;
                    }
                    jsonObject.addProperty(field.getName(), value);
                }
            }
            if (operation.equals(OperationType.INSERT)) {
                for (Field field : object.getClass().getSuperclass().getSuperclass().getDeclaredFields()) {
                    if (field.getName().equals("createdDate")) {
                        field.setAccessible(true);
                        actionDate = (Date) field.get(object);
                        continue;
                    }
                    if (field.getName().equals("createdBy")) {
                        field.setAccessible(true);
                        actionBy = (UserEntity) field.get(object);
                        continue;
                    }
                    if (actionDate != null && actionBy != null) {
                        break;
                    }
                }
            } else if (operation.equals(OperationType.UPDATE)) {
                for (Field field : object.getClass().getSuperclass().getSuperclass().getDeclaredFields()) {
                    if (field.getName().equals("lastModifiedDate")) {
                        field.setAccessible(true);
                        actionDate = (Date) field.get(object);
                        continue;
                    }
                    if (field.getName().equals("lastModifiedBY")) {
                        field.setAccessible(true);
                        actionBy = (UserEntity) field.get(object);
                        continue;
                    }
                    if (actionDate != null && actionBy != null) {
                        break;
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        var audit = AuditEntity.builder()
                .entity(object.getClass().getSimpleName())
                .referenceId(referenceId)
                .operation(operation)
                .actionDate(actionDate)
                .actionBy(actionBy)
                .payload(jsonObject.toString())
                .build();
        auditRepository.save(audit);
    }
}
